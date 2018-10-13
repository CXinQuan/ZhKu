package com.xyb.zhku.manager;

import com.xyb.zhku.bean.TeacherHomeWork;

import org.w3c.dom.ls.LSInput;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/9/28.
 */

public class ReleaseHomeworkObserverManager {
    static List<ReleaseHomeworkObserver> list = new ArrayList<>();

    static class MyManager {
        private static ReleaseHomeworkObserverManager manager = new ReleaseHomeworkObserverManager();
    }

    public static ReleaseHomeworkObserverManager getInstance() {
        return MyManager.manager;
    }

    public static void notifyChange(TeacherHomeWork teacherHomeWork) {
        for (ReleaseHomeworkObserver observer : list) {
            observer.update(teacherHomeWork);
        }
    }

    public static void add(ReleaseHomeworkObserver observer) {
        if (observer != null) {
            list.add(observer);
        }
    }


    public void remove(ReleaseHomeworkObserver observer) {
        if (list.contains(observer)) {
            list.remove(observer);
        }
    }


}
