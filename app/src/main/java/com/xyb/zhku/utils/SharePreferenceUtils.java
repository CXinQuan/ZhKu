package com.xyb.zhku.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xyb.zhku.bean.User;
import com.xyb.zhku.global.Constants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by lenovo on 2018/9/20.
 */

public class SharePreferenceUtils {
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "SharePreference_ZhKu";

    /**
     * 保存 用户信息
     */
    public static void saveUser(Context context, User user) {
        if (user != null) {
            put(context, Constants.USER, user.getName() + user.getPhone());
            put(context, Constants.PHONE, user.getPhone());
            put(context, Constants.NAME, user.getName());
            put(context, Constants.IDENTITY, user.getIdentity());
            put(context, Constants.SCHOOL_NUMBER, user.getSchool_number());
            put(context, Constants.OBJECTID, user.getObjectId());
            put(context, Constants.ENROLLMENT_YEAR, user.getEnrollment_year() != null ? user.getEnrollment_year() : "");
            put(context, Constants.MAJOR, user.getMajor() != null ? user.getMajor() : "");

            put(context, Constants.EMAIL, user.getEmail() != null ? user.getEmail() : "");

            put(context, Constants.UCLASS, user.getClassNumber() != -1 ? user.getClassNumber() : -1);
            put(context, Constants.COLLEGE, user.getCollege() != null ? user.getCollege() : "");
        }
    }

    public static  User getUser(Context context){
        User user=new User();
        user.setEmail((String) get(context,Constants.EMAIL,""));
        user.setEnrollment_year((String) get(context,Constants.ENROLLMENT_YEAR,""));
        user.setSchool_number((String) get(context,Constants.SCHOOL_NUMBER,""));
        user.setName((String) get(context,Constants.NAME,""));
        user.setClassNumber((int) get(context,Constants.UCLASS,-1));
        user.setCollege((String) get(context,Constants.COLLEGE,""));
        user.setIdentity((int) get(context,Constants.IDENTITY,0));
        user.setMajor((String) get(context,Constants.MAJOR,""));
        user.setObjectId((String) get(context,Constants.OBJECTID,""));
        user.setPhone((String) get(context,Constants.PHONE,""));
        return user;
    }


    /**
     * 清空用户信息
     */
    public static void clearUser(Context context) {
        remove(context, Constants.USER);
//            put(context, Constants.USER, user.getName() + user.getPhone());
//            put(context, Constants.PHONE, user.getPhone());
//            put(context, Constants.NAME, user.getName());
//            put(context, Constants.IDENTITY, user.getIdentity());
//            put(context, Constants.SCHOOL_NUMBER, user.getSchool_number());
//            put(context, Constants.OBJECTID, user.getObjectId());
//            put(context, Constants.ENROLLMENT_YEAR, user.getEnrollment_year() != null ? user.getEnrollment_year() : "");
//            put(context, Constants.MAJOR, user.getMajor() != null ? user.getMajor() : "");
//            put(context, Constants.UCLASS, user.getuClass() != null ? user.getuClass() : "");
//            put(context, Constants.COLLEGE, user.getCollege() != null ? user.getSchool_number() : "");
//
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        }
        SharedPreferencesCompat.apply(editor);
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }


    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }


    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }


}
