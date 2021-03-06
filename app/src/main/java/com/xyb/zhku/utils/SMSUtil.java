package com.xyb.zhku.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by lenovo on 2018/9/20.
 */

public class SMSUtil {
    /**
     * 判断宿舍号
     */
//    public static boolean isDormNmber(String str) {
//
//        String telRegex = "[1-9]\\d{2}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
//        if (TextUtils.isEmpty(str))
//            return false;
//        else
//            return str.matches(telRegex);
//    }
    public static boolean isDormNmber(String str) {
        // 宿舍号只能是 第一位数 是  1-9，第二位数是0-1，最后可以是0-9
        String telRegex = "[1-9][0-1]\\d{1}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(str))
            return false;
        else
            return str.matches(telRegex);
    }

    /**
     * @param i   是楼层
     * @param str 是宿舍号
     * @return
     */
    public static boolean isRightDormNmber(int i, String str) {
        if (!isDormNmber(str)) {
            return false;
        }
        ;
        int dormNumber = Integer.parseInt(str.trim());
        if (dormNumber / 100 == i) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将手机号码改为带 *  号的形式，手机号码加密
     *
     * @param phone
     * @return
     */
    public static String encryptionPhone(String phone) {
        if (phone.length() < 11) {
            phone = "点击绑定手机";
        } else {
            phone = phone.substring(0, 3) + "****" + phone.substring(7, 11);
        }
        return phone;
    }


    /**
     * 判断手机号码是否合理
     */
    public static boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
            return true;
        }
        //  Toast.makeText(activity, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、177（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][3578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 权限校验
     *
     * @param activity
     */
    public static void checkPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            int readPhone = activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            int receiveSms = activity.checkSelfPermission(Manifest.permission.RECEIVE_SMS);
            int readSms = activity.checkSelfPermission(Manifest.permission.READ_SMS);
            int readContacts = activity.checkSelfPermission(Manifest.permission.READ_CONTACTS);
            int readSdcard = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            int requestCode = 0;
            ArrayList<String> permissions = new ArrayList<String>();
            if (readPhone != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 0;
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (receiveSms != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 1;
                permissions.add(Manifest.permission.RECEIVE_SMS);
            }
            if (readSms != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 2;
                permissions.add(Manifest.permission.READ_SMS);
            }
            if (readContacts != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 3;
                permissions.add(Manifest.permission.READ_CONTACTS);
            }
            if (readSdcard != PackageManager.PERMISSION_GRANTED) {
                requestCode |= 1 << 4;
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (requestCode > 0) {
                String[] permission = new String[permissions.size()];
                activity.requestPermissions(permissions.toArray(permission), requestCode);
                return;
            }
        }
    }

}
