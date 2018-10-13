package com.xyb.zhku.fragment.student.notify;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.header.DropBoxHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.base.BaseStuNotifyFragment;
import com.xyb.zhku.bean.StuNotify;

/**
 * Created by lenovo on 2018/10/1.
 */

public class StuOtherFragment extends BaseStuNotifyFragment {

    @Override
    protected void initHeadView(ImageView iv_type_icon, TextView tv_type_text) {
        iv_type_icon.setImageResource(R.mipmap.other);
        tv_type_text.setText("其它");
    }

    @Override
    protected void initRefreshLayoutFooterAndHeader(SmartRefreshLayout smartrefreshlayout) {
        smartrefreshlayout.setRefreshHeader(new DropBoxHeader(mCtx));
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(manager);
    }

    @Override
    protected int dataType() {
        return StuNotify.OTHER;
    }

    @Override
    protected int setItemView() {
        return R.layout.notify_other_item;
    }
}
