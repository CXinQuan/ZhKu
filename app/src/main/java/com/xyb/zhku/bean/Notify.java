package com.xyb.zhku.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by lenovo on 2018/9/29.
 */

public class Notify extends BmobObject {
//    public static final int TEACHING = 0;
//    public static final int STU_WORK = 1;
//    public static final int OTHER = 2;

    String title;//标题
    String content;//内容
    BmobFile file;//附件
    int type; //通知类型

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BmobFile getFile() {
        return file;
    }

    public void setFile(BmobFile file) {
        this.file = file;
    }
}
