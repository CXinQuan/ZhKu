package com.xyb.zhku.fragment.teacher;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scwang.smartrefresh.header.TaurusHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.bean.StudentHomeWork;
import com.xyb.zhku.bean.TeacherHomeWork;
import com.xyb.zhku.email.SendMailUtil;
import com.xyb.zhku.global.Constants;
import com.xyb.zhku.ui.StuHomeworkFileActivity;
import com.xyb.zhku.utils.SharePreferenceUtils;
import com.xyb.zhku.utils.Utils;
import com.xyb.zhku.utils.ZipUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TeacherHomeWorkDetailCompletionFagment extends BaseFragment {
    @BindView(R.id.tv_submit_count)
    TextView tv_submit_count;
    @BindView(R.id.tv_max_number)
    TextView tv_max_number;
    @BindView(R.id.tv_has_correct)
    TextView tv_has_correct;
    @BindView(R.id.tv_not_correct)
    TextView tv_not_correct;
    @BindView(R.id.ll_school_number)
    LinearLayout ll_school_number;
    @BindView(R.id.ll_name)
    LinearLayout ll_name;
    @BindView(R.id.ll_state)
    LinearLayout ll_state;
    @BindView(R.id.ll_grade)
    LinearLayout ll_grade;
    @BindView(R.id.tv_school_number)
    TextView tv_school_number;
    @BindView(R.id.tv_state)
    TextView tv_state;
    @BindView(R.id.tv_grade)
    TextView tv_grade;
    @BindView(R.id.iv_school_number)
    ImageView iv_school_number;
    @BindView(R.id.iv_state)
    ImageView iv_state;
    @BindView(R.id.iv_grade)
    ImageView iv_grade;
    @BindView(R.id.rv_all_stu)
    RecyclerView rv_all_stu;
    @BindView(R.id.smartrefreshlayout)
    SmartRefreshLayout smartrefreshlayout;
    @BindView(R.id.tv_has_not_stu)
    TextView tv_has_not_stu;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    @BindView(R.id.tv_one_button_EmailReceive)
    TextView tv_one_button_EmailReceive;
    @BindView(R.id.progressbar_sendToEmail)
    ProgressBar progressbar_sendToEmail;


    @BindView(R.id.tv_one_button_download)
    TextView tv_one_button_download;

    int laststate = SCHOOL_NUMBER_UP;
    private static final int SCHOOL_NUMBER_UP = 0;
    private static final int SCHOOL_NUMBER_DOWN = 1;
    private static final int GRADE_UP = 2;
    private static final int GRADE_DOWN = 3;
    private static final int STATE_UP = 4;
    private static final int STATE_DOWN = 5;

    List<StudentHomeWork> lists = new ArrayList<>();
    TeacherHomeWork homeWork;
    TeacherHomeWorkDetailCompletionAdapter adapter;

    public TeacherHomeWorkDetailCompletionFagment(TeacherHomeWork homeWork) {
        this.homeWork = homeWork;
    }

    @Override
    protected int setView() {
        return R.layout.teacher_fragment_homework_detail_completion;
    }

    @Override
    protected void init(View view) {
        adapter = new TeacherHomeWorkDetailCompletionAdapter();
        rv_all_stu.setAdapter(adapter);
        initSmartRefreshLayout();
    }

    /**
     * 初始化下拉更新
     */
    private void initSmartRefreshLayout() {
        smartrefreshlayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(3000);//延迟3000毫秒后结束刷新
                refreshNotifyData();
            }
        });
        smartrefreshlayout.setEnableLoadMore(false);
        //smartrefreshlayout.setRefreshFooter(new ClassicsFooter(mCtx));
        smartrefreshlayout.setRefreshHeader(new TaurusHeader(mCtx));
        smartrefreshlayout.setPrimaryColorsId(R.color.colorPrimary);


    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        iv_school_number.setVisibility(View.VISIBLE);
        // TODO: 2018/9/26     按照objectId去查找
        if (homeWork.getObjectId() == null) {
            showToast("数据错误！");
            return;
        }
        // TOD 2018/9/26  拿到数据之后默认按 学号  升序 排序
        getStudentHomeWork(0);
    }

    /**
     * 初始化时加载数据
     *
     * @param skip
     */
    private void getStudentHomeWork(final int skip) {
        BmobQuery<StudentHomeWork> query = new BmobQuery<StudentHomeWork>();
        query.order("-updatedAt");  //  根据updatedAt 时间  从上到下  开始  返回  5条数据
        query.addWhereEqualTo("teacher_homework_id", homeWork.getObjectId());
        query.setSkip(skip);
        query.findObjects(new FindListener<StudentHomeWork>() {
            public void done(List<StudentHomeWork> object, BmobException e) {
                if (e == null) {
                    if (object.size() <= 0) {
                        // TODO: 2018/9/26     显示 暂时没有人提交
                        if (skip > 0) {
                            showToast("没有更多数据");
//                            rv_all_stu.setVisibility(View.VISIBLE);
//                            tv_has_not_stu.setVisibility(View.GONE);
                        } else {
                            // showToast("还没有学生提交作业");
                            rv_all_stu.setVisibility(View.GONE);
                            tv_has_not_stu.setVisibility(View.VISIBLE);
                        }
                    } else {
                        rv_all_stu.setVisibility(View.VISIBLE);
                        tv_has_not_stu.setVisibility(View.GONE);
                        lists.addAll(object);
                        updateStatistics(lists);
                        Collections.sort(lists, new Sch_numberSort());// 默认按学生学号排序
                        adapter.notifyDataSetChanged();// 更新学生作业显示
                    }
                } else {
                    showToast("服务器繁忙！");
                }
                //  closeSmartrefreshlayout();
            }
        });
    }

    /**
     * 下拉刷新 获取数据    可能批改作业的时候有学生上交作业
     */
    private void refreshNotifyData() {
        if (lists.size() <= 0) {   // 网络错误，之前没有获取到数据，现在有网络了，刷新，则还是加载初始化时的数据
            getStudentHomeWork(0);
        } else {
            BmobQuery<StudentHomeWork> query = new BmobQuery<StudentHomeWork>();
            query.order("-createdAt") //  createdAt 时间  从上到下  开始  返回  10条数据
                    .setLimit(10)
                    .addWhereGreaterThanOrEqualTo("createdAt", lists.get(0).getCreatedAt())  //比第一个item的时间还大的，那么就是要刷新出来的数据
                    .findObjects(new FindListener<StudentHomeWork>() {
                        public void done(List<StudentHomeWork> object, BmobException e) {
                            if (e == null) {
                                if (object.size() > 0) {
                                    lists.addAll(0, object);
                                    updateStatistics(lists);
                                    Collections.sort(lists, new Sch_numberSort());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    showToast("已是最新数据");
                                }
                            } else {
                                showToast("服务器繁忙！");  // 查询不到  e  也会为null ,是否有结果还是要看object 的size

                            }
                            smartrefreshlayout.finishRefresh(800);

                        }
                    });
        }

    }


    private void closeSmartrefreshlayout() {
        smartrefreshlayout.finishLoadMore();
        smartrefreshlayout.finishRefresh();
    }

    /**
     * 默认是  学号是升序
     * 状态是  先 未查看，在 已查看  默认降序
     * 成绩  默认是 降序
     *
     * @param view
     */
    @OnClick({R.id.ll_school_number, R.id.ll_state, R.id.ll_grade, R.id.tv_one_button_download, R.id.tv_one_button_EmailReceive})
    public void OnClick(View view) {
        iv_grade.setVisibility(View.INVISIBLE);
        iv_state.setVisibility(View.INVISIBLE);
        iv_school_number.setVisibility(View.INVISIBLE);
        tv_school_number.setTextColor(Color.parseColor("#000000"));
        tv_state.setTextColor(Color.parseColor("#000000"));
        tv_grade.setTextColor(Color.parseColor("#000000"));

        switch (view.getId()) {
            case R.id.ll_school_number:
                iv_school_number.setVisibility(View.VISIBLE);
                tv_school_number.setTextColor(Color.parseColor("#24b6e9"));
                if (laststate == SCHOOL_NUMBER_UP) {
                    iv_school_number.setImageResource(R.mipmap.down_s);
                    laststate = SCHOOL_NUMBER_DOWN;
                    Utils.showLog("学号降序操作");
                    // TOD 2018/9/26   学号降序操作
                    Collections.reverse(lists);
                    adapter.notifyDataSetChanged();

                }
//                else if (laststate == SCHOOL_NUMBER_DOWN) {
//                    iv_school_number.setImageResource(R.mipmap.up_s);
//                    laststate = SCHOOL_NUMBER_UP;
//                    Utils.showLog("学号升序操作");
//                }
                else {   //  最后一次不是  按 学号排序，那么就  默认 按  学号 升序 排序
                    iv_school_number.setImageResource(R.mipmap.up_s);
                    laststate = SCHOOL_NUMBER_UP;
                    Utils.showLog("学号升序操作");
                    //  学号升序操作
                    Collections.sort(lists, new Sch_numberSort());
                    adapter.notifyDataSetChanged();
                }

                break;

            case R.id.ll_state:
                iv_state.setVisibility(View.VISIBLE);
                tv_state.setTextColor(Color.parseColor("#24b6e9"));
                if (laststate == STATE_UP) {
                    iv_state.setImageResource(R.mipmap.down_s);
                    Utils.showLog("状态降序操作");
                    laststate = STATE_DOWN;
                    // TOD 2018/9/26   状态降序操作
                    Collections.reverse(lists);
                    adapter.notifyDataSetChanged();

                }
//                else if (laststate == STATE_DOWN) {
//                    iv_state.setImageResource(R.mipmap.up_s);
//                    Utils.showLog("状态升序操作");
//                    laststate = STATE_UP;
//                }
                else {   //  最后一次不是  按 状态排序，那么就  默认 按 状态 升序 排序
                    iv_state.setImageResource(R.mipmap.up_s);
                    laststate = STATE_UP;
                    Utils.showLog("状态升序操作");
                    // TOD 2018/9/26   状态升序操作
                    Collections.sort(lists, new StateSort());
                    adapter.notifyDataSetChanged();

                }

                break;
            case R.id.ll_grade:
                iv_grade.setVisibility(View.VISIBLE);
                tv_grade.setTextColor(Color.parseColor("#24b6e9"));
                if (laststate == GRADE_DOWN) {
                    iv_grade.setImageResource(R.mipmap.up_s);
                    Utils.showLog("成绩升序操作");
                    laststate = GRADE_UP;
                    //  成绩升序操作
                    Collections.reverse(lists);
                    adapter.notifyDataSetChanged();

                } else {  //如果最后一次不是按 成绩 排序 的，那么默认按  成绩降序操作
                    // 若最后一次是按成绩降序排序，那么也按 成绩升序操作
                    iv_grade.setImageResource(R.mipmap.down_s);
                    Utils.showLog("成绩降序操作");
                    laststate = GRADE_DOWN;
                    // TOD 2018/9/26   成绩降序操作
                    Collections.sort(lists, new GradeSort());
                    adapter.notifyDataSetChanged();
                }
                break;

            case R.id.tv_one_button_download:
//                if (homeWork.getStu_number_list().size() <= 0) {
//                    showToast("还没学生提交呢，无法下载");
//                    return;
//                }
//                progressbar.setVisibility(View.VISIBLE);
//                tv_one_button_download.setVisibility(View.GONE);
//                //   一键下载
//                for (int i = 0; i < lists.size(); i++) {
//                    downloadFile(lists.get(i), homeWork.getTitle(), i == lists.size() - 1);
//                }
                state = STATEDOWNLOADFILE;
                downloadOrEmailReceive();

                break;
            case R.id.tv_one_button_EmailReceive:
                state = STATEEMAILRECEVIE;
                downloadOrEmailReceive();
                break;
            default:
                break;
        }
    }

    /**
     * 状态 ：下载全部学生的作业文件 或者 是 转发邮箱接收
     */
    private static final int STATEDOWNLOADFILE = 0;
    private static final int STATEEMAILRECEVIE = 1;
    private int state = STATEDOWNLOADFILE;

    /**
     * 下载文件 或者 是 转发邮箱接收
     */
    private void downloadOrEmailReceive() {
        if (homeWork.getStu_number_list().size() <= 0) {
            showToast("还没学生提交呢，无法下载");
            return;
        }
        // TODO: 2018/10/27   一键下载
        if (state == STATEDOWNLOADFILE) {
            progressbar.setVisibility(View.VISIBLE);
            tv_one_button_download.setVisibility(View.GONE);
        } else if (state == STATEEMAILRECEVIE) {
            progressbar_sendToEmail.setVisibility(View.VISIBLE);
            tv_one_button_EmailReceive.setVisibility(View.GONE);
        }
        for (int i = 0; i < lists.size(); i++) {
            downloadFile(lists.get(i), homeWork.getTitle(), i == lists.size() - 1);
        }
    }


    /**
     * 使用okHttp 下载文件
     */
    public void downloadFile(final StudentHomeWork studentHomeWork, final String teacherHomeWorkTitle, final boolean isLastFile) {
        //   String url = "http://vfx.mtime.cn/Video/2016/07/24/mp4/160724055620533327_480.mp4";
        if (studentHomeWork == null) {
            showToast("下载失败");
            return;
        }
        final BmobFile bmobFile = studentHomeWork.getFile();

        Log.d("文件", bmobFile.getFilename() + bmobFile.getUrl());

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(bmobFile.getUrl()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  tv_download_file.setVisibility(View.VISIBLE); // 下载附件 不可见
                        //   pb_file_download.setVisibility(View.GONE);// 进度圆圈  可见
                        showToast("服务器繁忙");
                        if (state == STATEDOWNLOADFILE) {
                            progressbar.setVisibility(View.GONE);
                            tv_one_button_download.setVisibility(View.VISIBLE);
                        } else if (state == STATEEMAILRECEVIE) {
                            progressbar_sendToEmail.setVisibility(View.GONE);
                            tv_one_button_EmailReceive.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //拿到字节流
                InputStream is = response.body().byteStream();
                int len = 0;
                // 路径需要一个个创建
                File pathPackageName = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getActivity().getPackageName());
                if (!pathPackageName.exists()) {
                    pathPackageName.mkdir();
                }
                final File pathDownLoadFile = new File(pathPackageName + File.separator + "DownLoadFile");
                if (!pathDownLoadFile.exists()) {
                    pathDownLoadFile.mkdirs();
                }
                final File pathFile = new File(pathDownLoadFile + File.separator + teacherHomeWorkTitle);
                if (!pathFile.exists()) {
                    pathFile.mkdir();
                }
//                if (bmobFile.getFilename().endsWith("")) {
//
//                }
                String endName = getEndName(bmobFile.getFilename());

                // 201510214103_电子信息工程151_陈鑫权
                final File file = new File(pathFile, studentHomeWork.getStu_school_number() + "_" + studentHomeWork.getStu_major() + studentHomeWork.getEnrollment_year().substring(studentHomeWork.getEnrollment_year().toString().length() - 2) + studentHomeWork.getStu_class() + "_" + studentHomeWork.getStu_name() + endName); // bmobFile.getFilename()
                if (file.exists()) {
                    file.delete();
                }
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
                //关闭流
                fos.close();
                is.close();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                       // tv_download_file.setVisibility(View.VISIBLE); // 下载附件 不可见
//                      //  pb_file_download.setVisibility(View.GONE);// 进度圆圈  可见
//                       // showToast("下载完成，路径为：" + file.getAbsolutePath());
//                        // TODO: 2018/10/10    向服务器添加一个学号，表示该学生已经下载
//                        // TODO: 2018/10/10    不过此处需要判断，只有学生下载老师的作业文件才进行记录，否则 老师或者学生下载通知文件，则不做此操作
//
//
//                    }
//                })
                if (isLastFile) {
                    Log.d("下载完成", "开始压缩");
                    //  开始 压缩，并获取其 结束 监听
                    ZipUtils.zip(pathFile.getAbsolutePath(), new ZipUtils.FinishListener() {
                        @Override
                        public void onfinish(final File target) {  //target 是压缩后的文件
                            // TODO: 2018/10/27  压缩结束 ，删除该原文件夹   隐藏该ProgressBar
                            Utils.showLog("压缩完成");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // showToast("下载完成");
//                                    Snackbar.make(tv_one_button_download, "下载完成", Snackbar.LENGTH_LONG)
//                                            .setAction("打开", new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    //  File[] files = pathFile.listFiles();
//
//
////                                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
////                                                    //      File file = new File("文件夹");
////                                                    intent.setAction(android.content.Intent.ACTION_VIEW);
////                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                                                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////                                                        intent.setDataAndType(FileProvider.getUriForFile(getActivity().getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileProvider", target), "**/*//*"); // fileProvider
////                                                    } else {
////                                                        intent.setDataAndType(Uri.fromFile(pathFile.getParentFile()), "*/*");
////                                                    }
////                                                    intent.addCategory(Intent.CATEGORY_OPENABLE);//CATEGORY_DEFAULT
////                                                    getActivity().startActivity(intent);
//
//                                                    //OpenFileUtils.openFiles(mCtx,target.getAbsolutePath());
//
//
////获取父目录
//                                                     // File parentFlie = new File(file.getParent());
//                                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                                                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                                        intent.setDataAndType(FileProvider.getUriForFile(getActivity().getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileProvider", target), "**/*//*"); // fileProvider
//                                                    } else {
//                                                        intent.setDataAndType(Uri.fromFile(pathFile.getParentFile()), "*/*");
//                                                    }
//                                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                                                    startActivity(intent);
//
//                                                }
//                                            }).show();
                                    if (state == STATEDOWNLOADFILE) {
                                        progressbar.setVisibility(View.GONE);
                                        tv_one_button_download.setVisibility(View.VISIBLE);
                                    } else if (state == STATEEMAILRECEVIE) {
                                        progressbar_sendToEmail.setVisibility(View.GONE);
                                        tv_one_button_EmailReceive.setVisibility(View.VISIBLE);
                                        // TODO: 2018/12/9 将文件转发到邮箱，sharedPreference中动态获取教师的邮箱地址，在注册时需要添加邮箱的绑定，邮箱的正则判断
                                        if (target != null && target.isFile()) {
                                            String toAdd = (String) SharePreferenceUtils.get(mCtx, Constants.EMAIL, "");
                                            //  String toAdd = "1611205417@qq.com";
                                            if ("".equals(toAdd)) {
                                                showToast("邮箱异常，建议重新登录");
                                                return;
                                            }
                                            String subject = "易仲恺学生作业接收通知";
                                            SendMailUtil.send(target, toAdd, subject, target.getName());
                                        }
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    /***
     *   得到文件的后缀名  如： .doc  .pdf  .xml
     * @param fileName
     * @return
     */
    private String getEndName(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 处理某一项已经改变
     *
     * @param position
     * @param grade
     */
    public void handlePositionGradeChanged(final int position, final float grade) {
        // TOD 2018/9/26    向服务器提交成绩
        BmobQuery<StudentHomeWork> bmobQuery = new BmobQuery<StudentHomeWork>();
        bmobQuery.getObject(lists.get(position).getObjectId(), new QueryListener<StudentHomeWork>() {
            @Override
            public void done(StudentHomeWork object, BmobException e) {
                if (e == null) {
                    if (object != null) {
                        object.setStu_grade(grade);
                        object.setStu_state(StudentHomeWork.HAS_CHECK);
                        object.update(object.getObjectId(), new UpdateListener() {
                            public void done(BmobException e) {
                                if (e == null) {
                                    StudentHomeWork studentHomeWork = lists.get(position);
                                    if (grade != -1) {
                                        studentHomeWork.setStu_grade(grade);

                                        studentHomeWork.setStu_state(StudentHomeWork.HAS_CHECK);
                                    }
                                    adapter.notifyItemChanged(position);
                                    //  2018/9/26    更新已提交人数，最高分，已批改人数，未批改人数   将这个操作封装成一个方法
                                    updateStatistics(lists);

                                } else {
                                    showToast("服务器繁忙，更新数据失败！");
                                }
                            }
                        });
                    } else {
                        showToast("数据出错！");
                    }
                } else {
                    showToast("服务器繁忙，更新数据失败！");
                }
            }
        });
    }

    /**
     * 更新已提交人数，最高分，已批改人数，未批改人数
     */
    public void updateStatistics(List<StudentHomeWork> list) {
        int submit_count = 0;
        float max_number = 0;
        int has_correct = 0;
        int not_correct = 0;
        if (list != null && list.size() > 0) {
            for (StudentHomeWork homeWork : list) {
                if (homeWork.getStu_state() == StudentHomeWork.HAS_CHECK) {
                    has_correct++;
                } else {
                    not_correct++;
                }
                if (homeWork.getStu_grade() > max_number) {
                    max_number = homeWork.getStu_grade();
                }
            }
            submit_count = list.size();
        }
//        else {
//            submit_count = 0;
//            max_number = 0;
//            has_correct = 0;
//            not_correct = 0;
//        }
        tv_submit_count.setText(submit_count + "");
        tv_max_number.setText(max_number + "");
        tv_has_correct.setText(has_correct + "");
        tv_not_correct.setText(not_correct + "");
    }

    /**
     * 按学号排序
     */
    class Sch_numberSort implements Comparator<StudentHomeWork> {
        @Override
        public int compare(StudentHomeWork o1, StudentHomeWork o2) {
            return (int) (Long.parseLong(o1.getStu_school_number()) - Long.parseLong(o2.getStu_school_number()));
        }
    }

    /**
     * 按状态排序
     */
    class StateSort implements Comparator<StudentHomeWork> {

        public int compare(StudentHomeWork o1, StudentHomeWork o2) {
            return o1.getStu_state() - o2.getStu_state();
        }
    }

    /**
     * 按成绩排序
     */
    class GradeSort implements Comparator<StudentHomeWork> {
        public int compare(StudentHomeWork o1, StudentHomeWork o2) {
            return -(int) (o1.getStu_grade() - o2.getStu_grade());
        }
    }

    class TeacherHomeWorkDetailCompletionAdapter extends RecyclerView.Adapter<TeacherHomeWorkDetailCompletionViewHolder> {

        @Override
        public TeacherHomeWorkDetailCompletionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.teacher_homework_detail_item, parent, false);
            return new TeacherHomeWorkDetailCompletionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TeacherHomeWorkDetailCompletionViewHolder holder, final int position) {
            final StudentHomeWork stuHomeWork = lists.get(position);
            if (stuHomeWork != null) {
                holder.tv_school_number.setText(stuHomeWork.getStu_school_number());
                holder.tv_name.setText(stuHomeWork.getStu_name());
                holder.tv_state.setText(stuHomeWork.getStu_state() == 0 ? "未批改" : "已批改");
                holder.tv_state.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 加下划线
                holder.tv_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mCtx, StuHomeworkFileActivity.class);
                        //   intent.putExtra("fileUrl", stuHomeWork.getFile().getUrl());
                        intent.putExtra("stuHomeWork", stuHomeWork);
                        intent.putExtra("position", position);
                        Log(stuHomeWork.getFile().getUrl());
                        startActivityForResult(intent, 101);
                    }
                });
                holder.tv_grade.setText(stuHomeWork.getStu_grade() + "");
            }
        }

        @Override
        public int getItemCount() {
            return lists.size();
        }
    }

    class TeacherHomeWorkDetailCompletionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_school_number)
        TextView tv_school_number;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_state)
        TextView tv_state;
        @BindView(R.id.tv_grade)
        TextView tv_grade;

        public TeacherHomeWorkDetailCompletionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
