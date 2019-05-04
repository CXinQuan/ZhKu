package com.xyb.zhku.factory;

import com.xyb.zhku.base.BaseFragment;
import com.xyb.zhku.fragment.teacher.notify.OtherFragment;
import com.xyb.zhku.fragment.teacher.notify.PartyBuildFragment;
import com.xyb.zhku.fragment.teacher.notify.ScienceFragment;
import com.xyb.zhku.fragment.teacher.notify.TeachingFragment;

import java.util.HashMap;

/**
 * Created by lenovo on 2018/9/23.
 */

public class TeacherHomeFragmentFactory {

    private static HashMap<Integer, BaseFragment> mFragmentMap = new HashMap<Integer, BaseFragment>();

    public static BaseFragment createFragment(int position) {
        BaseFragment fragment = mFragmentMap.get(position);
        if (fragment == null) {
            switch (position) {
                case 0:
                    //  fragment = new TeacherTeachingFragment();
                    fragment=new TeachingFragment();

                    break;
                case 1:
                    //  fragment = new TeacherScienceFragment();
                    fragment=new ScienceFragment();
                    break;
                case 2:
                 //   fragment = new TeacherPartyBuildFragment();
                    fragment=new PartyBuildFragment();
                    break;
                case 3:
                   // fragment = new TeacherOtherFragment();
                    fragment=new OtherFragment();
                    break;
            }
        }
        mFragmentMap.put(position, fragment);
        return fragment;


    }


}
