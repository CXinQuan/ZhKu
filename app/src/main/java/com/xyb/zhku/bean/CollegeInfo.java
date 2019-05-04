package com.xyb.zhku.bean;

import java.util.ArrayList;

/**
 * Created by 陈鑫权  on 2019/4/2.
 */

public class CollegeInfo {
    public CollegeInfo(String name, String number) {
        this.name = name;
        this.number = number;
    }

    String name;
    String number;
    ArrayList<Major> majors;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ArrayList<Major> getMajors() {
        return majors;
    }

    public void setMajors(ArrayList<Major> majors) {
        this.majors = majors;
    }

    public static class Major {
        String name;
        String number;

        public Major(String name, String number) {
            this.name = name;
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
