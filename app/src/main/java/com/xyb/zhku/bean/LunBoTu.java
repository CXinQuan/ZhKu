package com.xyb.zhku.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by lenovo on 2018/10/12.
 */

public class LunBoTu extends BmobObject {
    BmobFile image; // 图片文件
    String description; // 图片描述

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }
}
