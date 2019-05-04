package com.xyb.zhku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.xyb.zhku.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 陈鑫权  on 2019/4/14.
 */

public class MyAutoCompleteTextViewAdapter extends BaseAdapter implements Filterable {
    private Context mContext;
    private List<String> mItems;
    private List<String> fData;
    private final Object mLock = new Object();
    private MyFilter mFilter;

    public MyAutoCompleteTextViewAdapter(Context context,List<String> items) {
        this.mContext = context;
        this.mItems=items;
        mFilter = new MyFilter();
    }

    public void transforData(List<String> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public String getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_auto_text, null);
            viewHolder.content = convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.content.setText(mItems.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView content;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class MyFilter extends Filter {
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (fData == null) {
                synchronized (mLock) {
                    fData = new ArrayList<>(mItems);
                }
            }
            int count = fData.size();
            ArrayList<String> values = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String value = fData.get(i);
                if (null != value && null != constraint && value.contains(constraint.toString())) {  //value.toLowerCase().contains(constraint.toString().toLowerCase())
                    values.add(value);
                }
            }
            results.values = values;
            results.count = values.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence arg0, FilterResults results) {
            mItems = (List<String>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}

