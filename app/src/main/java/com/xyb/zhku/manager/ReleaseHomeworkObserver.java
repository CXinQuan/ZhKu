package com.xyb.zhku.manager;

import com.xyb.zhku.bean.TeacherHomeWork;

/**
 * Created by lenovo on 2018/9/28.
 */

public interface ReleaseHomeworkObserver {
    public void update(TeacherHomeWork teacherHomeWork);
}
