package com.xyb.zhku.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.base.MyBaseAdapter;
import com.xyb.zhku.bean.TeacherHomeWork;
import com.xyb.zhku.bean.TeachingTask;
import com.xyb.zhku.global.Constants;
import com.xyb.zhku.manager.ReleaseHomeworkObserverManager;
import com.xyb.zhku.utils.FileUtil;
import com.xyb.zhku.utils.SharePreferenceUtils;
import com.xyb.zhku.utils.UIUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ReleaseHomeworkActivity extends BaseActivity {

    private static final int SCROLLVIEW = 1;
    private static final int TEXTVIEW = 2;

    @BindView(R.id.iv_head_back)
    ImageView iv_head_back;
    @BindView(R.id.tv_head_content)
    TextView tv_head_content;

//    @BindView(R.id.act_enrollment_year)
//    AutoCompleteTextView act_enrollment_year;
//    @BindView(R.id.act_homewwork_major)
//    AutoCompleteTextView act_homewwork_major;
//    @BindView(R.id.act_homewwork_subject)
//    AutoCompleteTextView act_homewwork_subject;

    @BindView(R.id.tv_not_teaching_task)
    TextView tv_not_teaching_task;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.Sp_subject)
    Spinner Sp_subject;

    @BindView(R.id.act_homewwork_title)
    AutoCompleteTextView act_homewwork_title;
    @BindView(R.id.tv_homework_filename)
    TextView tv_homework_filename;
    @BindView(R.id.act_homewwork_content)
    EditText act_homewwork_content;
    @BindView(R.id.tv_release)
    TextView tv_release;
    //    private String[] yearStr;
//    private String[] allMajor;
    TeacherHomeWork teacherHomeWork;
    private boolean isSubmiting_homework = false;
    private boolean isSubmiting_file = false;
    private TeachingTask task;
    private List<String> subjectList;
    private List<TeachingTask.SubjectClass> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_head_content.setText("发布作业");
        teacherHomeWork = new TeacherHomeWork();
//        allMajor = Utils.getAllMajor(mCtx);
//        yearStr = Utils.getYearStr();
//        UIUtils.bindArray(act_homewwork_major, allMajor);
//        UIUtils.bindArray(act_enrollment_year,yearStr);
        initData();

    }

    private void initData() {
        String teacherId = (String) SharePreferenceUtils.get(mCtx, Constants.SCHOOL_NUMBER, "");
        if (teacherId.equals("")) {
            showView(TEXTVIEW);
            tv_not_teaching_task.setText("账号异常，建议重新登录");
            return;
        }
        BmobQuery<TeachingTask> query = new BmobQuery<TeachingTask>();
        query.addWhereEqualTo("teacherId", teacherId);
        query.setLimit(1);
        query.findObjects(new FindListener<TeachingTask>() {
            public void done(List<TeachingTask> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        task = object.get(0);
//                        if (task != null) {


//                        } else {
//                            showView(TEXTVIEW);
//                            tv_not_teaching_task.setText("您还没有教学任务");
//                        }
                        showView(SCROLLVIEW);
                        subjectList = new ArrayList<String>();
                        taskList = task.getList();
                        for (TeachingTask.SubjectClass subjectClass : taskList) {
                            subjectList.add(subjectClass.getSubject());
                        }
                     //   UIUtils.bindSpinnerAdapter(Sp_subject, subjectList);
                        Sp_subject.setAdapter(new MyBaseAdapter(subjectList));

                    } else {
                        showView(TEXTVIEW);
                        tv_not_teaching_task.setText("您还没有教学任务");
                    }
                } else {
                    showView(TEXTVIEW);
                    tv_not_teaching_task.setText("服务器离家出走了");
                }
            }
        });
        Sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < subjectList.size(); i++) {
                    if (taskList.get(i).getSubject().equals(subjectList.get(i))) {
                        TeachingTask.SubjectClass subjectClass = taskList.get(i);
                        teacherHomeWork.setEnrollment_year(subjectClass.getYear());
                        teacherHomeWork.setMajor(subjectClass.getMajor());
                        teacherHomeWork.setSubject(subjectClass.getSubject());
                        teacherHomeWork.setClassList(subjectClass.getClassList());
                        break;
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * 显示 ScrollView 还是 异常View
     *
     * @param type
     */
    private void showView(int type) {
        switch (type) {
            case SCROLLVIEW:
                tv_not_teaching_task.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                break;
            case TEXTVIEW:
                tv_not_teaching_task.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                break;
        }
    }

    public int setContentViewLayout() {
        return R.layout.activity_release_homework;
    }

    @OnClick({R.id.iv_head_back, R.id.tv_release, R.id.tv_homework_filename})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_head_back:
                finish();
                break;
            case R.id.tv_release:
//                if (UIUtils.isEmtpy(act_homewwork_major)) {
//                    showToast("专业不能为空！");
//                    return;
//                }
//                if (UIUtils.isEmtpy(act_enrollment_year)) {
//                    showToast("年级不能为空！");
//                    return;
//                }
//
//                if (UIUtils.isEmtpy(act_homewwork_subject)) {
//                    showToast("科目不能为空！");
//                    return;
//                }
                if (UIUtils.isEmtpy(act_homewwork_title)) {
                    showToast("标题不能为空！");
                    return;
                }
                if (UIUtils.isEmtpy(act_homewwork_content)) {
                    showToast("作业说明不能为空！");
                    return;
                }
//                if (!Utils.contain(allMajor, act_homewwork_major.getText().toString().trim())) {
//                    showToast("专业格式输入有误！");
//                    return;
//                }
//                if (!Utils.contain(yearStr, act_enrollment_year.getText().toString().trim())) {
//                    showToast("年级格式输入有误！");
//                    return;
//                }

                tv_release.setText("正在发布...");
               /* teacherHomeWork.setEnrollment_year(act_enrollment_year.getText().toString().trim());
                teacherHomeWork.setMajor(act_homewwork_major.getText().toString().trim());
                teacherHomeWork.setSubject(act_homewwork_subject.getText().toString().trim());*/

                teacherHomeWork.setStu_number_list(new ArrayList<String>());
                teacherHomeWork.setTitle(act_homewwork_title.getText().toString().trim());
                teacherHomeWork.setContent(act_homewwork_content.getText().toString());
                String objectId = (String) SharePreferenceUtils.get(mCtx, Constants.OBJECTID, "");
                if (objectId.equals("")) {
                    // showToast("您的账户已过期，请重新登录！")
                    AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                    builder.setMessage("您的账户已过期，请重新登录！")
                            .setTitle("提示")
                            .setCancelable(false)
                            .setOnKeyListener(keylistener)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    // TODO: 2018/9/28    退出登录
                                    SharePreferenceUtils.clearUser(mCtx);
                                    jumpToAnotherActivity(LoginActivity.class);
                                    ReleaseHomeworkActivity.this.finish();
                                }
                            })
                            .create()
                            .show();
                } else {
                    teacherHomeWork.setTeacherId(objectId);
                }

                if (isSubmiting_homework) {
                    showToast("正在发布中，请稍后...");
                    return;
                }
                isSubmiting_homework = true;
                teacherHomeWork.save(new SaveListener<String>() {
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            if (objectId != null) {
                                showToast("发布成功");
                                // 通知显示多一行作业
                                ReleaseHomeworkObserverManager.getInstance().notifyChange(teacherHomeWork);
                            }
                            finish();
                        } else {
                            showToast("服务器繁忙！");
                            finish();
                        }
                        tv_release.setText("发布");
                        isSubmiting_homework = false;
                    }
                });
                break;

            case R.id.tv_homework_filename:
                if (isSubmiting_file) {
                    showToast("正在上传中，请稍后...");
                    return;
                }
                selectFile();
                break;
        }
    }

    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;//当按下 返回键的时候，直接什么都不做就消耗掉
            } else {
                return false;
            }
        }
    };


    public void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件上传"), 345);
        } catch (android.content.ActivityNotFoundException ex) {
            showToast("请先安装一个文件管理器!");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 345) {//345选择文件的请求码
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    if (data.getData() == null) {
                        return;
                    }
                    Uri uri = data.getData();
                    // 注意获取 doc 文件时，该 doc 文件不能是放在“下载”路径的文件
                    String path = FileUtil.getPath(mCtx, uri);
                    File file = new File(path);
                    tv_homework_filename.setText("正在上传文件...");
                    upload(file);
                }
            }
        }
    }

    public void upload(File file) {
        final BmobFile bmobFile = new BmobFile(file);//new File(realFilePath)
        bmobFile.uploadblock(new UploadFileListener() {
            public void done(BmobException e) {
                if (e == null) {
                    teacherHomeWork.setFile(bmobFile);
                    tv_homework_filename.setTextColor(Color.parseColor("#24b6e9"));
                    tv_homework_filename.setText(bmobFile.getFilename());
                    String fileUrl = bmobFile.getUrl();
                    Log("上传成功：", bmobFile.getUrl());
                    if (fileUrl.endsWith(".doc")) {
                        Log("该文件时doc文档", "该文件时doc文档");
                    }
                } else {
                    showToast("上传失败：" + e.getMessage() + "请重新上传！");
                    tv_homework_filename.setTextColor(Color.parseColor("#ff0000"));
                    tv_homework_filename.setText("上传失败，请重试！");
                }
                isSubmiting_file = false;
            }

            public void onProgress(Integer value) {
                Log("正在上传:", value + "");
            }
        });
    }

}
