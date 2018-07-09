package com.cgqfinal.library;

import com.cgqfinal.library.net.download.DownloadListener;
import com.cgqfinal.library.net.download.DownloadUtils;

import okhttp3.ResponseBody;

/**
 * Created by dell on 2018/5/24.
 */

public class Test {

    public void main(){
        //地址  全称   回调方法
        new DownloadUtils().download("", new DownloadListener() {
            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onSuccess(ResponseBody responseBody) {

            }

            @Override
            public void onFail(String message) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
