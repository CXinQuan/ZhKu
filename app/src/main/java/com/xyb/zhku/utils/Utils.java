package com.xyb.zhku.utils;

import android.content.Context;
import android.util.Log;

import com.xyb.zhku.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/9/20.
 */

public class Utils {

    /**
     * 项目完成的时候，将  Log.d(tag,text); 注释掉
     *
     * @param tag
     * @param text
     */
    public static void showLog(String tag, String text) {
        Log.d(tag, text);
    }

    public static void showLog(String text) {
        Log.d("测试", text);
    }

    public static boolean isAccountNumber(String str) {
        return SMSUtil.judgePhoneNums(str) || isSchoolNumber(str) || isTeacherNumber(str);
    }

    /**
     * 判断是否是学号
     */
    public static boolean isTeacherNumber(String str) {

        return str.length() == 5;
    }

    /**
     * 判断是否是学号
     */
    public static boolean isSchoolNumber(String str) {
        if (str.length() != 12) {
            return false;
        }
        return str.startsWith("20");
    }

    /**
     * 将当前年份 遍历得到 4个年份
     *
     * @return
     */
    public static String[] getYearStr_before() {

        String[] year = new String[5];
        long timeMillis = System.currentTimeMillis();
        DateFormat format = new SimpleDateFormat("yyyy");
        String year_str = format.format(timeMillis);
        int year_int = Integer.parseInt(year_str);
        for (int i = 0; i < year.length; i++) {
            year[i] = year_int-- + "";
        }
        return year;
//        System.out.println(year);
    }

    public static String[] getYearStr() {
        // String[] year = new String[4];
        List<String> list_year = new ArrayList();
        long timeMillis = System.currentTimeMillis();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time_str = format.format(timeMillis);
        int year_int = Integer.parseInt(time_str.substring(0, 4)); // 从0开始，4之前
        int month_int = Integer.parseInt(time_str.substring(5, 7));// 5开始 ，7之前
//        System.out.println(time_str);
//        System.out.println(year_int);
//        System.out.println(month_int);
        for (int i = 1; i < 5; i++) {  // 先将 17 16 15 14 拿出来
            list_year.add(year_int - i + "");
        }
        if (month_int >= 6) {//六月份开始就 将最后的那个年级移除
            list_year.remove(list_year.size() - 1);
        }
        if (month_int >= 9) { // 九月份开始就将 新年级添加进去
            list_year.add(0, year_int + "");
        }
        String[] years = new String[list_year.size()];
        list_year.toArray(years);
        return years;
    }


    /**
     * 获取资源文件中的
     */
    public static String[] getAllCollege(Context mCtx) {
        return mCtx.getResources().getStringArray(R.array.stu_college);
    }

    /**
     * 从资源文件中获取所有专业
     *
     * @param mCtx
     * @return
     */
    public static String[] getAllMajor(Context mCtx) {
        return mCtx.getResources().getStringArray(R.array.stu_major);
    }

    /**
     * 将专业和年份进行合并得到年级
     *
     * @param mCtx
     * @return
     */
    public static String[] getAllClass(Context mCtx) {
        // TODO: 2018/9/22    修改  电子信息工程151
        String[] allMajor = getAllMajor(mCtx);
        String[] allYear = getYearStr();
        List<String> list_class_no = new ArrayList<>();
        for (String str_year : allYear) {
            for (int i = 0; i <= 9; i++) {
                list_class_no.add(str_year.substring(str_year.length() - 2, str_year.length()) + i);
                Log.d("" + str_year.substring(str_year.length() - 2, str_year.length()) + i, "");
            }
        }
        List<String> list_class = new ArrayList<>();
        for (String class_no : list_class_no) {
            for (String major : allMajor) {
                list_class.add(major + class_no);
            }
        }
        String[] all_class = new String[list_class.size()];
        list_class.toArray(all_class);
        return all_class;
    }

    /**
     * 判断一个数组中是否包含  该字符串
     */
    public static boolean contain(String[] all, String str) {
        for (int i = 0; i < all.length; i++) {
            if (all[i].equals(str)) {
                return true;
            }
        }
        return false;
    }

}
