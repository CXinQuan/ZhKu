package com.xyb.zhku.utils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.bmob.v3.datatype.BmobFile;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 陈鑫权  on 2019/4/30.
 */

public class DownLoadUtils {

    static OkHttpClient client = new OkHttpClient();
    private static File file;

    public static void previewFile(final Activity context, final BmobFile bmobFile, final TbsReaderView mTbsReaderView) {

        Request request = new Request.Builder().get().url(bmobFile.getUrl())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(context, "服务器离家出走了");
                    }
                });
            }

            public void onResponse(Call call, Response response) throws IOException {
                //拿到字节流
                InputStream is = response.body().byteStream();
                int len = 0;
                // 路径需要一个个创建
                File pathPackageName = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + context.getPackageName());
                if (!pathPackageName.exists()) {
                    pathPackageName.mkdir();
                }
                final File pathDownLoadFile = new File(pathPackageName + File.separator + "PreViewFile");
                if (!pathDownLoadFile.exists()) {
                    pathDownLoadFile.mkdirs();
                }
                File pathFile = new File(pathDownLoadFile + File.separator + System.currentTimeMillis());
                if (!pathFile.exists()) {
                    pathFile.mkdir();
                }
                file = new File(pathFile, bmobFile.getFilename());
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
                final Bundle bundle = new Bundle();
                bundle.putString("filePath", file.getPath());
                bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
                boolean result = mTbsReaderView.preOpen(parseFormat(file.getName()), false);
                if (result) {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTbsReaderView.openFile(bundle);
                        }
                    });
                }
            }
        });
    }

    public static void deleteFile() {
        if (file != null && file.exists() && file.isFile()) {
            file.delete();
        }
    }


    private static String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


}
