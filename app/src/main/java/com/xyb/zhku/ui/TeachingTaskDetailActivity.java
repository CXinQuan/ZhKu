package com.xyb.zhku.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.bean.TeachingTask;
import com.xyb.zhku.manager.TeachingTaskUpdateManager;
import com.xyb.zhku.utils.UIUtils;
import com.xyb.zhku.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


public class TeachingTaskDetailActivity extends BaseActivity {
    @BindView(R.id.iv_head_back)
    ImageView ivHeadBack;
    @BindView(R.id.tv_head_content)
    TextView tvHeadContent;

    @BindView(R.id.act_subject)
    AutoCompleteTextView actSubject;
    @BindView(R.id.act_major)
    AutoCompleteTextView actMajor;
    @BindView(R.id.act_enrollment_year)
    AutoCompleteTextView actEnrollmentYear;
    @BindView(R.id.cb_all_first)
    CheckBox cbAllFirst;
    @BindView(R.id.cb_one)
    CheckBox cbOne;
    @BindView(R.id.cb_two)
    CheckBox cbTwo;
    @BindView(R.id.cb_three)
    CheckBox cbThree;
    @BindView(R.id.cb_four)
    CheckBox cbFour;
    @BindView(R.id.cb_all_second)
    CheckBox cbAllSecond;
    @BindView(R.id.cb_five)
    CheckBox cbFive;
    @BindView(R.id.cb_six)
    CheckBox cbSix;
    @BindView(R.id.cb_seven)
    CheckBox cbSeven;
    @BindView(R.id.cb_eight)
    CheckBox cbEight;
    @BindView(R.id.cb_all_third)
    CheckBox cbAllThird;
    @BindView(R.id.cb_nine)
    CheckBox cbNine;
    @BindView(R.id.cb_ten)
    CheckBox cbTen;
    @BindView(R.id.cb_eleven)
    CheckBox cb_eleven;
    @BindView(R.id.cb_twelve)
    CheckBox cb_twelve;
    @BindView(R.id.btn_submit_changed)
    Button btn_submit_changed;


    List<CheckBox> checkBoxList_All;
    List<CheckBox> checkBoxList_First;
    List<CheckBox> checkBoxList_Second;
    List<CheckBox> checkBoxList_Third;

    private TeachingTask.SubjectClass subjectClass;
    private TeachingTask task;
    private String[] yearStr;
    private String[] allMajor;
    //private TeachingTask.SubjectClass subjectClass1;
    boolean isSubmiting = false;
    private int position;


    // TODO: 2018/10/22   展示每一个 教学任务的 详情

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent getIntent = getIntent();
        subjectClass = (TeachingTask.SubjectClass) getIntent.getSerializableExtra("subjectClass");
        task = (TeachingTask) getIntent.getSerializableExtra("TeachingTask");
        position = (int) getIntent.getSerializableExtra("position");

        // TODO: 2018/10/22   初始化 View
        initCheckBox();
        tvHeadContent.setText("教学任务详情");
        actSubject.setText(subjectClass.getSubject());
        actMajor.setText(subjectClass.getMajor());
        actEnrollmentYear.setText(subjectClass.getYear());
        // List<String> classList = subjectClass.getClassList();
        List<Integer> classList = subjectClass.getClassList();
        // 班级格式：i + 1;
        for (Integer classInt : classList) {
            checkBoxList_All.get(classInt - 1).setChecked(true);
        }
        for (CheckBox checkBox : checkBoxList_All) {
            observeCheckBox(checkBox);
        }
        observeAutoCompleteTextView(actMajor);
        observeAutoCompleteTextView(actEnrollmentYear);
    }

    @OnClick({R.id.iv_head_back, R.id.btn_submit_changed})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_head_back:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
            case R.id.btn_submit_changed:

                // TODO: 2018/10/23    防止二次提交
                if (isSubmiting) {
                    showToast("正在提交，请稍后");
                    return;
                }
                // TODO: 2018/10/22     （Bmob更新）
//                if( task.getList().contains(subjectClass)){
//
//                }
                //  task.getList().remove(position);  // 先将原本的对象移除
                yearStr = Utils.getYearStr();
                allMajor = Utils.getAllMajor(mCtx);
                if (UIUtils.isEmtpy(actMajor) || !Arrays.asList(allMajor).contains(actMajor.getText().toString())) {
                    showToast("专业输入有误");
                    return;
                }
                if (UIUtils.isEmtpy(actEnrollmentYear) || !Arrays.asList(yearStr).contains(actEnrollmentYear.getText().toString())) {
                    showToast("年级输入有误");
                    return;
                }
                if (UIUtils.isEmtpy(actSubject)) {
                    showToast("课程名不能为空");
                    return;
                }
                boolean hasSelect = false;
                for (int i = 0; i < checkBoxList_All.size(); i++) {
                    if (checkBoxList_All.get(i).isChecked()) {
                        hasSelect = true;
                        break;
                    }
                }
                if (!hasSelect) {
                    showToast("请选择班级");
                    return;
                }
                isSubmiting = true;
                // 一条 课程对应班级  的 对象
              //  subjectClass1 = new TeachingTask.SubjectClass();
                String major = actMajor.getText().toString().trim();
                String subject = actSubject.getText().toString().trim();
                String year = actEnrollmentYear.getText().toString().trim();
//                subjectClass1.setMajor(major);
//                subjectClass1.setSubject(subject);
//                subjectClass1.setYear(year);
                //  List<String> classList = new ArrayList<>();
                List<Integer> classList = new ArrayList<>();
                for (int i = 0; i < checkBoxList_All.size(); i++) {
                    if (checkBoxList_All.get(i).isChecked()) {
                        //        classList.add(major + year.substring(2) + (i + 1));
                        classList.add(i + 1);
                    }
                }
               // subjectClass1.setClassList(classList);
                //   subjectClass.setSubject("");  课程没有改变
//                subjectClass.setMajor(actMajor.getText().toString());
//                subjectClass.setYear(actEnrollmentYear.getText().toString());
//                subjectClass.setClassList();
                //  task.getList().add(subjectClass1); // 添加上一个新的
//                TeachingTask.SubjectClass sourceSubjectClass = task.getList().get(position).;
//                sourceSubjectClass=subjectClass1;
                task.getList().get(position).setClassList(classList);
                task.getList().get(position).setSubject(subject);
                task.getList().get(position).setMajor(major);
                task.getList().get(position).setYear(year);
                task.update(task.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            TeachingTaskUpdateManager.getInstance().notifyChange(task);
                            btn_submit_changed.setVisibility(View.GONE);
                            showToast("保存成功");
                           // TeachingTaskDetailActivity.this.subjectClass = subjectClass1;  //  如果 修改后 提交了，仍然想再修改，再提交
                        } else {
                            showToast("服务器离家出走了");
                        }
                        isSubmiting = false;
                    }
                });
                break;
        }
    }

    private void initCheckBox() {
        checkBoxList_All = new ArrayList<>();
        checkBoxList_First = new ArrayList<>();
        checkBoxList_Second = new ArrayList<>();
        checkBoxList_Third = new ArrayList<>();
        checkBoxList_First.add(cbOne);
        checkBoxList_First.add(cbTwo);
        checkBoxList_First.add(cbThree);
        checkBoxList_First.add(cbFour);
        checkBoxList_Second.add(cbFive);
        checkBoxList_Second.add(cbSix);
        checkBoxList_Second.add(cbSeven);
        checkBoxList_Second.add(cbEight);
        checkBoxList_Third.add(cbNine);
        checkBoxList_Third.add(cbTen);
        checkBoxList_Third.add(cb_eleven);
        checkBoxList_Third.add(cb_twelve);
        checkBoxList_All.addAll(checkBoxList_First);
        checkBoxList_All.addAll(checkBoxList_Second);
        checkBoxList_All.addAll(checkBoxList_Third);
    }

    /**
     * 监听 AutoCompleteTextView
     */
    private void observeAutoCompleteTextView(AutoCompleteTextView acTextView) { // ,String initText
        acTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btn_submit_changed.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 监听 CheckBox
     */
    private void observeCheckBox(CheckBox checkBox) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btn_submit_changed.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int setContentViewLayout() {
        return R.layout.activity_teaching_task_detail;
    }

}
