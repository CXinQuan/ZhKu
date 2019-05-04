package com.xyb.zhku.global;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import com.mob.MobSDK;
import com.tencent.smtt.sdk.QbSdk;
import com.xyb.zhku.bean.ErrorInfo;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by lenovo on 2018/9/20.
 */

public class ZhKuApplication extends Application {
    private static Context context;
    private static Handler handler;
    private static int mainThreadId;
    ErrorInfo errorInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "33ca9c317f08d78c3c0e50724bede9ad");
        MobSDK.init(this);
        QbSdk.initX5Environment(this,null);
                mainThreadId = android.os.Process.myTid();
        context = getApplicationContext();
        handler = new Handler();

        /*
         * 获取全局错误日志，发送到服务器
         */
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable ex) {
                ex.printStackTrace();
                errorInfo=new ErrorInfo();
                errorInfo.setErrorInfo(ex.toString());
                errorInfo.setPhoneInfo(getUserPhoneInfo());
                errorInfo.setAppInfo(getAppInfo());
                Log.d("错误信息" ,ex.toString());
                errorInfo.save(new SaveListener<String>() {
                    public void done(String objectId, BmobException e) {
//                        if (e == null) {
//                           // System.exit(0);
//                        } else {
//                            //结束应用
//                           // System.exit(0);
//                        }
                        System.exit(0);
                    }
                });
            }
        });
    }

    /**
     *  用户手机信息
     * @return
     */
    private String getUserPhoneInfo() {
        StringBuilder ua = new StringBuilder("");
        ua.append("Android");// 手机系统平台
        ua.append("_手机Android系统版本:" + android.os.Build.VERSION.RELEASE);// 手机系统版本
        ua.append("_手机型号:" + android.os.Build.MODEL); // 手机型号
        return ua.toString();
    }

    /**
     *  用户当前app 的版本信息
     * @return
     */
    private String getAppInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null) {
            info = new PackageInfo();
        }
        return "App版本信息:" + info.versionName + '_' + info.versionCode;// app版本信息
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
