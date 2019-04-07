package com.xyb.zhku.factory;

import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.fragment.manager.StuInfoInputFragement;
import com.xyb.zhku.fragment.manager.notify.ReleaseNotifyFragment;
import com.xyb.zhku.fragment.manager.teachingtask.ReleaseTeachingTaskFragment;

import java.util.HashMap;


public class ManagerReleaseFragmentFactory {

    private static HashMap<Integer, BaseFragment> mFragmentMap = new HashMap<Integer, BaseFragment>();

    public static BaseFragment createFragment(int position) {
        BaseFragment fragment = mFragmentMap.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment=new ReleaseNotifyFragment();
                    break;
                case 1:
                    fragment=new ReleaseTeachingTaskFragment();
                    break;
                case 2:
                    fragment=new StuInfoInputFragement();
                    break;
            }
        }
        mFragmentMap.put(position, fragment);
        return fragment;
    }


}
