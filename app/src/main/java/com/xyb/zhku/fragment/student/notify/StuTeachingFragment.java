package com.xyb.zhku.fragment.student.notify;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.base.BaseStuNotifyFragment;
import com.xyb.zhku.bean.StuNotify;

/**
 * Created by lenovo on 2018/10/1.
 */

public class StuTeachingFragment extends BaseStuNotifyFragment {

    @Override
    protected void initHeadView(ImageView iv_type_icon, TextView tv_type_text) {
        iv_type_icon.setImageResource(R.mipmap.teaching);
        tv_type_text.setText("教学");
    }

    @Override
    protected void initRefreshLayoutFooterAndHeader(SmartRefreshLayout smartrefreshlayout) {
        smartrefreshlayout.setRefreshHeader(new WaterDropHeader(mCtx));

    }

    @Override
    protected int dataType() {
        return StuNotify.TEACHING;
    }

    @Override
    protected int setItemView() {
        return R.layout.notify_teaching_item;
    }
}
