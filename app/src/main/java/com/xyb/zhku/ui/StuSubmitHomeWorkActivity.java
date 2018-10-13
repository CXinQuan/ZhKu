package com.xyb.zhku.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.bean.StudentHomeWork;
import com.xyb.zhku.bean.TeacherHomeWork;
import com.xyb.zhku.bean.TeacherNotify;
import com.xyb.zhku.global.Constants;
import com.xyb.zhku.manager.SubmitHomeworkObserverManager;
import com.xyb.zhku.utils.FileUtil;
import com.xyb.zhku.utils.SharePreferenceUtils;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 *   学生上交作业  并查看该作业成绩
 */
public class StuSubmitHomeWorkActivity extends BaseActivity {
    @BindView(R.id.iv_head_back)
    ImageView iv_head_back;
    @BindView(R.id.tv_head_content)
    TextView tv_head_content;
    @BindView(R.id.tv_hw_detail_title)
    TextView tv_hw_detail_title;
    @BindView(R.id.tv_subject)
    TextView tv_subject;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.cv_enclosure)
    CardView cv_enclosure;
    @BindView(R.id.tv_enclosure)
    TextView tv_enclosure;
    @BindView(R.id.cv_content)
    CardView cv_content;
    @BindView(R.id.tv_content)
    TextView tv_content;
    @BindView(R.id.tv_grade)
    TextView tv_grade;  // 显示成绩
    @BindView(R.id.iv_upload)
    ImageView iv_upload;
    @BindView(R.id.tv_filename)
    TextView tv_filename;
    @BindView(R.id.iv_cancle)
    ImageView iv_cancle;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.cv_upload_file)
    CardView cv_upload_file;


    private int position;
    private TeacherHomeWork teacherhomeWork;
    private boolean hasUpLoad = false;  //已经上传文件
    private boolean hasSubmit = false;    // 已经上交作业了，此次进来是为了看是否 评分了
    private StudentHomeWork stuhomeWork;

    public int setContentViewLayout() {
        return R.layout.activity_stu_submit_homework;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stuhomeWork = new StudentHomeWork();
        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        teacherhomeWork = (TeacherHomeWork) intent.getSerializableExtra("TeacherHomeWork");
        hasUpLoad = hasSubmit = intent.getBooleanExtra("hasSubmit", false);

        if (teacherhomeWork == null) {
            Toast.makeText(this, "数据出错！", Toast.LENGTH_SHORT).show();
            return;
        }
        initView();
    }

    private void initView() {
        tv_head_content.setText("作业详情");

        if (teacherhomeWork != null) {
            tv_hw_detail_title.setText(teacherhomeWork.getTitle());
            tv_subject.setText("科目：" + teacherhomeWork.getSubject());
            tv_time.setText("时间：" + teacherhomeWork.getCreatedAt());
            if (teacherhomeWork.getContent() != null) {
                cv_content.setVisibility(View.VISIBLE);
                tv_content.setText(teacherhomeWork.getContent());
            } else {
                cv_content.setVisibility(View.GONE);
            }
            if (null != teacherhomeWork.getFile()) {
                cv_enclosure.setVisibility(View.VISIBLE);
                tv_enclosure.setText(teacherhomeWork.getFile().getFilename());
                cv_enclosure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 2018/9/25     点击附件之后的响应
                        Intent intent = new Intent(mCtx, EnclosureShowActivity.class);
                     //   intent.putExtra("fileUrl", homeWork.getFile().getUrl());
                        intent.putExtra("file", teacherhomeWork.getFile());
                        startActivity(intent);
                    }
                });
            } else {
                cv_enclosure.setVisibility(View.GONE);
            }


            if (hasSubmit) { // 已经提交作业，那么就像服务器获取成绩
                iv_cancle.setVisibility(View.VISIBLE);
                tv_filename.setTextColor(Color.parseColor("#24b6e9"));
                //  从服务器上获取成绩,看是否获取得到成绩   等待批改
                // TODO: 2018/10/2   在还没有成绩之前，还可以进行修改文件
                getGrade(teacherhomeWork.getObjectId(), (String) SharePreferenceUtils.get(mCtx, Constants.SCHOOL_NUMBER, ""));
            } else {
                // TODO: 2018/10/2   显示还没有提交的布局
                ll.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                tv_grade.setVisibility(View.GONE);
                tv_filename.setEnabled(true);
                tv_filename.setText("点击上传文件");
                tv_filename.setTextColor(Color.parseColor("#7e7e7e"));
                iv_cancle.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);
                btn_submit.setBackgroundColor(Color.parseColor("#2244ff"));
                btn_submit.setEnabled(true);
            }
        }
    }

    // TODO: 2018/10/2    上传完文件 应该 iv_cancle.setVisibility(); 可见 
    // TODO: hasUpLoad=true  tv_filename 显示文件名  
    // TODO: 更新 上一个界面的数据  添加上学号   

    boolean submitingStuhomeWork = false;

    @OnClick({R.id.btn_submit, R.id.tv_filename, R.id.iv_head_back, R.id.iv_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                // TODO: 2018/10/2   判断是否上传文件，再进行向服务器更新数据
                if (submitingStuhomeWork) {
                    showToast("正在上传作业，请稍后...");
                    return;
                }
//                  String stu_major; // 学生专业
//                    String stu_class; //学生班级
//                    String stu_school_number;//学号
//                    String stu_name;//姓名
//                    int stu_state = NOT_CHECK;//状态  是否批改
//                    float stu_grade = -1.0f;//成绩  默认是-1分
//                    BmobFile file;//附件（上交的作业）
//                    String teacher_homework_id; //对应老师作业的id
                String major = (String) SharePreferenceUtils.get(mCtx, Constants.MAJOR, "");
                String uclass = (String) SharePreferenceUtils.get(mCtx, Constants.UCLASS, "");
                final String school_number = (String) SharePreferenceUtils.get(mCtx, Constants.SCHOOL_NUMBER, "");
                String name = (String) SharePreferenceUtils.get(mCtx, Constants.NAME, "");
                if (major.equals("") || uclass.equals("") || school_number.equals("") || name.equals("")) {
                    showToast("账号异常，请重新登录");
                    return;
                }
                btn_submit.setText("正在提交作业...");
                submitingStuhomeWork = true;

                if (hasSubmit) {  // TODO: 2018/10/3   已经提交过作业了，那么则直接更新该 StudentHomework即可
                    if (stuhomeWork.getFile() == null) {
                        return;
                    }
                    BmobQuery<StudentHomeWork> query = new BmobQuery<StudentHomeWork>();
                    query.addWhereEqualTo("teacher_homework_id", teacherhomeWork.getObjectId());
                    query.addWhereEqualTo("stu_school_number", school_number);
                    query.setLimit(1);
                    query.findObjects(new FindListener<StudentHomeWork>() {
                        public void done(List<StudentHomeWork> object, BmobException e) {
                            if (e == null) {
                                if (object.size() == 1) {  //说明该条作业记录存在
                                    //更新该 StudentHomework
                                    StudentHomeWork homeWork_history = object.get(0);
                                    homeWork_history.setFile(stuhomeWork.getFile());
                                    homeWork_history.update(homeWork_history.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                showToast("提交作业成功");
                                                Log("   ", "已经提交过作业了，那么则直接更新该 StudentHomework即可");

                                                // 将 上一页的数据一起更新了
                                                SubmitHomeworkObserverManager.getInstance().notifyChange(teacherhomeWork, position);
                                                finish();
                                            } else {
                                                Log("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                            }
                                            btn_submit.setText("提交作业");
                                            submitingStuhomeWork = false;
                                        }
                                    });
                                }
                                btn_submit.setText("提交作业");
                                submitingStuhomeWork = false;
                            } else {
                                Log("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                btn_submit.setText("提交作业");
                                submitingStuhomeWork = false;
                            }
                        }
                    });


                } else {  // TODO: 2018/10/3   第一次上交作业
                    if (hasUpLoad&&stuhomeWork.getFile()!=null) {
                        stuhomeWork.setStu_state(StudentHomeWork.NOT_CHECK);
                        stuhomeWork.setStu_grade(-1);
                        stuhomeWork.setStu_class(uclass);
                        stuhomeWork.setStu_name(name);
                        stuhomeWork.setStu_school_number(school_number);
                        stuhomeWork.setStu_major(major);
                        stuhomeWork.setTeacher_homework_id(teacherhomeWork.getObjectId());
                        stuhomeWork.save(new SaveListener<String>() {
                            public void done(final String objectId, BmobException e) {
                                if (e == null) {
                                    // 提交作业成功后，应该将老师的TeacherHomeWork的Stu_number_list()进行更新
                                    //所以就先获取到 该老师的作业对象，再进行更新
                                    BmobQuery<TeacherHomeWork> query = new BmobQuery<TeacherHomeWork>();
                                    query.getObject(teacherhomeWork.getObjectId(), new QueryListener<TeacherHomeWork>() {
                                        public void done(TeacherHomeWork object, BmobException e) {
                                            if (e == null) {
                                                if (object != null) {
                                                    teacherhomeWork = object;
                                                    List<String> stu_number_list = teacherhomeWork.getStu_number_list();
                                                    if (stu_number_list == null) {
                                                        stu_number_list = new ArrayList<String>();
                                                    }
                                                    stu_number_list.add(school_number);
                                                    teacherhomeWork.setStu_number_list(stu_number_list);
                                                    teacherhomeWork.update(teacherhomeWork.getObjectId(), new UpdateListener() {
                                                        public void done(BmobException e) {
                                                            if (e == null) {
                                                                ///   Log("bmob", "更新成功");
                                                                showToast("提交作业成功");
                                                                SubmitHomeworkObserverManager.getInstance().notifyChange(teacherhomeWork, position);
                                                                finish();
                                                            } else {
                                                                Log("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                                            }
                                                            btn_submit.setText("提交作业");
                                                            submitingStuhomeWork = false;
                                                        }
                                                    });
                                                }
//                                            btn_submit.setText("提交作业");
//                                            submitingStuhomeWork = false;
                                            } else {
                                                Log("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                                                btn_submit.setText("提交作业");
                                                submitingStuhomeWork = false;
                                            }

                                        }
                                    });
//                                showToast("提交成功");
//                                // TODO: 更新 上一个界面的数据  添加上学号
//                                finish();
                                } else {
                                    btn_submit.setText("提交作业");
                                    submitingStuhomeWork = false;
                                }
                            }
                        });
                    }

                }

                break;
            case R.id.tv_filename:

                if (!hasUpLoad) {  // TODO: 2018/10/2   没有上传文件时，则点击的时候，则是选择文件上传
                    if (isSubmitingFile) {
                        showToast("正在上传文件...");
                        return;
                    }
                    selectFile();

                } else {   // TODO: 2018/10/2     显示该文件的详情  
                    Intent intent = new Intent(mCtx, EnclosureShowActivity.class);
                    intent.putExtra("file", stuhomeWork.getFile());
                    //intent.putExtra("fileUrl", stuhomeWork.getFile().getUrl());
                    startActivity(intent);
                }
                break;
            case R.id.iv_cancle:
                if (hasUpLoad) {//只有上传了文件才可以进行取消
                    tv_filename.setText("点击上传文件");
                    tv_filename.setTextColor(Color.parseColor("#7e7e7e"));
                    hasUpLoad = false;
                    iv_cancle.setVisibility(View.GONE);
                    btn_submit.setBackgroundColor(Color.parseColor("#44000000")); //等待重新上传文件
                    btn_submit.setEnabled(false);
                }

                break;
            case R.id.iv_head_back:
                finish();
                break;

        }
    }

    private void getGrade(String teacher_homework_id, String stu_school_number) {
        BmobQuery<StudentHomeWork> query = new BmobQuery<StudentHomeWork>();
        query.order("-updatedAt") //  根据updatedAt 时间  从上到下  开始  返回  5条数据
                .setLimit(1)
                .addWhereEqualTo("teacher_homework_id", teacher_homework_id)
                .addWhereEqualTo("stu_school_number", stu_school_number)
                .findObjects(new FindListener<StudentHomeWork>() {
                    public void done(List<StudentHomeWork> object, BmobException e) {
                        if (e == null) {
                            if (object.size() > 0) {
                                stuhomeWork = object.get(0);
                                if (stuhomeWork.getStu_grade() > -1) {
                                    // TODO: 2018/10/2   显示成绩 已经评分了
                                    pb.setVisibility(View.GONE);
                                    ll.setVisibility(View.VISIBLE);
                                    tv_grade.setVisibility(View.VISIBLE);
                                    tv_grade.setText("成绩：" + stuhomeWork.getStu_grade());
                                    //tv_filename.setEnabled(false);
                                    btn_submit.setVisibility(View.GONE);
                                    iv_cancle.setVisibility(View.GONE);
                                } else {
                                    pb.setVisibility(View.GONE);
                                    ll.setVisibility(View.VISIBLE);
                                    tv_grade.setVisibility(View.VISIBLE);
                                    tv_grade.setText("等待评分...");
                                    tv_filename.setEnabled(true);
                                    iv_cancle.setVisibility(View.VISIBLE);
                                    btn_submit.setVisibility(View.VISIBLE);
                                    btn_submit.setBackgroundColor(Color.parseColor("#44000000"));
                                    btn_submit.setEnabled(false);
                                }
                                tv_filename.setText(stuhomeWork.getFile().getFilename());
                            } else {
                                showToast("加载数据失败");
                            }
                        } else {
                            showToast("加载数据失败");
                        }
                        pb.setVisibility(View.GONE);
                        ll.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件上传"), 456);
        } catch (android.content.ActivityNotFoundException ex) {
            showToast("请安装一个文件管理器.");
        }
    }

    boolean isSubmitingFile = false;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 456) {//456选择文件的请求码
            if (data != null) {
                if (resultCode == RESULT_OK) {
                    if (data.getData() == null) {
                        return;
                    }
                    Uri uri = data.getData();
                    // 注意获取 doc 文件时，该 doc 文件不能是放在“下载”路径的文件
                    String path = FileUtil.getPath(mCtx, uri);
                    File file = new File(path);
                    tv_filename.setText("正在上传文件...");
                    isSubmitingFile = true;
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

                    stuhomeWork.setFile(bmobFile);
                    tv_filename.setTextColor(Color.parseColor("#24b6e9"));
                    tv_filename.setText(bmobFile.getFilename());

                    //iv_cancle.setVisibility(); 可见
                    // TODO: hasUpLoad=true  tv_filename 显示文件名
                    // TODO: 更新 上一个界面的数据  添加上学号
                    iv_cancle.setVisibility(View.VISIBLE);
                    hasUpLoad = true;

                    btn_submit.setEnabled(true);
                    btn_submit.setBackgroundColor(Color.parseColor("#2244ff"));

                    // Toast.makeText(mCtx, "上传成功：", Toast.LENGTH_SHORT).show();
                    String fileName = bmobFile.getUrl();
                    Log("上传成功：", bmobFile.getUrl());
                    if (fileName.endsWith(".doc")) {
                        Log("该文件时doc文档", "该文件时doc文档");
                    }
                } else {
                    Toast.makeText(mCtx, "上传失败：" + e.getMessage() + "请重新上传！", Toast.LENGTH_SHORT).show();
                }
                isSubmitingFile = false;
            }

            public void onProgress(Integer value) {
                Log("正在上传:", value + "");
            }
        });
    }
}
