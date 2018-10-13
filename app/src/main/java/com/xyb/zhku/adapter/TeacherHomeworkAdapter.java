package com.xyb.zhku.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xyb.zhku.R;
import com.xyb.zhku.bean.TeacherHomeWork;
import com.xyb.zhku.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2018/9/25.
 */

public class TeacherHomeworkAdapter extends RecyclerView.Adapter<TeacherHomeworkAdapter.TeacherHomeworkViewHolder> {

    private Context mCtx;
    private List<TeacherHomeWork> list;
    RecyclerView recyclerView;
    OnChildClickListener listener;
    DeleteListener deletelistener;

    public void setDeleteListener(DeleteListener listener) {
        this.deletelistener = listener;
    }

    public interface DeleteListener {
        void delete(int position);//删除哪一项
    }

    private float popupWindowX;

    public TeacherHomeworkAdapter(Context mCtx, List<TeacherHomeWork> list) {
        this.mCtx = mCtx;
        this.list = list;
    }

    public void setOnChildClickListener(OnChildClickListener listener) {
        this.listener = listener;
    }

    //一进来就会调用的方法
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
    }

    //销毁时调用的方法
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = null;
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public TeacherHomeworkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_homework_item, null);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                if (positionlistener != null) {
//                    float x = event.getX();
//                    positionlistener.position(x);
//                }
                popupWindowX = event.getX();
                return false;
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = recyclerView.getChildAdapterPosition(v);
                showPopupWindow(view, position);
                return true;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView != null && listener != null) {
                    int position = recyclerView.getChildAdapterPosition(v);
                    listener.onChlidClick(recyclerView, v, position, list.get(position));
                }
            }
        });
        return new TeacherHomeworkViewHolder(view);
    }

    protected void showPopupWindow(View view, final int position) {
        PopupWindow popupWindow = null;
        //1,窗体上展示的内容
        ImageView imageView = new ImageView(mCtx);
        imageView.setImageResource(R.mipmap.popupwindow_delete);
        //2,挂载在popupWindow上
        popupWindow = new PopupWindow(imageView, 200, 200, true);  //宽高各100，true表示要获取焦点
        //3,指定popupWindow的背景(设置上背景,点击返回按钮才会有响应)
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //一定要设置背景
        //4,指定popupWindow所在位置(挂载在哪个控件上,在父控件上的位置,偏离距离)

        popupWindow.showAsDropDown(view, (int) popupWindowX, -view.getHeight() - UIUtils.px2dip(mCtx,popupWindow.getHeight())); //在根布局上显示，正中间，距离正中间的x，y往右往下各偏离100

        final PopupWindow finalPopupWindow = popupWindow;
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (deletelistener != null) {
                    deletelistener.delete(position);
                    if (finalPopupWindow != null) {
                        finalPopupWindow.dismiss();
                    }
                }
            }
        });

    }

    public void onBindViewHolder(TeacherHomeworkViewHolder holder, int position) {
        TeacherHomeWork homeWork = list.get(position);
        holder.tv_major.setText("专业：" + homeWork.getMajor());
        holder.tv_subject.setText("科目：" + homeWork.getSubject());
        holder.tv_time.setText("时间：" + homeWork.getCreatedAt());
        holder.tv_title.setText("内容：" + homeWork.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TeacherHomeworkViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_homework)
        ImageView iv_homework;
        @BindView(R.id.tv_subject)
        TextView tv_subject;
        @BindView(R.id.tv_major)
        TextView tv_major;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.tv_title)
        TextView tv_title;

        public TeacherHomeworkViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnChildClickListener {
        public void onChlidClick(RecyclerView recyclerView, View itemView, int position, Object data);
    }


}

