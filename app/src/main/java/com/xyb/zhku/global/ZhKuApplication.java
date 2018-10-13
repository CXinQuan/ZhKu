package com.xyb.zhku.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.mob.MobSDK;

import cn.bmob.v3.Bmob;

/**
 * Created by lenovo on 2018/9/20.
 */

public class ZhKuApplication extends Application {
    private static Context context;
    private static Handler handler;
    private static int mainThreadId;
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "33ca9c317f08d78c3c0e50724bede9ad");
        MobSDK.init(this);
        mainThreadId = android.os.Process.myTid();
        context = getApplicationContext();
        handler = new Handler();
    }
    public static int getMainThreadId() {
        return mainThreadId;
    }
    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }
}
