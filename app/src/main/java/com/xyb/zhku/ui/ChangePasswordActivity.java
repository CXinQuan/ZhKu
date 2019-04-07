package com.xyb.zhku.ui;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.bean.User;
import com.xyb.zhku.utils.MD5Util;
import com.xyb.zhku.utils.SharePreferenceUtils;
import com.xyb.zhku.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.et_old_psw)
    EditText etOldPsw;
    @BindView(R.id.et_new_psw)
    EditText etNewPsw;
    @BindView(R.id.et_sure_psw)
    EditText etSurePsw;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private boolean isSubmiting_toBMob = false;


    public int setContentViewLayout() {
        return R.layout.activity_change_password;
    }

    @OnClick({R.id.btn_submit, R.id.head_back})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_submit:
                submitChange();
                break;
            case R.id.head_back:
                finish();
                break;
        }


    }

    /**
     * 提交修改密码
     */
    private void submitChange() {
        if (UIUtils.isEmtpy(etOldPsw) || UIUtils.isEmtpy(etNewPsw) || UIUtils.isEmtpy(etSurePsw)) {
            showToast("请填写完整信息");
            return;
        }
        if (!etNewPsw.getText().toString().equals(etSurePsw.getText().toString())) {
            showToast("两次输入密码不一致，请重新输入");
            return;
        }
        if (isSubmiting_toBMob) {    //防止多次注册
            showToast("正在提交验证");
            return;
        }
        isSubmiting_toBMob = true;
        User user = SharePreferenceUtils.getUser(mCtx);
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("school_number", user.getSchool_number())
                .addWhereEqualTo("password", MD5Util.encrypt(etOldPsw.getText().toString()))
                .setLimit(1)
                .findObjects(new FindListener<User>() {
                    public void done(final List<User> object, BmobException e) {
                        if (e == null) {
                            if (object.size() == 1) {  //说明旧密码正确
                                final User user = object.get(0);
                                user.setPassword(MD5Util.encrypt(etNewPsw.getText().toString().trim()));
                                user.update(user.getObjectId(), new UpdateListener() {
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            showToast("修改密码成功！");
                                            // 跳转页面 并保存用户信息
                                            SharePreferenceUtils.saveUser(mCtx, user);
                                            btnSubmit.setText("提交");
                                            isSubmiting_toBMob = false;
                                            // 判断是老师的主页 还是 学生的主页
                                            //jumpToAnotherActivity(MainActivity.class);
                                            finish();
                                        } else {
                                            showToast("服务器繁忙，修改失败!");
                                            Log("创建数据失败：", e.getMessage());
                                            btnSubmit.setText("提交");
                                            isSubmiting_toBMob = false;
                                        }
                                    }
                                });
                            } else {
                                showToast("密码错误");
                                btnSubmit.setText("提交");
                                isSubmiting_toBMob = false;
                            }
                        } else {
                            showToast("服务器繁忙！");
                            btnSubmit.setText("提交");
                            isSubmiting_toBMob = false;
                        }
                    }
                });
    }

}
