package com.xyb.zhku.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by lenovo on 2018/10/13.
 */

public class ErrorInfo extends BmobObject {
    String errorInfo;
    String phoneInfo;
    String appInfo;

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getPhoneInfo() {
        return phoneInfo;
    }

    public void setPhoneInfo(String phoneInfo) {
        this.phoneInfo = phoneInfo;
    }

    public String getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(String appInfo) {
        this.appInfo = appInfo;
    }
}
