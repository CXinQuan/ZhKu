package com.xyb.zhku.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.bean.User;
import com.xyb.zhku.utils.MD5Util;
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
    @BindView(R.id.iv_cancle_password)
    ImageView iv_cancle_password;
    @BindView(R.id.iv_cancle_phone)
    ImageView iv_cancle_phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 需要添加上 内容改变的监听，还需要添加焦点改变的监听
        initEditText(mEtUserPhone, iv_cancle_phone);
        initEditText(mEtUserPassword, iv_cancle_password);
        //给editText注册焦点变化的事件监听(有焦点+有文本内容的时候,x符号才会出现)
        MyOnFocusChangeListener myOnFocusChangeListener = new MyOnFocusChangeListener();
        mEtUserPhone.setOnFocusChangeListener(myOnFocusChangeListener);
        mEtUserPassword.setOnFocusChangeListener(myOnFocusChangeListener);
    }

    @OnClick({R.id.tv_forget_password, R.id.tv_new_user, R.id.tv_login, R.id.iv_cancle_phone, R.id.iv_cancle_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cancle_phone:
                mEtUserPhone.setText("");
                iv_cancle_phone.setVisibility(View.GONE);
                break;
            case R.id.iv_cancle_password:
                mEtUserPassword.setText("");
                iv_cancle_password.setVisibility(View.GONE);
                break;
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
                query.addWhereEqualTo("password",MD5Util.encrypt(mEtUserPassword.getText().toString().trim()));
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
                                showToast("登录成功");
                                mBtnLogin.setText("登录");
                                // TODO: 2018/9/22    判断是老师的主页 还是 学生的主页
//                                if (user.getIdentity() == User.STUDENT) {
//                                   // jumpToAnotherActivity(StuMainActivity.class_icon);
//                                }else{
//                                    jumpToAnotherActivity(MainActivity.class_icon);
//                                }
                                int identify = user.getIdentity();
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

    /**
     * 将 EditText et,ImageView iv_cancle 联合起来，当EditText内容改变时，iv_cancle对应的显示
     *
     * @param et
     * @param iv_cancle
     */
    private void initEditText(final EditText et, final ImageView iv_cancle) {
        iv_cancle.setVisibility(View.GONE);// 默认是不出现的
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {//当文本有变化后,如果文本内容不是空的,可以显示x号让,用户可以点击删除
                String phone = et.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    iv_cancle.setVisibility(View.VISIBLE); //显示x,提示用户可以删除
                } else {
                    iv_cancle.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 获取到焦点的时候也要判断是否显示 取消 按钮
     */
    class MyOnFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (v.getId() == R.id.et_user_phone) {
                //v就是注册此焦点变化监听的控件
                //hasFocus 在回调此方法的时候,是否获取了焦点  true 有焦点  false没有焦点
                String phone = mEtUserPhone.getText().toString();
                if (!TextUtils.isEmpty(phone) && hasFocus) { // 只有焦点在其身上并且其Text不为空，才显示，否则没有焦点或者内容为空都不显示
                    iv_cancle_phone.setVisibility(View.VISIBLE);
                } else {
                    iv_cancle_phone.setVisibility(View.GONE);
                }
            } else if (v.getId() == R.id.et_user_password) {
                String phoneOther = mEtUserPassword.getText().toString();
                if (!TextUtils.isEmpty(phoneOther) && hasFocus) {
                    iv_cancle_password.setVisibility(View.VISIBLE);
                } else {
                    iv_cancle_password.setVisibility(View.GONE);
                }
            }

        }
    }

}
