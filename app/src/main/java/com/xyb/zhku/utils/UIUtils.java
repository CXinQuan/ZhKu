package com.xyb.zhku.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.global.ZhKuApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/9/20.
 */

public class UIUtils {
    // /////////////////加载布局文件//////////////////////////
    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    // /////////////////判断是否运行在主线程//////////////////////////
    public static boolean isRunOnUIThread() {
        // 获取当前线程id, 如果当前线程id和主线程id相同, 那么当前就是主线程
        int myTid = android.os.Process.myTid();
        if (myTid == getMainThreadId()) {
            return true;
        }
        return false;
    }

    // 运行在主线程
    public static void runOnUIThread(Runnable r) {
        if (isRunOnUIThread()) {
            // 已经是主线程, 直接运行
            r.run();
        } else {
            // 如果是子线程, 借助handler让其运行在主线程
            getHandler().post(r);
        }
    }

    public static Context getContext() {
        return ZhKuApplication.getContext();
    }

    public static Handler getHandler() {
        return ZhKuApplication.getHandler();
    }

    public static int getMainThreadId() {
        return ZhKuApplication.getMainThreadId();
    }

    /**
     * 判断一个 EditText 的内容是否为空
     *
     * @param editText
     * @return
     */
    public static boolean isEmtpy(EditText editText) {
        if (editText != null
                && !editText.getText().toString().trim().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 对 AutoCompleteTextView 绑定 数据
     *
     * @param autoCompleteTextView
     * @param autoString
     */
    public static void bindArray(AutoCompleteTextView autoCompleteTextView, String[] autoString) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, autoString);
        autoCompleteTextView.setAdapter(adapter);     // 绑定adapter
    }

    /**
     * 对 AutoCompleteTextView 绑定 数据
     *
     * @param autoCompleteTextView
     * @param autoString
     */
    public static void bindArray(AutoCompleteTextView autoCompleteTextView, List<String> autoString) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, autoString);
        autoCompleteTextView.setAdapter(adapter);     // 绑定adapter
    }

    public static void bindSpinnerAdapter(Spinner spinner, String[] arr) {   //simple_dropdown_item_1line  simple_spinner_item
        if (spinner != null && arr != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, arr);
            spinner.setAdapter(adapter);
        }
    }

    public static void bindSpinnerAdapter(Spinner spinner, List<String> arr) {   //simple_dropdown_item_1line  simple_spinner_item
        if (spinner != null && arr != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, arr);
            spinner.setAdapter(adapter);
        }
    }

    public static void bindSpinnerBaseAdapter(Context ctx, Spinner spinner, String[] arr) {
          spinner.setAdapter(new SpinnerAdapter(arr));
       //spinner.setAdapter(new MyArrayAdapter(ctx, arr));
    }


    /**
     * 根据手机的分辨率dip 的单位转成px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从px 的单位转成dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    static class SpinnerAdapter extends BaseAdapter {
        List<String> list;

        public SpinnerAdapter(String[] arr) {
            list = new ArrayList();
            for (String str : arr) {
                list.add(str);
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, null);
//            TextView textView = new TextView(parent.getContext());

            view.setText(list.get(position));
            view.setTextColor(Color.BLACK);
            return view;
        }
    }


}
