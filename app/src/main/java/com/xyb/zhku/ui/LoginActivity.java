package com.xyb.zhku.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.bean.User;
import com.xyb.zhku.global.Constants;
import com.xyb.zhku.utils.SMSUtil;
import com.xyb.zhku.utils.SharePreferenceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_user_phone)
    EditText mEtUserPhone;
    @BindView(R.id.et_user_password)
    EditText mEtUserPassword;
    @BindView(R.id.tv_login)
    TextView mBtnLogin;
    @BindView(R.id.tv_new_user)
    TextView mTvNewUser;
    @BindView(R.id.tv_forget_password)
    TextView mTvForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.tv_forget_password, R.id.tv_new_user, R.id.tv_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_new_user:
                jumpToAnotherActivity(RegisterActivity.class);
                break;
            case R.id.tv_forget_password: // TODO: 2018/9/22   忘记密码 
                jumpToAnotherActivity(ForgetPasswordActivity.class);
                break;
            case R.id.tv_login:
                if (!SMSUtil.judgePhoneNums(mEtUserPhone.getText().toString().trim())) {
                    showToast("请正确输入手机号码！");
                    return;
                }
                if (mEtUserPassword.getText().toString().trim() == null ||
                        TextUtils.isEmpty(mEtUserPassword.getText().toString().trim())) {
                    showToast("请输入密码");
                    return;
                }
                mBtnLogin.setText("正在登录，请稍后...");
                BmobQuery<User> query = new BmobQuery<User>();
                //查询条件
                query.addWhereEqualTo("phone", mEtUserPhone.getText().toString().trim());
                query.addWhereEqualTo("password", mEtUserPassword.getText().toString().trim());
                //返回1条数据，如果不加上这条语句，默认返回10条数据
                query.setLimit(1);
                //执行查询方法
                query.findObjects(new FindListener<User>() {
                    public void done(List<User> object, BmobException e) {
                        if (e == null) {  //查询 有与没有 e都为空，但是object的长度就不一定了
                            if (object.size() == 1) {
                                Log.d("该用户存在", "该用户存在");
                                User user = object.get(0);
                                if (user != null) {
                                    // TODO: 2018/9/22    保存用户信息
                                    SharePreferenceUtils.saveUser(mCtx, user);
                                }
                                showToast( "登录成功");
                                mBtnLogin.setText("登录");
                                // TODO: 2018/9/22    判断是老师的主页 还是 学生的主页
//                                if (user.getIdentity() == User.STUDENT) {
//                                   // jumpToAnotherActivity(StuMainActivity.class_icon);
//                                }else{
//                                    jumpToAnotherActivity(MainActivity.class_icon);
//                                }
                                int identify=user.getIdentity();
                                if (identify != -1) {
                                    Intent intent = new Intent(mCtx, MainActivity.class);
                                 //   intent.putExtra(Constants.IDENTITY,identify);
                                    startActivity(intent);
                                }
                                finish();
                            } else {
                                mBtnLogin.setText("登录");
                                showToast("账号或密码输入有误");
                            }
                        } else {
                            showToast("服务器繁忙");
                            mBtnLogin.setText("登录");
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int setContentViewLayout() {
        return R.layout.activity_login;
    }


}
