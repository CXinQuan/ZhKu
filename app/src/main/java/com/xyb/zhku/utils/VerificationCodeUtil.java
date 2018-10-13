package com.xyb.zhku.utils;

import com.xyb.zhku.global.Constants;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by lenovo on 2018/9/20.
 */

public class VerificationCodeUtil {

    /**
     * 生成验证码
     * @return
     */
    public static String yanzhengma() {
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < Constants.YZM_LENGTH) {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set) {
            sb.append("" + i);
        }
        return sb.toString();
    }

}
