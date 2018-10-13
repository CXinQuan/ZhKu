package com.xyb.zhku.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by lenovo on 2018/9/23.
 */

public class TeacherPartyBuildRecycleAdapter extends RecyclerView.Adapter {

    private Context mCtx;

    public TeacherPartyBuildRecycleAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(mCtx);
        textView.setText("测试");
        textView.setTextSize(20);
        return new TeacherPartyBuildRecycleViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 100;
    }
    class TeacherPartyBuildRecycleViewHolder extends RecyclerView.ViewHolder {

        public TeacherPartyBuildRecycleViewHolder(View itemView) {
            super(itemView);
        }
    }
}


