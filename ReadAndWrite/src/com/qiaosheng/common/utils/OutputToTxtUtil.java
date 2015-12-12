package com.qiaosheng.common.utils;

import com.qiaosheng.common.pojo.model.JinpinOneLinePOJO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午5:52
 * To change this template use File | Settings | File Templates.
 */
public  class OutputToTxtUtil {
    private static final Logger log = LoggerFactory.getLogger(OutputToTxtUtil.class);


    public void generateText(){
        System.out.println("Null!");
    }


    public static void writeTo(Map<Integer, StringBuffer> lineMap, String fileName){
        try {
            if( fileName == null ){
                System.err.println("输出文件未指定！" );
                return;
            }

            FileOutputStream fos=new FileOutputStream(new File(fileName));
            OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw=new BufferedWriter(osw);
            for(int i=0; i< lineMap.size(); i++){
                if( lineMap.get(i)==null ){
                    log.warn("Empty line at line {} while output to file {}", i, fileName);
                    continue;
                }
                bw.write(lineMap.get(i).append("\r\n").toString());
            }
            //注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
            bw.close();
            osw.close();
            fos.close();
        } catch (Exception e) {
            System.out.println("不能输出到txt文件.");
            e.printStackTrace();
        }finally {

        }
    }


}
