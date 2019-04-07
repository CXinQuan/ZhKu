package com.xyb.zhku.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 陈鑫权  on 2018/12/10.
 */

public class EmailUtil {
    /**
     * 判断是否是邮箱
     *1611205417@qq.com
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {

            Pattern p = Pattern.compile(strPattern);
            Matcher m = p.matcher(email);
           // boolean matches = m.matches();
            return m.matches();

            //return email.matches(strPattern);
        }
    }


}
