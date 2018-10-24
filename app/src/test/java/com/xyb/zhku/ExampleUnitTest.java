package com.xyb.zhku;

import com.xyb.zhku.utils.MD5Util;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test1() throws Exception {
        long timeMillis = System.currentTimeMillis();
        DateFormat format = new SimpleDateFormat("yyyy");
        String year_str = format.format(timeMillis);
        int year = Integer.parseInt(year_str);
        System.out.println(year);
    }

    @Test
    public void test2() throws Exception {
        String encrypt = MD5Util.encrypt("2");
        System.out.println(encrypt);
    }
    @Test
    public void test3() throws Exception{
        // String[] year = new String[4];
        List<String> list_year = new ArrayList();

        long timeMillis = System.currentTimeMillis();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time_str = format.format(timeMillis);
        int year_int = Integer.parseInt(time_str.substring(0, 4)); // 从0开始，4之前
        int month_int = Integer.parseInt(time_str.substring(5, 7));// 5开始 ，7之前
        System.out.println(time_str);
        System.out.println(year_int);
        System.out.println(month_int);

        for (int i = 1; i < 5; i++) {  // 先将 17 16 15 14 拿出来
            list_year.add(year_int-i + "");
        }
        if (month_int >= 6) {//六月份开始就 将最后的那个年级移除
            list_year.remove(list_year.size()-1);
        }

        if(month_int>=9){ // 九月份开始就将 新年级添加进去
            list_year.add(0,year_int+"");
        }
        //return (String[]) list_year.toArray();

        for (String str:list_year){
            System.out.println(str);
        }

    }

}