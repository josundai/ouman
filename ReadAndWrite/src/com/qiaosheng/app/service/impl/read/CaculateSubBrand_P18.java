package com.qiaosheng.app.service.impl.read;

import com.qiaosheng.app.service.IBasicServiceInterface;
import com.qiaosheng.common.pojo.model.JinpinOneLinePOJO;
import com.qiaosheng.common.utils.BasicConfiguredValues;
import com.qiaosheng.common.utils.BasicConstants;
import com.qiaosheng.common.utils.OutputToTxtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午5:03
 * For page 13 and 14.
 */
@Component
public class CaculateSubBrand_P18 implements IBasicServiceInterface {
    private static final Logger log = LoggerFactory.getLogger(CaculateSubBrand_P18.class);

    private static List<String> outputTitleSequence = new ArrayList<>();

    //Key is "subBrand->year： subType:saleNumber"
    Map<String, Map<Integer, SaleNumbers>> outputMap = new HashMap<>();

    Map<String, BrandInfo> brandMap = new HashMap<String,BrandInfo>();

    private static String QIANYIN = "牵引";
    private static String HEAVY_PINBAN= "重型平板";
    private static String MIDDLE_PINBAN= "中型平板";
    private static String ZIXIE= "自卸";
    private static String OTHER= "Others";


    static List<String> functionTypes = new ArrayList<>();
    static {
        functionTypes.add(QIANYIN);
        functionTypes.add(HEAVY_PINBAN);
        functionTypes.add(MIDDLE_PINBAN);
        functionTypes.add(ZIXIE);

    }



    public void run(){
        try {
            Map<Integer, StringBuffer> output = generateText(ReadJinpinOneLineFromSource.readJinpinInputData);
            OutputToTxtUtil.writeTo( output, BasicConfiguredValues.PAGE18_OUTPUT_DIRECTORY+"品牌销量对比分析.txt");
        } catch (Exception e) {
            log.error("Failed to generate report.", e);
        }
    }

    public Map<Integer, StringBuffer> generateText(List<JinpinOneLinePOJO> jinpinOneList) {
        readJinpinList( jinpinOneList );

        Map<Integer, StringBuffer> outputLines = new HashMap<>();
        int lineIndex = 0;

        for( Map.Entry<String, BrandInfo> brandEntry : brandMap.entrySet()){

            String subBrand = brandEntry.getKey();
            BrandInfo brandInfo = brandEntry.getValue();
            StringBuffer sb = new StringBuffer( brandInfo.brand ).append(BasicConstants.TAB_WORD_BREAK).append(brandInfo.grade).append(BasicConstants.TAB_WORD_BREAK).append(brandInfo.subBrand).append(BasicConstants.TAB_WORD_BREAK);
            Map<Integer, SaleNumbers> saleNumberPerYear = outputMap.get(subBrand);
            if( saleNumberPerYear == null ){
                continue;
            }
            for( int year =2012; year<=2018; year++){
                SaleNumbers saleNumbers = saleNumberPerYear.get(year);
                if( saleNumbers == null ){
                    sb.append("0").append(BasicConstants.TAB_WORD_BREAK).append("0").append(BasicConstants.TAB_WORD_BREAK).append("0").append(BasicConstants.TAB_WORD_BREAK).append("0").append(BasicConstants.TAB_WORD_BREAK).append("0").append(BasicConstants.TAB_WORD_BREAK);
                    continue;
                }
                sb.append(saleNumbers.saleNumberPerType.get(QIANYIN)==null?0:saleNumbers.saleNumberPerType.get(QIANYIN)).append(BasicConstants.TAB_WORD_BREAK)
                        .append(saleNumbers.saleNumberPerType.get(HEAVY_PINBAN) == null ? 0 : saleNumbers.saleNumberPerType.get(HEAVY_PINBAN)).append(BasicConstants.TAB_WORD_BREAK)
                        .append(saleNumbers.saleNumberPerType.get(MIDDLE_PINBAN) == null ? 0 : saleNumbers.saleNumberPerType.get(MIDDLE_PINBAN)).append(BasicConstants.TAB_WORD_BREAK)
                        .append(saleNumbers.saleNumberPerType.get(ZIXIE) == null ? 0 : saleNumbers.saleNumberPerType.get(ZIXIE)).append(BasicConstants.TAB_WORD_BREAK)
                        .append(saleNumbers.totalSaleNumber).append(BasicConstants.TAB_WORD_BREAK);
            }

            outputLines.put( lineIndex, sb );
            lineIndex++;
        }

        return outputLines;
    }

    //String is Type as above. Value is           销售情况per每个地区每年
    private void readJinpinList( List<JinpinOneLinePOJO> jinpinOneList){
        readBrandInfo(jinpinOneList);

        for( JinpinOneLinePOJO jinpinOneLinePOJO : jinpinOneList ){
            String subBrand = jinpinOneLinePOJO.subBrand;
//            if( subBrand.equals("D310")){
//                log.warn(" D310: {}  {}   {}  {}",  jinpinOneLinePOJO.year, jinpinOneLinePOJO.brand, jinpinOneLinePOJO.subBrand, jinpinOneLinePOJO.sellerName);
//            }
            Map<Integer, SaleNumbers> saleNumberPerTypeYear = outputMap.get(subBrand );
            if( saleNumberPerTypeYear == null){
                saleNumberPerTypeYear = new HashMap<>();
                outputMap.put(subBrand, saleNumberPerTypeYear);
            }
            SaleNumbers saleNumberPerType = saleNumberPerTypeYear.get( jinpinOneLinePOJO.year);
            if( saleNumberPerType == null ){
                saleNumberPerType = new SaleNumbers();
                saleNumberPerTypeYear.put(jinpinOneLinePOJO.year, saleNumberPerType);
            }
            String type = getOutputType(jinpinOneLinePOJO);
            if( type.equals( OTHER)){
                continue;
            }

            Map<String, Integer> outputSaleNumberPerType = saleNumberPerType.saleNumberPerType;
            int totalN = outputSaleNumberPerType.get(type)==null?0: outputSaleNumberPerType.get(type);
            outputSaleNumberPerType.put( type, totalN+ jinpinOneLinePOJO.saleNumber);
            saleNumberPerType.totalSaleNumber += jinpinOneLinePOJO.saleNumber;
        }
    }

    private String getOutputType(JinpinOneLinePOJO jinpinOneLinePOJO){

        if( jinpinOneLinePOJO.functionType.equals(BasicConstants.TYPE_QIANYIN)){
            return QIANYIN;
        }else if( jinpinOneLinePOJO.functionType.equals(BasicConstants.TYPE_ZIXIE)){
            return ZIXIE;

        }else if( jinpinOneLinePOJO.functionType.equals(BasicConstants.TYPE_PINBAN)){
            if( jinpinOneLinePOJO.heavyOrNot.equals("重型")){
                return HEAVY_PINBAN;
            }else{
                return MIDDLE_PINBAN;
            }
        }
        return OTHER;
    }

    private void readBrandInfo(List<JinpinOneLinePOJO> jinpinOneList){
        for( JinpinOneLinePOJO jinpinOneLinePOJO: jinpinOneList){
            brandMap.put( jinpinOneLinePOJO.subBrand, new BrandInfo(jinpinOneLinePOJO.brand, jinpinOneLinePOJO.grade,jinpinOneLinePOJO.subBrand ) );
        }
    }

    class SaleNumbers{
        Map<String, Integer> saleNumberPerType = new HashMap<>();
        int totalSaleNumber = 0;
    }


   class BrandInfo{
       String brand;
       String grade; //档次
       String subBrand;

       public BrandInfo(String brand, String grade, String subBrand){
           this.brand = brand;
           this.grade = grade;
           this.subBrand = subBrand;
       }
   }

}
