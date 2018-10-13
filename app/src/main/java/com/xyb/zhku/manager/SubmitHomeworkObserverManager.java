package com.xyb.zhku.manager;

import com.xyb.zhku.bean.TeacherHomeWork;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/9/28.
 */

public class SubmitHomeworkObserverManager {
    static List<SubmitHomeworkObserver> list = new ArrayList<>();

    static class MyManager {
        private static SubmitHomeworkObserverManager manager = new SubmitHomeworkObserverManager();
    }

    public static SubmitHomeworkObserverManager getInstance() {
        return MyManager.manager;
    }

    public static void notifyChange(TeacherHomeWork teacherHomeWork,int position) {
        for (SubmitHomeworkObserver observer : list) {
            observer.update(teacherHomeWork,position);
        }
    }

    public static void add(SubmitHomeworkObserver observer) {
        if (observer != null) {
            list.add(observer);
        }
    }


    public void remove(SubmitHomeworkObserver observer) {
        if (list.contains(observer)) {
            list.remove(observer);
        }
    }


}
