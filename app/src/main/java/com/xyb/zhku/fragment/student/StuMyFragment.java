package com.xyb.zhku.fragment.student;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.global.Constants;
import com.xyb.zhku.ui.BindPhoneActivity;
import com.xyb.zhku.ui.ChangePasswordActivity;
import com.xyb.zhku.ui.ChangePhoneActivity;
import com.xyb.zhku.ui.LoginActivity;
import com.xyb.zhku.utils.SMSUtil;
import com.xyb.zhku.utils.SharePreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import event.AddOrChangePhoneEvent;

/**
 * Created by lenovo on 2018/10/1.
 */

public class StuMyFragment extends BaseFragment {// TeacherMyFragment
    @BindView(R.id.tv_my_major)
    TextView tv_my_major;

    @BindView(R.id.tv_my_class)
    TextView tv_my_class;

    @BindView(R.id.tv_my_enrollment_year)
    TextView tv_my_enrollment_year;

    @BindView(R.id.tv_my_school_number_name)
    TextView tv_my_school_number_name;

    @BindView(R.id.btn_logout)
    Button btnLogout;


    @BindView(R.id.tv_my_name)
    TextView tvMyName;
    @BindView(R.id.tv_my_phone)
    TextView tvMyPhone;
    @BindView(R.id.tv_my_school_number)
    TextView tvMySchoolNumber;
    @BindView(R.id.tv_my_colleg)
    TextView tvMyColleg;


    @Override
    protected int setView() {
        return R.layout.stu_fragment_my;
    }

    @Override
    protected void init(View view) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addChangePhone(AddOrChangePhoneEvent event) {
        if (event != null && event.getPhone() != null) {
            tvMyPhone.setText(SMSUtil.encryptionPhone(event.getPhone()));
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        // super.initData(savedInstanceState);
        String name = (String) SharePreferenceUtils.get(mCtx, Constants.NAME, "姓名异常，建议重新登录");
        String phone = (String) SharePreferenceUtils.get(mCtx, Constants.PHONE, "点击绑定手机");
        String college = (String) SharePreferenceUtils.get(mCtx, Constants.COLLEGE, "学院名异常，建议重新登录");
        String school_number = (String) SharePreferenceUtils.get(mCtx, Constants.SCHOOL_NUMBER, "账号异常，建议重新登录");
        String major = (String) SharePreferenceUtils.get(mCtx, Constants.MAJOR, "专业异常，建议重新登录");
        int my_class = (int) SharePreferenceUtils.get(mCtx, Constants.UCLASS, -1);
        String enrollment_year = (String) SharePreferenceUtils.get(mCtx, Constants.ENROLLMENT_YEAR, "入学年份异常，建议重新登录");

        tvMyName.setText(name);
        tvMyPhone.setText(SMSUtil.encryptionPhone(phone));
        tvMyColleg.setText(college);
        tvMySchoolNumber.setText(school_number);
        tv_my_class.setText(my_class + "班");
        tv_my_enrollment_year.setText(enrollment_year);
        tv_my_major.setText(major);
        tv_my_school_number_name.setText("学号");
    }


    @OnClick({R.id.btn_logout, R.id.rl_changed_psw, R.id.rl_bind_phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:
                // 弹出 对话框 是否进行 注销
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                AlertDialog dialog = builder.setTitle("注销")
                        .setMessage("是否确定注销")
                        .setIcon(R.mipmap.logout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharePreferenceUtils.clear(mCtx);
                                startActivity(new Intent(mCtx, LoginActivity.class));
                                dialog.dismiss();
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("取消", null).create();
                dialog.show();
                break;
            case R.id.rl_changed_psw:
                Intent intent = new Intent(mCtx, ChangePasswordActivity.class);
                startActivity(intent);

                break;
            case R.id.rl_bind_phone:
                // 根据是否已经绑定了手机进行 跳转
                addOrChangePhone();
                break;
            default:
                break;
        }
    }

    /**
     * 添加手机号码 或者跟换手机 号码
     */
    private void addOrChangePhone() {
        String phone = (String) SharePreferenceUtils.get(mCtx, Constants.PHONE, "");
        if (SMSUtil.judgePhoneNums(phone)) {
            // 修改手机 先弹出Dialog进行验证
            showSureDialog();
        } else {
            // 添加手机号码
            Intent intent = new Intent(mCtx, BindPhoneActivity.class);
            startActivity(intent);
        }
    }

    private Dialog dialog;

    /**
     * 确认是否进行修改手机号码
     */
    private void showSureDialog() {
        dialog = new Dialog(mCtx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_is_change_phone);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        dialog.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, ChangePhoneActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
