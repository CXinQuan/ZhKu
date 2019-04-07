package com.xyb.zhku.fragment.manager;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.base.MyBaseAdapter;
import com.xyb.zhku.bean.CollegeInfo;
import com.xyb.zhku.bean.User;
import com.xyb.zhku.utils.CollegeInfoUtils;
import com.xyb.zhku.utils.MD5Util;
import com.xyb.zhku.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 陈鑫权  on 2019/4/2.
 */

public class StuInfoInputFragement extends BaseFragment {
    @BindView(R.id.sp_cllege)
    Spinner spCllege;
    @BindView(R.id.sp_major)
    Spinner spMajor;
    @BindView(R.id.sp_year)
    Spinner spYear;

    @BindView(R.id.sp_class)
    Spinner spClass;

    @BindView(R.id.et_number)
    EditText etNumber;

    @BindView(R.id.btn_sure)
    Button btnSure;
    private ArrayList<CollegeInfo> allCollegeInfo;


    private ArrayList<CollegeInfo.Major> majorsList;
    String collegeName;
    String major;
    private String year;
    int userClass = 1;
    CollegeInfo userCollegeInfo;
    CollegeInfo.Major userMajor;
    private TextView tv_progress;
    private Dialog dialog;
    int i = 0;
    int number;
    private User user;
    private static final int SUCCESS = 1;
    private static final int FAIL = 2;


    android.os.Handler handler = new android.os.Handler(new android.os.Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == SUCCESS) {
                i++;
                if (i > number) {
                    handler.removeCallbacksAndMessages(null);
                    i = 0;
                    number = 0;
                    etNumber.setText("");
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    return true;
                }
                handler.removeMessages(SUCCESS);
                user.setSchool_number(year + userMajor.getNumber() + userClass + String.format("%02d", i));
                user.setName("学生" + String.format("%02d", i));

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
                                                        showPrigressDialog(i, number);
                                                    }
                                                    handler.sendEmptyMessage(SUCCESS);
                                                } else {
                                                    showToast("服务器离家出走了");
                                                    handler.sendEmptyMessageDelayed(FAIL, 2000);
                                                }
                                            }
                                        });
                                    }else{  // 如果已经存在了，那么就直接录入下一个
                                        showPrigressDialog(i, number);
                                        handler.sendEmptyMessage(SUCCESS);
                                    }
                                } else {
                                    showToast("服务器离家出走了");
                                    handler.sendEmptyMessageDelayed(FAIL, 2000);
                                }
                            }
                        });

            } else if (msg.what == FAIL) {
                i = 0;
                number = 0;
                etNumber.setText("");
                if (dialog != null) {
                    dialog.dismiss();
                }
                handler.removeCallbacksAndMessages(null);
            }
            return true;
        }
    });


    @Override
    protected int setView() {
        return R.layout.fragment_stu_info_input;
    }

    @Override
    protected void init(View view) {
        allCollegeInfo = CollegeInfoUtils.getAllCollegeInfo();
        collegeName = allCollegeInfo.get(0).getName();
        major = allCollegeInfo.get(0).getMajors().get(0).getName();
        spCllege.setAdapter(new CollegeAdapter());
        spCllege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userCollegeInfo = allCollegeInfo.get(position);
                collegeName = userCollegeInfo.getName();
                majorsList = userCollegeInfo.getMajors();
                spMajor.setAdapter(new MajorAdapter(majorsList));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spMajor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userMajor = majorsList.get(position);
                major = userMajor.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final String[] yearStr = Utils.getYearStr();
        // UIUtils.bindSpinnerAdapter(spYear, yearStr);

        spYear.setAdapter(new MyBaseAdapter(yearStr));
        String allClass[] = new String[12];
        for (int i = 0; i < 12; i++) {
            allClass[i] = (i + 1) + " 班";
        }
        //  UIUtils.bindSpinnerAdapter(spClass, allClass);
        spClass.setAdapter(new MyBaseAdapter(allClass));
        year = yearStr[0];
        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = yearStr[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userClass = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberStr = etNumber.getText().toString().intern();
                try {
                    number = Integer.parseInt(numberStr);
                } catch (Exception e) {
                    showToast("请输入正确的正整数人数");
                    return;
                }
                // 显示ProgressDialog 循环遍历 添加 User
                user = new User();
                user.setIdentity(User.STUDENT);
                user.setEnrollment_year(year);
                user.setCollege(collegeName);
                user.setMajor(major);
                user.setClassNumber(userClass);
                user.setPassword(MD5Util.encrypt("1"));
                dialog = new Dialog(mCtx);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_progress);
                dialog.setCancelable(false);
                Window window = dialog.getWindow();
                window.getDecorView().setPadding(0, 0, 0, 0);
                dialog.show();
                tv_progress = dialog.findViewById(R.id.tv_progress);
                handler.sendEmptyMessage(SUCCESS);
            }
        });
    }

    private void showPrigressDialog(int number, int total) {
        tv_progress.setText(number + "/" + total);
        if (number == total) {
            dialog.dismiss();
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    class CollegeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (allCollegeInfo != null) {
                return allCollegeInfo.size();
            }
            return 0;
        }

        @Override
        public CollegeInfo getItem(int position) {
            return allCollegeInfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(mCtx);
            textView.setText(allCollegeInfo.get(position).getName());
            textView.setTextSize(18);
            return textView;
        }
    }

    class MajorAdapter extends BaseAdapter {
        ArrayList<CollegeInfo.Major> majors;

        public MajorAdapter(ArrayList<CollegeInfo.Major> majors) {
            this.majors = majors;
        }

        @Override
        public int getCount() {
            if (majors != null) {
                return majors.size();
            }
            return 0;
        }

        @Override
        public CollegeInfo.Major getItem(int position) {
            return majors.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(mCtx);
            textView.setText(majors.get(position).getName());
            textView.setTextSize(18);
            return textView;
        }
    }


}
