package com.xyb.zhku.fragment.teacher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.adapter.TeacherPartyBuildRecycleAdapter;
import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.global.Constants;
import com.xyb.zhku.ui.LoginActivity;
import com.xyb.zhku.utils.SMSUtil;
import com.xyb.zhku.utils.SharePreferenceUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class TeacherMyFragment extends BaseFragment {
    @BindView(R.id.tv_my_name)
    TextView tv_my_name;
    @BindView(R.id.tv_my_phone)
    TextView tv_my_phone;
    @BindView(R.id.tv_my_school_number)
    TextView tv_my_school_number;
    @BindView(R.id.tv_my_colleg)
    TextView tv_my_colleg;
    @BindView(R.id.btn_logout)
    Button btn_logout;

    @Override
    protected int setView() {
        return R.layout.teacher_fragment_my;
    }

    @Override
    protected void init(View view) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

        String name = (String) SharePreferenceUtils.get(mCtx, Constants.NAME, "姓名异常，建议重新登录");
        String phone = (String) SharePreferenceUtils.get(mCtx, Constants.PHONE, "");
        String college = (String) SharePreferenceUtils.get(mCtx, Constants.COLLEGE, "学院名异常，建议重新登录");
        String school_number = (String) SharePreferenceUtils.get(mCtx, Constants.SCHOOL_NUMBER, "账号异常，建议重新登录");
        String encryptionPhone = null;
        if (SMSUtil.judgePhoneNums(phone)) {
            encryptionPhone = SMSUtil.encryptionPhone(phone);
        }else {
            encryptionPhone="账号异常，建议重新登录";
        }
        tv_my_name.setText(name);
        tv_my_colleg.setText(college);
        tv_my_phone.setText(encryptionPhone);
        tv_my_school_number.setText(school_number);
    }

    @OnClick({R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:
                // TODO: 2018/10/7       弹出 对话框 是否进行 注销
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
        }
    }
}
