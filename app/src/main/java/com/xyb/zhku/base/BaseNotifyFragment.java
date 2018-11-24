package com.xyb.zhku.base;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xyb.zhku.R;
import com.xyb.zhku.bean.TeacherNotify;
import com.xyb.zhku.ui.NotifyDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by lenovo on 2018/9/30.
 */

public abstract class BaseNotifyFragment extends BaseFragment {

    @BindView(R.id.recyclerview)
    public RecyclerView recyclerview;
    @BindView(R.id.iv_type_icon)
    ImageView iv_type_icon;
    @BindView(R.id.tv_type_text)
    TextView tv_type_text;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.pb)
    ProgressBar pb;

    List<TeacherNotify> lists = new ArrayList<>();
    NotifyAdapter adapter;
    boolean isFirst = true;

    @Override
    protected int setView() {
        return R.layout.teacher_fragment_notify;
    }

    @Override
    protected void init(View view) {
        adapter = new NotifyAdapter();
        recyclerview.setAdapter(adapter);
        // iv_type_icon.setImageResource(R.mipmap.teaching);
        // tv_type_text.setText("教学");
        initHeadView(iv_type_icon, tv_type_text);
        initSmartRefreshLayout();
        // pb.setVisibility(View.VISIBLE); 不能在此显示，只有加载数据的时候才显示，因为重新加载界面不一定重新加载数据，所以就导致不消失
        // 会 onViewCreated  但不会 onActivityCreated
    }

    /**
     * 初始化 头部 图标 和 文字
     */
    protected abstract void initHeadView(ImageView iv_type_icon, TextView tv_type_text);

    /**
     * 初始化RefreshLayout的头和脚部样式
     *
     * @param smartrefreshlayout
     */
    protected abstract void initRefreshLayoutFooterAndHeader(SmartRefreshLayout smartrefreshlayout);

    /**
     * 向服务器 请求的 通知数据类型
     *
     * @return
     */
    protected abstract int dataType();

    /**
     * 子布局  View
     *
     * @return
     */
    protected abstract int setItemView();

    private void initSmartRefreshLayout() {
        smartrefreshlayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(3000);//延迟3000毫秒后结束刷新  相当于 网络请求超时时间为3秒
                //  getNotifyData(0);
                refreshNotifyData();
            }

            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(3000);//延迟3000毫秒后结束刷新
                getNotifyData(lists.size());
            }
        });
        //smartrefreshlayout.setRefreshFooter(new ClassicsFooter(mCtx));
        //smartrefreshlayout.setRefreshHeader(new BezierCircleHeader(mCtx));
        //smartrefreshlayout.setPrimaryColorsId(R.color.colorPrimary);
        initRefreshLayoutFooterAndHeader(smartrefreshlayout);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (isFirst) {
            pb.setVisibility(View.VISIBLE);  //只有加载数据的时候才将Progress打开 因为使用HashMap缓存了Fragment，重新加载界面不一定重新加载数据
            getNotifyData(0);
            isFirst = false;
        }
    }

//    @Override
//    public void onStart() {
//        //如果是第一次进入，则无论是否请求到数据，都在3秒后让其消失，如果是后来进来的，则马上让其消失
////        long time=0;
////        if (isFirst) {
////            time=3000;
////        }else{
////            time=0;
////        }
//        new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                pb.setVisibility(View.GONE);
//                return false;
//            }
//        }).sendEmptyMessageDelayed(0, isFirst ? 3000 : 0);//time
//        super.onStart();
//    }

    private void getNotifyData(final int skip) {
        BmobQuery<TeacherNotify> query = new BmobQuery<TeacherNotify>();
        query.order("-createdAt") //  根据updatedAt 时间  从上到下  开始  返回  5条数据
                .setLimit(10)
                .setSkip(skip) // 忽略前10条数据（即第一页数据结果）
                .addWhereEqualTo("type", dataType())//TeacherNotify.TEACHING
                .findObjects(new FindListener<TeacherNotify>() {
                    public void done(List<TeacherNotify> object, BmobException e) {
                        if (e == null) {
                            if (object.size() > 0) {
                                if (skip <= 0) {
                                    lists.clear();
                                }
                                lists.addAll(object);
                                adapter.notifyDataSetChanged();
                                pb.setVisibility(View.GONE);
                            } else {
                                //  showToast("没有更多数据...");
                                smartrefreshlayout.setNoMoreData(true);

                            }
                        } else {
                            showToast("服务器繁忙！");
                        }
                        pb.setVisibility(View.GONE);
                        smartrefreshlayout.finishLoadMore(800);
                    }
                });
    }

    /**
     * 下拉刷新  从服务器获取数据
     */
    private void refreshNotifyData() {
        if (lists.size() <= 0) {   // 网络错误，之前没有获取到数据，现在有网络了，刷新，则先是
            getNotifyData(0);
        } else {
            BmobQuery<TeacherNotify> query = new BmobQuery<TeacherNotify>();
            query.order("-createdAt") //  createdAt 时间  从上到下  开始  返回  10条数据
                    .setLimit(10)
                    .addWhereGreaterThanOrEqualTo("createdAt", lists.get(0).getCreatedAt())  //比第一个item的时间还大的，那么就是要刷新出来的数据
                    .addWhereEqualTo("type", dataType())//TeacherNotify.TEACHING
                    .findObjects(new FindListener<TeacherNotify>() {
                        public void done(List<TeacherNotify> object, BmobException e) {
                            if (e == null) {
                                if (object.size() > 0) {
                                    lists.addAll(0, object);
                                    adapter.notifyDataSetChanged();
                                    // pb.setVisibility(View.GONE);

                                } else {
                                    showToast("已是最新数据");
                                }
                                // smartrefreshlayout.finishRefresh();
                            } else {
                                showToast("服务器繁忙！");  // 查询不到  e  也会为null ,是否有结果还是要看object 的size

                            }
                            smartrefreshlayout.finishRefresh(800);
                            // pb.setVisibility(View.GONE);
                        }
                    });
        }

    }


    class NotifyAdapter extends RecyclerView.Adapter<NotifyViewHolder> {
        @Override
        public NotifyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(setItemView(), parent, false);//R.layout.notify_teaching_item
            return new NotifyViewHolder(view);
        }

        public void onBindViewHolder(NotifyViewHolder holder, int position) {
            TeacherNotify notify = lists.get(position);
            holder.setPosition(position);
            holder.tv_notify_content.setText(notify.getContent() == null ? "" : notify.getContent());
            holder.tv_notify_time.setText(notify.getCreatedAt());
            holder.tv_notify_title.setText(notify.getTitle());
        }

        @Override
        public int getItemCount() {
            return lists.size();
        }
    }

    class NotifyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_notify_time)
        TextView tv_notify_time;
        @BindView(R.id.tv_notify_title)
        TextView tv_notify_title;
        @BindView(R.id.tv_notify_content)
        TextView tv_notify_content;
        int position;

        public void setPosition(int position) {
            this.position = position;
        }

        public NotifyViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //initTvContent(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, NotifyDetailActivity.class);
                    intent.putExtra("Notify", lists.get(position));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                                Pair.create(itemView.findViewById(R.id.tv_notify_time), "textViewTime"),
                                Pair.create(itemView.findViewById(R.id.tv_notify_title), "textViewTitle"),
                                Pair.create(itemView.findViewById(R.id.tv_notify_content), "textViewContent")
                        ).toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
