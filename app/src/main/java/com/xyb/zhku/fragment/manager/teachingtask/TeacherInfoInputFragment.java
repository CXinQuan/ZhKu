package com.xyb.zhku.fragment.manager.teachingtask;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.base.MyBaseAdapter;
import com.xyb.zhku.bean.CollegeInfo;
import com.xyb.zhku.bean.User;
import com.xyb.zhku.utils.CollegeInfoUtils;
import com.xyb.zhku.utils.EmailUtil;
import com.xyb.zhku.utils.MD5Util;
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

/**
 * Created by 陈鑫权  on 2019/4/9.
 */

public class TeacherInfoInputFragment extends BaseFragment {
    @BindView(R.id.tv_reg_school_number)
    TextView tvRegSchoolNumber;
    @BindView(R.id.et_teach_school_number)
    EditText etTeachSchoolNumber;
    @BindView(R.id.et_teach_name)
    EditText etTeachName;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.tv_reg_email)
    TextView tvRegEmail;
    @BindView(R.id.et_teach_email)
    EditText etTeachEmail;
    @BindView(R.id.sp_teach_college)
    Spinner spTeachCollege;
    @BindView(R.id.btn_sure)
    Button btnSure;

    String teachCollege;

    @Override
    protected int setView() {
        return R.layout.manager_input_teacher_info_fragment;
    }

    @Override
    protected void init(View view) {
        ArrayList<CollegeInfo> allCollegeInfo = CollegeInfoUtils.getAllCollegeInfo();
        final List<String> allCollege = new ArrayList<>();
        for (CollegeInfo college : allCollegeInfo) {
            allCollege.add(college.getName());
        }
        teachCollege = allCollege.get(0);
        spTeachCollege.setAdapter(new MyBaseAdapter(allCollege));
        spTeachCollege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                teachCollege = allCollege.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }


    @OnClick({R.id.btn_sure})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                submit();
                break;

        }
    }

    /**
     * 提交，开始录入
     */
    private void submit() {
        if (UIUtils.isEmtpy(etTeachSchoolNumber) || !Utils.isTeacherNumber(etTeachSchoolNumber.getText().toString())) {
            showToast("教师工号格式不正确");
            return;
        }
        if (UIUtils.isEmtpy(etTeachName)) {
            showToast("教师姓名不能为空");
            return;
        }
        if (UIUtils.isEmtpy(etTeachEmail) || !EmailUtil.isEmail(etTeachEmail.getText().toString())) {
            showToast("教师邮箱格式不正确");
            return;
        }
        final User user = new User();
        user.setIdentity(User.TEACHER);
        user.setSchool_number(etTeachSchoolNumber.getText().toString());
        user.setCollege(teachCollege);
        user.setEmail(etTeachEmail.getText().toString());
        user.setName(etTeachName.getText().toString());
        user.setPassword(MD5Util.encrypt("1"));
        final Dialog dialog = new Dialog(mCtx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        dialog.findViewById(R.id.tv_progress).setVisibility(View.GONE);
        dialog.show();

        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.addWhereEqualTo("school_number", user.getSchool_number())
                .findObjects(new FindListener<User>() {
                    public void done(List<User> object, BmobException e) {
                        if (e == null) {
                            if (object.size() == 0) {   // 不存在的时候才添加进去
                                user.save(new SaveListener<String>() {
                                    public void done(String objectId, BmobException e) {
                                        if (e == null) {
                                            if (objectId != null && !"".equals(objectId)) {
                                                showToast("录入成功");
                                                dialog.dismiss();
                                                etTeachSchoolNumber.setText("");
                                                etTeachName.setText("");
                                                etTeachEmail.setText("");
                                                spTeachCollege.setSelection(0);
                                            }
                                        } else {
                                            showToast("服务器离家出走了");
                                        }
                                    }
                                });
                            } else {  // 如果已经存在了
                                showToast("该教师已经存在");
                                dialog.dismiss();
                            }
                        } else {
                            showToast("服务器离家出走了");
                        }
                    }
                });
    }


}
