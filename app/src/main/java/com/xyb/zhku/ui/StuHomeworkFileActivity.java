package com.xyb.zhku.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.bean.StudentHomeWork;
import com.xyb.zhku.global.Constants;
import com.xyb.zhku.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *   老师对学生作业进行评分
 */
public class StuHomeworkFileActivity extends BaseActivity {
    @BindView(R.id.iv_head_back)
    ImageView iv_head_back;
    @BindView(R.id.tv_head_content)
    TextView tv_head_content;
    @BindView(R.id.et_grade)
    EditText et_grade;
    @BindView(R.id.tv_submit_grade)
    TextView tv_submit_grade;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.pb)
    ProgressBar pb;

    WebSettings settings;
    private int position;
    private StudentHomeWork stuHomeWork;
    private float grade;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        stuHomeWork = (StudentHomeWork) intent.getSerializableExtra("stuHomeWork");
        position = intent.getIntExtra("position", -1);
        if(stuHomeWork.getStu_grade()>0){
            et_grade.setText(stuHomeWork.getStu_grade()+"");
        }
        tv_head_content.setText("作业详情");
        if (stuHomeWork != null && stuHomeWork.getFile() != null && stuHomeWork.getFile().getUrl() != null) {
            String fileurl = Constants.OFFICEBASEURL + stuHomeWork.getFile().getUrl();
            initWebView(fileurl);
        }

    }

    @Override
    public int setContentViewLayout() {
        return R.layout.activity_stu_homework_file;
    }

    /**
     *  初始化 WebView
     * @param url
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initWebView(String url) {
            settings = webview.getSettings();
            //设置自适应屏幕，两者合用
            settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
            settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
            settings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
            settings.setBuiltInZoomControls(true);// 显示缩放按钮(wap网页不支持)
            settings.setJavaScriptEnabled(true);// 支持js功能
            settings.setDomStorageEnabled(true);
            settings.setAllowFileAccessFromFileURLs(true);
            // settings.setBlockNetworkImage(false); //设置网页在加载的时候暂时不加载图片
            webview.loadUrl(url);
            webview.setWebViewClient(new WebViewClient() {
                // 开始加载网页
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    pb.setVisibility(View.VISIBLE);
                }

                // 网页加载结束
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    pb.setVisibility(View.GONE);
                }

                // 所有链接跳转会走此方法
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //  System.out.println("跳转链接:" + url);
                    view.loadUrl(url);//此行代码必须添加上去，否则不会显示
                    return true;// 在跳转链接时强制在当前webview中加载
                }
            });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                System.out.println("进度:" + newProgress);
                if (newProgress >= 95) {
                    pb.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }

    @OnClick({R.id.iv_head_back, R.id.tv_submit_grade})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_head_back:
                finish();
                break;
            case R.id.tv_submit_grade:
                if (!UIUtils.isEmtpy(et_grade)) {
                    try {
                        grade = Float.parseFloat(et_grade.getText().toString().trim());
                    } catch (Exception e) {
                        showToast("请正确输入分数！");
                        return;
                    }
                    if (grade < 0||grade>100) {
                        showToast("请正确输入大于0小于101的数！");
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("position", position);
                    intent.putExtra("grade", grade);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    showToast("请您评分！");
                    return;
                }
                break;
        }

    }


}
