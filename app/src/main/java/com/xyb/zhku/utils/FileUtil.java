package com.xyb.zhku.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by lenovo on 2018/9/20.
 */

public class FileUtil {
    private String sdpath;

    public FileUtil() {

    }

    public String getSdpath() {
        return sdpath;
    }

    public FileUtil(String SDPATH) {
        //得到外部存储设备的目录（/SDCARD）
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }

    /**
     * 在SD卡上创建文件
     *
     * @param fileName
     * @return
     * @throws java.io.IOException
     */
    public File createSDFile(String fileName) throws IOException {
        File file = new File(sdpath + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName 目录名字
     * @return 文件目录
     */
    public File createDir(String dirName) {
        File dir = new File(sdpath + dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     */
    public boolean isFileExist(String fileName) {
        File file = new File(sdpath + fileName);
        return file.exists();
    }

    public File write2SDFromInput(String path, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;

        try {
            createDir(path);
            try {
                file = createSDFile(path + fileName);
            } catch (Exception ex) {
                //ex.printStackTrace();
                file = new File(path + "/" + fileName);
                Utils.showLog("", " path is  >>> " + path + "/" + fileName);
                //file.mkdir();
                String dir = file.getParent();
                Utils.showLog("", " dir is  >>> " + dir);
                new File(dir).mkdirs();
                //file.mkdirs();
                file.createNewFile();
            }

            output = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int num = 0;
            while ((num = input.read(buffer)) != -1) {
                output.write(buffer, 0, num);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    /**
     * 专为Android4.4以上设计的从Uri获取文件路径
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {

                // LogUtils.logInfoStar("isExternalStorageDocument");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                // LogUtils.logInfoStar("isDownloadsDocument");
                final String id = DocumentsContract.getDocumentId(uri);
                try {
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            // Uri.parse("content://com/android/providers/downloads/documents/document"),
                            Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                } catch (Exception e) {
                    Toast.makeText(context, "不要上传“下载”路径的文件！", Toast.LENGTH_SHORT).show();
                }
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                // LogUtils.logInfoStar("isMediaDocument");
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * 判断该文件是否是office文件
     *   不支持预览  .txt  .pdf
     * @param file
     * @return
     */
    public static boolean isOfffice(BmobFile file) {

        return file.getFilename().endsWith(".doc") ||
                file.getFilename().endsWith(".xls") ||
                file.getFilename().endsWith(".ppt") ||
                file.getFilename().endsWith(".docx") ||
                file.getFilename().endsWith(".xlsx") ||
                file.getFilename().endsWith(".pptx");
    }


//    public static String getPath(Context context, Uri uri) {
//        if ("content".equalsIgnoreCase(uri.getScheme())) {
//            String[] projection = {"_data"};
//            Cursor cursor = null;
//            try {
//                cursor = context.getContentResolver().query(uri, projection, null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow("_data");
//                if (cursor.moveToFirst()) {
//                    return cursor.getString(column_index);
//                }
//            } catch (Exception e) {
//            }
//        }else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//        return null;
//    }


}

