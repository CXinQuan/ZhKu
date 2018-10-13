package com.xyb.zhku.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by lenovo on 2018/9/25.
 */

public class StudentHomeWork extends BmobObject {
    public static final int HAS_CHECK = 1;
    public static final int NOT_CHECK = 0;


    String stu_major; // 学生专业
    String stu_class; //学生班级
    String stu_school_number;//学号
    String stu_name;//姓名
    int stu_state = NOT_CHECK;//状态  是否批改
    float stu_grade = -1.0f;//成绩  默认是-1分
    BmobFile file;//附件（上交的作业）
    String teacher_homework_id; //对应老师作业的id

    public float getStu_grade() {
        return stu_grade;
    }

    public void setStu_grade(float stu_grade) {
        this.stu_grade = stu_grade;
    }

    public String getStu_major() {
        return stu_major;
    }

    public void setStu_major(String stu_major) {
        this.stu_major = stu_major;
    }

    public String getStu_class() {
        return stu_class;
    }

    public void setStu_class(String stu_class) {
        this.stu_class = stu_class;
    }

    public String getStu_school_number() {
        return stu_school_number;
    }

    public void setStu_school_number(String stu_school_number) {
        this.stu_school_number = stu_school_number;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public int getStu_state() {
        return stu_state;
    }

    public void setStu_state(int stu_state) {
        this.stu_state = stu_state;
    }


    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }

    public String getTeacher_homework_id() {
        return teacher_homework_id;
    }

    public void setTeacher_homework_id(String teacher_homework_id) {
        this.teacher_homework_id = teacher_homework_id;
    }
}
