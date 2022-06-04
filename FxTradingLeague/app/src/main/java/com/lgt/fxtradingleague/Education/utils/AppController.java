package com.lgt.fxtradingleague.Education.utils;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;



import java.io.File;

import io.reactivex.plugins.RxJavaPlugins;

public class AppController extends Application {
    private static AppController mInstance;


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        RxJavaPlugins.setErrorHandler(throwable -> {}); // nothing or some logging


    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this); // add this
    }
}
