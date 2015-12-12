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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午5:03
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CaculateSellerInfoByType_P11 implements IBasicServiceInterface {
    private static final Logger log = LoggerFactory.getLogger(CaculateSellerInfoByType_P11.class);
    // key is functionType, such as "公路车网络， 工程车网" or "牵引车    平板车      自卸车
    //String is Type as above. Value is           销售情况per每个地区每年
    Map<String, SaleInfoPerYear> outputSaleInfoPerQuyu = new HashMap<>();

    Map<String, Map<Integer,Integer>> totalSalePerYearQuyu = new HashMap<>();
    Map<Integer,Integer> totalCountrySaleNumPerYear = new HashMap<>();

    private static List<String> outputTitleSequence = new ArrayList<>();

    //TODO:
    static{

        outputTitleSequence.add("牵引车");
        outputTitleSequence.add("平板车");
        outputTitleSequence.add("自卸车");
        outputTitleSequence.add("a");
        outputTitleSequence.add("b");
        outputTitleSequence.add("c");
        outputTitleSequence.add("d");

    }


    public void run(){
        try {
            generateText(ReadJinpinOneLineFromSource.readJinpinInputData);
        } catch (Exception e) {
            log.error("Failed to generate report.", e);
        }
    }

    public void generateText(List<JinpinOneLinePOJO> jinpinOneList) {
        readJinpinList( jinpinOneList );

        OutputToTxtUtil.writeTo(parseSaleNumberToLines(outputSaleInfoPerQuyu), BasicConfiguredValues.PAGE11_OUTPUT_DIRECTORY+"区域销量.txt");
        OutputToTxtUtil.writeTo( parseSalePercentageToLines(outputSaleInfoPerQuyu), BasicConfiguredValues.PAGE12_OUTPUT_DIRECTORY +"区域占有率.txt");

    }

    private Map<Integer, StringBuffer>parseSaleNumberToLines(Map<String, SaleInfoPerYear> outputSaleInfoPerQuyu) {

        Map<Integer, StringBuffer> lineOutputMap = new HashMap<>();

        int lineIndex = 0;
        for( QuyuBasicInfo quyuBasicInfo: BasicCommonValues.quYuBasicInfoListWithOrder){
            String warQu = quyuBasicInfo.getWarQu();
            String quYuName = quyuBasicInfo.getQuYuName();
            StringBuffer sb = new StringBuffer(warQu).append(BasicConstants.TAB_WORD_BREAK).append(quYuName).append(BasicConstants.TAB_WORD_BREAK);

            SaleInfoPerYear saleInfoPerYear = outputSaleInfoPerQuyu.get(quYuName);

            Map<Integer, Map<String, Integer>> saleNumberPerYear = saleInfoPerYear.saleNumberPerYear;


            Map<Integer,Integer> totalSalPerYear= new HashMap<>();
            for( int i=2012; i<=2018; i++){
                Map<String, Integer> saleNumberMap = saleNumberPerYear.get(i);
                if( saleNumberMap == null ){
                    saleNumberMap = new HashMap<>();
                }
                Integer saleNumber = null;
                int totalSumNumber = 0;
                for( String subType : outputTitleSequence ){
                    saleNumber = saleNumberMap.get(subType);
                    sb.append( saleNumber==null?0:  saleNumber).append(BasicConstants.TAB_WORD_BREAK);
                    totalSumNumber += ( saleNumber==null?0:  saleNumber);
                    totalSalPerYear.put( i, totalSumNumber/2);
                }
                sb.append(totalSumNumber/2).append(BasicConstants.TAB_WORD_BREAK);
                int totalSaleNum = totalCountrySaleNumPerYear.get(i)==null?0:totalCountrySaleNumPerYear.get(i);
                totalCountrySaleNumPerYear.put(i, totalSaleNum+ (totalSumNumber/2));
            }
            totalSalePerYearQuyu.put(quYuName,totalSalPerYear);

            lineOutputMap.put(lineIndex, sb);
            lineIndex ++;
        }
        return lineOutputMap;
    }

    private Map<Integer, StringBuffer>  parseSalePercentageToLines(Map<String, SaleInfoPerYear> outputSaleInfoPerQuyu) {
        Map<Integer, StringBuffer> lineOutputMap = new HashMap<>();

        int lineIndex = 0;
        for( QuyuBasicInfo quyuBasicInfo: BasicCommonValues.quYuBasicInfoListWithOrder){


            String warQu = quyuBasicInfo.getWarQu();
            String quYuName = quyuBasicInfo.getQuYuName();
            StringBuffer sb = new StringBuffer(warQu).append(BasicConstants.TAB_WORD_BREAK).append(quYuName).append(BasicConstants.TAB_WORD_BREAK);

            SaleInfoPerYear saleInfoPerYear = outputSaleInfoPerQuyu.get(quYuName);

            Map<Integer, Map<String, Double>> salePercentagePerYear = saleInfoPerYear.salePercentagePerYear;

            Map<Integer, Integer> totalSaleOfQuyu =  totalSalePerYearQuyu.get(quYuName);
            if(totalSaleOfQuyu==null){
                totalSaleOfQuyu = new HashMap<>();
            }

            for( int i=2012; i<=2018; i++){
                Map<String, Double> salePercentageMap = salePercentagePerYear.get(i);
                if( salePercentageMap == null ){
                    salePercentageMap = new HashMap<>();
                }
                Double salePercentage = null;
                for( String subType : outputTitleSequence ){
                    salePercentage = salePercentageMap.get(subType);
                    sb.append( salePercentage==null?0:  salePercentage).append(BasicConstants.TAB_WORD_BREAK);
                }


                double countrySaleNum = (double)  (totalCountrySaleNumPerYear.get(i)==null?0:totalCountrySaleNumPerYear.get(i));
                double saleNumOfYear = totalSaleOfQuyu.get(i)==null?0:totalSaleOfQuyu.get(i);
                double percentage =  countrySaleNum==0?0: (saleNumOfYear/countrySaleNum);

                sb.append(BasicConstants.DecimalFormat.format(percentage)).append(BasicConstants.TAB_WORD_BREAK);

            }
//            sb.append(BasicConstants.ENTER_LINE_BREAK);
            lineOutputMap.put(lineIndex, sb);
            lineIndex ++;
        }
        return lineOutputMap;
    }

    //String is Type as above. Value is           销售情况per每个地区每年
    private void readJinpinList( List<JinpinOneLinePOJO> jinpinOneList){
        //全国某类车的全部销量. key is Year;-->functionType
        Map<Integer, Map<String, Integer>> saleTotalNumberPerYearPerType = new HashMap<>();
//        Map<Integer, Map<String, Integer>> saleTotalNumberPerYearForPriority = new HashMap<>();

        for( JinpinOneLinePOJO jinpinOneLinePOJO : jinpinOneList ){
            SaleInfoPerYear saleInfoPerYear = outputSaleInfoPerQuyu.get( jinpinOneLinePOJO.quYu );
            if( saleInfoPerYear == null ){
                saleInfoPerYear = new SaleInfoPerYear();
                outputSaleInfoPerQuyu.put( jinpinOneLinePOJO.quYu, saleInfoPerYear);
            }


            Integer year = jinpinOneLinePOJO.year;
            String functionType = jinpinOneLinePOJO.functionType;
            //TODO:   确认到底是按照什么分类。
            String priority =  jinpinOneLinePOJO.globalNetworkTypePriority;
            if( year == null ){
                log.error("Find Null year. Set it as Default: 2015");
                year = 2015;
            }
            Map<String, Integer> saleNumberPerType = saleInfoPerYear.saleNumberPerYear.get( year );
            if( saleNumberPerType==null ){
                saleNumberPerType = new HashMap<>();
                saleInfoPerYear.saleNumberPerYear.put(year, saleNumberPerType);
            }

            int saleNumberOfOneYearOneFunctionType = saleNumberPerType.get(functionType )==null?0:saleNumberPerType.get( functionType );
            saleNumberPerType.put(functionType , saleNumberOfOneYearOneFunctionType+jinpinOneLinePOJO.saleNumber);

            int saleNumberOfOneYearOnePriority = saleNumberPerType.get( priority)==null?0:saleNumberPerType.get( priority );
            saleNumberPerType.put( priority, saleNumberOfOneYearOnePriority+jinpinOneLinePOJO.saleNumber);

            Map<String, Integer> totalSaleNumberPerType =  saleTotalNumberPerYearPerType.get(year);
            if( totalSaleNumberPerType == null ){
                totalSaleNumberPerType = new HashMap<>();
                saleTotalNumberPerYearPerType.put(year, totalSaleNumberPerType);
            }

            int totalSaleForOneFunction = totalSaleNumberPerType.get( functionType)==null?0:totalSaleNumberPerType.get( functionType);
            totalSaleNumberPerType.put(functionType, totalSaleForOneFunction + jinpinOneLinePOJO.saleNumber);
            int totalSaleForOnePriority = totalSaleNumberPerType.get( priority)==null?0:totalSaleNumberPerType.get( priority);
            totalSaleNumberPerType.put(priority, totalSaleForOnePriority + jinpinOneLinePOJO.saleNumber);
        }
        //上面获得了 每个区域每个分类的年销量 跟  每个分类的年销量。下面计算占有率

        for(Map.Entry<String, SaleInfoPerYear> item: outputSaleInfoPerQuyu.entrySet()){
            String quYu = item.getKey();
            SaleInfoPerYear saleInfoPerYear = item.getValue();

            for( int i=2012; i<=2018; i++ ){
                Map<String, Double> salePercentagePerType = saleInfoPerYear.salePercentagePerYear.get(i);
                if( salePercentagePerType==null){
                    salePercentagePerType = new HashMap<>();
                    saleInfoPerYear.salePercentagePerYear.put(i, salePercentagePerType);
                }

                Map<String, Integer> saleNumberPerType = saleInfoPerYear.saleNumberPerYear.get(i);
                if( saleNumberPerType==null ){
                    log.error("NO record for year {} while sum the total sale number", i );
                    saleNumberPerType = new HashMap<>();
                    saleInfoPerYear.saleNumberPerYear.put(i, saleNumberPerType);
                }

                // key: functionType; value totalSaleNumberMap.
                for(Map.Entry<String, Integer> saleNumberForTypeItem : saleNumberPerType.entrySet()){
                    String type = saleNumberForTypeItem.getKey();
                    double saleNumber =  (double ) saleNumberForTypeItem.getValue();

                    double totalSaleNumber = 0;
                    try {
                        totalSaleNumber = (double )saleTotalNumberPerYearPerType.get(i).get(type);
                    } catch (Exception e) {  }
                    String percentage = BasicConstants.DecimalFormat.format(saleNumber / totalSaleNumber);
                    salePercentagePerType.put(type, Double.parseDouble(percentage));
                }
            }
        }
    }



    public class SaleInfoPerYear {

        //all String key is "Integer" Year

        public Map<Integer, Map<String, Integer>> saleNumberPerYear = new HashMap<>();

        public Map<Integer, Map<String, Double>> salePercentagePerYear = new HashMap<>();


    }
}
