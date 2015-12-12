package com.qiaosheng.common.utils;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午11:05
 * To change this template use File | Settings | File Templates.
 */
public class ReadParseHelper {


    public static boolean parseForBoolean(String input ){

        if( input==null)
            return false;
        if( input.equalsIgnoreCase("Y") || input.equals("是") || input.equals("√")){
            return true;
        }
        return false;
    }

    public static String parseForPriority(String input ){
        if( input==null)
            return BasicConstants.SPECIAL_SELL_TYPE_D;
        if( input.equalsIgnoreCase("a") || input.startsWith("a")|| input.startsWith("A")){
            return BasicConstants.SPECIAL_SELL_TYPE_A;
        }
        if( input.equalsIgnoreCase("b") || input.startsWith("b")|| input.startsWith("B")){
            return BasicConstants.SPECIAL_SELL_TYPE_B;
        }
        if( input.equalsIgnoreCase("c") || input.startsWith("c")|| input.startsWith("C")){
            return BasicConstants.SPECIAL_SELL_TYPE_C;
        }
        if( input.equalsIgnoreCase("d") || input.startsWith("d")|| input.startsWith("D")){
            return BasicConstants.SPECIAL_SELL_TYPE_D;
        }
        return BasicConstants.SPECIAL_SELL_TYPE_D;
    }

}
