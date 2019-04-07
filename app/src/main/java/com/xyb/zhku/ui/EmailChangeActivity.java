package com.xyb.zhku.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.bean.User;
import com.xyb.zhku.global.Constants;
import com.xyb.zhku.utils.EmailUtil;
import com.xyb.zhku.utils.SharePreferenceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class EmailChangeActivity extends BaseActivity {
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.et_email_change)
    EditText etEmailChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 2018/12/10    从sharedPerference中获取objectId，更换邮箱， 将邮箱在保存在sharedPerference
        initEditText();
    }

    @OnClick({R.id.tv_change, R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_change:
                String email = etEmailChange.getText().toString().trim();
                if ("".equals(email)) {
                    showToast("请输入邮箱");
                    return;
                }
                if (!EmailUtil.isEmail(email)) {
                    showToast("请输入正确的邮箱");
                    return;
                }
                updateUserToBmob(email);
                break;
            default:
                break;

        }
    }


    private void updateUserToBmob(final String email) {

        String objectId = (String) SharePreferenceUtils.get(mCtx, Constants.OBJECTID, "");


        if ("".equals(objectId)) {
            showToast("账号异常，建议重新登录");
            return;
        }

        BmobQuery<User> query = new BmobQuery<User>();
        //查询条件
        query.addWhereEqualTo("objectId", objectId);
        query.setLimit(1);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(final List<User> object, BmobException e) {
                if (e == null) {
                    if (object.size() == 1) {
                        final User user = object.get(0);
                        user.setEmail(email);
                        user.update(user.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    //   SharePreferenceUtils.saveUser(mCtx, user);
                                    SharePreferenceUtils.put(mCtx, Constants.EMAIL, email);
                                    Intent intent = new Intent();
                                    intent.putExtra(Constants.EMAIL, email);
                                    setResult(RESULT_OK, intent);
                                    showToast("修改成功");
                                    finish();
                                } else {
                                    showToast("服务器繁忙，修改失败!");
                                }
                            }
                        });
                    } else {
                        showToast("该用户不存在！");
                    }
                } else {
                    showToast("服务器离家出走了！");
                }
            }
        });
    }


    private void initEditText() {
        etEmailChange.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvChange.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public int setContentViewLayout() {
        return R.layout.activity_email_change;
    }


}
