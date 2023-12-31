package com.sgx.kotlin;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    private static Context context;
    public static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        SgxPlayMP3Utils.getInstance(this);
        TTSUtils.getInstance(this);
        context=this;
        initGreenDao();

    }
    public static Context getmContext(){
        return context;
    }
    public static void initGreenDao() {
        DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(new GreenDaoContext(), "PortableMessage.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }

    public static DaoSession getmDaoSession() {
        return mDaoSession;
    }
}
