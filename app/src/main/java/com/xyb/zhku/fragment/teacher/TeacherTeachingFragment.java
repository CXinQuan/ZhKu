package com.xyb.zhku.fragment.teacher;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.header.DeliveryHeader;
import com.scwang.smartrefresh.header.TaurusHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xyb.zhku.R;
import com.xyb.zhku.adapter.TeacherPartyBuildRecycleAdapter;
import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.bean.TeacherNotify;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by lenovo on 2018/9/22.
 */

public class TeacherTeachingFragment extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.iv_type_icon)
    ImageView iv_type_icon;
    @BindView(R.id.tv_type_text)
    TextView tv_type_text;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;


    List<TeacherNotify> lists = new ArrayList<>();
    TeachingAdapter adapter;
    boolean isFirst = true;

    @Override
    protected int setView() {
        return R.layout.teacher_fragment_notify;
    }

    @Override
    protected void init(View view) {
        adapter = new TeachingAdapter();
        recyclerview.setAdapter(adapter);
        iv_type_icon.setImageResource(R.mipmap.teaching);
        tv_type_text.setText("教学");
        initSmartRefreshLayout();
    }

    private void initSmartRefreshLayout() {
        smartrefreshlayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(3000);//延迟3000毫秒后结束刷新
                getTeacherNotifyData(lists.size());

            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(3000);//延迟3000毫秒后结束刷新
                getTeacherNotifyData(0);
            }
        });
        smartrefreshlayout.setRefreshFooter(new ClassicsFooter(mCtx));
        smartrefreshlayout.setRefreshHeader(new BezierCircleHeader(mCtx));
        smartrefreshlayout.setPrimaryColorsId(R.color.colorPrimary);
    }
    @Override
    protected void initData(Bundle savedInstanceState) {
        if (isFirst) {
            getTeacherNotifyData(0);
            isFirst = false;
        }
    }

    private void getTeacherNotifyData(final int skip) {
        BmobQuery<TeacherNotify> query = new BmobQuery<TeacherNotify>();
        query.order("-updatedAt") //  根据updatedAt 时间  从上到下  开始  返回  5条数据
                .setLimit(15)
                .setSkip(skip) // 忽略前10条数据（即第一页数据结果）
                .addWhereEqualTo("type", TeacherNotify.TEACHING)
                .findObjects(new FindListener<TeacherNotify>() {
                    public void done(List<TeacherNotify> object, BmobException e) {
                        if (e == null) {
                            if (object.size() > 0) {
                                if (skip <= 0) {
                                    lists.clear();
                                }
                                lists.addAll(object);
                                adapter.notifyDataSetChanged();
                            } else {
                                showToast("没有更多数据...");
                            }
                        } else {
                            showToast("服务器繁忙！");
                        }
                    }
                });
    }

    class TeachingAdapter extends RecyclerView.Adapter<TeachingViewHolder> {
        @Override
        public TeachingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_teaching_item, parent, false);
            return new TeachingViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TeachingViewHolder holder, int position) {
            TeacherNotify notify = lists.get(position);
            holder.tv_notify_content.setText(notify.getContent());
            holder.tv_notify_time.setText(notify.getCreatedAt());
            holder.tv_notify_title.setText(notify.getTitle());
        }

        @Override
        public int getItemCount() {
            return lists.size();
        }
    }

    class TeachingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_notify_time)
        TextView tv_notify_time;
        @BindView(R.id.tv_notify_title)
        TextView tv_notify_title;
        @BindView(R.id.tv_notify_content)
        TextView tv_notify_content;

        public TeachingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
