package com.xyb.zhku.fragment.teacher;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.picasso.Picasso;
import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.bean.LunBoTu;
import com.xyb.zhku.factory.TeacherHomeFragmentFactory;
import com.xyb.zhku.ui.SearchNotifyActivity;
import com.xyb.zhku.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TeacherHomeFragment extends BaseFragment {
    @BindView(R.id.slider)
    SliderLayout sliderLayout;     // viewpager 等价于 SliderLayout


    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.linearLayout_search)
    LinearLayout linearLayout_search;
    //   FragmentManager manager;
//    public TeacherHomeFragment(FragmentManager manager) {
//        this.manager = manager;
//    }

    protected int setView() {
        return R.layout.teacher_fragment_home;
    }

    protected void init(View view) {
        initSliderView();
        // viewPager.setAdapter(new MyViewPageAdapter(manager));
        //解决 设置 viewPager.setOffscreenPageLimit(3) 时报错
        //因为这里是fragment嵌套，不能使用 getSupportFragmentManager,应使用getChildFragmentManager()
        viewPager.setAdapter(new MyViewPageAdapter(getChildFragmentManager()));

        viewPager.setOffscreenPageLimit(3); //默认只能缓存1个页面，所以这里设置缓存3个，当滑到第4个页面的时候，第1个页码就不会被销毁了
        //使 滑动的时候不会 重新 创建 Fragment
        tablayout.setupWithViewPager(viewPager);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.linearLayout_search})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.linearLayout_search:
                Intent intent = new Intent(mCtx, SearchNotifyActivity.class);
                // startActivity(intent);
                //通过使用共享LinearLayout 实现动画
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "linearLayout").toBundle());
                break;
        }
    }

    protected void initData(Bundle savedInstanceState) {

    }

    /**
     * 轮播图
     */
    public void initSliderView() {
//        // TODO: 2018/9/28     从服务器上获取 轮播图
//        List<String> list = new ArrayList<>();
//        list.add("http://bmob-cdn-20487.b0.upaiyun.com/2018/07/12/e0171d6e40db4237807d57d541386a62.jpg");
//        list.add("http://bmob-cdn-20487.b0.upaiyun.com/2018/07/12/ec863e23407314ab80ebd490f14de984.jpg");
//        list.add("http://bmob-cdn-20487.b0.upaiyun.com/2018/07/12/d53434a44020b16880d17aebf2746cf8.gif");
//        for (int i = 0; i < list.size(); i++) {
//            TextSliderView textSliderView = new TextSliderView(mCtx);
//            textSliderView
//                    //.description(promotionList.get(i).getInfo())//给轮播图的每一个图片,添加描述文字 将promotionList.get(i).getInfo()进行替换为自己的描述信息
//                    .image(list.get(i))//指定图片 将promotionList.get(i).getPic()进行替换
//                    .setScaleType(BaseSliderView.ScaleType.Fit);//ScaleType设置图片展示方式(fitxy  centercrop)
//            slider.addSlider(textSliderView); //向SliderLayout控件的内部添加条目
//        }
       // slider.stopAutoCycle();
       // slider.startAutoCycle(12000, 12000, true); // 延迟4秒后开始循环，每隔4秒循环一次，用户触摸后不循环
        int default_pic_count = 3;
        final List<TextSliderView> sliderViewList = new ArrayList<>();
        for (int i = 0; i < default_pic_count; i++) {
            TextSliderView textSliderView = new TextSliderView(mCtx); // TextSliderView 等价于 ImageView,只不过添加了一个显示文本功能
            textSliderView
                    //.description(promotionList.get(i).getInfo())//给轮播图的每一个图片,添加描述文字 将promotionList.get(i).getInfo()进行替换为自己的描述信息
                    .error(R.mipmap.default_pic)  // 加载失败图片
                    .empty(R.mipmap.default_pic)  // 默认图片
                    // .image(list.get(i))//指定图片 将promotionList.get(i).getPic()进行替换
                    .setScaleType(BaseSliderView.ScaleType.Fit);//ScaleType设置图片展示方式(fitxy  centercrop)
            sliderViewList.add(textSliderView);
            sliderLayout.addSlider(textSliderView); //向SliderLayout控件的内部添加条目
        }

        // TODO: 2018/9/28     从服务器上获取 轮播图，如果图片数量比3大的话
        // todo 那么就 slider.addSlider(textSliderView);  并设置其 默认图片
        BmobQuery<LunBoTu> query = new BmobQuery<LunBoTu>();
        boolean isCache = query.hasCachedResult(LunBoTu.class);
        if (isCache) {
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 先从缓存取数据，如果没有的话，再从网络取。
        } else {
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);    // 如果没有缓存的话，则先从网络中取
        }
        query.findObjects(new FindListener<LunBoTu>() {
            public void done(List<LunBoTu> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        for (int i = 0; i < object.size(); i++) {
                            LunBoTu lunBoTu = object.get(i);
                            if (i >= 3) {
                                TextSliderView textSliderView = new TextSliderView(mCtx);
                                textSliderView.error(R.mipmap.default_pic)
                                        .empty(R.mipmap.default_pic)
                                        .image(lunBoTu.getImage().getUrl())
                                        .description(lunBoTu.getDescription() != null ? lunBoTu.getDescription() : "")
                                        .setScaleType(BaseSliderView.ScaleType.Fit);//ScaleType设置图片展示方式(fitxy  centercrop)
                                sliderViewList.add(textSliderView);
                                sliderLayout.addSlider(textSliderView); //向SliderLayout控件的内部添加条目
                            } else {
                                sliderViewList.get(i).image(lunBoTu.getImage().getUrl());
                                sliderViewList.get(i).description(lunBoTu.getDescription() != null ? lunBoTu.getDescription() : "");
                            }
                        }
                    } else {  // 没有获取到轮播图
                        List<String> list = new ArrayList<>();
                        list.add("http://bmob-cdn-20487.b0.upaiyun.com/2018/07/12/e0171d6e40db4237807d57d541386a62.jpg");
                        list.add("http://bmob-cdn-20487.b0.upaiyun.com/2018/07/12/ec863e23407314ab80ebd490f14de984.jpg");
                        list.add("http://bmob-cdn-20487.b0.upaiyun.com/2018/07/12/d53434a44020b16880d17aebf2746cf8.gif");
                        for (int i = 0; i < list.size(); i++) {
                            sliderViewList.get(i).image(list.get(i));
                        }
                    }
                } else {
                    final List<String> list = new ArrayList<>();
                    list.add("http://bmob-cdn-20487.b0.upaiyun.com/2018/07/12/e0171d6e40db4237807d57d541386a62.jpg");
                    list.add("http://bmob-cdn-20487.b0.upaiyun.com/2018/07/12/ec863e23407314ab80ebd490f14de984.jpg");
                    list.add("http://bmob-cdn-20487.b0.upaiyun.com/2018/07/12/d53434a44020b16880d17aebf2746cf8.gif");
                    for (int i = 0; i < list.size(); i++) {
                        sliderViewList.get(i).image(list.get(i));
                    }
                }
            }
        });
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default); //默认指定的动画类型
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);    //指定圆点指示器的位置
        sliderLayout.setCustomAnimation(new DescriptionAnimation());  //定义一个描述动画
        sliderLayout.setDuration(4000);   //动画时长

    }

    class MyViewPageAdapter extends FragmentPagerAdapter {
        String[] teacherTitle = new String[]{"教学", "科研", "党建", "其它"};

        public MyViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            return TeacherHomeFragmentFactory.createFragment(position);
        }

        public int getCount() {
            return teacherTitle.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return teacherTitle[position];
        }
    }


}
