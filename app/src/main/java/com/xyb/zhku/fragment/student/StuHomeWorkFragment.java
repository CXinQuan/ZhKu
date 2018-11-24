package com.xyb.zhku.fragment.student;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.header.PhoenixHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.bean.TeacherHomeWork;
import com.xyb.zhku.global.Constants;
import com.xyb.zhku.manager.SubmitHomeworkObserver;
import com.xyb.zhku.manager.SubmitHomeworkObserverManager;
import com.xyb.zhku.ui.LoginActivity;
import com.xyb.zhku.ui.StuSubmitHomeWorkActivity;
import com.xyb.zhku.utils.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by lenovo on 2018/10/1.
 */

public class StuHomeWorkFragment extends BaseFragment implements SubmitHomeworkObserver {
    @BindView(R.id.rv_homework)
    RecyclerView rv_homework;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;

    // TeacherHomeworkAdapter adapter;
    StuHomeWorkAdapter adapter;
    private List<TeacherHomeWork> list;
    private String major;
    private String enrollment_year;
    private int classNubmer;
    private String school_number;

    protected int setView() {
        return R.layout.stu_fragment_homework;
    }

    @Override
    protected void init(View view) {
        SubmitHomeworkObserverManager.getInstance().add(this);
        list = new ArrayList<>();
        //  fab.hide();
        fab.setImageResource(R.mipmap.up);
        //  adapter = new TeacherHomeworkAdapter(mCtx, list);
        GridLayoutManager layoutManager = new GridLayoutManager(mCtx, 1);
        rv_homework.setLayoutManager(layoutManager);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position <= 5) {
                    fab.hide();
                } else {
                    fab.show();
                }
                return 1;
            }
        });

        adapter = new StuHomeWorkAdapter();
        school_number = (String) SharePreferenceUtils.get(mCtx, Constants.SCHOOL_NUMBER, "");
        if (school_number.equals("")) {
            showToast("账户异常，建议退出后重新登录");
        }

        rv_homework.setAdapter(adapter);// TODO: 2018/10/1  另外写一个 Adapter 因为需要根据是否提交进行显示
       /* adapter.setOnChildClickListener(new TeacherHomeworkAdapter.OnChildClickListener() {
            @Override
            public void onChlidClick(RecyclerView recyclerView, View itemView, int position, Object data) {
//                Intent intent = new Intent(mCtx, TeacherHomeWorkDetailActivity.class);
//                intent.putExtra("TeacherHomeWork", list.get(position));
//                startActivity(intent);
                // T 2018/10/1    进入作业详情 进行提交作业


            }
        });*/
        initSmartRefreshLayout();
    }


    //  2018/9/28     初始化上拉刷新下拉加载更多
    private void initSmartRefreshLayout() {
        smartrefreshlayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(3000);//延迟3000毫秒后结束加载
                getTeacherHomework(major, enrollment_year, classNubmer, list.size());
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(3000);//延迟3000毫秒后结束刷新
                getTeacherHomework(major, enrollment_year, classNubmer, 0);
            }
        });
        smartrefreshlayout.setRefreshFooter(new ClassicsFooter(mCtx));
        smartrefreshlayout.setRefreshHeader(new PhoenixHeader(mCtx));
        smartrefreshlayout.setPrimaryColorsId(R.color.colorPrimary);
    }


    @OnClick({R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                rv_homework.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        major = (String) SharePreferenceUtils.get(mCtx, Constants.MAJOR, "");
        enrollment_year = (String) SharePreferenceUtils.get(mCtx, Constants.ENROLLMENT_YEAR, "");
        classNubmer = (int) SharePreferenceUtils.get(mCtx, Constants.UCLASS, -1);

        if (major.trim().equals("") || enrollment_year.equals("") || classNubmer == -1) {
            showToast("账号信息已过期，请重新登录！");
            Log("账号信息已过期，请重新登录！");
            Intent intent = new Intent(mCtx, LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            getTeacherHomework(major, enrollment_year, classNubmer, 0);
        }
    }

    /**
     * 从服务器中获取自己专业的作业
     */
    protected void getTeacherHomework(String major, String enrollment_year, int classNumber, final int skip) {
        BmobQuery<TeacherHomeWork> query = new BmobQuery<TeacherHomeWork>();
        List<Integer> classNum = new ArrayList<>();
        classNum.add(classNumber);
        query.addWhereEqualTo("major", major)
                .addWhereEqualTo("enrollment_year", enrollment_year)
                .addWhereContainsAll("classList", classNum)
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
                                sortList();
                                adapter.notifyDataSetChanged();
                            } else {
                                //   showToast("没有更多数据...");
                                smartrefreshlayout.setNoMoreData(true);
                            }
                        } else {
                            showToast("服务器离家出走了");
                        }
                        // smartrefreshlayout.finishRefresh();
                        smartrefreshlayout.finishLoadMore(800);
                    }
                });
    }

    /**
     * 将已 完成和未完成 的作业 分开
     */
    private void sortList() {
        List<TeacherHomeWork> noFinish = new ArrayList<>();
        List<TeacherHomeWork> hasFinish = new ArrayList<>();

        for (TeacherHomeWork homeWork : list) {
            if (homeWork.getStu_number_list().contains(school_number)) {
                hasFinish.add(homeWork);
            } else {
                noFinish.add(homeWork);
            }
        }
        list.clear();
        list.addAll(noFinish);
        list.addAll(hasFinish);
    }

    @Override
    public void update(TeacherHomeWork teacherHomeWork, int position) {
        //  list.get(position).getStu_number_list().add((String) SharePreferenceUtils.get(mCtx, Constants.SCHOOL_NUMBER, ""));
        list.get(position).setStu_number_list(teacherHomeWork.getStu_number_list());
        sortList();
        adapter.notifyItemChanged(position+1); // 需要更新的是 第 position+1 项
    }

    public static final int HEAD = 0;
    public static final int BODY = 1;

    class StuHomeWorkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEAD) {
                View view = LayoutInflater.from(mCtx).inflate(R.layout.stu_homework_head, parent, false);
                return new StuHomeWorkViewHeadHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_homework_item, parent, false);
                return new StuHomeWorkViewBodyHolder(view);
            }
        }

        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) != HEAD) {
                position--;  //  只需要在此进减去1即可
                ((StuHomeWorkViewBodyHolder) holder).setPosition(position);
                TeacherHomeWork homeWork = list.get(position);
                List<String> stu_number_list = homeWork.getStu_number_list();
                if (stu_number_list != null && stu_number_list.size() > 0) {
                    if (stu_number_list.contains(school_number)) { // 已提交
                        ((StuHomeWorkViewBodyHolder) holder).setHasSubmit(true);
                        ((StuHomeWorkViewBodyHolder) holder).cv.setCardBackgroundColor(Color.parseColor("#ffcccc"));
                    } else {
                        ((StuHomeWorkViewBodyHolder) holder).setHasSubmit(false);
                        ((StuHomeWorkViewBodyHolder) holder).cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    }
                } else {
                    ((StuHomeWorkViewBodyHolder) holder).setHasSubmit(false);
                    ((StuHomeWorkViewBodyHolder) holder).cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                }
                ((StuHomeWorkViewBodyHolder) holder).iv_homework.setImageResource(R.mipmap.book);
//                ((StuHomeWorkViewBodyHolder) holder).tv_major.setText("专业：" + homeWork.getMajor());
                ((StuHomeWorkViewBodyHolder) holder).tv_major.setVisibility(View.GONE);
                ((StuHomeWorkViewBodyHolder) holder).tv_subject.setText("科目：" + homeWork.getSubject());
                ((StuHomeWorkViewBodyHolder) holder).tv_time.setText("时间：" + homeWork.getCreatedAt());
                ((StuHomeWorkViewBodyHolder) holder).tv_title.setText("内容：" + homeWork.getTitle());
            }
        }

        public int getItemViewType(int position) {
            if (position == 0) {
                return HEAD;
            } else {
                return BODY;
            }
            //   return super.getItemViewType(position);
        }

        public int getItemCount() {
            return list.size() + 1;
        }

    }

    class StuHomeWorkViewBodyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_homework)
        ImageView iv_homework;
        @BindView(R.id.tv_subject)
        TextView tv_subject;
        @BindView(R.id.tv_major)
        TextView tv_major;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.cv)
        CardView cv;
        int position;
        boolean hasSubmit;

        public void setPosition(int position) {
            this.position = position;
        }

        public void setHasSubmit(boolean hasSubmit) {
            this.hasSubmit = hasSubmit;
        }

        public StuHomeWorkViewBodyHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, StuSubmitHomeWorkActivity.class); // TODO: 2018/10/2   进入作业详情页，进行提交作业
                    intent.putExtra("position", position);
                    intent.putExtra("TeacherHomeWork", list.get(position));
                    intent.putExtra("hasSubmit", hasSubmit);

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Pair<View, String> pair1 = Pair.create(itemView.findViewById(R.id.tv_title), "tv_title");
                        Pair<View, String> pair2 = Pair.create(itemView.findViewById(R.id.tv_time), "tv_time");
                        Pair<View, String> pair3 = Pair.create(itemView.findViewById(R.id.tv_subject), "tv_subject");
                        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pair1, pair2, pair3).toBundle();
                        startActivity(intent, bundle);
                    } else {
                        startActivity(intent);
                    }

                    //   startActivity(intent);//ForResult , 111
                }
            });
        }
    }

    class StuHomeWorkViewHeadHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        ImageView imageView;

        public StuHomeWorkViewHeadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onDestroy() {
        SubmitHomeworkObserverManager.getInstance().remove(this);
        super.onDestroy();
    }
}
