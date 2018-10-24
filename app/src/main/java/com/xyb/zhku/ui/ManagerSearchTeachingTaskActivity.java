package com.xyb.zhku.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.bean.TeachingTask;
import com.xyb.zhku.manager.TeachingTaskObserver;
import com.xyb.zhku.manager.TeachingTaskUpdateManager;
import com.xyb.zhku.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static com.xyb.zhku.R.id.et_condition;
import static com.xyb.zhku.R.id.iv_cancle;
import static com.xyb.zhku.R.id.iv_head_back;

public class ManagerSearchTeachingTaskActivity extends BaseActivity implements TeachingTaskObserver {

    @BindView(iv_head_back)
    ImageView ivHeadBack;
    @BindView(et_condition)
    EditText etCondition;
    @BindView(iv_cancle)
    ImageView ivCancle;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_not_result)
    TextView tvNotResult;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    List<TeachingTask> teachingTasks;
    TeachingTaskAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    private void initView() {
        TeachingTaskUpdateManager.getInstance().add(this);
        teachingTasks = new ArrayList<>();
        adapter = new TeachingTaskAdapter();
        recyclerview.setAdapter(adapter);
        etCondition.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                //当文本有变化后,如果文本内容不是空的,可以显示x号让,用户可以点击删除
                String phone = etCondition.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    ivCancle.setVisibility(View.VISIBLE);//显示x,提示用户可以删除
                } else {
                    ivCancle.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    public int setContentViewLayout() {
        return R.layout.activity_manager_search_teaching_task;
    }

    @OnClick({iv_head_back, iv_cancle, R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case iv_head_back:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
            case iv_cancle:
                etCondition.setText("");
                ivCancle.setVisibility(View.GONE);
                break;

            case R.id.tv_search:
                // TODO: 2018/10/21   搜索
                if (UIUtils.isEmtpy(etCondition)) {
                    showToast("请输入教师工号或姓名");
                    return;
                }
                getTeachingTask(etCondition.getText().toString());
                break;


        }
    }

    /**
     * 根据教师名字 或者 Id 获取教学任务
     *
     * @param text
     */
    private void getTeachingTask(String text) {
        BmobQuery<TeachingTask> query1 = new BmobQuery<TeachingTask>();
        query1.addWhereEqualTo("teacherName", text);
        BmobQuery<TeachingTask> query2 = new BmobQuery<TeachingTask>();
        query2.addWhereEqualTo("teacherId", text);

        List<BmobQuery<TeachingTask>> queries = new ArrayList<BmobQuery<TeachingTask>>();
        queries.add(query1);
        queries.add(query2);

        BmobQuery<TeachingTask> query = new BmobQuery<TeachingTask>();
        query.or(queries);

        query.findObjects(new FindListener<TeachingTask>() {
            public void done(List<TeachingTask> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        progressbar.setVisibility(View.GONE);
                        tvNotResult.setVisibility(View.GONE);
                        recyclerview.setVisibility(View.VISIBLE);
                        if (teachingTasks.size() > 0) {
                            teachingTasks.clear();
                        }
                        teachingTasks.addAll(object);
                        adapter.notifyDataSetChanged();

                    } else {
                        progressbar.setVisibility(View.GONE);
                        tvNotResult.setVisibility(View.VISIBLE);
                        recyclerview.setVisibility(View.GONE);
                    }
                } else {
                    //showToast("服务器离家出走了");
                    progressbar.setVisibility(View.GONE);
                    tvNotResult.setVisibility(View.VISIBLE);
                    tvNotResult.setText("服务器离家出走了");
                    recyclerview.setVisibility(View.GONE);
                }
            }
        });
    }

    class TeachingTaskAdapter extends RecyclerView.Adapter<TeachingTaskViewHolder> {

        @Override
        public TeachingTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_teacher_recyclerview_item, parent, false);
            return new TeachingTaskViewHolder(view);
        }

        public void onBindViewHolder(TeachingTaskViewHolder holder, int position) {
            TeachingTask task = teachingTasks.get(position);
            holder.tvTeacherName.setText("教师姓名：" + task.getTeacherName());
            holder.tvTeacherId.setText("教学工号：" + task.getTeacherId());
            holder.setPosition(position);
        }

        public int getItemCount() {
            return teachingTasks.size();
        }
    }

    class TeachingTaskViewHolder extends RecyclerView.ViewHolder {
        //        @BindView(R.id.iv_teacher)
        //        ImageView ivTeacher;
        @BindView(R.id.tv_teacher_name)
        TextView tvTeacherName;
        @BindView(R.id.tv_teacher_id)
        TextView tvTeacherId;
        int position;

        public void setPosition(int position) {
            this.position = position;
        }

        public TeachingTaskViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2018/10/21   显示该教师的 教学任务
                    Intent intent = new Intent(mCtx, TeachingTaskListActivity.class);
                    intent.putExtra("TeachingTask", teachingTasks.get(position));
                    intent.putExtra("SearchTeachingTask_Position", position);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        Pair<View, String> teachingTask_item = Pair.create(itemView, "teachingTask_item");
                        //    Pair<ImageView, String> iv_head_back = Pair.create(ivHeadBack, "iv_head_back");
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ManagerSearchTeachingTaskActivity.this,
                                teachingTask_item).toBundle());
//                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ManagerSearchTeachingTaskActivity.this,
//                                Pair.create(itemView.findViewById(R.id.tab_icon_iv), "image"),
//                                Pair.create(itemView.findViewById(R.id.tab_icon_iv), "title"))
//                                .toBundle());

                    } else {
                        startActivity(intent);
                    }

                }
            });
        }
    }


    @Override
    public void update(TeachingTask teachingtask) {
        //  点击进去后，进行添加 或者 修改后，该 TeachingTask 则已经改变了 都需要进行更新
/*        for (TeachingTask teachingTask : teachingTasks) {
            if (teachingTask.getTeacherId().equals(teachingtask.getTeacherId())) {
                teachingTask = teachingtask;
            }
        }*/

        for (int i = 0; i < teachingTasks.size(); i++) {
            if (teachingTasks.get(i).getTeacherId().equals(teachingtask.getTeacherId())) {
//                teachingTasks.add(i,teachingtask);  不能这么做，因为先在0前面插入一个，此时 插入的对象为0，
//                teachingTasks.remove(i); 再移除，相当于没有改变
                teachingTasks.get(i).setList(teachingtask.getList());
                teachingTasks.get(i).setTeacherName(teachingtask.getTeacherName());
                teachingTasks.get(i).setTeacherId(teachingtask.getTeacherId());

            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        TeachingTaskUpdateManager.getInstance().remove(this);
        super.onDestroy();
    }

}