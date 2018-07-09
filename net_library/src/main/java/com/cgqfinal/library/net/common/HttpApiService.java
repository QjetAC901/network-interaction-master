package com.cgqfinal.library.net.common;

import com.cgqfinal.library.token.IGlobalManager;
import com.cgqfinal.library.token.ProxyHandler;

import java.lang.reflect.Proxy;

import retrofit2.Retrofit;

/**
 * 网络请求入口  创建Service类
 */
public class HttpApiService{

    /**
     * 普通网络请求获取Service
     * @param cls
     * @param baseUrl
     * @param <T>
     * @return
     */
    public static <T> T getApiService(Class<T> cls, String baseUrl) {
        Retrofit retrofit = RetrofitUtils.getRetrofitBuilder(baseUrl).build();
        return retrofit.create(cls);
    }

    /**
     * 添加代理 动态获取Token的ApiService
     * @param tClass
     * @param baseUrl
     * @param manager
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxyApiService(Class<T> tClass,String baseUrl,IGlobalManager manager) {
        T t = RetrofitUtils.getRetrofitBuilder(baseUrl)
                .build().create(tClass);
        return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class<?>[] { tClass }, new ProxyHandler(t, manager));
    }
}
