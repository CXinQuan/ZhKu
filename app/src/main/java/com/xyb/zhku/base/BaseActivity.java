package com.xyb.zhku.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.xyb.zhku.utils.ToastUtil;
import com.xyb.zhku.utils.Utils;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by lenovo on 2018/7/14.
 */

public abstract class BaseActivity extends Activity {
    public Context mCtx = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Looper.prepare();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏   也就是不会出现 时间+电池电量 这一栏
        //全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(setContentViewLayout());
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= 21) {//状态栏全透明
            Window window = getWindow();
            window.clearFlags(0x04000000                                  // LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | 0x08000000);                                        // LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            window.getDecorView().setSystemUiVisibility(0x00000400        // View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | 0x00000200                                          // View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | 0x00000100);                                        // View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(0x80000000);                                  // LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        }

     //   NetUtils.getNetState(this);




    }

    /**
     * 显示吐司
     *
     * @param text
     */
    public void showToast(String text) {
        ToastUtil.showToast(mCtx, text);
    }

    /**
     * 弹出Snack
     */
    public void showSnackBar(View view, String str) {
        final Snackbar snackbar = Snackbar.make(view, str, Snackbar.LENGTH_SHORT);
        snackbar.show();
        snackbar.setAction("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
    }

    /**
     * 打印日志
     *
     * @param tag
     * @param text
     */
    public void Log(String tag, String text) {
        Utils.showLog(tag, text);
    }

    /**
     * 跳转到另一个页面
     *
     * @param cls
     */
    public void jumpToAnotherActivity(Class<?> cls) {
        Intent intent = new Intent(mCtx, cls);
        startActivity(intent);
    }

    /* 检查使用权限 */
    protected void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                PackageManager pm = getPackageManager();
                PackageInfo pi = pm.getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
                ArrayList<String> list = new ArrayList<String>();
                for (String p : pi.requestedPermissions) {
                    if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                        list.add(p);
                    }
                }
                if (list.size() > 0) {
                    String[] permissions = list.toArray(new String[list.size()]);
                    if (permissions != null) {
                        requestPermissions(permissions, 1);
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (permissions != null && permissions.length > 0) {
                StringBuilder sb = null;
                String permission;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        continue;
                    }
                    if (sb == null) {
                        sb = new StringBuilder();
                    }
                    permission = permissions[i];
                }
                if (sb != null) {
                    //toast 提示用户已经禁用了必要的权限，然后去权限中心打开即可
                    Toast.makeText(getApplication(), "您已经禁用了必要的权限，建议您去权限中心打开！", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 设置布局资源
     *
     * @return
     */
    public abstract int setContentViewLayout();

}




