package com.xyb.zhku.manager;

import com.xyb.zhku.bean.TeachingTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/9/28.
 */

public class TeachingTaskUpdateManager {
    static List<TeachingTaskObserver> list = new ArrayList<>();

    static class MyManager {
        private static TeachingTaskUpdateManager manager = new TeachingTaskUpdateManager();
    }

    public static TeachingTaskUpdateManager getInstance() {
        return MyManager.manager;
    }

    public static void notifyChange(TeachingTask teachingtask) {
        for (TeachingTaskObserver observer : list) {
            observer.update(teachingtask);
        }
    }

    public static void add(TeachingTaskObserver observer) {
        if (observer != null) {
            list.add(observer);
        }
    }


    public void remove(TeachingTaskObserver observer) {
        if (list.contains(observer)) {
            list.remove(observer);
        }
    }


}
