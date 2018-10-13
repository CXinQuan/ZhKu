package com.xyb.zhku.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.xyb.zhku.R;
import com.xyb.zhku.bean.TeacherHomeWork;
import com.xyb.zhku.fragment.teacher.TeacherHomeWorkDetailCompletionFagment;
import com.xyb.zhku.fragment.teacher.TeacherHomeWorkDetailFagment;
import com.xyb.zhku.utils.NetUtils;
import com.xyb.zhku.utils.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeacherHomeWorkDetailActivity extends AppCompatActivity {
    @BindView(R.id.iv_head_back)
    ImageView ivBack;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    HashMap<String, Fragment> map = new HashMap<>();
    private TeacherHomeWork homeWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_teacher_home_work_detail);
        ButterKnife.bind(this);
      //  NetUtils.getNetState(this);
        Intent intent = getIntent();
        homeWork = (TeacherHomeWork) intent.getSerializableExtra("TeacherHomeWork");
        if (homeWork == null) {
            Toast.makeText(this, "数据出错！", Toast.LENGTH_SHORT).show();
            return;
        }
        initView();
    }

    public void initView() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager.setAdapter(new TeacherHomeWorkDetailAdapter(getSupportFragmentManager()));
        tablayout.setupWithViewPager(viewPager);
    }


    class TeacherHomeWorkDetailAdapter extends FragmentPagerAdapter {
        String[] titles = new String[]{"完成情况", "作业详情"};

        public TeacherHomeWorkDetailAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            if (position == 1) {
                Fragment fragment = map.get("1");
                if (fragment == null) {
                    fragment = new TeacherHomeWorkDetailFagment(homeWork);
                    map.put("1", fragment);
                }
                return fragment;
            } else {
                Fragment fragment = map.get("0");
                if (fragment == null) {
                    fragment = new TeacherHomeWorkDetailCompletionFagment(homeWork);
                    map.put("0", fragment);
                }
                return fragment;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        public int getCount() {
            return titles.length;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            Utils.showLog(requestCode+"");
         //   switch (requestCode) {

               // case 101:    // TODO: 2018/9/26    评分后返回来的 处理
                    TeacherHomeWorkDetailCompletionFagment fragment = (TeacherHomeWorkDetailCompletionFagment) (map.get("0"));
                    int position=intent.getIntExtra("position",-1);
                    float grade=intent.getFloatExtra("grade",-1);
                    if(position!=-1&&grade!=-1){
                        fragment.handlePositionGradeChanged(position,grade);
                    }
                  //  break;
          //  }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
}
