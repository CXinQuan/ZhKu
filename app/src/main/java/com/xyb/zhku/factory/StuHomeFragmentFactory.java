package com.xyb.zhku.factory;

import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.fragment.student.notify.StuOtherFragment;
import com.xyb.zhku.fragment.student.notify.StuTeachingFragment;
import com.xyb.zhku.fragment.student.notify.StuWorkFragment;
import com.xyb.zhku.fragment.teacher.notify.OtherFragment;
import com.xyb.zhku.fragment.teacher.notify.PartyBuildFragment;
import com.xyb.zhku.fragment.teacher.notify.ScienceFragment;
import com.xyb.zhku.fragment.teacher.notify.TeachingFragment;

import java.util.HashMap;

/**
 * Created by lenovo on 2018/9/23.
 */

public class StuHomeFragmentFactory {

    private static HashMap<Integer, BaseFragment> mFragmentMap = new HashMap<Integer, BaseFragment>();

    public static BaseFragment createFragment(int position) {
        BaseFragment fragment = mFragmentMap.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    fragment=new StuTeachingFragment();
                    break;
                case 1:
                    fragment=new StuWorkFragment();
                    break;
                case 2:
                    fragment=new StuOtherFragment();
                    break;
            }
        }
        mFragmentMap.put(position, fragment);
        return fragment;
    }


}
