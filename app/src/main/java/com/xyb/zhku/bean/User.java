package com.xyb.zhku.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by lenovo on 2018/9/20.
 */

public class User extends BmobObject {

    public static final int STUDENT = 0;
    public static final int TEACHER = 1;
    public static final int MANAGER_NOTIFY = 2;
    public static final int MANAGER_TEACHINGTASK = 3;


    /**
     * 手机号码
     */
    String phone;
    /**
     *   邮箱号
     */
    String email;
    /**
     * 密码
     */
    String password;
    /**
     * 姓名
     */
    String name;
    /**
     * 学号
     */
    String school_number;
    /**
     * 用户身份
     */
    int identity;
    /**
     * 学院
     */
    String college;
    /**
     * 入学年份
     */
    String enrollment_year;
    /**
     * 专业
     */
    String major;
    /**
     * 班级
     */
    int classNumber;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public int getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool_number() {
        return school_number;
    }

    public void setSchool_number(String school_number) {
        this.school_number = school_number;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getEnrollment_year() {
        return enrollment_year;
    }

    public void setEnrollment_year(String enrollment_year) {
        this.enrollment_year = enrollment_year;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

//    public String getuClass() {
//        return uClass;
//    }
//
//    public void setuClass(String uClass) {
//        this.uClass = uClass;
//    }
}
