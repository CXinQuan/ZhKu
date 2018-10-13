package com.xyb.zhku.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.widget.Toast;

import java.io.IOException;

/**
 * 只需要在每一个activity中配置，fragment则不需要，否则一个activity会弹出多个吐司
 *
 */

public class NetUtils {


    /**
     * 初始化网络链接状态的监听 ，在没网络的时候提供更好的交互
     * <p>
     * onAvailable方法是网络链接的回调，
     * onLost方法是网络断开的回调
     */
    public static void getNetState(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.requestNetwork(new NetworkRequest.Builder().
                    build(), new ConnectivityManager.NetworkCallback() {
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    if (!ping()) {
                        Toast.makeText(context, "连接不到服务器", Toast.LENGTH_SHORT).show();
                    }
                }

                public void onLost(Network network) {
                    super.onLost(network);
                    if (!ping()) {
                        Toast.makeText(context, "连接不到服务器", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /**
     * 判断当前状态是否可用
     * ping  一下百度 看网络是否能用
     *
     * @return
     */
    public static boolean ping() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("ping -c 3 www.baidu.com");// https://www.bmob.cn/
            int ret=process.waitFor();
            return ret == 0;  // 0 为 可用，其他为不可用
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}


