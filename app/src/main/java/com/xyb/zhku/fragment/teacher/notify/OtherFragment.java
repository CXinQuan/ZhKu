package com.xyb.zhku.fragment.teacher.notify;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.header.DropBoxHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseNotifyFragment;
import com.xyb.zhku.bean.TeacherNotify;

/**
 * Created by lenovo on 2018/9/30.
 */

public class OtherFragment extends BaseNotifyFragment {
    @Override
    protected void initHeadView(ImageView iv_type_icon, TextView tv_type_text) {
        iv_type_icon.setImageResource(R.mipmap.other);
        tv_type_text.setText("其它");
    }

    @Override
    protected void initRefreshLayoutFooterAndHeader(SmartRefreshLayout smartrefreshlayout) {
        smartrefreshlayout.setRefreshFooter(new ClassicsFooter(mCtx));
        smartrefreshlayout.setRefreshHeader(new DropBoxHeader(mCtx));
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(manager);
    }

    @Override
    protected int dataType() {
        return TeacherNotify.OTHER;
    }

    @Override
    protected int setItemView() {
        return R.layout.notify_other_item;
    }
}
