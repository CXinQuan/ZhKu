package com.xyb.zhku.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xyb.zhku.R;
import com.xyb.zhku.global.Constants;
import com.xyb.zhku.utils.SharePreferenceUtils;


/**
 * Created by 陈鑫权  on 2019/5/1.
 */

public class LoadModeDialog extends Dialog {

    OnLoadModeClickListener onLoadModeClickListener;

    public OnLoadModeClickListener getOnLoadModeClickListener() {
        return onLoadModeClickListener;
    }

    public void setOnLoadModeClickListener(OnLoadModeClickListener onLoadModeClickListener) {
        this.onLoadModeClickListener = onLoadModeClickListener;
    }

    public LoadModeDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(final Context cotext) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.more_load_mode);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.horizontalMargin = 0;
        window.setAttributes(attributes);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.getDecorView().setBackgroundColor(Color.WHITE);
        window.getDecorView().setBackgroundColor(Color.parseColor("#00000000"));
        findViewById(R.id.tv_tbs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLoadModeClickListener != null) {
                    SharePreferenceUtils.put(cotext, Constants.LOADMODE, Constants.TBS);
                    onLoadModeClickListener.onLoadModeClick(Constants.TBS);
                }
                dismiss();
            }
        });

        findViewById(R.id.tv_microsoft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLoadModeClickListener != null) {
                    SharePreferenceUtils.put(cotext, Constants.LOADMODE, Constants.MICROSOFT);
                    onLoadModeClickListener.onLoadModeClick(Constants.MICROSOFT);
                }
                dismiss();
            }
        });
        findViewById(R.id.tv_ow365).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLoadModeClickListener != null) {
                    SharePreferenceUtils.put(cotext, Constants.LOADMODE, Constants.OW365);
                    onLoadModeClickListener.onLoadModeClick(Constants.OW365);
                }
                dismiss();
            }
        });
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

    interface OnLoadModeClickListener {
        void onLoadModeClick(int loadMode);
    }

}
