package com.xyb.zhku.bean;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by lenovo on 2018/10/15.
 */

public class TeachingTask extends BmobObject {
    String teacherId; // 老师工号
    String teacherName;// 老师名字
    List<SubjectClass> list; // 对应任务

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public List<SubjectClass> getList() {
        return list;
    }

    public void setList(List<SubjectClass> list) {
        this.list = list;
    }

    public static class SubjectClass implements Serializable {  // 课程对应班级
        String subject; // 科目
        String year;// 年级
        String major;//专业
        //  List<String> classList; // 班级列表
        List<Integer> classList; // 班级列表

        public List<Integer> getClassList() {
            return classList;
        }

        public void setClassList(List<Integer> classList) {
            this.classList = classList;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }

//        public List<String> getClassList() {
//            return classList;
//        }
//
//        public void setClassList(List<String> classList) {
//            this.classList = classList;
//        }
    }

}
