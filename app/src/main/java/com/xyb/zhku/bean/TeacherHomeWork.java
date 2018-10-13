package com.xyb.zhku.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by lenovo on 2018/9/25.
 */

public class TeacherHomeWork extends BmobObject {
    String teacherId;
    String major; //专业
    String enrollment_year; //年级
    String subject;//科目
    // String time;
    String title;//标题
    String content;//作业要求
    BmobFile file;//附件
    List<String> stu_number_list;  //用于记录上交的学生

    public List<String> getStu_number_list() {
        return stu_number_list;
    }

    public String getEnrollment_year() {
        return enrollment_year;
    }

    public void setEnrollment_year(String enrollment_year) {
        this.enrollment_year = enrollment_year;
    }

    public void setStu_number_list(List<String> stu_number_list) {
        this.stu_number_list = stu_number_list;
    }

    public TeacherHomeWork() {
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public TeacherHomeWork(String major, String subject, String time, String title, String content) {
        this.major = major;
        this.subject = subject;

        this.title = title;
        this.content = content;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }
}
