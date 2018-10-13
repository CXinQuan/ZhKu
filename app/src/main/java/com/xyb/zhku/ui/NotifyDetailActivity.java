package com.xyb.zhku.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.bean.Notify;
import com.xyb.zhku.bean.StuNotify;
import com.xyb.zhku.bean.TeacherNotify;

import butterknife.BindView;
import butterknife.OnClick;

public class NotifyDetailActivity extends BaseActivity {
    @BindView(R.id.tv_notify_title)
    TextView tv_notify_title;
    @BindView(R.id.tv_notify_time)
    TextView tv_notify_time;
    @BindView(R.id.tv_notify_filename)
    TextView tv_notify_filename;
    @BindView(R.id.tv_notify_content)
    TextView tv_notify_content;
    @BindView(R.id.cv_file)
    CardView cv_file;

    @BindView(R.id.iv_head_back)
    ImageView iv_head_back;
    @BindView(R.id.tv_head_content)
    TextView tv_head_content;


    public int setContentViewLayout() {
        return R.layout.activity_notify_detail;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        final Notify notify = (Notify) intent.getSerializableExtra("Notify"); // 由于老师和学生的通知都要在这里进行显示，所以将老师和学生的通知提取出一个父类的Notify出来
        tv_head_content.setText("通知详情");

        if (notify != null) {
            if (notify.getTitle() != null) {
                tv_notify_title.setText(notify.getTitle());
            }
            if (notify.getContent() != null) {
                tv_notify_content.setText(notify.getContent());
            }
            tv_notify_time.setText(notify.getCreatedAt());
            if (notify.getFile() != null) {
                cv_file.setVisibility(View.VISIBLE);
                tv_notify_filename.setText(notify.getFile().getFilename());
                cv_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(mCtx, EnclosureShowActivity.class);

                        //  intent1.putExtra("fileUrl", notify.getFile().getUrl());
                        intent1.putExtra("file", notify.getFile());
                        startActivity(intent1);
                    }
                });
            } else {
                cv_file.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.iv_head_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_head_back:
                finish();
                break;
        }
    }

}
