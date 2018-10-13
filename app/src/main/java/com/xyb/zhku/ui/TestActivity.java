package com.xyb.zhku.ui;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xyb.zhku.R;
import com.xyb.zhku.bean.StuNotify;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.a.This;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TestActivity extends AppCompatActivity {
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        Dialog dialog=new Dialog(this);
//        AlertDialog.Builder builder=new AlertDialog.Builder(this);
//        dialog.getWindow().setDimAmount(0f);//核心代码
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//


        TextView btn = (TextView) findViewById(R.id.btn);
        et = (EditText) findViewById(R.id.et);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getDataFromBmob(0, 0, et.getText().toString().trim());// "^[A‐Z]\\d"
            }
        });

    }

    List<StuNotify> list = new ArrayList<>();
    int count;
    int startSize; // 符合

    //在近期 n 条 StuNotify 中查询，获取到这 n 条数据之后才进行查询，
    // 跳过多少条，  什么日期  一周、一个月、三个月、六个月
    private void getDataFromBmob(final int skip, final int limit, BmobDate startdate, BmobDate enddate, final String str) {
        BmobQuery<StuNotify> query = new BmobQuery<StuNotify>();
        query.setSkip(skip)
                .setLimit(limit)
                .addWhereLessThanOrEqualTo("createdAt", enddate)
                //   .addWhereGreaterThanOrEqualTo("createdAt", startdate)
                .findObjects(new FindListener<StuNotify>() {
                    public void done(List<StuNotify> object, BmobException e) {
                    }
                });

    }


    private void getDataFromBmob(final int endSize, final int skip, final String str) {
        BmobQuery<StuNotify> query = new BmobQuery<StuNotify>();
        query.setSkip(skip)
                // .addWhereEndsWith("title",  str)
                //  .addWhereMatches("title",  str)
                .setLimit(50)
                .findObjects(new FindListener<StuNotify>() {
                    public void done(List<StuNotify> object, BmobException e) {
                        count += object.size();
                        if (e == null) {
                            if (object.size() > 0) {

                                if (endSize <= 0) {
                                    startSize = list.size();
                                } else {
                                    startSize = endSize;
                                }
                                for (StuNotify notify : object) {
                                    //Log.d("测试", notify.getTitle());
                                    if (notify.getTitle().contains(str)) {
                                        list.add(notify);
                                        Log.d(notify.getTitle() + "", System.currentTimeMillis() + "");
                                    }
                                }
                                if (list.size() - startSize < 10) {
                                    getDataFromBmob(startSize, count, str);
                                }

                            } else {
                                Log.d("测试，成功但是没有数据", "成功但是没有数据");
                                Toast.makeText(TestActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            Log.d("测试，获取不到数据，还有错", e.getMessage() + e.getErrorCode());
                        }


                    }
                });
    }


}
