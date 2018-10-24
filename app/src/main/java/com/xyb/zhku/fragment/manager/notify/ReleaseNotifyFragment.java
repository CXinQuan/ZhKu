package com.xyb.zhku.fragment.manager.notify;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.bean.Notify;
import com.xyb.zhku.bean.StuNotify;
import com.xyb.zhku.bean.TeacherNotify;
import com.xyb.zhku.ui.EnclosureShowActivity;
import com.xyb.zhku.utils.FileUtil;
import com.xyb.zhku.utils.UIUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by lenovo on 2018/10/16.
 */

public class ReleaseNotifyFragment extends BaseFragment {
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.rb_student)
    RadioButton rb_student;
    @BindView(R.id.rb_teacher)
    RadioButton rb_teacher;
    @BindView(R.id.act_notify_title)
    AutoCompleteTextView act_notify_title;
    @BindView(R.id.tv_nofity_filename)
    TextView tv_nofity_filename;
    @BindView(R.id.iv_cancle)
    ImageView iv_cancle;
    @BindView(R.id.act_notify_content)
    EditText act_notify_content;
    @BindView(R.id.tv_release)
    Button tv_release;

    @BindView(R.id.rg_stu)
    RadioGroup rg_stu;
    @BindView(R.id.rb_student_teaching)
    RadioButton rb_student_teaching;
    @BindView(R.id.rb_student_work)
    RadioButton rb_student_work;
    @BindView(R.id.rb_student_other)
    RadioButton rb_student_other;

    @BindView(R.id.rg_teacher)
    RadioGroup rg_teacher;
    @BindView(R.id.rb_teacher_teaching)
    RadioButton rb_teacher_teaching;
    @BindView(R.id.rb_teacher_science)
    RadioButton rb_teacher_science;
    @BindView(R.id.rb_teacher_party)
    RadioButton rb_teacher_party;
    @BindView(R.id.rb_teacher_other)
    RadioButton rb_teacher_other;

    Notify notify;
    TeacherNotify teacherNotify = new TeacherNotify();
    StuNotify stuNotify = new StuNotify();

    boolean isSelectStudent = true;

    protected int setView() {
        return R.layout.manager_release_notify_fragment;
    }

    @Override
    protected void init(View view) {
        notify = new Notify();
        notify.setType(StuNotify.TEACHING); // 初始状态
        rb_student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isSelectStudent = true;
                    rg_stu.setVisibility(View.VISIBLE);
                    rg_teacher.setVisibility(View.GONE);
                    rb_student_teaching.setChecked(true);
                    notify.setType(StuNotify.TEACHING);
                } else {
                    isSelectStudent = false;
                    rg_stu.setVisibility(View.GONE);
                    rg_teacher.setVisibility(View.VISIBLE);
                    rb_teacher_teaching.setChecked(true);
                    notify.setType(TeacherNotify.TEACHING);
                }
            }
        });

        rg_stu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_student_teaching:
                        notify.setType(StuNotify.TEACHING);
                        break;
                    case R.id.rb_student_work:
                        notify.setType(StuNotify.STU_WORK);
                        break;
                    case R.id.rb_student_other:
                        notify.setType(StuNotify.OTHER);
                        break;
                }
            }
        });
        rg_teacher.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_teacher_teaching:
                        notify.setType(TeacherNotify.TEACHING);
                        break;
                    case R.id.rb_teacher_science:
                        notify.setType(TeacherNotify.SCIENCE);
                        break;
                    case R.id.rb_teacher_party:
                        notify.setType(TeacherNotify.PARTYBUILD);
                        break;
                    case R.id.rb_teacher_other:
                        notify.setType(TeacherNotify.OTHER);
                        break;
                }
            }
        });
    }

    boolean isSubmitingNotify = false;

    @OnClick({R.id.tv_release, R.id.iv_cancle, R.id.tv_nofity_filename})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cancle:
                notify.setFile(null);
                tv_nofity_filename.setText("点击添加附件");
                tv_nofity_filename.setTextColor(Color.parseColor("#55666666"));
                break;
            case R.id.tv_nofity_filename:
                if (isSubmitingFile) {
                    showToast("正在上传文件...");
                    return;
                }
                if (notify.getFile() == null) {
                    // TODO: 2018/10/16   添加附件
                    selectFile();

                } else {
                    // TODO: 2018/10/16   显示附件
                    Intent intent = new Intent(mCtx, EnclosureShowActivity.class);
                    intent.putExtra("file", notify.getFile());
                    startActivity(intent);
                }
                break;

            case R.id.tv_release:
                if (isSubmitingNotify) {
                    showToast("发布中，请稍后...");
                    return;
                }
                if (UIUtils.isEmtpy(act_notify_title)) {
                    showToast("标题不能为空");
                    return;
                }
                if (UIUtils.isEmtpy(act_notify_content)) {
                    showToast("内容不能为空");
                    return;
                }
                isSubmitingNotify = true;
                tv_release.setText("正在发布...");
                if (isSelectStudent) {
                    stuNotify = new StuNotify();
                    stuNotify.setTitle(act_notify_title.getText().toString());
                    stuNotify.setContent(act_notify_content.getText().toString());
                    stuNotify.setFile(notify.getFile());
                    stuNotify.setType(notify.getType());
                    stuNotify.save(new SaveListener<String>() {
                        public void done(String s, BmobException e) {
                            if (e == null) { // 成功
                                showToast("发布成功");
                                restoreView();
                                stuNotify = null;  //只在成功的时候清空，不成功，等待网络恢复了还可以继续发布
                            } else {
                                showToast("服务器繁忙");
                            }
                            isSubmitingNotify = false;
                            tv_release.setText("发布");
                        }
                    });
                } else {
                    teacherNotify = new TeacherNotify();
                    teacherNotify.setTitle(act_notify_title.getText().toString());
                    teacherNotify.setContent(act_notify_content.getText().toString());
                    teacherNotify.setFile(notify.getFile());
                    teacherNotify.setType(notify.getType());
                    teacherNotify.save(new SaveListener<String>() {
                        public void done(String s, BmobException e) {
                            if (e == null) { // 成功
                                showToast("发布成功");
                                restoreView();
                                teacherNotify = null;
                            } else {
                                showToast("服务器繁忙");
                            }
                            isSubmitingNotify = false;
                            tv_release.setText("发布");
                        }
                    });
                }
                break;
        }
    }


    /**
     * 通知提交结束后，做的清理工作
     */
    private void restoreView() {
        notify.setFile(null);
        act_notify_content.setText("");
        act_notify_title.setText("");
        tv_nofity_filename.setText("点击添加附件");
        tv_nofity_filename.setTextColor(Color.parseColor("#55666666"));
        iv_cancle.setVisibility(View.GONE);
    }


    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    public void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件上传"), 100);
        } catch (android.content.ActivityNotFoundException ex) {
            showToast("请安装一个文件管理器.");
        }
    }

    boolean isSubmitingFile = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {//456选择文件的请求码
            if (data != null) {
//                if (resultCode == getActivity().RESULT_OK) {  }
                if (data.getData() == null) {
                    showToast("请从文件管理器中选择附件");
                    return;
                }
                Uri uri = data.getData();
                // 注意获取 doc 文件时，该 doc 文件不能是放在“下载”路径的文件
                String path = FileUtil.getPath(mCtx, uri);
                File file = new File(path);
                tv_nofity_filename.setText("正在上传文件...");
                isSubmitingFile = true;
                upload(file);
            }
        }
    }

    public void upload(File file) {
        final BmobFile bmobFile = new BmobFile(file);//new File(realFilePath)
        bmobFile.uploadblock(new UploadFileListener() {
            public void done(BmobException e) {
                if (e == null) {
                    notify.setFile(bmobFile);
                    // hasUpLoad=true  tv_filename 显示文件名
                    tv_nofity_filename.setTextColor(Color.parseColor("#24b6e9"));
                    tv_nofity_filename.setText(bmobFile.getFilename());
                    iv_cancle.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(mCtx, "上传失败：" + e.getMessage() + "请重新上传！", Toast.LENGTH_SHORT).show();
                }
                isSubmitingFile = false;
            }
            public void onProgress(Integer value) {
                Log.d("正在上传:", value + "");
            }
        });
    }


}
