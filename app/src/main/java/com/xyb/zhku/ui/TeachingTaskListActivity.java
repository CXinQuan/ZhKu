package com.xyb.zhku.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.bean.TeachingTask;
import com.xyb.zhku.manager.TeachingTaskObserver;
import com.xyb.zhku.manager.TeachingTaskUpdateManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class TeachingTaskListActivity extends BaseActivity implements TeachingTaskObserver {

    @BindView(R.id.tv_teacher_name)
    TextView tvTeacherName;
    @BindView(R.id.tv_teacher_id)
    TextView tvTeacherId;

    @BindView(R.id.recyclerview_AllTeachingTask)
    RecyclerView recyclerviewAllTeachingTask;

    @BindView(R.id.iv_head_back)
    ImageView ivHeadBack;
    @BindView(R.id.tv_head_content)
    TextView tvHeadContent;
    private TeachingTask task;
    private List<TeachingTask.SubjectClass> subjectClasses;
    TeachingTaskListAdapter adapter;


    // TODO: 2018/10/21    缺少  item View
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        task = (TeachingTask) getIntent().getSerializableExtra("TeachingTask");
        TeachingTaskUpdateManager.getInstance().add(this);
        initView();
        subjectClasses = task.getList();
    }

    private void initView() {
        if (task != null) {
            tvHeadContent.setText("教学任务详情");
            tvTeacherName.setText("教师姓名：" + task.getTeacherName());
            tvTeacherId.setText("教学工号：" + task.getTeacherId());
            adapter = new TeachingTaskListAdapter();
            recyclerviewAllTeachingTask.setAdapter(adapter);

        }
    }

    public int setContentViewLayout() {
        return R.layout.activity_teaching_task_list;
    }

    @OnClick({R.id.iv_head_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_head_back:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
        }
    }

    @Override
    public void update(TeachingTask teachingtask) {
        task=teachingtask;//  因为 task 已经改变了， 如果不修改的话，那么点击另外的则会将修改前的给传过去
        subjectClasses = teachingtask.getList();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        TeachingTaskUpdateManager.getInstance().remove(this);
        super.onDestroy();
    }

    // TODO: 2018/10/22   填充 recyclerviewAllTeachingTask
    class TeachingTaskListAdapter extends RecyclerView.Adapter<TeachingTaskListViewHolder> {

        @Override
        public TeachingTaskListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_teachingtask_list_item, parent, false);
            return new TeachingTaskListViewHolder(view);
        }

        public void onBindViewHolder(TeachingTaskListViewHolder holder, final int position) {
            holder.setPosition(position);
            final TeachingTask.SubjectClass subjectClass = subjectClasses.get(position);
            String major = subjectClass.getMajor();
            String year = subjectClass.getYear();
            String subject = subjectClass.getSubject();
            holder.tvEnrollmentYear.setText("年级：" + year);
            holder.tvMajor.setText("专业：" + major);
            holder.tvSubject.setText("课程：" + subject);
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2018/10/22   删除操作  （Bmob更新）
                    AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                    builder.setIcon(R.mipmap.delete)
                            .setMessage("是否删除该教学任务")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    task.getList().remove(position);
                                    task.update(task.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                subjectClasses.remove(position);
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                showToast("服务器离家出走了");
                                            }
                                        }
                                    });
                                }
                            }).setNegativeButton("取消", null)
                            .create()
                            .show();
                }
            });
        }

        public int getItemCount() {
            return subjectClasses.size();
        }
    }

    class TeachingTaskListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_subject)
        TextView tvSubject;
        @BindView(R.id.tv_major)
        TextView tvMajor;
        @BindView(R.id.tv_enrollment_year)
        TextView tvEnrollmentYear;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        int position;

        public void setPosition(int position) {
            this.position = position;
        }

        public TeachingTaskListViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2018/10/21   显示该教师的 教学任务详情
                    Intent intent = new Intent(mCtx, TeachingTaskDetailActivity.class);
                    intent.putExtra("subjectClass", subjectClasses.get(position));
                    intent.putExtra("TeachingTask",task);
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
            });
        }
    }


}
