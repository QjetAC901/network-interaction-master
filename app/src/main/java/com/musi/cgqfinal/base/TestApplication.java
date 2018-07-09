package com.musi.cgqfinal.base;

import android.app.Application;

import com.cgqfinal.library.utils.Utils;

/**
 * Created by Chen_Mr on 2018/4/11.
 */

public class TestApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
