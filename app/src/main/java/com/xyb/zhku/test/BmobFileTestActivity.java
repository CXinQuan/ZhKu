package com.xyb.zhku.test;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.utils.FileUtil;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import butterknife.BindView;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by lenovo on 2018/9/24.
 */

public class BmobFileTestActivity extends BaseActivity {
    @BindView(R.id.btn_cumbit)
    Button btn_cumbit;

    public int setContentViewLayout() {
        return R.layout.bmobfile_test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_cumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });
    }

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
