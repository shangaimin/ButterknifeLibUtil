package com.sgx.kotlin;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        SgxPlayMP3Utils.getInstance(this);
        TTSUtils.getInstance(this);
        context=this;

    }
    public static Context getmContext(){
        return context;
    }
}
