package com.xyb.zhku.global;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 陈鑫权  on 2018/12/9.
 */

public class ZhKuThreadPool {
    public ZhKuThreadPool() {
    }

    private ExecutorService executors = Executors.newCachedThreadPool();

    static class ZhKuThreadPoolHolder {
        static ZhKuThreadPool threadPool = new ZhKuThreadPool();
    }

    public static  ZhKuThreadPool getInstance() {
        return ZhKuThreadPoolHolder.threadPool;
    }

    public ExecutorService getThreadPool() {
        return executors;
    }


}
