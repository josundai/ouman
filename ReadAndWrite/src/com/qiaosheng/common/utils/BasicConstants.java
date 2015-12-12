package com.qiaosheng.common.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午3:37
 * To change this template use File | Settings | File Templates.
 */
public class BasicConstants {

    public static final String SPECIAL_SELL_TYPE_A = "a";
    public static final String SPECIAL_SELL_TYPE_B = "b";
    public static final String SPECIAL_SELL_TYPE_C = "c";
    public static final String SPECIAL_SELL_TYPE_D = "d";
    public static final String SPECIAL_SELL_TYPE_Empty = "非形象店";

    public static final String CITY_TYPE_SHENGHUI = "省会";
    public static int CITYNUMBER_SHENGHUI = 31;
    public static final String CITY_TYPE_DIJI = "地级市";
    public static int CITYNUMBER_DIJI = 333;
    public static final String CITY_TYPE_XIANGJI = "县级市";
    public static int CITYNUMBER_XIANGJI = 2009;

    public static final int NUM_CITY_SHENGHUI = 31;
    public static final int NUM_CITY_DIJI = 333;
    public static final int NUM_CITY_XIANGJI = 2009;

    public static List<String> cityTypeList = new ArrayList<>();


    public static final String NETWORKTYPE_ROAD = "公路车网络";
    public static final String NETWORKTYPE_MACHINE = "工程车网络";

    public static final String TYPE_QIANYIN = "牵引车";
    public static final String TYPE_PINBAN = "平板车";
    public static final String TYPE_ZIXIE = "自卸车";
    public static final String GLOBAL = "总体";

    public static final String PRIORITY_SUMMARY = "总计";

    public static final String BRAND_OUMAN = "欧曼";
    public static final String BRAND_DONGFENG = "东风";
    public static final String BRAND_JIEFANG = "解放";
    public static final String BRAND_OTHERS = "其他品牌";

    public static List<String> fenleiTypeList = new ArrayList<>();
    public static List<String> priorityList = new ArrayList<>();
    public static List<String> priorityListAndAll = new ArrayList<>();

    static{
        fenleiTypeList.add(GLOBAL);
        fenleiTypeList.add( NETWORKTYPE_ROAD);
        fenleiTypeList.add( NETWORKTYPE_MACHINE);
        fenleiTypeList.add( TYPE_QIANYIN);
        fenleiTypeList.add( TYPE_PINBAN);
        fenleiTypeList.add( TYPE_ZIXIE);

        priorityList.add("a");
        priorityList.add("b");
        priorityList.add("c");
        priorityList.add("d");

        priorityListAndAll.add("a");
        priorityListAndAll.add("b");
        priorityListAndAll.add("c");
        priorityListAndAll.add("d");
        priorityListAndAll.add(PRIORITY_SUMMARY);

        cityTypeList.add(CITY_TYPE_SHENGHUI);
        cityTypeList.add(CITY_TYPE_DIJI);
        cityTypeList.add(CITY_TYPE_XIANGJI);


    }


    public static final String TAB_WORD_BREAK = "\t";

    public static final String ENTER_LINE_BREAK = "\n";

    public static final String ZERO_WORD_FILL = "0";


    public static final DecimalFormat DecimalFormat = new DecimalFormat("###.0000");



}
