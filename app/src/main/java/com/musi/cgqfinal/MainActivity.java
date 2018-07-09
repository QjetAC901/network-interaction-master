package com.musi.cgqfinal;

import android.os.Bundle;

import com.cgqfinal.library.Constants;
import com.cgqfinal.library.net.common.DefaultObserver;
import com.cgqfinal.library.net.common.ProgressUtils;
import com.cgqfinal.library.utils.LogUtils;
import com.musi.cgqfinal.bean.Welfare;
import com.musi.cgqfinal.service.RetrofitHelper;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 展示调用方式
 */
public class MainActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBelleData();
    }

    private int currentPages = 1;

    /**
     * 1、创建Application类  Utils初始化{ Utils.init(this);}
     * 2、修改library的Constants类的请求地址
     * 3、创建Service类  定义网络请求接口
     * 4、创建返回数据实体类、
     * 5、链式调用方法如下
     */
    private void initBelleData() {
        RetrofitHelper.getApiService(Constants.GANK_HTTP_ADDRESS)
                .getWelfare(20, currentPages)
                .compose(this.<List<Welfare>>bindToLifecycle())
                .compose(ProgressUtils.<List<Welfare>>applyProgressBar(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<Welfare>>() {
                    @Override
                    public void onSuccess(List<Welfare> response) {
                        // TODO: 2018/7/9 成功
                        LogUtils.e("TAG", "请求成功：" + response.toString());
                    }

                    @Override
                    public void onFinish() {
                        // TODO: 2018/7/9 失败
                        LogUtils.e("TAG", "请求失败");
                    }
                });
    }
}
