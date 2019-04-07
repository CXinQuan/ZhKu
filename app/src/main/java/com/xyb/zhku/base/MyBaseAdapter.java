package com.xyb.zhku.base;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈鑫权  on 2019/4/4.
 */

public class MyBaseAdapter extends BaseAdapter {
    List<String> list;

    public MyBaseAdapter(List<String> list) {
        this.list = list;
    }

    public MyBaseAdapter(String[] strs) {
        list = new ArrayList<>();
        if (strs != null) {
            for (String str : strs) {
                list.add(str);
            }
        }
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
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
        TextView textView = new TextView(parent.getContext());
        textView.setTextSize(18);
        textView.setTextColor(Color.parseColor("#020002"));
        textView.setText(list.get(position));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        return textView;
    }
}
