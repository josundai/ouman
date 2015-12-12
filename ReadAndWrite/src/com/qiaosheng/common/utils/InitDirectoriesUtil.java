package com.qiaosheng.common.utils;

import java.io.File;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午2:30
 * To change this template use File | Settings | File Templates.
 */
public class InitDirectoriesUtil {

    public static void initDirectories(){

        for(String directory : BasicConfiguredValues.allDirectory){
            checkDirectory(directory);
        }
    }

    private static void checkDirectory(String directory) {

        File file = new File(directory +"readme.txt");
        File parent = file.getParentFile();
        if(parent!=null&&!parent.exists()){
            parent.mkdirs();
        }
    }
}
