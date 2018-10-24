package com.xyb.zhku.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.hxb.easynavigition.view.EasyNavigitionBar;
import com.xyb.zhku.R;
import com.xyb.zhku.bean.User;
import com.xyb.zhku.fragment.manager.ManagerReleaseFragment;
import com.xyb.zhku.fragment.student.StuHomeFragment;
import com.xyb.zhku.fragment.student.StuHomeWorkFragment;
import com.xyb.zhku.fragment.student.StuMyFragment;
import com.xyb.zhku.fragment.teacher.TeacherHomeFragment;
import com.xyb.zhku.fragment.teacher.TeacherHomeWorkFragment;
import com.xyb.zhku.fragment.teacher.TeacherMyFragment;
import com.xyb.zhku.global.Constants;
import com.xyb.zhku.utils.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigitionBar)
    EasyNavigitionBar navigitionBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_teacher_main);
        ButterKnife.bind(this);
        // NetUtils.getNetState(this);
//        Intent intent = getIntent();
//        int identify = intent.getIntExtra(Constants.IDENTITY, -1);
//        if (identify != User.STUDENT && identify != User.TEACHER) {
//            startActivity(new Intent(MainActivity.this, LoginActivity.class));
//            Toast.makeText(this, "账号已过期，请重新登录！", Toast.LENGTH_SHORT).show();
//            finish();
//        }
        int identify = (int) SharePreferenceUtils.get(this, Constants.IDENTITY, -1);

        // TODO: 2018/10/16   identify此处用于测试，该行代码需要注释掉
        //identify = User.MANAGER_TEACHINGTASK;


        if (identify != User.STUDENT && identify != User.TEACHER && identify != User.MANAGER_NOTIFY && identify != User.MANAGER_TEACHINGTASK) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            Toast.makeText(this, "账号已过期，请重新登录", Toast.LENGTH_SHORT).show();
            finish();
        }
        initNavigitionBar(identify);
    }

    public void initNavigitionBar(int identify) {
        List<Fragment> fragments = new ArrayList<>();

        String[] tabText = {"首页", "作业", "我的"};
        int[] normalIcon = {R.mipmap.home_m, R.mipmap.order_m, R.mipmap.my_m};//未选中icon
        int[] selectIcon = {R.mipmap.home_s, R.mipmap.order_s, R.mipmap.my_s};//选中时icon

        String[] tabTextManager = {"首页", "发布", "我的"};
        int[] normalIconManager = {R.mipmap.home_m, R.mipmap.release_home_n, R.mipmap.my_m};//未选中icon
        int[] selectIconManager = {R.mipmap.home_s, R.mipmap.release_home_s, R.mipmap.my_s};//选中时icon

        if (identify == User.TEACHER) {
            fragments.add(new TeacherHomeFragment());//getSupportFragmentManager()
            fragments.add(new TeacherHomeWorkFragment());
            fragments.add(new TeacherMyFragment());
            navigitionBar.titleItems(tabText)
                    .normalIconItems(normalIcon)
                    .selectIconItems(selectIcon);
        } else if (identify == User.STUDENT) {
            // TODO: 2018/9/30      添加学生的fragment
            fragments.add(new StuHomeFragment());
            fragments.add(new StuHomeWorkFragment());
            fragments.add(new StuMyFragment());
            navigitionBar.titleItems(tabText)
                    .normalIconItems(normalIcon)
                    .selectIconItems(selectIcon);
        } else if (identify == User.MANAGER_NOTIFY || identify == User.MANAGER_TEACHINGTASK) {
            fragments.add(new TeacherHomeFragment());
            fragments.add(new ManagerReleaseFragment(identify));
            fragments.add(new TeacherMyFragment());
            navigitionBar.titleItems(tabTextManager)
                    .normalIconItems(normalIconManager)
                    .selectIconItems(selectIconManager);
        }

        navigitionBar.fragmentList(fragments)
                .selectTextColor(Color.parseColor("#3f84ff"))
                .fragmentManager(getSupportFragmentManager())
                .build();
        checkPermissions();




/*
navigitionBar.titleItems(tabText)      //必传  Tab文字集合
               .normalIconItems(normalIcon)   //必传  Tab未选中图标集合
               .selectIconItems(selectIcon)   //必传  Tab选中图标集合
               .fragmentList(fragments)       //必传  fragment集合
               .fragmentManager(getSupportFragmentManager())     //必传
               .iconSize(20)     //Tab图标大小
               .tabTextSize(10)   //Tab文字大小
               .tabTextTop(2)     //Tab文字距Tab图标的距离
               .normalTextColor(Color.parseColor("#666666"))   //Tab未选中时字体颜色
               .selectTextColor(Color.parseColor("#333333"))   //Tab选中时字体颜色
               .scaleType(ImageView.ScaleType.CENTER_INSIDE)  //同 ImageView的ScaleType
               .navigitionBackground(Color.parseColor("#80000000"))   //导航栏背景色
               .onTabClickListener(new EasyNavigitionBar.OnTabClickListener() {   //Tab点击事件  return true 页面不会切换
                   @Override
                   public boolean onTabClickEvent(View view, int position) {
                       return false;
                   }
               })
               .smoothScroll(false)  //点击Tab  Viewpager切换是否有动画
               .canScroll(false)    //Viewpager能否左右滑动
               .mode(EasyNavigitionBar.MODE_ADD)   //默认MODE_NORMAL 普通模式  //MODE_ADD 带加号模式
               .anim(Anim.ZoomIn)                //点击Tab时的动画
               .addIconSize(36)    //中间加号图片的大小
               .addLayoutHeight(100)   //包含加号的布局高度 背景透明  所以加号看起来突出一块
               .navigitionHeight(40)  //导航栏高度
               .lineHeight(100)         //分割线高度  默认1px
               .lineColor(Color.parseColor("#ff0000"))
               .addLayoutRule(EasyNavigitionBar.RULE_BOTTOM) //RULE_CENTER 加号居中addLayoutHeight调节位置 EasyNavigitionBar.RULE_BOTTOM 加号在导航栏靠下
               .addLayoutBottom(10)   //加号到底部的距离
               .hasPadding(true)    //true ViewPager布局在导航栏之上 false有重叠
               .hintPointLeft(-3)  //调节提示红点的位置hintPointLeft hintPointTop（看文档说明）
               .hintPointTop(-3)
               .hintPointSize(6)    //提示红点的大小
               .msgPointLeft(-10)  //调节数字消息的位置msgPointLeft msgPointTop（看文档说明）
               .msgPointTop(-10)
               .msgPointTextSize(9)  //数字消息中字体大小
               .msgPointSize(18)    //数字消息红色背景的大小
               .addAlignBottom(true)  //加号是否同Tab文字底部对齐  RULE_BOTTOM时有效；
               .addTextTopMargin(50)  //加号文字距离加号图片的距离
               .addTextSize(15)      //加号文字大小
               .addNormalTextColor(Color.parseColor("#ff0000"))    //加号文字未选中时字体颜色
               .addSelectTextColor(Color.parseColor("#00ff00"))    //加号文字选中时字体颜色
               .build();

               //数字消息大于99显示99+ 小于等于0不显示，取消显示则可以navigitionBar.setMsgPointCount(2, 0)
                navigitionBar.setMsgPointCount(2, 109);
                navigitionBar.setMsgPointCount(0, 5);
                //红点提示 第二个参数控制是否显示
                navigitionBar.setHintPoint(3, true);

                //清除第四个红点提示
                navigitionBar.clearHintPoint(3);
                //清除第一个数字消息
                navigitionBar.clearMsgPoint(0);
* */


    }

    long firstTime = 0;

    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(this, "再次点击退出应用程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 111:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /* 检查使用权限 */
    protected void checkPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                PackageManager pm = getPackageManager();
                PackageInfo pi = pm.getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
                ArrayList<String> list = new ArrayList<String>();
                for (String p : pi.requestedPermissions) {
                    if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                        list.add(p);
                    }
                }
                if (list.size() > 0) {
                    String[] permissions = list.toArray(new String[list.size()]);
                    if (permissions != null) {
                        requestPermissions(permissions, 1);
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (permissions != null && permissions.length > 0) {
                StringBuilder sb = null;
                String permission;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        continue;
                    }
                    if (sb == null) {
                        sb = new StringBuilder();
                    }
                    permission = permissions[i];
                }
                if (sb != null) {
                    //toast 提示用户已经禁用了必要的权限，然后去权限中心打开即可
                    //  Toast.makeText(getApplication(), "您已经禁用了必要的权限，建议您去权限中心打开！", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
