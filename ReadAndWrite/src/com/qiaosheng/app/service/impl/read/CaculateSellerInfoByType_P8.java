package com.qiaosheng.app.service.impl.read;

import com.qiaosheng.app.service.IBasicServiceInterface;
import com.qiaosheng.common.pojo.model.JinpinOneLinePOJO;
import com.qiaosheng.common.utils.BasicCommonValues;
import com.qiaosheng.common.utils.BasicConfiguredValues;
import com.qiaosheng.common.utils.BasicConstants;
import com.qiaosheng.common.utils.OutputToTxtUtil;
import com.qiaosheng.common.utils.QuyuBasicInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1, get all SellerInfo at first.
 *
 *
 */
@Component
public class CaculateSellerInfoByType_P8 implements IBasicServiceInterface {
    private static final Logger log = LoggerFactory.getLogger(CaculateSellerInfoByType_P8.class);
    // key is functionType, such as "公路车网络， 工程车网" or "牵引车    平板车      自卸车
    //Key is SellerName;  Value is  销售情况perFunctionPeryear.
    Map<String, SaleStageBasicInfo> outputSellStageInfoPerType = new HashMap<>();

    //Key is Type: network, function,and all
    Map<String, SaleStagePerType> outputMap = new HashMap<>();



    public void run(){
        try {
            generateText(ReadJinpinOneLineFromSource.readJinpinInputData);
        } catch (Exception e) {
            log.error("Failed to generate report.", e);
        }
    }

    public void generateText(List<JinpinOneLinePOJO> jinpinOneList) {
        Map<String, SaleStagePerType> saleStageResult =  readSubValues( jinpinOneList) ;
        for( Map.Entry<String, SaleStagePerType> entry : saleStageResult.entrySet()){
            String fileName = BasicConfiguredValues.PAGE8And8_OUTPUT_DIRECTORY + entry.getKey() + "_各区域渠道销量台阶.txt";
            OutputToTxtUtil.writeTo( generateTxt(entry.getValue()) ,fileName);
        }
    }

    private Map<Integer, StringBuffer> generateTxt(SaleStagePerType saleStageResult) {
        Map<Integer, StringBuffer> outputLines = new HashMap<>();

        Map<String, Map<Integer,Map<String,Integer>>> input = saleStageResult.saleOutputMap;

        int lineIndex = 0;
        for(QuyuBasicInfo quyuBasicInfo :BasicCommonValues.quYuBasicInfoListWithOrder){

            String quyuName = quyuBasicInfo.getQuYuName();

            StringBuffer sb = new StringBuffer(quyuBasicInfo.getWarQu());
            sb.append(BasicConstants.TAB_WORD_BREAK).append(quyuName).append(BasicConstants.TAB_WORD_BREAK);

            Map<Integer,Map<String, Integer>> oneLineForQuyu = input.get( quyuName );
            if( oneLineForQuyu==null )
                oneLineForQuyu = new HashMap<>();
            for( int i=2012; i<=2018; i++ ){
                Map<String, Integer> oneYear = oneLineForQuyu.get(i);
                if( oneYear == null )
                    oneYear = new HashMap<>();
                sb.append(oneYear.get(SaleStage.Over2000)==null?0:oneYear.get(SaleStage.Over2000)).append(BasicConstants.TAB_WORD_BREAK);
                sb.append(oneYear.get(SaleStage.Over1000)==null?0:oneYear.get(SaleStage.Over1000)).append(BasicConstants.TAB_WORD_BREAK);
                sb.append(oneYear.get(SaleStage.Over800)==null?0:oneYear.get(SaleStage.Over800)).append(BasicConstants.TAB_WORD_BREAK);
                sb.append(oneYear.get(SaleStage.Over500)==null?0:oneYear.get(SaleStage.Over500)).append(BasicConstants.TAB_WORD_BREAK);
                sb.append(oneYear.get(SaleStage.Over300)==null?0:oneYear.get(SaleStage.Over300)).append(BasicConstants.TAB_WORD_BREAK);
                sb.append(oneYear.get(SaleStage.Over100)==null?0:oneYear.get(SaleStage.Over100)).append(BasicConstants.TAB_WORD_BREAK);
                sb.append(oneYear.get(SaleStage.Less100)==null?0:oneYear.get(SaleStage.Less100)).append(BasicConstants.TAB_WORD_BREAK);
            }
//            sb.append(BasicConstants.ENTER_LINE_BREAK);
            outputLines.put( lineIndex, sb );
            lineIndex++;
        }
        return outputLines;
    }


    public   Map<String, SaleStagePerType> readSubValues(List<JinpinOneLinePOJO> jinpinOneList){
        outputSellStageInfoPerType = readByBothNetworkAndFunction(jinpinOneList);

        Map<String, SaleStagePerType> outputMap = new HashMap<>();



        for( SaleStageBasicInfo saleStageBasicInfo : outputSellStageInfoPerType.values() ){
            String quYuName = saleStageBasicInfo.quYuName;
            String sellerName = saleStageBasicInfo.sellerName;

            Map<String, Map<Integer, Integer>> saleNumberPerYearTypeOfFunction = saleStageBasicInfo.saleNumberPerYearTypeOfFunction;
            Map<String, Map<Integer, Integer>> saleNumberPerYearTypeOfNetwork = saleStageBasicInfo.saleNumberPerYearTypeOfNetwork;
            Map<Integer, Integer> saleNumberPerYear = saleStageBasicInfo.saleNumberPerYear;


            //For Output of       功能分类

            for( Map.Entry<String,Map<Integer, Integer>> inputItem : saleNumberPerYearTypeOfFunction.entrySet()){
                //Input is ready
                String functionType = inputItem.getKey();
                Map<Integer, Integer> saleNumberOfYear = inputItem.getValue();
                //key is year; value is totalSaleNumberMap;

                //Prepare for Output
                SaleStagePerType saleStagePerType = outputMap.get(functionType);
                if( saleStagePerType == null ){
                    saleStagePerType = new SaleStagePerType();
                    outputMap.put(functionType, saleStagePerType);
                }

                //Set the value for Output
                Map<Integer,Map<String,Integer>> yearStage = saleStagePerType.saleOutputMap.get( quYuName );
                if( yearStage == null){
                    yearStage = new HashMap<>();
                    saleStagePerType.saleOutputMap.put(quYuName, yearStage);
                }

                for( int i=2012; i<=2018; i++){
                    Map<String, Integer> stageNumberPerYear = yearStage.get(i);
                    if( stageNumberPerYear == null ){
                        stageNumberPerYear = new HashMap<>();
                        yearStage.put(i, stageNumberPerYear);
                    }
                    int saleNumber = saleNumberOfYear.get(i)==null?0:saleNumberOfYear.get(i);
                    String saleStage = SaleStage.getStage( saleNumber );
                    int sellerNumberOfStage =   stageNumberPerYear.get(i)==null?0:stageNumberPerYear.get(i);
                    stageNumberPerYear.put(saleStage, sellerNumberOfStage+1);
                }
            }

            //For Output of       Network分类
            for( Map.Entry<String,Map<Integer, Integer>> inputItem : saleNumberPerYearTypeOfNetwork.entrySet()){
                //Input is ready
                String functionType = inputItem.getKey();
                Map<Integer, Integer> saleNumberOfYear = inputItem.getValue();
                //key is year; value is totalSaleNumberMap;

                //Prepare for Output
                SaleStagePerType saleStagePerType = outputMap.get(functionType);
                if( saleStagePerType == null ){
                    saleStagePerType = new SaleStagePerType();
                    outputMap.put(functionType, saleStagePerType);
                }

                //Set the value for Output
                Map<Integer,Map<String,Integer>> yearStage = saleStagePerType.saleOutputMap.get( quYuName );
                if( yearStage == null){
                    yearStage = new HashMap<>();
                    saleStagePerType.saleOutputMap.put(quYuName, yearStage);
                }

                for( int i=2012; i<=2018; i++){
                    Map<String, Integer> stageNumberPerYear = yearStage.get(i);
                    if( stageNumberPerYear == null ){
                        stageNumberPerYear = new HashMap<>();
                        yearStage.put(i, stageNumberPerYear);
                    }
                    int saleNumber = saleNumberOfYear.get(i)==null?0:saleNumberOfYear.get(i);
                    String saleStage = SaleStage.getStage( saleNumber );
                    int sellerNumberOfStage =   stageNumberPerYear.get(i)==null?0:stageNumberPerYear.get(i);
                    stageNumberPerYear.put(saleStage, sellerNumberOfStage+1);
                }
            }

            //For Output of       Global
            for( Map.Entry<String,Map<Integer, Integer>> inputItem : saleNumberPerYearTypeOfNetwork.entrySet()){
                //Input is ready
                String type = BasicConstants.GLOBAL;
                Map<Integer, Integer> saleNumberOfYear = inputItem.getValue();

                //Prepare for Output
                SaleStagePerType saleStagePerType = outputMap.get(type);
                if( saleStagePerType == null ){
                    saleStagePerType = new SaleStagePerType();
                    outputMap.put(type, saleStagePerType);
                }

                //Set the value for Output
                Map<Integer,Map<String,Integer>> yearStage = saleStagePerType.saleOutputMap.get( quYuName );
                if( yearStage == null){
                    yearStage = new HashMap<>();
                    saleStagePerType.saleOutputMap.put(quYuName, yearStage);
                }

                for( int i=2012; i<=2018; i++){
                    Map<String, Integer> stageNumberPerYear = yearStage.get(i);
                    if( stageNumberPerYear == null ){
                        stageNumberPerYear = new HashMap<>();
                        yearStage.put(i, stageNumberPerYear);
                    }
                    int saleNumber = saleNumberOfYear.get(i)==null?0:saleNumberOfYear.get(i);
                    String saleStage = SaleStage.getStage( saleNumber );
                    int sellerNumberOfStage =   stageNumberPerYear.get(i)==null?0:stageNumberPerYear.get(i);
                    stageNumberPerYear.put(saleStage, sellerNumberOfStage+1);
                }
            }

        }
        return outputMap;
    }





    private Map<String, SaleStageBasicInfo> readByBothNetworkAndFunction(List<JinpinOneLinePOJO> jinpinOneList) {
        Map<String, SaleStageBasicInfo> typeSellInfo = new HashMap<>();

//        第一遍是为了读取： sellerName

        for(JinpinOneLinePOJO oneLine : jinpinOneList ){

            String networkType = oneLine.networkType;
            String functionType = oneLine.functionType;

            String sellerName = oneLine.sellerName;
            SaleStageBasicInfo saleStageBasicInfo = typeSellInfo.get(sellerName);
            if( saleStageBasicInfo == null ){
                saleStageBasicInfo = new SaleStageBasicInfo(oneLine.sellerName, oneLine.quYu);
                typeSellInfo.put(sellerName, saleStageBasicInfo);
            }
            Map<Integer, Integer> networkTypeSalePerYear = saleStageBasicInfo.saleNumberPerYearTypeOfNetwork.get(networkType);
            Map<Integer, Integer> functionTypeSalePerYear = saleStageBasicInfo.saleNumberPerYearTypeOfFunction.get(functionType);

            if( networkTypeSalePerYear== null ){
                networkTypeSalePerYear = new HashMap<>();
                saleStageBasicInfo.saleNumberPerYearTypeOfNetwork.put(networkType, networkTypeSalePerYear);
            }
            if( functionTypeSalePerYear== null ){
                functionTypeSalePerYear = new HashMap<>();
                saleStageBasicInfo.saleNumberPerYearTypeOfFunction.put(functionType, functionTypeSalePerYear);
            }


            int totalSaleNumberOfNetwork = networkTypeSalePerYear.get(oneLine.year)==null?0: networkTypeSalePerYear.get(oneLine.year);
            networkTypeSalePerYear.put(oneLine.year, totalSaleNumberOfNetwork+oneLine.saleNumber );

            int totalSaleNumberOffunction = functionTypeSalePerYear.get(oneLine.year)==null?0: functionTypeSalePerYear.get(oneLine.year);
            functionTypeSalePerYear.put(oneLine.year, totalSaleNumberOffunction+oneLine.saleNumber );

            int totalNumber = saleStageBasicInfo.saleNumberPerYear.get(oneLine.year)==null?0:saleStageBasicInfo.saleNumberPerYear.get(oneLine.year);
            saleStageBasicInfo.saleNumberPerYear.put(oneLine.year, totalNumber + oneLine.saleNumber);
        }
//        Second: calculate the SellerNumber, and average SellerNumber.
        return typeSellInfo;
    }


    class SaleStageBasicInfo{
        public String sellerName;
        public String quYuName;
        //Key is WangluoLeixing;
        public Map<String, Map<Integer, Integer>> saleNumberPerYearTypeOfNetwork = new HashMap<>();
        public Map<String, Map<Integer, Integer>> saleNumberPerYearTypeOfFunction = new HashMap<>();
        public Map<Integer, Integer> saleNumberPerYear = new HashMap<>();

        public SaleStageBasicInfo(String sellerName, String quYu) {
            this.sellerName = sellerName;
            this.quYuName = quYu;
        }
    }

    class SaleStagePerType{
        //including both Network , function and All.
        //key is Quyu
        public Map<String, Map<Integer,Map<String,Integer>>> saleOutputMap = new HashMap<>();

    }
    static class SaleStage{
        public static final String Over2000 = "2000台以上";
        public static final String Over1000 = "1000-2000台";
        public static final String Over800 = "800-1000台";
        public static final String Over500 = "500-800台";
        public static final String Over300 ="300-500台";
        public static final String Over100 = "1000-300台";
        public static final String Less100 = "100台以下";

        public static String getStage(Integer input ){
            if( input ==null)
                return Less100;

            if( input>2000){
                return Over2000;
            }else if( input>=1000){
                return Over1000;
            }else if( input>=800){
                return Over800;
            }else if( input>=500){
                return Over500;
            }else if( input>=300){
                return Over300;
            }else if( input>=100){
                return Over100;
            }else{
                return  Less100;
            }
        }

    }

}
