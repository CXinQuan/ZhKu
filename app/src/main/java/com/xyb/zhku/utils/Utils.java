package com.xyb.zhku.utils;

import android.content.Context;
import android.util.Log;

import com.xyb.zhku.R;

import java.text.DateFormat;
import java.text.ParseException;
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
    public static String[] getYearStr() {

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
        List<String> list_class_no=new ArrayList<>();

         for (String str_year : allYear) {
            for (int i=0;i<=9;i++) {
                list_class_no.add(str_year.substring(str_year.length() - 2, str_year.length())+i);
                Log.d(""+str_year.substring(str_year.length() - 2, str_year.length())+i,"");
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
