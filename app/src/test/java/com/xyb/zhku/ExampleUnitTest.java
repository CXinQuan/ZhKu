package com.xyb.zhku;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
        DateFormat format=new SimpleDateFormat("yyyy");
        String year_str = format.format(timeMillis);
        int year=Integer.parseInt(year_str);
        System.out.println(year);
    }



}