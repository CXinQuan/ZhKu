package com.xyb.zhku.fragment.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.header.PhoenixHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xyb.zhku.R;
import com.xyb.zhku.adapter.TeacherHomeworkAdapter;
import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.bean.TeacherHomeWork;
import com.xyb.zhku.global.Constants;
import com.xyb.zhku.manager.ReleaseHomeworkObserver;
import com.xyb.zhku.manager.ReleaseHomeworkObserverManager;
import com.xyb.zhku.ui.LoginActivity;
import com.xyb.zhku.ui.ReleaseHomeworkActivity;
import com.xyb.zhku.ui.TeacherHomeWorkDetailActivity;
import com.xyb.zhku.utils.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by lenovo on 2018/9/22.
 */

public class TeacherHomeWorkFragment extends BaseFragment implements ReleaseHomeworkObserver {
    @BindView(R.id.rv_homework)
    RecyclerView rv_homework;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;

    TeacherHomeworkAdapter adapter;
    private List<TeacherHomeWork> list;
    private String objectid;


    protected int setView() {
        return R.layout.teacher_fragment_homework;
    }

    @Override
    protected void init(View view) {
        ReleaseHomeworkObserverManager.getInstance().add(this);
        list = new ArrayList<>();
        adapter = new TeacherHomeworkAdapter(mCtx, list);
//        adapter.setPositionListener(new TeacherHomeworkAdapter.PositionListener() {
//            @Override
//            public void position(float x) {
//                popupWindowX=x;
//            }
//        });d

        rv_homework.setAdapter(adapter);
        adapter.setOnChildClickListener(new TeacherHomeworkAdapter.OnChildClickListener() {
            @Override
            public void onChlidClick(RecyclerView recyclerView, View itemView, int position, Object data) {
                Intent intent = new Intent(mCtx, TeacherHomeWorkDetailActivity.class);
                intent.putExtra("TeacherHomeWork", list.get(position));
                startActivity(intent);
            }
        });
        adapter.setDeleteListener(new TeacherHomeworkAdapter.DeleteListener() {
            @Override
            public void delete(final int position) {
                // TODO: 2018/10/8    向服务器 删除 该条作业
                TeacherHomeWork teacherHomeWork = list.get(position);
                teacherHomeWork.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            // 删除成功
                            showToast("删除成功");
                            list.remove(position);
                            adapter.notifyItemRemoved(position);
                        } else {
                            // 删除失败
                            showToast("服务器繁忙，稍后重试");
                        }
                    }
                });
            }
        });
        initSmartRefreshLayout();
    }

    // TODO: 2018/9/28     初始化上拉刷新下拉加载更多
    private void initSmartRefreshLayout() {
        smartrefreshlayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(3000);//延迟3000毫秒后结束加载
                getTeacherHomework(objectid, list.size());
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(3000);//延迟3000毫秒后结束刷新
                getTeacherHomework(objectid, 0);
            }
        });
        smartrefreshlayout.setRefreshFooter(new ClassicsFooter(mCtx));
        smartrefreshlayout.setRefreshHeader(new PhoenixHeader(mCtx));
        smartrefreshlayout.setPrimaryColorsId(R.color.colorPrimary);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        list.add(new TeacherHomeWork("电子信息工程", "通信原理", "2018-9-25 17:01", "第一次实验课后来看到发红包了空间发的小伙伴流口水的回来看吧", "各位同学:\n" +
//                "\n" +
//                "大学好，本学期相关教学资源（电子课件PPT及实验指导）和作业的布置与上交全部采用该课程网络教学平台，请大家尽快按要求完成注册，加入相应的课程平台"));// String major, String subject, String time, String title, String content
//        adapter.notifyDataSetChanged();
        objectid = (String) SharePreferenceUtils.get(mCtx, Constants.OBJECTID, "");
        if (objectid.trim().equals("")) {
            showToast("账号信息已过期，请重新登录！");
            Log("账号信息已过期，请重新登录！");
            Intent intent = new Intent(mCtx, LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            getTeacherHomework(objectid, 0);
        }
    }

    // TODO: 2018/9/28      查询的时候需要通过老师的id去查询，学生才是通过专业去查询

    /**
     * 从服务器中获取自己发布的作业
     */
    protected void getTeacherHomework(String teacherId, final int skip) {
        // final List<TeacherHomeWork> list=new ArrayList<>();
        BmobQuery<TeacherHomeWork> query = new BmobQuery<TeacherHomeWork>();
        query.addWhereEqualTo("teacherId", teacherId)
                .setLimit(10)
                .setSkip(skip)
                .order("-createdAt")
                .findObjects(new FindListener<TeacherHomeWork>() {
                    public void done(List<TeacherHomeWork> object, BmobException e) {
                        if (e == null) {
                            if (object.size() > 0) {
                                if (skip <= 0) {
                                    list.clear();
                                }
                                list.addAll(object);
                                adapter.notifyDataSetChanged();
                            } else {
                              //  showToast("没有更多数据...");
                                smartrefreshlayout.setNoMoreData(true);
                            }
                        } else {
                            showToast("服务器繁忙...");  // 如果该老师从来未发布过作业，那么就会触发此代码
                        }
                        smartrefreshlayout.finishLoadMore(800);
                    }
                });
    }

    @OnClick({R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                // TODO: 2018/9/28    发布作业 
                Intent intent = new Intent(mCtx, ReleaseHomeworkActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 发布作业成功后返回后  添加 一行作业
     *
     * @param teacherHomeWork
     */
    @Override
    public void update(TeacherHomeWork teacherHomeWork) {
        if (teacherHomeWork != null) {
            list.add(0, teacherHomeWork);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        ReleaseHomeworkObserverManager.getInstance().remove(this);
        super.onDestroy();
    }
}
