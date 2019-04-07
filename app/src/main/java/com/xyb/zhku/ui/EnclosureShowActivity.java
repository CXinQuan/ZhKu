package com.xyb.zhku.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseActivity;
import com.xyb.zhku.global.Constants;
import com.xyb.zhku.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EnclosureShowActivity extends BaseActivity {
    @BindView(R.id.iv_head_back)
    ImageView iv_head_back;
    @BindView(R.id.tv_head_content)
    TextView tv_head_content;
    @BindView(R.id.webview)
    WebView webview;
    private WebSettings settings;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.pb_file_download)
    ProgressBar pb_file_download;
    @BindView(R.id.tv_download_file)
    TextView tv_download_file;
    @BindView(R.id.tv_not_support)
    TextView tv_not_support;


    private BmobFile file;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        tv_head_content.setText("附件详情");

        file = (BmobFile) intent.getSerializableExtra("file");

        if (file != null) {
            if (!FileUtil.isOfffice(file)) {
                tv_not_support.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
                webview.setVisibility(View.GONE);
            } else {
                tv_not_support.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                webview.setVisibility(View.VISIBLE);
                String url = file.getUrl();
                if (url != null) {
                    String officeUrl = Constants.OFFICEBASEURL + url;
                    initWebView(officeUrl);
                }
            }
        }

    }

    /**
     * 初始化 WebView
     *
     * @param url
     */
    //  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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
                view.loadUrl(url);//此行代码必须添加上去，否则不会显示
                return true;// 在跳转链接时强制在当前webview中加载
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                // System.out.println("进度:" + newProgress);
                if (newProgress >= 95) {
                    pb.setVisibility(View.GONE);
                    tv_not_support.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }


    @Override
    public int setContentViewLayout() {
        return R.layout.activity_enclosure_show;
    }

    @OnClick({R.id.iv_head_back, R.id.tv_download_file})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_head_back:
                finish();
                break;
            case R.id.tv_download_file:
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setIcon(R.mipmap.download)
                        .setTitle("下载附件")
                        .setMessage("是否确定下载该附件")
                        .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // /data/data/<application package>/files目录    mCtx.getFilesDir().getAbsolutePath()
                                // String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "DownLoadFile" + File.separator + System.currentTimeMillis();
                                // File downloadfile = new File(path, file.getFilename());
                                tv_download_file.setVisibility(View.GONE); // 下载附件 不可见
                                pb_file_download.setVisibility(View.VISIBLE);// 进度圆圈  可见
                                // 底层是以 异步任务 实现的 AsyncTask<Params, Progress, Result>
//                                file.download(downloadfile, new DownloadFileListener() {
////                                    @Override
////                                    public void onStart() {
////                                        tv_download_file.setVisibility(View.GONE); // 下载附件 不可见
////                                        pb_file.setVisibility(View.VISIBLE);// 进度圆圈  可见
////                                        super.onStart();
////                                    }
//
//                                    public void done(String savePath, BmobException e) {
//                                        //成功与否
//                                        if (e == null) {
//                                            showToast("下载成功，已保存" + savePath);
//                                        } else {
//                                            showToast("下载失败，请重试");
//                                            e.printStackTrace();
//                                        }
//                                        tv_download_file.setVisibility(View.VISIBLE); // 下载附件 不可见
//                                        pb_file.setVisibility(View.GONE);// 进度圆圈  可见
//
//                                    }
//
//                                    @Override
//                                    public void onProgress(Integer integer, long l) {
//                                        Log("下载进度：" + integer, "测试");
//
//                                    }
//                                });

                                // TODO: 2018/10/9    BmobFile 的下载 方法不好，可以选择使用okhttp 等其他网络架构的方法去下载
                                //  因为已经知道了url，没必要一定要通过BmobFile去下载
                                downloadFile(file);
                            }
                        })
                        .setNegativeButton("取消", null).create().show();
                break;
            default:
                break;
        }
    }

    /**
     * 使用okHttp 下载文件
     */
    public void downloadFile(final BmobFile bmobFile) {
        //   String url = "http://vfx.mtime.cn/Video/2016/07/24/mp4/160724055620533327_480.mp4";
        Log("文件", bmobFile.getFilename() + bmobFile.getUrl());

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(bmobFile.getUrl())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_download_file.setVisibility(View.VISIBLE); // 下载附件 不可见
                        pb_file_download.setVisibility(View.GONE);// 进度圆圈  可见
                        showToast("服务器繁忙");
                    }
                });
            }

            public void onResponse(Call call, Response response) throws IOException {
                //拿到字节流
                InputStream is = response.body().byteStream();
                int len = 0;
                // 路径需要一个个创建
                File pathPackageName = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getPackageName());
                if (!pathPackageName.exists()) {
                    pathPackageName.mkdir();
                }
                final File pathDownLoadFile = new File(pathPackageName + File.separator + "DownLoadFile");
                if (!pathDownLoadFile.exists()) {
                    pathDownLoadFile.mkdirs();
                }
                File pathFile = new File(pathDownLoadFile + File.separator + System.currentTimeMillis());
                if (!pathFile.exists()) {
                    pathFile.mkdir();
                }
                final File file = new File(pathFile, bmobFile.getFilename());
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
                Log("下载完成", "下载完成");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_download_file.setVisibility(View.VISIBLE); // 下载附件 不可见
                        pb_file_download.setVisibility(View.GONE);// 进度圆圈  可见
                        showToast("下载完成，路径为：" + file.getAbsolutePath());
                        // TODO: 2018/10/10    向服务器添加一个学号，表示该学生已经下载
                        // TODO: 2018/10/10    不过此处需要判断，只有学生下载老师的作业文件才进行记录，否则 老师或者学生下载通知文件，则不做此操作 


                    }
                });


            }
        });

    }

}
