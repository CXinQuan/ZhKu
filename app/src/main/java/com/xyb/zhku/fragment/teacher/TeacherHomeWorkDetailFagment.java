package com.xyb.zhku.fragment.teacher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.bean.TeacherHomeWork;
import com.xyb.zhku.ui.EnclosureShowActivity;

import butterknife.BindView;
@SuppressLint("ValidFragment")
public class TeacherHomeWorkDetailFagment extends BaseFragment {
    TeacherHomeWork homeWork;

    @BindView(R.id.tv_hw_detail_title)
    TextView tv_hw_detail_title;
    @BindView(R.id.tv_subject)
    TextView tv_subject;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_enclosure)
    TextView tv_enclosure;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.cv_enclosure)
    CardView cv_enclosure;
    @BindView(R.id.cv_content)
    CardView cv_content;

    public TeacherHomeWorkDetailFagment(TeacherHomeWork homeWork) {
        this.homeWork = homeWork;
    }

    @Override
    protected int setView() {
        return R.layout.teacher_fragment_homework_detail;
    }

    @Override
    protected void init(View view) {
        if (homeWork != null) {
            tv_hw_detail_title.setText(homeWork.getTitle());
            tv_subject.setText("科目：" + homeWork.getSubject());
            tv_time.setText("时间：" + homeWork.getCreatedAt());
            if (homeWork.getContent() != null) {
                cv_content.setVisibility(View.VISIBLE);
                tv_content.setText(homeWork.getContent());
            } else {
                cv_content.setVisibility(View.GONE);
            }
            if (null != homeWork.getFile()) {
                cv_enclosure.setVisibility(View.VISIBLE);
                tv_enclosure.setText(homeWork.getFile().getFilename());
                cv_enclosure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 2018/9/25     点击附件之后的响应
                        Intent intent = new Intent(mCtx, EnclosureShowActivity.class);
                        intent.putExtra("file", homeWork.getFile());
                        //intent.putExtra("fileUrl",homeWork.getFile().getUrl());
                        startActivity(intent);
                    }
                });
            } else {
                cv_enclosure.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

}
