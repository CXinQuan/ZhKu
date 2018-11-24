package com.xyb.zhku.test;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.bean.TeachingTask;
import com.xyb.zhku.utils.FileUtil;
import com.xyb.zhku.utils.ZipUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by lenovo on 2018/9/24.
 */

public class BmobFileTestActivity extends BaseActivity {
    @BindView(R.id.btn_cumbit)
    Button btn_cumbit;
    @BindView(R.id.btn_query)
    Button btn_query;
    @BindView(R.id.tv_result)
    TextView tv_result;

    public int setContentViewLayout() {
        return R.layout.bmobfile_test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_cumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // selectFile();
                //  testListInList();
                //  BmobFile file=new BmobFile();
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                        + getPackageName() + File.separator + "测试");
                if (!file.exists()) {
                    file.mkdirs();
                }
                Log(file.getAbsolutePath(), "测试");
                ZipUtils.zip(file.getAbsolutePath(), new ZipUtils.FinishListener() {
                    @Override
                    public void onfinish(File target) {

                    }
                });

            }
        });
    }

    @OnClick({R.id.btn_query})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_query:
                queryListInList();
                break;
        }
    }

    private void queryListInList() {
        BmobQuery<TeachingTask> query = new BmobQuery<TeachingTask>();
        query.findObjects(new FindListener<TeachingTask>() {
            public void done(List<TeachingTask> object, BmobException e) {
                if (e == null) {
                    TeachingTask teachingTask = object.get(0);
                    List<TeachingTask.SubjectClass> list = teachingTask.getList();
                    StringBuilder sb = new StringBuilder();
                    for (TeachingTask.SubjectClass subjectClass : list) {
                        sb.append(subjectClass.getClassList().get(0));
                    }
                    tv_result.setText(sb.toString());
                } else {
                    Toast.makeText(mCtx, "加载数据出错！", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


  /*  private void testListInList() {
        TeachingTask task = new TeachingTask();
        task.setTeacherId("00001");
        task.setTeacherName("张三");
        List<TeachingTask.SubjectClass> list = new ArrayList<>();

        TeachingTask.SubjectClass sc1 = new TeachingTask.SubjectClass();
        List<String> list_class_1 = new ArrayList<>();
        list_class_1.add("电子151");
        list_class_1.add("电子152");
        list_class_1.add("电子153");
        sc1.setClassList(list_class_1);
        sc1.setMajor("电子信息工程");
        sc1.setSubject("数字电路");

        TeachingTask.SubjectClass sc2 = new TeachingTask.SubjectClass();
        List<String> list_class_2 = new ArrayList<>();
        list_class_2.add("通信151");
        list_class_2.add("通信152");
        list_class_2.add("通信153");
        sc2.setClassList(list_class_2);
        sc2.setMajor("通信工程");
        sc2.setSubject("通信原理");
        list.add(sc1);
        list.add(sc2);
        task.setList(list);
        task.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log("测试", s + "00000000000000000");
                } else {
                    e.printStackTrace();
                    Log("错误代码" + e.getErrorCode(), "  错误");

                }

            }
        });
    }*/


    public void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");

        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件上传"), 345);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "请安装一个文件管理器.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mCtx, "上传成功：", Toast.LENGTH_SHORT).show();
                    String fileName = bmobFile.getUrl();
                    Log("上传成功：", bmobFile.getUrl());
                    if (fileName.endsWith(".doc")) {
                        Log("该文件时doc文档", "该文件时doc文档");
                    }

                } else {
                    Toast.makeText(mCtx, "上传失败：" + e.getMessage() + "请重新上传！", Toast.LENGTH_SHORT).show();
                }
            }

            public void onProgress(Integer value) {
                Log("正在上传:", value + "");
            }
        });
    }


}
