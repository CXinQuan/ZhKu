package com.xyb.zhku.fragment.student;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.fragment.teacher.TeacherMyFragment;
import com.xyb.zhku.global.Constants;
import com.xyb.zhku.utils.SharePreferenceUtils;

import butterknife.BindView;

/**
 * Created by lenovo on 2018/10/1.
 */

public class StuMyFragment extends TeacherMyFragment {
    @BindView(R.id.tv_my_major)
    TextView tv_my_major;
    @BindView(R.id.tv_my_class)
    TextView tv_my_class;
    @BindView(R.id.tv_my_enrollment_year)
    TextView tv_my_enrollment_year;

    protected int setView() {
        return R.layout.stu_fragment_my;
    }
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        String  major = (String) SharePreferenceUtils.get(mCtx, Constants.MAJOR, "专业异常，建议重新登录");
        String  my_class = (String) SharePreferenceUtils.get(mCtx, Constants.UCLASS, "班级异常，建议重新登录");
        String  enrollment_year = (String) SharePreferenceUtils.get(mCtx, Constants.ENROLLMENT_YEAR, "入学年份异常，建议重新登录");
        tv_my_class.setText(my_class);
        tv_my_enrollment_year.setText(enrollment_year);
        tv_my_major.setText(major);

    }

}
