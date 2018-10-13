package com.xyb.zhku.fragment.student.notify;

import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.base.BaseStuNotifyFragment;
import com.xyb.zhku.bean.StuNotify;
/**
 * Created by lenovo on 2018/10/1.
 */

public class StuWorkFragment extends BaseStuNotifyFragment {

    @Override
    protected void initHeadView(ImageView iv_type_icon, TextView tv_type_text) {
        iv_type_icon.setImageResource(R.mipmap.stu_work);
        tv_type_text.setText("学生工作");
    }

    @Override
    protected void initRefreshLayoutFooterAndHeader(SmartRefreshLayout smartrefreshlayout) {
        smartrefreshlayout.setRefreshHeader(new BezierCircleHeader(mCtx));

    }

    @Override
    protected int dataType() {
        return StuNotify.STU_WORK;
    }

    @Override
    protected int setItemView() {
        return R.layout.notify_science_item;
    }
}
