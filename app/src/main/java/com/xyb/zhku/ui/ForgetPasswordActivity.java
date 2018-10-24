package com.xyb.zhku.ui;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.bean.User;
import com.xyb.zhku.utils.MD5Util;
import com.xyb.zhku.utils.SMSUtil;
import com.xyb.zhku.utils.SharePreferenceUtils;
import com.xyb.zhku.utils.UIUtils;

import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ForgetPasswordActivity extends BaseActivity {
    @BindView(R.id.et_reg_phone)
    EditText et_reg_phone;
    @BindView(R.id.et_reg_yzm)
    EditText et_reg_yzm;
    @BindView(R.id.tv_get_yzm)
    TextView tv_get_yzm;
    @BindView(R.id.et_reg_passsword)
    EditText et_reg_passsword;
    @BindView(R.id.et_reg_passsword_sure)
    EditText et_reg_passsword_sure;
    @BindView(R.id.btn_regiest)
    Button btn_regiest;
    @BindView(R.id.tv_password_login)
    TextView tv_password_login;       //密码登录

    private static final int KEEP_TIME_MIN = 100;
    private static final int RESET_TIME = 101;
    //发送验证码成功
    private static final int SEND_CODE_SUCCESS = 102;
    //发送验证码失败
    private static final int SEND_CODE_FAIL = 103;
    //检测验证码和手机能够匹配
    private static final int CHECK_CODE_SUCCESS = 104;
    //检测验证码和手机不能匹配
    private static final int CHECK_CODE_FAIL = 105;
    private int time = 60;
    boolean isSubmiting_toMob = false;
    boolean isSubmiting_toBMob = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setContentViewLayout() {
        return R.layout.activity_forget_password;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEEP_TIME_MIN:
                    time--;
                    //时间更新在页面上
                    tv_get_yzm.setText("(" + time + "s)后再发");
                    break;
                case RESET_TIME:
                    //重新初始化time，等待下一次获取验证码
                    time = 60;
                    //时间更新在页面上
                    tv_get_yzm.setText("重新获取验证码");
                    tv_get_yzm.setClickable(true);
                    break;
                case SEND_CODE_SUCCESS:
                    showToast("验证码下发成功");
                    tv_get_yzm.setClickable(false);
                    break;
                case SEND_CODE_FAIL:
                    showToast("验证码下发失败");
                    tv_get_yzm.setClickable(true);
                    break;
                case CHECK_CODE_SUCCESS:
                    isSubmiting_toMob = false;
                    tv_get_yzm.setClickable(true);
                    if (!isSubmiting_toBMob) {    //防止多次注册
                        isSubmiting_toBMob = true;
                        updateUserToBmob();
                    }
                    break;
                case CHECK_CODE_FAIL:
                    showToast("验证码验证失败");
                    btn_regiest.setText("确认修改");
                    isSubmiting_toMob = false;
                    break;
            }
        }
    };

    /**
     * 更新用户的密码
     */
    private void updateUserToBmob() {
        BmobQuery<User> query = new BmobQuery<User>();
        //查询条件
        query.addWhereEqualTo("phone", et_reg_phone.getText().toString().trim());
        query.setLimit(1);
        query.findObjects(new FindListener<User>() {
            public void done(final List<User> object, BmobException e) {
                if (e == null) {
                    if (object.size() == 1) {
                        final User user = object.get(0);
                        user.setPassword(MD5Util.encrypt(et_reg_passsword.getText().toString().trim()));
                        user.update(user.getObjectId(), new UpdateListener() {
                            public void done(BmobException e) {
                                if (e == null) {
                                    showToast("修改密码成功！");
                                    // TODO: 2018/9/22    跳转页面 并保存用户信息
                                    SharePreferenceUtils.saveUser(mCtx, user);
                                    btn_regiest.setText("确认修改");
                                    isSubmiting_toBMob = false;
                                    // TODO: 2018/9/22    判断是老师的主页 还是 学生的主页

                                    jumpToAnotherActivity(MainActivity.class);
                                    finish();
                                } else {
                                    showToast("服务器繁忙，修改失败!");
                                    Log("创建数据失败：", e.getMessage());
                                    btn_regiest.setText("确认修改");
                                    isSubmiting_toBMob = false;
                                }
                            }
                        });
                    } else {
                        showToast("该用户不存在！");
                        btn_regiest.setText("确认修改");
                        isSubmiting_toBMob = false;
                    }
                } else {
                    showToast("服务器繁忙！");
                    btn_regiest.setText("确认修改");
                    isSubmiting_toBMob = false;
                }
            }
        });
    }

    @OnClick({R.id.btn_regiest, R.id.head_back, R.id.tv_get_yzm, R.id.tv_password_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back:
                finish();
                break;
            case R.id.tv_password_login:
                jumpToAnotherActivity(LoginActivity.class);
                finish();
                break;
            case R.id.tv_get_yzm:
                if (SMSUtil.judgePhoneNums(et_reg_phone.getText().toString().trim())) {
                    //请求发送短信
                    sendCode("86", et_reg_phone.getText().toString());
                    new Thread() { //倒计时  timerTask  handler
                        @Override
                        public void run() {
                            //每个1秒钟减少数组1
                            while (time > 0) {
                                //通过hanlder机制,告知主线程更新时间,更新时间周期,1秒钟更新一次
                                handler.sendEmptyMessage(KEEP_TIME_MIN);
                                try {
                                    Thread.sleep(999);//因为执行代码需要时间
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            //跳出循环，说明已经到达0秒了,在60秒的计时过程中没有获取到验证码,重新获取验证码
                            handler.sendEmptyMessage(RESET_TIME);
                        }
                    }.start();

                } else {
                    showToast("请正确输入手机号码！");
                }
                break;
            case R.id.btn_regiest:

                if (UIUtils.isEmtpy(et_reg_phone) || UIUtils.isEmtpy(et_reg_passsword)
                        || UIUtils.isEmtpy(et_reg_passsword_sure) || UIUtils.isEmtpy(et_reg_yzm)) {
                    showToast("请填写必要项！");
                    return;
                }
                // 判断两次密码是否一致
                if (!et_reg_passsword.getText().toString().trim().equals(et_reg_passsword_sure.getText().toString().trim())) {
                    showToast("两次密码输入不一致！");
                    return;
                }
                if (isSubmiting_toMob || isSubmiting_toBMob) {  //正在注册
                    return;
                }
                // 正在验证， 需要等验证完才能再次点击，防止多次注册
                btn_regiest.setText("正在修改...");
                if (!isSubmiting_toMob) {   //防止多次验证
                    isSubmiting_toMob = true;
                    submitCode("86", et_reg_phone.getText().toString().trim(), et_reg_yzm.getText().toString());
                }
                break;
        }
    }

    /**
     * 请求验证码
     * country表示国家代码，如“86”
     * phone表示手机号码
     */
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //给指定手机下发短信验证码的这个事件是成功的
                        handler.sendEmptyMessage(SEND_CODE_SUCCESS);
                    }
                } else {
                    // TODO 处理错误的结果
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //给指定手机下发短信验证码的这个事件是失败的
                        handler.sendEmptyMessage(SEND_CODE_FAIL);
                    }
                }
            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }

    /**
     * 提交验证码
     * country表示国家代码，如“86”
     * phone表示手机号码
     * code表示验证码，如“1357”
     */
    public void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //验证码和手机号码是匹配的,做用户的注册和登录
                        handler.sendEmptyMessage(CHECK_CODE_SUCCESS);
                    }
                } else {
                    // TODO 失败 处理错误的结果
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //验证码和手机号码是不匹配的
                        handler.sendEmptyMessage(CHECK_CODE_FAIL);
                    }
                }
            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    }

}
