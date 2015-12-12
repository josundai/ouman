package com.qiaosheng.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午4:37
 * To change this template use DIRECTORY | Settings | DIRECTORY Templates.
 */
public class BasicConfiguredValues {

    //TODO: change this for new directory.
    public static  String JINPIN_DIRECTORY = "D:\\project\\欧曼\\forJava\\jingpin\\";
    //Java Output directory.
    public static  String basicDir = "D:\\project\\欧曼\\输出数据\\ExcelsForPPT\\javaOutput\\";

    public static   String PAGE1_OUTPUT_TXTName = basicDir + "page1\\各功能授权渠道评估.txt";
    public static   String PAGE2And3_OUTPUT_DIRECTORY = basicDir + "page2And3\\";
    public static   String PAGE6And7_OUTPUT_DIRECTORY = basicDir + "page6And7\\";
    public static   String PAGE4And5_OUTPUT_DIRECTORY = basicDir + "page4And5\\";
    public static   String PAGE8And8_OUTPUT_DIRECTORY = basicDir + "page8And9\\";
    public static   String PAGE10_OUTPUT_DIRECTORY = basicDir + "page10\\各价值链组合评价.txt";
    public static   String PAGE11_OUTPUT_DIRECTORY = basicDir + "page11\\";
    public static   String PAGE12_OUTPUT_DIRECTORY = basicDir + "page12\\";
    public static   String PAGE13_OUTPUT_DIRECTORY = basicDir + "page13\\";
    public static   String PAGE14_OUTPUT_DIRECTORY = basicDir + "page14\\";
    public static   String PAGE15_OUTPUT_DIRECTORY = basicDir + "page15\\";
    public static   String PAGE16_OUTPUT_DIRECTORY = basicDir + "page16\\";
    public static   String PAGE17_OUTPUT_DIRECTORY = basicDir + "page17\\";
    public static   String PAGE18_OUTPUT_DIRECTORY = basicDir + "page18\\";
    public static   String PAGE19_OUTPUT_DIRECTORY = basicDir + "page19\\";

    public static final String NETWORKPLAN_OUTPUT_DIRECTORY = basicDir + "网络规划数据\\";



    public static List<String> allDirectory = new ArrayList<>();

    public static void reloadDirctories(){

        PAGE1_OUTPUT_TXTName = basicDir + "page1\\各功能授权渠道评估.txt";
        PAGE2And3_OUTPUT_DIRECTORY = basicDir + "page2And3\\";
        PAGE6And7_OUTPUT_DIRECTORY = basicDir + "page6And7\\";
        PAGE4And5_OUTPUT_DIRECTORY = basicDir + "page4And5\\";
        PAGE8And8_OUTPUT_DIRECTORY = basicDir + "page8And9\\";
        PAGE10_OUTPUT_DIRECTORY = basicDir + "page10\\各价值链组合评价.txt";
        PAGE11_OUTPUT_DIRECTORY = basicDir + "page11\\";
        PAGE12_OUTPUT_DIRECTORY = basicDir + "page12\\";
        PAGE13_OUTPUT_DIRECTORY = basicDir + "page13\\";
        PAGE14_OUTPUT_DIRECTORY = basicDir + "page14\\";
        PAGE15_OUTPUT_DIRECTORY = basicDir + "page15\\";
        PAGE16_OUTPUT_DIRECTORY = basicDir + "page16\\";
        PAGE17_OUTPUT_DIRECTORY = basicDir + "page17\\";
        PAGE18_OUTPUT_DIRECTORY = basicDir + "page18\\";
        PAGE19_OUTPUT_DIRECTORY = basicDir + "page19\\";



        allDirectory.add( PAGE1_OUTPUT_TXTName );
        allDirectory.add( PAGE2And3_OUTPUT_DIRECTORY );
        allDirectory.add( PAGE6And7_OUTPUT_DIRECTORY );
        allDirectory.add( PAGE4And5_OUTPUT_DIRECTORY );
        allDirectory.add( PAGE8And8_OUTPUT_DIRECTORY );
        allDirectory.add( PAGE10_OUTPUT_DIRECTORY );
        allDirectory.add( PAGE11_OUTPUT_DIRECTORY );
        allDirectory.add( PAGE12_OUTPUT_DIRECTORY );
        allDirectory.add( PAGE13_OUTPUT_DIRECTORY );
        allDirectory.add( PAGE14_OUTPUT_DIRECTORY );
        allDirectory.add( PAGE15_OUTPUT_DIRECTORY );
        allDirectory.add( PAGE16_OUTPUT_DIRECTORY );
        allDirectory.add( PAGE17_OUTPUT_DIRECTORY );
        allDirectory.add( PAGE18_OUTPUT_DIRECTORY );
        allDirectory.add( PAGE19_OUTPUT_DIRECTORY );
        allDirectory.add( NETWORKPLAN_OUTPUT_DIRECTORY );
    }

}
