package com.xyb.zhku.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.bean.Notify;
import com.xyb.zhku.bean.StuNotify;
import com.xyb.zhku.bean.TeacherNotify;
import com.xyb.zhku.bean.User;
import com.xyb.zhku.global.Constants;
import com.xyb.zhku.utils.SharePreferenceUtils;
import com.xyb.zhku.utils.TimeUtils;
import com.xyb.zhku.utils.UIUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SearchNotifyActivity extends BaseActivity {
    @BindView(R.id.et_condition)
    EditText et_condition;
    @BindView(R.id.tv_search)
    TextView tv_search;
    @BindView(R.id.tv_not_data)
    TextView tv_not_data;
    @BindView(R.id.tv_skip)
    TextView tv_skip;
    @BindView(R.id.tv_limit)
    TextView tv_limit;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.sp_skip)
    Spinner sp_skip;
    @BindView(R.id.sp_limit)
    Spinner sp_limit;
    @BindView(R.id.sp_time)
    Spinner sp_time;
    @BindView(R.id.tv_proper_order)
    TextView tv_proper_order;
    @BindView(R.id.tv_reverse_order)
    TextView tv_reverse_order;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.iv_cancle)
    ImageView iv_cancle;
    @BindView(R.id.iv_head_back)
    ImageView iv_head_back;

    private String[] limits;
    private String[] skips;
    private String[] times;

    int limitBmob;
    int skipBmob;
    int dayBmob;

    List<Notify> notifies;
    private int identity;
    NotifyAdapter adapter;

    boolean isProperOrder = true;  //  默认是 正序 排序

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notifies = new ArrayList<>();
        adapter = new NotifyAdapter();
        //   Intent intent = getIntent();
        identity = (int) SharePreferenceUtils.get(mCtx, Constants.IDENTITY, -1);
        if (identity == -1) {
            Toast.makeText(this, "账号异常，建议退出重新登录", Toast.LENGTH_SHORT).show();
        }
        limits = getResources().getStringArray(R.array.limit);
        skips = getResources().getStringArray(R.array.skip);
        times = getResources().getStringArray(R.array.time);
        initView();
    }

    @Override
    public int setContentViewLayout() {
        return R.layout.activity_search_notify;
    }

    private void initView() {

        rv.setAdapter(adapter);
        iv_cancle.setVisibility(View.GONE);// 默认是不出现的
        et_condition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //当文本有变化后,如果文本内容不是空的,可以显示x号让,用户可以点击删除
                String phone = et_condition.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    //显示x,提示用户可以删除
                    iv_cancle.setVisibility(View.VISIBLE);
                } else {
                    iv_cancle.setVisibility(View.GONE);
                }
            }
        });

        UIUtils.bindSpinnerAdapter(sp_limit, limits);
        UIUtils.bindSpinnerAdapter(sp_skip, skips);
        UIUtils.bindSpinnerAdapter(sp_time, times);
        // 10 、30、50、100
        sp_limit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        ((TextView) view).setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                        limitBmob = Integer.parseInt(limits[position]);
                        sp_skip.setSelection(0, true);
                        sp_time.setSelection(0, true);
                        //  sp_limit.setSelection(position);
                        tv_skip.setTextColor(Color.parseColor("#000000"));
                        tv_time.setTextColor(Color.parseColor("#000000"));
                        tv_limit.setTextColor(Color.parseColor("#2244ff"));
                        skipBmob = 0;
                        dayBmob = 0;
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_skip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        ((TextView) view).setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                        skipBmob = Integer.parseInt(skips[position]);
                        sp_limit.setSelection(0, true);
                        sp_time.setSelection(0, true);
                        //sp_skip.setSelection(position);
                        tv_limit.setTextColor(Color.parseColor("#000000"));
                        tv_time.setTextColor(Color.parseColor("#000000"));
                        tv_skip.setTextColor(Color.parseColor("#2244ff"));
                        limitBmob = 0;
                        dayBmob = 0;
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        break;
                    case 1:
                        dayBmob = 7;
                        responseSp_Time();
                        break;
                    case 2:
                        dayBmob = 30;
                        responseSp_Time();
                        break;
                    case 3:
                        dayBmob = 90;
                        responseSp_Time();
                        break;
                    case 4:
                        dayBmob = 180;
                        responseSp_Time();
                        break;
                }
                ((TextView) view).setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /**
     * 点击Spinner后的响应
     */
    private void responseSp_Time() {
        sp_skip.setSelection(0, true);
        sp_limit.setSelection(0, true);
        // sp_time.setSelection(position);
        tv_skip.setTextColor(Color.parseColor("#000000"));
        tv_limit.setTextColor(Color.parseColor("#000000"));
        tv_time.setTextColor(Color.parseColor("#2244ff"));
        limitBmob = 0;
        skipBmob = 0;
    }


    @OnClick({R.id.tv_search, R.id.tv_proper_order, R.id.tv_reverse_order, R.id.iv_cancle, R.id.iv_head_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_head_back:
                //  finish();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 执行完动画才进行  销毁
                    finishAfterTransition();
                } else {
                    finish();
                }
                break;
            case R.id.tv_proper_order:
                tv_proper_order.setTextColor(Color.parseColor("#2244ff"));
                tv_reverse_order.setTextColor(Color.parseColor("#000000"));
                isProperOrder = true; //正序
                if (notifies.size() > 0) {  // 因为 有可能 notifies 是找不到结果的，此时就不必要 反向更新 了，只要记录一下就行了
                    // TODO: 2018/10/5     RecycleView通知更新
                    Collections.reverse(notifies);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.tv_reverse_order:
                tv_proper_order.setTextColor(Color.parseColor("#000000"));
                tv_reverse_order.setTextColor(Color.parseColor("#2244ff"));
                isProperOrder = false; //倒序
                if (notifies.size() > 0) {
                    // TODO: 2018/10/5     RecycleView通知更新
                    Collections.reverse(notifies);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.tv_search:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                if (identity != User.STUDENT && identity != User.TEACHER) {
                    Toast.makeText(this, "账号异常，建议退出重新登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (UIUtils.isEmtpy(et_condition)) {
                    showToast("搜素关键字不能为空");
                    return;
                }
                notifies.clear();
                // adapter.notifyDataSetChanged();
                rv.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                tv_not_data.setVisibility(View.GONE);
                if (identity == User.TEACHER) {
                    getTeacherNotifyFromBmob(limitBmob, skipBmob, dayBmob, et_condition.getText().toString().trim());
                } else if (identity == User.STUDENT) {
                    getStuNotifyFromBmob(limitBmob, skipBmob, dayBmob, et_condition.getText().toString().trim());
                }
                //test();
                break;
            case R.id.iv_cancle:
                et_condition.setText("");
                iv_cancle.setVisibility(View.GONE);
                break;
        }
    }

    //region Description
/*    private void test() {
        BmobQuery<TeacherNotify> q1 = new BmobQuery<TeacherNotify>();
        String start = "2015-05-01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        q1.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(date));
        q1.findObjects(new FindListener<TeacherNotify>() {
            public void done(List<TeacherNotify> object, BmobException e) {

                Log("返回来的数据长度：", object.size() + "");

            }
        });

    }*/
    //endregion

    private void getTeacherNotifyFromBmob(int limit, int skip, int day, final String str) {

        BmobQuery<TeacherNotify> query = new BmobQuery<TeacherNotify>();
        if (limit <= 0) {
            query.setLimit(200);
        } else {
            query.setLimit(limit);
        }
        if (day <= 0) {   // 如果没有查询条件，则默认是查询一年内的所有通知
            query.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(getDateString(180)));
        } else {
            query.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(getDateString(day)));
        }

        query.setSkip(skip);
        query.findObjects(new FindListener<TeacherNotify>() {
            public void done(List<TeacherNotify> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        Log("返回来的数据长度：", object.size() + "条");
                        for (TeacherNotify notify : object) {
                            if (notify.getTitle().contains(str)) {
                                Log(notify.getTitle(), "#####");
                                notifies.add(notify);
                            }
                        }
                        if (notifies.size() > 0) {
                            pb.setVisibility(View.GONE);
                            rv.setVisibility(View.VISIBLE);
                            tv_not_data.setVisibility(View.GONE);
                            // TODO: 2018/10/5     适配器通知更新
                            adapter.notifyDataSetChanged();
                        } else {
                            pb.setVisibility(View.GONE);
                            tv_not_data.setVisibility(View.VISIBLE);
                            rv.setVisibility(View.GONE);
                        }
                    } else {
                        pb.setVisibility(View.GONE);
                        tv_not_data.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                    }

                } else {
                    showToast("服务器繁忙");
                    pb.setVisibility(View.GONE);
                    tv_not_data.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getStuNotifyFromBmob(int limit, int skip, int day, final String str) {

        BmobQuery<StuNotify> query = new BmobQuery<StuNotify>();
        if (limit <= 0) {
            query.setLimit(200);//最多也是返回200条
        } else {
            query.setLimit(limit);
        }
        if (day <= 0) {   // 如果没有查询条件，则默认是查询  半年内的所有通知
            query.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(getDateString(180)));
        } else {
            query.addWhereGreaterThanOrEqualTo("createdAt", new BmobDate(getDateString(day)));
        }
        query.setSkip(skip);
        query.findObjects(new FindListener<StuNotify>() {
            public void done(List<StuNotify> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        Log("返回来的数据长度：", object.size() + "条");
                        for (StuNotify notify : object) {
                            if (notify.getTitle().contains(str)) {
                                Log(notify.getTitle(), "#####");
                                notifies.add(notify);
                            }
                        }
                        if (notifies.size() > 0) {
                            pb.setVisibility(View.GONE);
                            rv.setVisibility(View.VISIBLE);
                            tv_not_data.setVisibility(View.GONE);
                            if (!isProperOrder) {// 如果是倒序 ，那么就进行倒一下 ，但是无论倒序还是正序，都需要 adapter.notifyDataSetChanged();
                                Collections.reverse(notifies);
                            }
                            // TODO: 2018/10/5     适配器通知更新
                            adapter.notifyDataSetChanged();
                        } else {
                            pb.setVisibility(View.GONE);
                            tv_not_data.setVisibility(View.VISIBLE);//找不到结果
                            rv.setVisibility(View.GONE);
                        }
                    } else {
                        pb.setVisibility(View.GONE);
                        tv_not_data.setVisibility(View.VISIBLE);//找不到结果
                        rv.setVisibility(View.GONE);
                    }
                } else {
                    showToast("服务器繁忙");
                    pb.setVisibility(View.GONE);
                    tv_not_data.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                }
            }
        });
    }

    private Date getDateString(long day) {  // 此处必须是一个long 值，如果是 int的话， 24 * 60 * 60 * 1000 * day乘出来的结果溢出，会产生计算异常
        long time = System.currentTimeMillis() - (long) (24 * 60 * 60 * 1000 * day);
        Log(System.currentTimeMillis() + "系统时间是", System.currentTimeMillis() + "");
        String s = TimeUtils.geDateTime(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
        //  return new Date(TimeUtils.geDateTime(time));
    }

    class NotifyAdapter extends RecyclerView.Adapter<NotifyViewHolder> {
        @Override
        public NotifyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_other_item, parent, false);//R.layout.notify_teaching_item
            return new NotifyViewHolder(view);
        }

        public void onBindViewHolder(NotifyViewHolder holder, int position) {
            Notify notify = notifies.get(position);
            holder.setPosition(position);
            holder.tv_notify_content.setText(notify.getContent() == null ? "" : notify.getContent());
            holder.tv_notify_time.setText(notify.getCreatedAt());
            holder.tv_notify_title.setText(notify.getTitle());
        }

        @Override
        public int getItemCount() {
            return notifies.size();
        }
    }

    class NotifyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_notify_time)
        TextView tv_notify_time;
        @BindView(R.id.tv_notify_title)
        TextView tv_notify_title;
        @BindView(R.id.tv_notify_content)
        TextView tv_notify_content;
        int position;

        public void setPosition(int position) {
            this.position = position;
        }

        public NotifyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, NotifyDetailActivity.class);
                    intent.putExtra("Notify", notifies.get(position));
                    startActivity(intent);
                }
            });
        }
    }


}
