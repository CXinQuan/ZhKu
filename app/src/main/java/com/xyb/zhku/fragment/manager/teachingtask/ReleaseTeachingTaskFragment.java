package com.xyb.zhku.fragment.manager.teachingtask;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.bean.TeachingTask;
import com.xyb.zhku.bean.User;
import com.xyb.zhku.ui.ManagerSearchTeachingTaskActivity;
import com.xyb.zhku.utils.UIUtils;
import com.xyb.zhku.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by lenovo on 2018/10/16.
 */

public class ReleaseTeachingTaskFragment extends BaseFragment {

    @BindView(R.id.cb_one)
    CheckBox cbOne;
    @BindView(R.id.cb_two)
    CheckBox cbTwo;
    @BindView(R.id.cb_three)
    CheckBox cbThree;
    @BindView(R.id.cb_four)
    CheckBox cbFour;
    @BindView(R.id.cb_five)
    CheckBox cbFive;
    @BindView(R.id.cb_six)
    CheckBox cbSix;
    @BindView(R.id.cb_seven)
    CheckBox cbSeven;
    @BindView(R.id.cb_eight)
    CheckBox cbEight;
    @BindView(R.id.cb_nine)
    CheckBox cbNine;
    @BindView(R.id.cb_ten)
    CheckBox cbTen;
    @BindView(R.id.cb_eleven)
    CheckBox cb_eleven;
    @BindView(R.id.cb_twelve)
    CheckBox cb_twelve;

    @BindView(R.id.cb_all_first)
    CheckBox cb_all_first;
    @BindView(R.id.cb_all_second)
    CheckBox cb_all_second;
    @BindView(R.id.cb_all_third)
    CheckBox cb_all_third;

    @BindView(R.id.tv_release)
    Button tvRelease;
    @BindView(R.id.linearLayout_search)
    LinearLayout linearLayoutSearch;

    @BindView(R.id.textinputlayout)
    TextInputLayout textinputlayout;
    @BindView(R.id.textinputedittext_teacherid)
    TextInputEditText textinputedittext_teacherid;
    @BindView(R.id.act_major)
    AutoCompleteTextView actMajor;
    //    @BindView(R.id.act_enrollment_year)
//    AutoCompleteTextView actEnrollmentYear;
    @BindView(R.id.act_subject)
    AutoCompleteTextView actSubject;
    @BindView(R.id.iv_cancle)
    ImageView iv_cancle;

    @BindView(R.id.Sp_enrollment_year)
    Spinner Sp_enrollment_year;

    List<CheckBox> checkBoxList_All;
    List<CheckBox> checkBoxList_First;
    List<CheckBox> checkBoxList_Second;
    List<CheckBox> checkBoxList_Third;
    boolean isTeacher = false;
    private String[] yearStr;
    private String[] allMajor;

    User teacher;
    private TeachingTask.SubjectClass sc;


    protected int setView() {
        return R.layout.manager_release_teachingtask_fragment;
    }

    @Override
    protected void init(View view) {
        // TODO: 2018/10/17   将所有的 CheckBox 添加到一个List 中，最后才能够进行遍历 isCheck
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

        initCheckBox(checkBoxList_First, cb_all_first);
        initCheckBox(checkBoxList_Second, cb_all_second);
        initCheckBox(checkBoxList_Third, cb_all_third);

        //<editor-fold desc=" 已经被initCheckBox 替代了">
 /*       // TODO: 2018/10/18    全选  取消按钮
        cb_all_first.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (CheckBox checkBox : checkBoxList_First) {
                        checkBox.setChecked(true);
                    }
                } else {
                    for (CheckBox checkBox : checkBoxList_First) {
                        checkBox.setChecked(false);
                    }
                }
            }
        });
        cb_all_second.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (CheckBox checkBox : checkBoxList_Second) {
                        checkBox.setChecked(true);
                    }
                } else {
                    for (CheckBox checkBox : checkBoxList_Second) {
                        checkBox.setChecked(false);
                    }
                }
            }
        });
        cb_all_third.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (CheckBox checkBox : checkBoxList_Third) {
                        checkBox.setChecked(true);
                    }
                } else {
                    for (CheckBox checkBox : checkBoxList_Third) {
                        checkBox.setChecked(false);
                    }
                }
            }
        });*/
        //</editor-fold>

        yearStr = Utils.getYearStr();
        allMajor = Utils.getAllMajor(mCtx);
        // 一条 课程对应班级  的 对象
        sc = new TeachingTask.SubjectClass();
        // UIUtils.bindArray(actEnrollmentYear, yearStr);

        // 测试 Spinner

        // TODO: 2018/10/26     改变初始化代码  年级已经改为 Spinner
        UIUtils.bindSpinnerAdapter(Sp_enrollment_year, yearStr);
        UIUtils.bindArray(actMajor, allMajor);

        Sp_enrollment_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sc.setYear(yearStr[position]);
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        textinputedittext_teacherid.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                //当文本有变化后,如果文本内容不是空的,可以显示x号让,用户可以点击删除
                String teacherid = textinputedittext_teacherid.getText().toString();
                if (!TextUtils.isEmpty(teacherid)) {
                    //显示x,提示用户可以删除
                    iv_cancle.setVisibility(View.VISIBLE);
                } else {
                    iv_cancle.setVisibility(View.GONE);
                }
            }
        });

        textinputedittext_teacherid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) { // 获取到焦点，
                    // textinputlayout.setError("");
                    if (!UIUtils.isEmtpy(textinputedittext_teacherid)) {
                        iv_cancle.setVisibility(View.VISIBLE);
                    } else {
                        iv_cancle.setVisibility(View.GONE);
                    }
                } else { // 焦点离开
                    if (!UIUtils.isEmtpy(textinputedittext_teacherid)) {
                        BmobQuery<User> query = new BmobQuery<User>();
                        query.addWhereEqualTo("school_number", textinputedittext_teacherid.getText().toString().trim());
                        query.addWhereEqualTo("identity", User.TEACHER);
                        query.setLimit(1);
                        query.findObjects(new FindListener<User>() {
                            public void done(List<User> object, BmobException e) {
                                String text;
                                if (e == null) {
                                    if (object.size() > 0) {
                                        teacher = object.get(0);
                                        text = teacher.getName();
                                        isTeacher = true;
                                    } else {
                                        text = "找不到此工号的老师";
                                        isTeacher = false;
                                    }
                                } else {
                                    text = "服务器离家出走了";
                                    isTeacher = false;
                                }
                                textinputlayout.setError(text);
                            }
                        });
                    }
                }

            }
        });


    }

    /**
     * 初始化 一排 CheckBox
     *
     * @param checkBoxList
     * @param checkBoxAll
     */
    private void initCheckBox(final List<CheckBox> checkBoxList, final CheckBox checkBoxAll) {
        boolean isChild;// 是否是子CheckBox产生的检查
       //子CheckBox响应全选的CheckBox时 有bug
       // 初始化 各个 子的 ChechBox
        for (CheckBox checkBox : checkBoxList) { //  checkBoxList_First
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    boolean isAllSelect = true; // 假设全部都已经被选中了
                    for (CheckBox checkBox : checkBoxList) { // checkBoxList_First
                        if (!checkBox.isChecked()) { // 只要有一个没被选中 ，就说明 不是全部被选中，如果遍历完之后还没有找到  没选中的那么就设置为true了
                            isAllSelect = false;
                            break;
                        }
                    }
                    checkBoxAll.setChecked(isAllSelect); //cb_all_first
                }
            });
        }

        // 初始化 全选的 CheckBox
        //<editor-fold desc="使用setOnCheckedChangeListener的初始化">
/*        checkBoxAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("测试","setOnCheckedChangeListener被执行");
//                if (isChecked) {
//                    for (CheckBox checkBox : checkBoxList) {
//                        checkBox.setChecked(true);
//                    }
//                } else {
//
//                    for (CheckBox checkBox : checkBoxList) {
//                        checkBox.setChecked(false);
//                    }
//                }
            }
        });*/
        //</editor-fold>

        /**
         *setOnCheckedChangeListener 会比  setOnClickListener 先被执行，所以应该将该状态设置为当前状态
         */
        checkBoxAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Log.d("测试","setOnClickListener被执行");
                if (checkBoxAll.isChecked()) {
                    checkBoxAll.setChecked(true);
                    for (CheckBox checkBox : checkBoxList) {
                        checkBox.setChecked(true);
                    }
                } else {
                    checkBoxAll.setChecked(false);
                    for (CheckBox checkBox : checkBoxList) {
                        checkBox.setChecked(false);
                    }
                }
            }
        });



    }

    boolean isReleasing = false;

    @OnClick({R.id.linearLayout_search, R.id.tv_release, R.id.iv_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearLayout_search:

                Intent intent = new Intent(mCtx, ManagerSearchTeachingTaskActivity.class);
                Bundle bundle = null;
                // TODO: 2018/10/18   开启搜索界面  共享元素开启
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "manger_linearLayout").toBundle();
                    startActivity(intent, bundle);
                } else {
                    startActivity(intent);
                }
                break;

            case R.id.iv_cancle:
                textinputedittext_teacherid.setText("");
                iv_cancle.setVisibility(View.GONE);
                textinputlayout.setError("");
                break;

            case R.id.tv_release:
                if (isReleasing) {
                    showToast("正在提交，请稍后");
                    return;
                }
                if (!isTeacher) {
                    showError(textinputlayout, "请输入正确的工号");
                    return;
                }
                // TODO: 2018/10/18   判空
                if (UIUtils.isEmtpy(actMajor) || !Arrays.asList(allMajor).contains(actMajor.getText().toString())) {
                    showToast("专业输入有误");
                    return;
                }
//                if (UIUtils.isEmtpy(actEnrollmentYear) || !Arrays.asList(yearStr).contains(actEnrollmentYear.getText().toString())) {
//                    showToast("年级输入有误");
//                    return;
//                }
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

//                // 一条 课程对应班级  的 对象
//                sc = new TeachingTask.SubjectClass();
                String major = actMajor.getText().toString().trim();
                String subject = actSubject.getText().toString().trim();
                //String year = actEnrollmentYear.getText().toString().trim();
                sc.setMajor(major);
                sc.setSubject(subject);

                //  List<String> classList = new ArrayList<>();
                List<Integer> classList = new ArrayList<>();
                for (int i = 0; i < checkBoxList_All.size(); i++) {
                    if (checkBoxList_All.get(i).isChecked()) {
                        //        classList.add(major + year.substring(2) + (i + 1));
                        classList.add(i + 1);
                    }
                }
                sc.setClassList(classList);

                isReleasing = true;
                tvRelease.setText("正在提交");

                // TODO: 2018/10/18   先根据该 Id 获取该老师的 教学任务表，如果获取不到，创建新的教学任务添加进去
                // 如果获取到了，那么就将该任务表添加到本次的任务表中
                BmobQuery<TeachingTask> query = new BmobQuery<TeachingTask>();
                query.addWhereEqualTo("teacherId", textinputedittext_teacherid.getText().toString().trim());
                query.setLimit(1);
                query.findObjects(new FindListener<TeachingTask>() {
                    public void done(List<TeachingTask> object, BmobException e) {
                        if (e == null) {
                            if (object.size() > 0) { // 该 教师已经有教学任务了，更新 添加 教学任务
                                TeachingTask teachingTask_before = object.get(0);
                                teachingTask_before.getList().add(sc);
                                // TODO: 2018/10/18    更新
                                teachingTask_before.update(teachingTask_before.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            showToast("添加成功");
                                        } else {
                                            showToast("服务器离家出走了");
                                        }
                                        isReleasing = false;
                                        tvRelease.setText("添加");
                                        restoreView();
                                    }
                                });

                            } else { // 该教师没有教学任务（新教师）  添加进教学任务
                                TeachingTask task = new TeachingTask();
                                List<TeachingTask.SubjectClass> subjectClassList = new ArrayList<>();
                                task.setTeacherId(textinputedittext_teacherid.getText().toString().trim());
                                task.setTeacherName(teacher.getName());
                                task.setList(subjectClassList);
                                subjectClassList.add(sc);
                                // TODO: 2018/10/18   保存
                                task.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            showToast("添加成功");
                                        } else {
                                            showToast("服务器离家出走了");
                                        }
                                        isReleasing = false;
                                        tvRelease.setText("添加");
                                        restoreView();
                                    }
                                });
                            }
                        } else {
                            showToast("服务器离家出走了");
                            isReleasing = false;
                            tvRelease.setText("添加");
                        }
                    }
                });
                break;
        }
    }

    private void restoreView() {
        //actEnrollmentYear.setText("");
        actMajor.setText("");
        actSubject.setText("");
        textinputedittext_teacherid.setText("");
        textinputlayout.getEditText().setFocusable(true);
        textinputlayout.getEditText().setFocusableInTouchMode(true);
        textinputlayout.getEditText().requestFocus();
        iv_cancle.setVisibility(View.INVISIBLE);
        for (int i = 0; i < checkBoxList_All.size(); i++) {
            checkBoxList_All.get(i).setChecked(false);
        }
        cb_all_first.setChecked(false);
        cb_all_second.setChecked(false);
        cb_all_third.setChecked(false);
    }

    /**
     * 显示错误提示，并获取焦点
     *
     * @param textInputLayout
     * @param error
     */
    private void showError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }

    protected void initData(Bundle savedInstanceState) {

    }


}
