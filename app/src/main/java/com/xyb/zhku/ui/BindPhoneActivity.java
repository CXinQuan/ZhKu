package com.xyb.zhku.ui;

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
import com.xyb.zhku.bean.User;
import com.xyb.zhku.utils.SMSUtil;
import com.xyb.zhku.utils.SharePreferenceUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import event.AddOrChangePhoneEvent;

import static com.xyb.zhku.R.id.tv_get_yzm;

/**
 * Created by 陈鑫权  on 2019/4/7.
 */

public class BindPhoneActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.head_text)
    TextView headText;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_yzm)
    EditText etYzm;
    @BindView(tv_get_yzm)
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
    boolean isSubmiting_toBMob = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isChangePhone = getIntent().getBooleanExtra("isChangePhone", false);
        if (isChangePhone) {
            headText.setText("修改手机号码");
        } else {
            headText.setText("绑定手机");
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEEP_TIME_MIN:
                    time--;
                    //时间更新在页面上
                    tvGetYzm.setText("(" + time + "s)后再发");
                    break;
                case RESET_TIME:
                    //重新初始化time，等待下一次获取验证码
                    time = 60;
                    //时间更新在页面上
                    tvGetYzm.setText("重新获取验证码");
                    tvGetYzm.setClickable(true);
                    break;
                case SEND_CODE_SUCCESS:
                    showToast("验证码下发成功");
                    tvGetYzm.setClickable(false);
                    break;
                case SEND_CODE_FAIL:
                    showToast("验证码下发失败");
                    tvGetYzm.setClickable(true);
                    break;
                case CHECK_CODE_SUCCESS:
                    isSubmiting_toMob = false;
                    tvGetYzm.setClickable(true);
                    if (!isSubmiting_toBMob) {    //防止多次注册
                        isSubmiting_toBMob = true;
                        addPhoneToBmob();
                    }
                    break;
                case CHECK_CODE_FAIL:
                    showToast("验证码验证失败");
                    btnSubmit.setText("提交");
                    isSubmiting_toMob = false;
                    break;
            }
        }
    };

    /**
     * 添加手机号码
     */
    private void addPhoneToBmob() {
        final User user = SharePreferenceUtils.getUser(mCtx);
        final BmobQuery<User> query_phone_is_exist = new BmobQuery<>();
        query_phone_is_exist.addWhereEqualTo("phone", etPhone.getText().toString())
                .findObjects(new FindListener<User>() {
                    public void done(final List<User> object, BmobException e) {
                        if (e == null) {
                            if (object.size() > 0) {
                                showToast("该手机已经注册了");
                                btnSubmit.setText("提交");
                                isSubmiting_toBMob = false;
                                return;
                            } else {
                                BmobQuery<User> query = new BmobQuery<>();
                                query.addWhereEqualTo("school_number", user.getSchool_number())
                                        .setLimit(1)
                                        .findObjects(new FindListener<User>() {
                                            public void done(final List<User> object, BmobException e) {
                                                if (e == null) {
                                                    if (object.size() == 1) {
                                                        final User user = object.get(0);
                                                        user.setPhone(etPhone.getText().toString());
                                                        user.update(user.getObjectId(), new UpdateListener() {
                                                            public void done(BmobException e) {
                                                                if (e == null) {
                                                                    SharePreferenceUtils.saveUser(mCtx, user);
                                                                    btnSubmit.setText("提交");
                                                                    isSubmiting_toBMob = false;
                                                                    EventBus.getDefault().post(new AddOrChangePhoneEvent(user.getPhone()));
                                                                    finish();
                                                                } else {
                                                                    showToast("服务器繁忙");
                                                                    btnSubmit.setText("提交");
                                                                    isSubmiting_toBMob = false;
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        btnSubmit.setText("提交");
                                                        isSubmiting_toBMob = false;
                                                    }
                                                } else {
                                                    showToast("服务器繁忙");
                                                    btnSubmit.setText("提交");
                                                    isSubmiting_toBMob = false;
                                                }
                                            }
                                        });
                            }
                        } else {
                            showToast("服务器繁忙");
                            btnSubmit.setText("提交");
                            isSubmiting_toBMob = false;
                        }
                    }
                });
    }

    @Override
    public int setContentViewLayout() {
        return R.layout.activity_bind_phone;
    }

    @OnClick({R.id.iv_back, R.id.btn_submit, tv_get_yzm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case tv_get_yzm:
                getYZM();
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private void submit() {
        if (!isSubmiting_toMob) {   //防止多次验证
            isSubmiting_toMob = true;
            submitCode("86", etPhone.getText().toString().trim(), etYzm.getText().toString());
        }
    }

    /**
     * 获取验证码
     */
    private void getYZM() {
        if (SMSUtil.judgePhoneNums(etPhone.getText().toString().trim())) {
            //请求发送短信
            sendCode("86", etPhone.getText().toString());
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
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
    }
}
