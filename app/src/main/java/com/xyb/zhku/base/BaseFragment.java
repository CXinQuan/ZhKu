package com.xyb.zhku.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xyb.zhku.utils.Utils;

import butterknife.ButterKnife;

/**
 * Created by lenovo on 2018/9/22.
 */

public abstract class BaseFragment extends Fragment {
    public BaseFragment() {
    }

    public Context mCtx;
    public View rootView;

    protected abstract int setView();

    protected abstract void init(View view);

    protected abstract void initData(Bundle savedInstanceState);

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCtx = getContext();
        rootView = View.inflate(container.getContext(), setView(), null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    public void showToast(String text) {
        Toast.makeText(mCtx, text, Toast.LENGTH_SHORT).show();
    }
    public void Log(String text){
        Utils.showLog(text);
    }

}

