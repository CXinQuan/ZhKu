package com.xyb.zhku.fragment.manager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xyb.zhku.R;
import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.bean.User;
import com.xyb.zhku.factory.ManagerReleaseFragmentFactory;

import butterknife.BindView;

/**
 * Created by lenovo on 2018/10/16.
 */
@SuppressLint("ValidFragment")
public class ManagerReleaseFragment extends BaseFragment {
    @BindView(R.id.manager_tablayout)
    TabLayout manager_tablayout;
    @BindView(R.id.manager_viewpager)
    ViewPager manager_viewpager;

    int identify;



    public ManagerReleaseFragment() {
    }

    public ManagerReleaseFragment(int identify) {
        this.identify = identify;
    }

    @Override
    protected int setView() {
        return R.layout.manager_release_fragment;
    }

    @Override
    protected void init(View view) {

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getChildFragmentManager());
        manager_viewpager.setAdapter(adapter);
        manager_tablayout.setupWithViewPager(manager_viewpager);
        if(identify==User.MANAGER_NOTIFY){
            manager_tablayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    String[] titles = new String[]{"发布通知", "录入教学任务","录入学生信息"};

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ManagerReleaseFragmentFactory.createFragment(position);
        }

        @Override
        public int getCount() {
            if (identify == User.MANAGER_NOTIFY) {
                return 1;
            } else if (identify == User.MANAGER_TEACHINGTASK) {
                return 3;
            }
            return 0;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
