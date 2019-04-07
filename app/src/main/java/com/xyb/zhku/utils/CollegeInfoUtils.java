package com.xyb.zhku.utils;

import com.xyb.zhku.bean.CollegeInfo;

import java.util.ArrayList;

/**
 * Created by 陈鑫权  on 2019/4/2.
 */

public class CollegeInfoUtils {
    public static ArrayList<CollegeInfo> getAllCollegeInfo() {

        ArrayList<CollegeInfo> collegeInfos = new ArrayList<>();

        CollegeInfo cityBuild = new CollegeInfo("城乡建设学院", "205");
        ArrayList<CollegeInfo.Major> cityBuildMajors = new ArrayList<>();
        CollegeInfo.Major major1 = new CollegeInfo.Major("给排水科学与工程", "20524");
        CollegeInfo.Major major2 = new CollegeInfo.Major("土木工程", "20534");
        CollegeInfo.Major major3 = new CollegeInfo.Major("城乡规划", "20514");
        cityBuildMajors.add(major1);
        cityBuildMajors.add(major2);
        cityBuildMajors.add(major3);
        cityBuild.setMajors(cityBuildMajors);
        collegeInfos.add(cityBuild);

        CollegeInfo animal = new CollegeInfo("动物科技学院", "218");
        ArrayList<CollegeInfo.Major> animalMajors = new ArrayList<>();
        CollegeInfo.Major major4 = new CollegeInfo.Major("水产养殖学", "21824");
        CollegeInfo.Major major5 = new CollegeInfo.Major("动物科学", "21814");
        animalMajors.add(major4);
        animalMajors.add(major5);
        animal.setMajors(animalMajors);
        collegeInfos.add(animal);

        CollegeInfo manager = new CollegeInfo("管理学院", "109");
        ArrayList<CollegeInfo.Major> managerMajors = new ArrayList<>();
        CollegeInfo.Major major6 = new CollegeInfo.Major("财务管理", "10924");
        CollegeInfo.Major major7 = new CollegeInfo.Major("人力资源管理", "10974");
        CollegeInfo.Major major8 = new CollegeInfo.Major("市场营销", "10934");
        CollegeInfo.Major major_gs = new CollegeInfo.Major("工商管理", "10944");
        CollegeInfo.Major major_kj = new CollegeInfo.Major("会计学", "10964");

        managerMajors.add(major6);
        managerMajors.add(major7);
        managerMajors.add(major8);
        managerMajors.add(major_gs);
        managerMajors.add(major_kj);
        manager.setMajors(managerMajors);
        collegeInfos.add(manager);

        CollegeInfo he = new CollegeInfo("何香凝艺术设计学院", "112");
        ArrayList<CollegeInfo.Major> heMajors = new ArrayList<>();
        CollegeInfo.Major major9 = new CollegeInfo.Major("视觉传达设计", "11234");
        CollegeInfo.Major major10 = new CollegeInfo.Major("产品设计", "11224");
        CollegeInfo.Major major11 = new CollegeInfo.Major("环境设计", "11214");
        heMajors.add(major9);
        heMajors.add(major10);
        heMajors.add(major11);
        he.setMajors(heMajors);
        collegeInfos.add(he);


        CollegeInfo hua = new CollegeInfo("化学化工学院", "210");
        ArrayList<CollegeInfo.Major> huaMajors = new ArrayList<>();
        CollegeInfo.Major major12 = new CollegeInfo.Major("高分子材料与工程", "21024");
        CollegeInfo.Major major13 = new CollegeInfo.Major("应用化学", "21014");
        CollegeInfo.Major major14 = new CollegeInfo.Major("化学工程与工艺", "21034");
        CollegeInfo.Major majorclhx = new CollegeInfo.Major("材料化学", "21044");
        huaMajors.add(major12);
        huaMajors.add(major13);
        huaMajors.add(major14);
        huaMajors.add(majorclhx);
        hua.setMajors(huaMajors);
        collegeInfos.add(hua);


        CollegeInfo xin = new CollegeInfo("信息科学与技术学院", "102");
        ArrayList<CollegeInfo.Major> xinMajors = new ArrayList<>();
        CollegeInfo.Major major15 = new CollegeInfo.Major("网络工程", "10226");
        CollegeInfo.Major major16 = new CollegeInfo.Major("物联网工程", "10215");
        CollegeInfo.Major major17 = new CollegeInfo.Major("电子信息工程", "10214");
        CollegeInfo.Major major18 = new CollegeInfo.Major("通信工程", "10217");
        CollegeInfo.Major majorjsj = new CollegeInfo.Major("计算机科学与技术", "10228");
        CollegeInfo.Major majorxxgl = new CollegeInfo.Major("信息管理与信息系统", "10229");
        xinMajors.add(major15);
        xinMajors.add(major16);
        xinMajors.add(major17);
        xinMajors.add(major18);
        xinMajors.add(majorjsj);
        xinMajors.add(majorxxgl);
        xin.setMajors(xinMajors);
        collegeInfos.add(xin);

        CollegeInfo huanjing = new CollegeInfo("环境科学与工程学院", "206");
        ArrayList<CollegeInfo.Major> huanjingMajors = new ArrayList<>();
        CollegeInfo.Major majorhjkx = new CollegeInfo.Major("环境科学", "20634");
        CollegeInfo.Major majorhjgc = new CollegeInfo.Major("环境工程", "20624");
        CollegeInfo.Major majorzyhj = new CollegeInfo.Major("资源环境科学", "20614");
        huanjingMajors.add(majorhjkx);
        huanjingMajors.add(majorhjgc);
        huanjingMajors.add(majorzyhj);
        huanjing.setMajors(huanjingMajors);
        collegeInfos.add(huanjing);


        CollegeInfo jidian = new CollegeInfo("机电工程学院", "208");
        ArrayList<CollegeInfo.Major> jidianMajors = new ArrayList<>();
        CollegeInfo.Major major_nydl = new CollegeInfo.Major("能源与动力工程", "20814");
        CollegeInfo.Major major_jxsj = new CollegeInfo.Major("机械设计制造及其自动化", "20824");
        CollegeInfo.Major major_jxdz = new CollegeInfo.Major("机械电子工程", "20834");
        jidianMajors.add(major_nydl);
        jidianMajors.add(major_jxsj);
        jidianMajors.add(major_jxdz);
        jidian.setMajors(jidianMajors);
        collegeInfos.add(jidian);

        CollegeInfo jingmao = new CollegeInfo("经贸学院", "216");
        ArrayList<CollegeInfo.Major> jingmaoMajors = new ArrayList<>();
        CollegeInfo.Major major_nljj = new CollegeInfo.Major("农林经济管理", "21614");
        CollegeInfo.Major major_tzx = new CollegeInfo.Major("投资学", "21684");
        CollegeInfo.Major major_hzjj = new CollegeInfo.Major("会展经济与管理", "21664");
        CollegeInfo.Major major_gjjj = new CollegeInfo.Major("国际经济与贸易", "21624");
        jingmaoMajors.add(major_nljj);
        jingmaoMajors.add(major_tzx);
        jingmaoMajors.add(major_hzjj);
        jingmaoMajors.add(major_gjjj);
        jingmao.setMajors(jingmaoMajors);
        collegeInfos.add(jingmao);

        CollegeInfo nongye = new CollegeInfo("农业与生物学院", "201");
        ArrayList<CollegeInfo.Major> nongyeMajors = new ArrayList<>();
        CollegeInfo.Major major_zwbh = new CollegeInfo.Major("植物保护", "20114");
        CollegeInfo.Major major_swkx = new CollegeInfo.Major("生物科学", "20124");
        CollegeInfo.Major major_swjs = new CollegeInfo.Major("生物技术", "20134");
        CollegeInfo.Major major_nx = new CollegeInfo.Major("农学", "20144");
        CollegeInfo.Major major_swkxl = new CollegeInfo.Major("生物科学类", "20154");
        CollegeInfo.Major major_zzkx = new CollegeInfo.Major("种子科学与工程", "20164");
        nongyeMajors.add(major_zwbh);
        nongyeMajors.add(major_swkx);
        nongyeMajors.add(major_swjs);
        nongyeMajors.add(major_nx);
        nongyeMajors.add(major_swkxl);
        nongyeMajors.add(major_zzkx);
        nongye.setMajors(nongyeMajors);
        collegeInfos.add(nongye);

        CollegeInfo qinggong = new CollegeInfo("轻工食品学院", "207");
        ArrayList<CollegeInfo.Major> qinggongMajors = new ArrayList<>();
        CollegeInfo.Major major_swgc = new CollegeInfo.Major("生物工程", "20734");
        CollegeInfo.Major major_bzgc = new CollegeInfo.Major("包装工程", "20744");
        CollegeInfo.Major major_spkx = new CollegeInfo.Major("食品科学与工程", "20714");
        CollegeInfo.Major major_spzl = new CollegeInfo.Major("食品质量与安全", "20724");
        qinggongMajors.add(major_swgc);
        qinggongMajors.add(major_bzgc);
        qinggongMajors.add(major_spkx);
        qinggongMajors.add(major_spzl);
        qinggong.setMajors(qinggongMajors);
        collegeInfos.add(qinggong);

        CollegeInfo renwen = new CollegeInfo("人文与社会科学学院", "114");
        ArrayList<CollegeInfo.Major> renwenMajors = new ArrayList<>();
        CollegeInfo.Major major_shgz = new CollegeInfo.Major("社会工作", "11424");
        CollegeInfo.Major major_xzgl = new CollegeInfo.Major("行政管理", "11414");
        renwenMajors.add(major_shgz);
        renwenMajors.add(major_xzgl);
        renwen.setMajors(renwenMajors);
        collegeInfos.add(renwen);

        CollegeInfo waiguoyu = new CollegeInfo("外国语学院", "211");
        ArrayList<CollegeInfo.Major> waiguoyuMajors = new ArrayList<>();
        CollegeInfo.Major major_riyu = new CollegeInfo.Major("日语", "21124");
        CollegeInfo.Major major_swyy = new CollegeInfo.Major("商务英语", "21134");
        CollegeInfo.Major major_engish = new CollegeInfo.Major("英语", "21114");
        waiguoyuMajors.add(major_riyu);
        waiguoyuMajors.add(major_swyy);
        waiguoyuMajors.add(major_engish);
        waiguoyu.setMajors(waiguoyuMajors);
        collegeInfos.add(waiguoyu);

        CollegeInfo yuanyi = new CollegeInfo("园艺园林学院", "215");
        ArrayList<CollegeInfo.Major> yuanyiMajors = new ArrayList<>();
        CollegeInfo.Major major_yl = new CollegeInfo.Major("园林", "21524");
        CollegeInfo.Major major_yy = new CollegeInfo.Major("园艺", "21514");
        yuanyiMajors.add(major_yl);
        yuanyiMajors.add(major_yy);
        yuanyi.setMajors(yuanyiMajors);
        collegeInfos.add(yuanyi);

        CollegeInfo zidonghua = new CollegeInfo("自动化学院", "117");
        ArrayList<CollegeInfo.Major> zidonghuaMajors = new ArrayList<>();
        CollegeInfo.Major major_dqgc = new CollegeInfo.Major("电气工程及其自动化", "11734");
        CollegeInfo.Major major_zdh = new CollegeInfo.Major("自动化", "11724");
        zidonghuaMajors.add(major_dqgc);
        zidonghuaMajors.add(major_zdh);
        zidonghua.setMajors(zidonghuaMajors);
        collegeInfos.add(zidonghua);

        return collegeInfos;
    }


}
