package com.xyb.zhku.email;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Button btn_send_text;
    private Button btn_send_file;
    private EditText et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        et_content = (EditText) findViewById(R.id.et_content);
//        btn_send_text = (Button) findViewById(R.id.btn_send_text);
//        btn_send_file = (Button) findViewById(R.id.btn_send_file);

        btn_send_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  SendMailUtil.send(toAdd,subject,content);
            }
        });
        btn_send_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File pathPackageName = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "com.xyb.zhku" + File.separator + "DownLoadFile");

                if (!pathPackageName.exists()) {
                    Toast.makeText(MainActivity.this, "该路径不存在", Toast.LENGTH_SHORT).show();
                } else {
                    File realFile = new File(pathPackageName, "作业4.zip");
                  // SendMailUtil.send(realFile, toAdd,subject,content);
                }
            }
        });
    }
}
