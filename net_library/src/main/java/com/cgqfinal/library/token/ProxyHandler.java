
package com.cgqfinal.library.token;

import android.text.TextUtils;

import com.cgqfinal.library.Constants;
import com.cgqfinal.library.net.exception.TokenInvalidException;
import com.cgqfinal.library.net.common.CommonService;
import com.cgqfinal.library.net.common.DefaultObserver;
import com.cgqfinal.library.net.common.RetrofitUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import retrofit2.http.Query;

public class ProxyHandler implements InvocationHandler {

    private final static String TAG = "Token_Proxy";

    private final static String TOKEN = "token";

    private final static int REFRESH_TOKEN_VALID_TIME = 30;
    private static long tokenChangedTime = 0;
    private Throwable mRefreshTokenError = null;
    private boolean mIsTokenNeedRefresh;

    private Object mProxyObject;
    private IGlobalManager mGlobalManager;

    public ProxyHandler(Object proxyObject, IGlobalManager globalManager) {
        mProxyObject = proxyObject;
        mGlobalManager = globalManager;
    }

    /**
     * Refresh the token when the current token is invalid.
     *
     * @return Observable
     */
    private Observable<?> refreshTokenWhenTokenInvalid() {
        synchronized (ProxyHandler.class) {
            // Have refreshed the token successfully in the valid time.
            if (new Date().getTime() - tokenChangedTime < REFRESH_TOKEN_VALID_TIME) {
                mIsTokenNeedRefresh = true;
                return Observable.just(true);
            } else {
                RetrofitUtils
                        .getRetrofitBuilder(Constants.GANK_HTTP_ADDRESS)
                        .build()
                        .create(CommonService.class)
                        .refreshToken()
                        .subscribe(new DefaultObserver<TokenResponse>() {
                            @Override
                            public void onSuccess(TokenResponse response) {
                                if (response != null) {
                                    mIsTokenNeedRefresh = true;
                                    tokenChangedTime = new Date().getTime();
                                    GlobalToken.updateToken(response.getToken());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                mRefreshTokenError=e;
                            }
                        });
                if (mRefreshTokenError != null) {
                    return Observable.error(mRefreshTokenError);
                } else {
                    return Observable.just(true);
                }
            }
        }
    }

    /**
     * Update the token of the args in the method.
     *
     * PS： 因为这里使用的是 GET 请求，所以这里就需要对 Query 的参数名称为 token 的方法。
     * 若是 POST 请求，或者使用 Body ，自行替换。因为 参数数组已经知道，进行遍历找到相应的值，进行替换即可（更新为新的 token 值）。
     */
    private void updateMethodToken(Method method, Object[] args) {
        if (mIsTokenNeedRefresh && !TextUtils.isEmpty(GlobalToken.getToken())) {
            Annotation[][] annotationsArray = method.getParameterAnnotations();
            Annotation[] annotations;
            if (annotationsArray != null && annotationsArray.length > 0) {
                for (int i = 0; i < annotationsArray.length; i++) {
                    annotations = annotationsArray[i];
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof Query) {
                            if (TOKEN.equals(((Query) annotation).value())) {
                                args[i] = GlobalToken.getToken();
                            }
                        }
                    }
                }
            }
            mIsTokenNeedRefresh = false;
        }
    }

    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {
        return Observable.just(true).flatMap(new Function<Object, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Object o) throws Exception {
                try {
                    try {
                        if (mIsTokenNeedRefresh) {
                            updateMethodToken(method, args);
                        }
                        return (Observable<?>) method.invoke(mProxyObject, args);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Throwable> observable) throws Exception {
                return observable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        if (throwable instanceof TokenInvalidException) {
                            // access_token过期
                            return refreshTokenWhenTokenInvalid();
                        }
                        // refresh_token 过期  使用IGlobalManager接口 在调用地方传入的Manager实现逻辑
                        return Observable.error(throwable);
                    }
                });
            }
        });
    }
}
