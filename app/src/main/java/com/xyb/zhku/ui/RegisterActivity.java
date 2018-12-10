package com.xyb.zhku.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.bean.User;
import com.xyb.zhku.utils.EmailUtil;
import com.xyb.zhku.utils.MD5Util;
import com.xyb.zhku.utils.SMSUtil;
import com.xyb.zhku.utils.SharePreferenceUtils;
import com.xyb.zhku.utils.UIUtils;
import com.xyb.zhku.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class RegisterActivity extends BaseActivity {
    @BindView(R.id.head_back)
    ImageView head_back;
    @BindView(R.id.head_text)
    TextView head_text;

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

    @BindView(R.id.rb_reg_student)
    RadioButton rb_reg_student;
    @BindView(R.id.rb_reg_teacher)
    RadioButton rb_reg_teacher;
    @BindView(R.id.et_reg_school_number)
    EditText et_reg_school_number;
    @BindView(R.id.et_reg_name)
    EditText et_reg_name;
    @BindView(R.id.act_reg_enrollment_year)
    AutoCompleteTextView act_reg_enrollment_year;
    @BindView(R.id.act_reg_college)
    AutoCompleteTextView act_reg_college;
    @BindView(R.id.act_reg_major)
    AutoCompleteTextView act_reg_major;
    @BindView(R.id.act_reg_class)
    AutoCompleteTextView act_reg_class;
    @BindView(R.id.btn_regiest)
    Button btn_regiest;
    @BindView(R.id.ll_reg_class)
    LinearLayout ll_reg_class;
    @BindView(R.id.ll_reg_major)
    LinearLayout ll_reg_major;
    @BindView(R.id.ll_reg_enrollment_year)
    LinearLayout ll_reg_enrollment_year;
    @BindView(R.id.tv_reg_school_number)
    TextView tv_reg_school_number;

    @BindView(R.id.ll_email)
    LinearLayout ll_email;
    @BindView(R.id.et_reg_email)
    EditText et_reg_email;


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
    private String[] year;
    private String[] allCollege;
    private String[] allMajor;
    private String[] allClass;
    private int time = 60;
    boolean isSubmiting_toMob = false;
    boolean isSubmiting_toBMob = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        year = Utils.getYearStr();
        allCollege = Utils.getAllCollege(mCtx);
        allMajor = Utils.getAllMajor(mCtx);
        allClass = Utils.getAllClass(mCtx);
        bindArray(act_reg_enrollment_year, year);
        bindArray(act_reg_college, allCollege);
        bindArray(act_reg_major, allMajor);
        bindArray(act_reg_class, allClass);
        // 监听 Radiobutton 如果是老师 则不出现学号 或者改为 老师的工号
        rb_reg_student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll_reg_class.setVisibility(View.VISIBLE);
                    ll_reg_major.setVisibility(View.VISIBLE);
                    ll_email.setVisibility(View.GONE);
                    ll_reg_enrollment_year.setVisibility(View.VISIBLE);
                    tv_reg_school_number.setText("学号");
                } else {
                    ll_reg_class.setVisibility(View.GONE);
                    ll_reg_major.setVisibility(View.GONE);
                    ll_reg_enrollment_year.setVisibility(View.GONE);
                    ll_email.setVisibility(View.VISIBLE);
                    tv_reg_school_number.setText("工号");
                }
            }
        });
        rb_reg_teacher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    /**
     * 对 AutoCompleteTextView 绑定 数据
     *
     * @param autoCompleteTextView
     * @param autoString
     */
    public void bindArray(AutoCompleteTextView autoCompleteTextView, String[] autoString) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, autoString);
        autoCompleteTextView.setAdapter(adapter);     // 绑定adapter
    }

    @Override
    public int setContentViewLayout() {
        return R.layout.activity_register;
    }

    @OnClick({R.id.btn_regiest, R.id.head_back, R.id.tv_get_yzm})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.head_back:
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

                if (UIUtils.isEmtpy(et_reg_phone) || UIUtils.isEmtpy(et_reg_name)
                        || UIUtils.isEmtpy(et_reg_passsword) || UIUtils.isEmtpy(et_reg_passsword_sure)
                        || UIUtils.isEmtpy(et_reg_school_number) || UIUtils.isEmtpy(et_reg_yzm)) {
                    showToast("请填写必要项！");
                    return;
                }
                // 分老师还是学生
                if (rb_reg_student.isChecked()) {
                    if (UIUtils.isEmtpy(act_reg_class) || UIUtils.isEmtpy(act_reg_college)
                            || UIUtils.isEmtpy(act_reg_enrollment_year) || UIUtils.isEmtpy(act_reg_major)) {
                        showToast("请填写必要项！");
                        return;
                    }
                    // 判断两次密码是否一致
                    if (!et_reg_passsword.getText().toString().trim().equals(et_reg_passsword_sure.getText().toString().trim())) {
                        showToast("两次密码输入不一致！");
                        return;
                    }
                    //  判断各项填写是否符合 学生 判断学院、年级、等 是否是String数组里面的预备选项
                    //学号必须是 以 20开头的 而且必须是12位
                    if (!Utils.isSchoolNumber(et_reg_school_number.getText().toString().trim())) {
                        showSnackBar(act_reg_major, "学号输入格式有误！");
                        return;
                    }
                    if (!contain(year, act_reg_enrollment_year.getText().toString().trim())) {
                        showSnackBar(act_reg_major, "年份输入格式有误！");
                        return;
                    }
//                    if (!contain(allCollege, act_reg_college.getText().toString().trim())) {
//                        showSnackBar(act_reg_college, "学院输入格式有误！");
//                        return;
//                    }
                    if (!contain(allMajor, act_reg_major.getText().toString().trim())) {
                        showSnackBar(act_reg_major, "专业输入格式有误！");
                        return;
                    }
                    if (!contain(allClass, act_reg_class.getText().toString().trim())) {
                        showSnackBar(act_reg_class, "班级输入格式有误！");
                        return;
                    }
                } else {
                    // TODO: 2018/9/22   老师的逻辑处理，添加邮箱的 正则表达式 判断
                    if (UIUtils.isEmtpy(et_reg_email)) {
                        showToast("请输入邮箱");
                        return;
                    }
                    if (!EmailUtil.isEmail(et_reg_email.getText().toString().trim())) {
                        showToast("请输入正确的邮箱地址");
                        return;
                    }
                }
                if (!contain(allCollege, act_reg_college.getText().toString().trim())) {
                    showSnackBar(act_reg_college, "学院输入格式有误！");
                    return;
                }
                if (isSubmiting_toMob || isSubmiting_toBMob) {  //正在注册
                    return;
                }
                // 正在验证， 需要等验证完才能再次点击，防止多次注册
                btn_regiest.setText("正在注册...");
                if (!isSubmiting_toMob) {   //防止多次验证
                    isSubmiting_toMob = true;
                    submitCode("86", et_reg_phone.getText().toString().trim(), et_reg_yzm.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    public boolean contain(String[] all, String str) {
        for (int i = 0; i < all.length; i++) {
            if (all[i].equals(str)) {
                return true;
            }
        }
        return false;
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
                        addUserToBmob();
                    }
                    break;
                case CHECK_CODE_FAIL:
                    showToast("验证码验证失败");
                    btn_regiest.setText("注册");
                    isSubmiting_toMob = false;
                    break;
            }
        }
    };

    /**
     * 验证码   验证成功，先查  该用户是否存在
     */
    private void addUserToBmob() {
        BmobQuery<User> query = new BmobQuery<User>();
        //查询条件
        //query.addWhereEqualTo("phone", et_reg_phone.getText().toString().trim());

        // 只要电话或者学号（工号） 是已经存在的，那么，就说明该用户已经存在
        BmobQuery<User> queryPhone = new BmobQuery<User>();
        BmobQuery<User> querySchoolNumber = new BmobQuery<User>();
        queryPhone.addWhereEqualTo("phone",et_reg_phone.getText().toString().trim());
        querySchoolNumber.addWhereEqualTo("school_number", et_reg_school_number.getText().toString().trim());
        List<BmobQuery<User>> accountNumber = new ArrayList<BmobQuery<User>>();
        accountNumber.add(queryPhone);
        accountNumber.add(querySchoolNumber);
        query.or(accountNumber);

        query.setLimit(1);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if (object.size() == 1) {
                        showToast("该用户已经存在");
                        btn_regiest.setText("注册");
                    } else {
                        //  如果该用户不存在就向服务器插入一条记录
                        final User user = new User();
                        // TODO: 2018/9/21   获取用户信息
                        if (rb_reg_student.isChecked()) {
                            user.setIdentity(User.STUDENT);
                            user.setEnrollment_year(act_reg_enrollment_year.getText().toString().trim());
                            user.setMajor(act_reg_major.getText().toString().trim());
                            user.setClassNumber(Integer.parseInt(act_reg_class.getText().toString().trim()));
                        } else {
                            user.setIdentity(User.TEACHER);
                            user.setEmail(et_reg_email.getText().toString().trim());
                        }
                        user.setCollege(act_reg_college.getText().toString().trim());
                        user.setSchool_number(et_reg_school_number.getText().toString().trim());
                        user.setPhone(et_reg_phone.getText().toString().trim());
                        user.setName(et_reg_name.getText().toString().trim());
                        user.setPassword(MD5Util.encrypt(et_reg_passsword.getText().toString().trim()));
                        user.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    showToast("成功注册账户" + objectId);
                                    Log("添加数据成功，返回objectId为：", objectId);
                                    // TODO: 2018/9/22    跳转页面 并保存用户信息
                                    SharePreferenceUtils.saveUser(mCtx, user);
                                    btn_regiest.setText("注册");
                                    isSubmiting_toBMob = false;
                                    //  2018/9/22   MainActivity会自行 判断是老师的主页 还是 学生的主页
                                    jumpToAnotherActivity(MainActivity.class);
                                    finish();
                                } else {
                                    showToast("服务器繁忙，创建失败!");
                                    Log("创建数据失败：", e.getMessage());
                                    btn_regiest.setText("注册");
                                    isSubmiting_toBMob = false;
                                }
                            }
                        });
                    }
                } else {
                    showToast("服务器繁忙!");
                    Log("创建数据失败：", e.getMessage());
                    btn_regiest.setText("注册");
                    isSubmiting_toBMob = false;
                }
            }
        });
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
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    }

}
