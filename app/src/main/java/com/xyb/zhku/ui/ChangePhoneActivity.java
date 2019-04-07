package com.xyb.zhku.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.utils.SMSUtil;
import com.xyb.zhku.utils.SharePreferenceUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by 陈鑫权  on 2019/4/7.
 * <p>
 * 主要在于验证 原来的 手机号码
 */
public class ChangePhoneActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.head_text)
    TextView headText;
    @BindView(R.id.et_phone)
    TextView etPhone;
    @BindView(R.id.et_yzm)
    EditText etYzm;
    @BindView(R.id.tv_get_yzm)
    TextView tvGetYzm;
    @BindView(R.id.btn_submit)
    Button btnSubmit;


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
    private String phone;


    @Override
    public int setContentViewLayout() {
        return R.layout.activity_change_phone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone = (String) SharePreferenceUtils.get(this, "phone", "");
        etPhone.setText("向手机号码 " + SMSUtil.encryptionPhone(phone) + " 进行验证");

    }

    // boolean isChangePhone = getIntent().getBooleanExtra("isChangePhone", false);
    // TODO: 2019/4/7   获取验证码进行 确认 获取权限 更改号码，只要验证成功就跳转到绑定手机号码的界面
    @OnClick({R.id.tv_get_yzm, R.id.btn_submit, R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_get_yzm:
                getYZM();
                break;
            case R.id.btn_submit:
                submit();
                break;
        }

    }

    /**
     * 提交验证码
     */
    private void submit() {
        if (!SMSUtil.judgePhoneNums(phone)) {
            showToast("手机号码异常");
            return;
        }
        if ("".equals(etYzm.getText().toString().trim())) {
            showToast("验证码不能为空");
            return;
        }
        submitCode("86", phone, etYzm.getText().toString().trim());
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEEP_TIME_MIN:
                    time--;
                    tvGetYzm.setText("(" + time + "s)后再发");//时间更新在页面上
                    break;
                case RESET_TIME:
                    time = 60;  //重新初始化time，等待下一次获取验证码
                    tvGetYzm.setText("重新获取验证码");    //时间更新在页面上
                    tvGetYzm.setClickable(true);
                    break;
                case SEND_CODE_SUCCESS:
                    showToast("验证码下发成功");
                    tvGetYzm.setClickable(false);
                    break;
                case SEND_CODE_FAIL:
                    showToast("验证码下发失败");
                    isSubmiting_toMob = false;
                    tvGetYzm.setClickable(true);
                    break;
                case CHECK_CODE_SUCCESS:
                    isSubmiting_toMob = false;
                    tvGetYzm.setClickable(true);
                    Intent intent = new Intent(mCtx, BindPhoneActivity.class);
                    intent.putExtra("isChangePhone", true);
                    startActivity(intent);
                    finish();
                    break;
                case CHECK_CODE_FAIL:
                    showToast("验证码验证失败");
                    isSubmiting_toMob = false;
                    tvGetYzm.setClickable(true);
                    break;
            }
        }
    };

    /**
     * 获取验证码
     */
    private void getYZM() {
        if (!SMSUtil.judgePhoneNums(phone)) {
            showToast("手机号码异常");
            return;
        }
        if (isSubmiting_toMob) {
            return;
        }
        isSubmiting_toMob = true;
        tvGetYzm.setClickable(false);
        sendCode("86", phone);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

}
