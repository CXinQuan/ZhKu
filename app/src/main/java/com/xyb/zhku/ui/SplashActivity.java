package com.xyb.zhku.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.bean.User;
import com.xyb.zhku.global.Constants;
import com.xyb.zhku.utils.SharePreferenceUtils;

public class SplashActivity extends BaseActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScaleAnimation animation_scale = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation animation_alpha = new AlphaAnimation(0.0f, 1.0f);
        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(animation_alpha);
        animation.addAnimation(animation_scale);
        animation.setDuration(2000);
        animation.setInterpolator(new BounceInterpolator());
        animation.setFillAfter(true);  //停留在最后的位置，这样就不会在结束后还返回原始状态
        ImageView iv_logo = (ImageView) findViewById(R.id.iv_logo);
        iv_logo.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {

                String user = (String) SharePreferenceUtils.get(mCtx, Constants.USER, "");
                int identity = (int) SharePreferenceUtils.get(mCtx, Constants.IDENTITY, -1);
                if (identity != User.STUDENT && identity != User.TEACHER && identity != User.MANAGER_NOTIFY && identity != User.MANAGER_TEACHINGTASK) {
                    jumpToAnotherActivity(LoginActivity.class);
                } else {
                    // TODO: 2018/9/22    判断是老师的主页 还是 学生的主页
//                    if (identity == User.STUDENT) {
//                        // TODO: 2018/9/30  进入学生的主页
//                        //   jumpToAnotherActivity(StuMainActivity.class_icon);
//                    } else {
//                        //  2018/9/30  进入老师的主页
//                        jumpToAnotherActivity(MainActivity.class_icon);
//                    }
                    Intent intent = new Intent(mCtx, MainActivity.class);
                    intent.putExtra(Constants.IDENTITY, identity);
                    startActivity(intent);
                }
                finish();
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
    }

    @Override
    public int setContentViewLayout() {
        return R.layout.activity_splash;
    }
}
