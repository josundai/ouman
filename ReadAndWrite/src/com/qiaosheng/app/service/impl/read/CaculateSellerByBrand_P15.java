package com.qiaosheng.app.service.impl.read;

import com.qiaosheng.app.service.IBasicServiceInterface;
import com.qiaosheng.common.pojo.model.JinpinOneLinePOJO;
import com.qiaosheng.common.utils.BasicCommonValues;
import com.qiaosheng.common.utils.BasicConfiguredValues;
import com.qiaosheng.common.utils.BasicConstants;
import com.qiaosheng.common.utils.CityBasicInfoPerYear;
import com.qiaosheng.common.utils.CityPriority;
import com.qiaosheng.common.utils.OutputToTxtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.qiaosheng.common.utils.BasicConstants.*;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午5:03
 * For page 13 and 14.
 */
@Component
public class CaculateSellerByBrand_P15 implements IBasicServiceInterface {
    private static final Logger log = LoggerFactory.getLogger(CaculateSellerByBrand_P15.class);

    private static List<String> outputTitleSequence = new ArrayList<>();

    //  private Map<CalculatedKey, CalculatedOutputItem> outputCalculatedMap = new HashMap<>();
    // key is rotTitle. sub key is "a,b,c,d"
    Map<String, Map<String,CalculatedOutputItem>> outputObjectMap = new HashMap<>();


    static  List<String> rowTitleList = new ArrayList<>();
    private static  String ROAD_QIANYIN_OUMAN ="公路车网\t    牵引\t  欧曼  ";
    private static  String ROAD_QIANYIN_DONGFENG ="公路车网\t    牵引\t  东风本部  ";
    private static  String ROAD_QIANYIN_JIEFANG ="公路车网\t    牵引\t  解放本部  ";
    private static  String ROAD_QIANYIN_OTHER ="公路车网\t    牵引\t  其他品牌    ";

    private static  String ROAD_PINBAN_OUMAN ="公路车网\t    平板\t  欧曼  ";
    private static  String ROAD_PINBAN_DONGFENG  ="公路车网\t    平板\t  东风本部  ";
    private static  String ROAD_PINBAN_JIEFANG = "公路车网\t    平板\t  解放本部  ";
    private static  String ROAD_PINBAN_OTHER ="公路车网\t    平板\t  其他品牌    ";

    private static  String MACHINE_ZIXIE_OUMAN =  "工程车网\t    自卸\t  欧曼  ";
    private static  String MACHINE_ZIXIE_DONGFENG =  "工程车网\t    自卸\t  东风本部  ";
    private static  String MACHINE_ZIXIE_JIEFANG =  "工程车网\t    自卸\t  解放本部  ";
    private static  String MACHINE_ZIXIE_OTHER =  "工程车网\t    自卸\t  其他品牌  ";

    static {
        rowTitleList.add( ROAD_QIANYIN_OUMAN );
        rowTitleList.add( ROAD_QIANYIN_DONGFENG );
        rowTitleList.add( ROAD_QIANYIN_JIEFANG );
        rowTitleList.add( ROAD_QIANYIN_OTHER );

        rowTitleList.add( ROAD_PINBAN_OUMAN );
        rowTitleList.add( ROAD_PINBAN_DONGFENG );
        rowTitleList.add( ROAD_PINBAN_JIEFANG );
        rowTitleList.add( ROAD_PINBAN_OTHER );

        rowTitleList.add( MACHINE_ZIXIE_OUMAN );
        rowTitleList.add( MACHINE_ZIXIE_DONGFENG );
        rowTitleList.add( MACHINE_ZIXIE_JIEFANG );
        rowTitleList.add( MACHINE_ZIXIE_OTHER );
    }





//    int qianyinSaleNumPerPriority = 0;
    Map<String, Integer> pinbanSaleNumPerPriority = new HashMap<>();
    Map<String, Integer> zixieSaleNumPerPriority = new HashMap<>();
    Map<String, Integer> qianyinSaleNumPerPriority = new HashMap<>();


    //各个优先级的城市数量de map
    Map<String, Integer> qianyinCityNumberPerPriority = new HashMap<>();
    Map<String, Integer> pinbanCityNumberPerPriority = new HashMap<>();
    Map<String, Integer> zixieCityNumberPerPriority = new HashMap<>();


    static{
        outputTitleSequence.add(SPECIAL_SELL_TYPE_A);
        outputTitleSequence.add(SPECIAL_SELL_TYPE_B);
        outputTitleSequence.add(SPECIAL_SELL_TYPE_C);
        outputTitleSequence.add(SPECIAL_SELL_TYPE_D);


    }


    public void run(){
        generateText(ReadJinpinOneLineFromSource.readJinpinInputData);
    }

    public void generateText(List<JinpinOneLinePOJO> jinpinOneList) {
        try {
            readJinpinList( jinpinOneList );
        } catch (Exception e) {
            log.error("Failed to generate report.", e);
        }
    }

    //String is Type as above. Value is           销售情况per每个地区每年
    private void readJinpinList( List<JinpinOneLinePOJO> jinpinOneList){

        readAndCalculate( jinpinOneList);

        OutputToTxtUtil.writeTo( convertToPage15(outputObjectMap), BasicConfiguredValues.PAGE15_OUTPUT_DIRECTORY + "覆盖度对比分析.txt");

        OutputToTxtUtil.writeTo( convertToPage16(outputObjectMap), BasicConfiguredValues.PAGE16_OUTPUT_DIRECTORY+"点均销量对比分析.txt");
    }



    private Map<Integer, StringBuffer> convertToPage15(Map<String, Map<String, CalculatedOutputItem>> outputObjectMap) {

        Map<Integer, StringBuffer> outputMap = new HashMap<>();

        int lineIndex = 0;
        for(String rowTile : rowTitleList ){
            StringBuffer sb = new StringBuffer( rowTile ).append(BasicConstants.TAB_WORD_BREAK);
            Map<String, CalculatedOutputItem> calculatedOutputItemMap = outputObjectMap.get(rowTile);
            if( calculatedOutputItemMap==null ){
                outputMap.put(lineIndex, sb );
                continue;
            }

            for( String priorityString : BasicConstants.priorityList ){
                CalculatedOutputItem calculatedOutputItem = calculatedOutputItemMap.get( priorityString);
                if( calculatedOutputItem == null ){
                    sb.append( 0 ).append(BasicConstants.TAB_WORD_BREAK).append( 0 ).append(BasicConstants.TAB_WORD_BREAK).append( 0 ).append(BasicConstants.TAB_WORD_BREAK).append( 0 ).append(BasicConstants.TAB_WORD_BREAK);
                    continue;
                }
                sb.append(calculatedOutputItem.totalCityNumber).append(BasicConstants.TAB_WORD_BREAK)
                        .append(calculatedOutputItem.coveredCityNumber).append(BasicConstants.TAB_WORD_BREAK)
                        .append(calculatedOutputItem.getSellerNumber()).append(BasicConstants.TAB_WORD_BREAK)
                        .append(calculatedOutputItem.cityPercentage).append(BasicConstants.TAB_WORD_BREAK);
            }
            outputMap.put(lineIndex, sb );
            lineIndex++;
        }
        return outputMap;
    }

    private Map<Integer, StringBuffer> convertToPage16(Map<String, Map<String, CalculatedOutputItem>> outputObjectMap) {

        Map<Integer, StringBuffer> outputMap = new HashMap<>();
        int lineIndex = 0;
        for(String rowTile : rowTitleList ){
            StringBuffer sb = new StringBuffer( rowTile ).append(BasicConstants.TAB_WORD_BREAK);
            Map<String, CalculatedOutputItem> calculatedOutputItemMap = outputObjectMap.get(rowTile);
            if( calculatedOutputItemMap==null ){
                outputMap.put(lineIndex, sb );
                continue;
            }

            for( String priorityString : BasicConstants.priorityList ){
                CalculatedOutputItem calculatedOutputItem = calculatedOutputItemMap.get( priorityString);
                if( calculatedOutputItem == null ){
                    sb.append( 0 ).append(BasicConstants.TAB_WORD_BREAK).append( 0 ).append(BasicConstants.TAB_WORD_BREAK).append( 0 ).append(BasicConstants.TAB_WORD_BREAK).append( 0 ).append(BasicConstants.TAB_WORD_BREAK);
                    continue;
                }
                sb.append(calculatedOutputItem.totalCityNumber).append(BasicConstants.TAB_WORD_BREAK)
                        .append(calculatedOutputItem.getSellerNumber()).append(BasicConstants.TAB_WORD_BREAK)
                        .append(calculatedOutputItem.totalSaleNumber_Mid).append(BasicConstants.TAB_WORD_BREAK)
                        .append(calculatedOutputItem.salePercentage).append(BasicConstants.TAB_WORD_BREAK)
                        .append(calculatedOutputItem.averageSaleNumber).append(BasicConstants.TAB_WORD_BREAK);
            }
            outputMap.put(lineIndex, sb );
            lineIndex++;
        }
        return outputMap;
    }


    private String getFunctionType( String matchedRow ){
        if (matchedRow.equals(ROAD_QIANYIN_OUMAN)|| matchedRow.equals(ROAD_QIANYIN_DONGFENG) || matchedRow.equals(ROAD_QIANYIN_JIEFANG) || (matchedRow.equals(ROAD_QIANYIN_OTHER)) ){
            return TYPE_QIANYIN;
        }
        if (matchedRow.equals(ROAD_PINBAN_OUMAN) || matchedRow.equals(ROAD_PINBAN_DONGFENG) ||     matchedRow.equals(ROAD_PINBAN_JIEFANG) ||  matchedRow.equals(ROAD_PINBAN_OTHER)) {
            return TYPE_PINBAN;
        }

        if (matchedRow.equals(MACHINE_ZIXIE_OUMAN) || matchedRow.equals(MACHINE_ZIXIE_DONGFENG) || matchedRow.equals(MACHINE_ZIXIE_JIEFANG) || matchedRow.equals(MACHINE_ZIXIE_OTHER)) {
            return TYPE_ZIXIE;
        }
        return TYPE_QIANYIN;
    }




    private void readAndCalculate( List<JinpinOneLinePOJO> jinpinOneList){
        initCityNumberMap();


        Map<String, SellerAllBandInfo> result = new HashMap<>();
        for( JinpinOneLinePOJO jinpinOneLinePOJO : jinpinOneList ){
            if( jinpinOneLinePOJO.year!= 2015){
                continue;
            }

            SellerAllBandInfo sellerAllBandInfo = result.get(jinpinOneLinePOJO.getSellerName());
            if( sellerAllBandInfo == null){
                sellerAllBandInfo = new SellerAllBandInfo(jinpinOneLinePOJO.sellerName, jinpinOneLinePOJO.city);
                result.put(jinpinOneLinePOJO.sellerName, sellerAllBandInfo);
            }

            sellerAllBandInfo.priorityPerNetwork.put( NETWORKTYPE_MACHINE,  jinpinOneLinePOJO.machineNetworkTypePriority);
            sellerAllBandInfo.priorityPerNetwork.put( NETWORKTYPE_ROAD, jinpinOneLinePOJO.roadNetworkTypePriority );

            sellerAllBandInfo.priorityPerFunction.put(TYPE_ZIXIE, jinpinOneLinePOJO.zixieTypePriority);
            sellerAllBandInfo.priorityPerFunction.put(TYPE_QIANYIN, jinpinOneLinePOJO.qianyinTypePriority);
            sellerAllBandInfo.priorityPerFunction.put(TYPE_PINBAN, jinpinOneLinePOJO.pinbanTypePriority);

            switch(jinpinOneLinePOJO.brand ){
                case BRAND_OUMAN:
                    sellerAllBandInfo.isOuman = true;
                    break;
                case BRAND_JIEFANG:
                    sellerAllBandInfo.isJiefang = true;
                    break;
                case BRAND_DONGFENG:
                    sellerAllBandInfo.isDongfeng = true;
                    break;
                default:
                    sellerAllBandInfo.isOthers=true;
                    break;
            }
            switch( jinpinOneLinePOJO.functionType ){
                case TYPE_PINBAN:
                    sellerAllBandInfo.isPinban=true;
                    break;
                case TYPE_ZIXIE:
                    sellerAllBandInfo.isZixie= true;
                    break;
                case TYPE_QIANYIN:
                    sellerAllBandInfo.isQianyin=true;
                    break;
            }
            switch( jinpinOneLinePOJO.networkType ){
                case NETWORKTYPE_MACHINE:
                    sellerAllBandInfo.isMachineNetwork=true;
                    break;
                case NETWORKTYPE_ROAD:
                    sellerAllBandInfo.isRoadNetwork= true;
                    break;
            }
            Map<String, Integer> saleNumberMap = sellerAllBandInfo.saleNumberPerTypes;
            if( sellerAllBandInfo.isRoadNetwork && sellerAllBandInfo.isQianyin && sellerAllBandInfo.isOuman ){
                saleNumberMap.put(ROAD_QIANYIN_OUMAN, jinpinOneLinePOJO.saleNumber);
                int qianyinTS = qianyinSaleNumPerPriority.get(jinpinOneLinePOJO.qianyinTypePriority)==null?0:qianyinSaleNumPerPriority.get(jinpinOneLinePOJO.qianyinTypePriority);
                qianyinSaleNumPerPriority.put(jinpinOneLinePOJO.qianyinTypePriority,  qianyinTS + jinpinOneLinePOJO.saleNumber);

            }
            if( sellerAllBandInfo.isRoadNetwork && sellerAllBandInfo.isQianyin && sellerAllBandInfo.isDongfeng ){

                saleNumberMap.put(ROAD_QIANYIN_DONGFENG, jinpinOneLinePOJO.saleNumber);
                int qianyinTS = qianyinSaleNumPerPriority.get(jinpinOneLinePOJO.qianyinTypePriority)==null?0:qianyinSaleNumPerPriority.get(jinpinOneLinePOJO.qianyinTypePriority);
                qianyinSaleNumPerPriority.put(jinpinOneLinePOJO.qianyinTypePriority,  qianyinTS + jinpinOneLinePOJO.saleNumber);
            }
            if( sellerAllBandInfo.isRoadNetwork && sellerAllBandInfo.isQianyin && sellerAllBandInfo.isJiefang ){
                saleNumberMap.put(ROAD_QIANYIN_JIEFANG, jinpinOneLinePOJO.saleNumber);
                int qianyinTS = qianyinSaleNumPerPriority.get(jinpinOneLinePOJO.qianyinTypePriority)==null?0:qianyinSaleNumPerPriority.get(jinpinOneLinePOJO.qianyinTypePriority);
                qianyinSaleNumPerPriority.put(jinpinOneLinePOJO.qianyinTypePriority,  qianyinTS + jinpinOneLinePOJO.saleNumber);
            }
            if( sellerAllBandInfo.isRoadNetwork && sellerAllBandInfo.isQianyin && sellerAllBandInfo.isOthers ){
                saleNumberMap.put(ROAD_QIANYIN_OTHER, jinpinOneLinePOJO.saleNumber);
                int qianyinTS = qianyinSaleNumPerPriority.get(jinpinOneLinePOJO.qianyinTypePriority)==null?0:qianyinSaleNumPerPriority.get(jinpinOneLinePOJO.qianyinTypePriority);
                qianyinSaleNumPerPriority.put(jinpinOneLinePOJO.qianyinTypePriority,  qianyinTS + jinpinOneLinePOJO.saleNumber);
            }

            //------ Road, pinban + 4 brands
            if( sellerAllBandInfo.isRoadNetwork && sellerAllBandInfo.isPinban && sellerAllBandInfo.isOuman ){
                saleNumberMap.put(ROAD_PINBAN_OUMAN, jinpinOneLinePOJO.saleNumber);

                int pinbanTs = pinbanSaleNumPerPriority.get(jinpinOneLinePOJO.pinbanTypePriority)==null?0:pinbanSaleNumPerPriority.get(jinpinOneLinePOJO.pinbanTypePriority);
                pinbanSaleNumPerPriority.put(jinpinOneLinePOJO.pinbanTypePriority,  pinbanTs + jinpinOneLinePOJO.saleNumber);
            }
            if( sellerAllBandInfo.isRoadNetwork && sellerAllBandInfo.isPinban && sellerAllBandInfo.isDongfeng ){
                saleNumberMap.put(ROAD_PINBAN_DONGFENG, jinpinOneLinePOJO.saleNumber);
                int pinbanTs = pinbanSaleNumPerPriority.get(jinpinOneLinePOJO.pinbanTypePriority)==null?0:pinbanSaleNumPerPriority.get(jinpinOneLinePOJO.pinbanTypePriority);
                pinbanSaleNumPerPriority.put(jinpinOneLinePOJO.pinbanTypePriority,  pinbanTs + jinpinOneLinePOJO.saleNumber);
            }
            if( sellerAllBandInfo.isRoadNetwork && sellerAllBandInfo.isPinban && sellerAllBandInfo.isJiefang ){
                saleNumberMap.put(ROAD_PINBAN_JIEFANG, jinpinOneLinePOJO.saleNumber);
                int pinbanTs = pinbanSaleNumPerPriority.get(jinpinOneLinePOJO.pinbanTypePriority)==null?0:pinbanSaleNumPerPriority.get(jinpinOneLinePOJO.pinbanTypePriority);
                pinbanSaleNumPerPriority.put(jinpinOneLinePOJO.pinbanTypePriority,  pinbanTs + jinpinOneLinePOJO.saleNumber);
            }
            if( sellerAllBandInfo.isRoadNetwork && sellerAllBandInfo.isPinban && sellerAllBandInfo.isOthers ){
                saleNumberMap.put(ROAD_PINBAN_OTHER, jinpinOneLinePOJO.saleNumber);
                int pinbanTs = pinbanSaleNumPerPriority.get(jinpinOneLinePOJO.pinbanTypePriority)==null?0:pinbanSaleNumPerPriority.get(jinpinOneLinePOJO.pinbanTypePriority);
                pinbanSaleNumPerPriority.put(jinpinOneLinePOJO.pinbanTypePriority,  pinbanTs + jinpinOneLinePOJO.saleNumber);
            }
            //---------- machine zixie + 4 brands
            if( sellerAllBandInfo.isMachineNetwork && sellerAllBandInfo.isZixie && sellerAllBandInfo.isOuman ){
                saleNumberMap.put(MACHINE_ZIXIE_OUMAN, jinpinOneLinePOJO.saleNumber);
                int zixieTs = zixieSaleNumPerPriority.get(jinpinOneLinePOJO.zixieTypePriority)==null?0:zixieSaleNumPerPriority.get(jinpinOneLinePOJO.zixieTypePriority);
                zixieSaleNumPerPriority.put(jinpinOneLinePOJO.zixieTypePriority,  zixieTs + jinpinOneLinePOJO.saleNumber);
            }
            if( sellerAllBandInfo.isMachineNetwork && sellerAllBandInfo.isZixie && sellerAllBandInfo.isDongfeng ){
                saleNumberMap.put(MACHINE_ZIXIE_DONGFENG, jinpinOneLinePOJO.saleNumber);
                int zixieTs = zixieSaleNumPerPriority.get(jinpinOneLinePOJO.zixieTypePriority)==null?0:zixieSaleNumPerPriority.get(jinpinOneLinePOJO.zixieTypePriority);
                zixieSaleNumPerPriority.put(jinpinOneLinePOJO.zixieTypePriority,  zixieTs + jinpinOneLinePOJO.saleNumber);
            }
            if( sellerAllBandInfo.isMachineNetwork && sellerAllBandInfo.isZixie && sellerAllBandInfo.isJiefang ){
                saleNumberMap.put(MACHINE_ZIXIE_JIEFANG, jinpinOneLinePOJO.saleNumber);
                int zixieTs = zixieSaleNumPerPriority.get(jinpinOneLinePOJO.zixieTypePriority)==null?0:zixieSaleNumPerPriority.get(jinpinOneLinePOJO.zixieTypePriority);
                zixieSaleNumPerPriority.put(jinpinOneLinePOJO.zixieTypePriority,  zixieTs + jinpinOneLinePOJO.saleNumber);
            }
            if( sellerAllBandInfo.isMachineNetwork && sellerAllBandInfo.isZixie && sellerAllBandInfo.isOthers ){
                saleNumberMap.put(MACHINE_ZIXIE_OTHER, jinpinOneLinePOJO.saleNumber);
                int zixieTs = zixieSaleNumPerPriority.get(jinpinOneLinePOJO.zixieTypePriority)==null?0:zixieSaleNumPerPriority.get(jinpinOneLinePOJO.zixieTypePriority);
                zixieSaleNumPerPriority.put(jinpinOneLinePOJO.zixieTypePriority,  zixieTs + jinpinOneLinePOJO.saleNumber);
            }
            result.put( jinpinOneLinePOJO.sellerName, sellerAllBandInfo);
        }

        //------------------------------------ Set the citNumber----
        for(Map.Entry<String, SellerAllBandInfo> sellerItem : result.entrySet()){
            //input is ready
            String sellerName = sellerItem.getKey();
            SellerAllBandInfo sellerAllBandInfo = sellerItem.getValue();

            //each city may match several rows! Need multiple types.
            Set<String> matchedRowSet = new HashSet<>();
            if( sellerAllBandInfo.isRoadNetwork && sellerAllBandInfo.isQianyin && sellerAllBandInfo.isOuman ){
                matchedRowSet.add(ROAD_QIANYIN_OUMAN);
            }
            if( sellerAllBandInfo.isRoadNetwork && sellerAllBandInfo.isQianyin && sellerAllBandInfo.isDongfeng ){
                matchedRowSet.add(ROAD_QIANYIN_DONGFENG);
            }
            if( sellerAllBandInfo.isRoadNetwork && sellerAllBandInfo.isQianyin && sellerAllBandInfo.isJiefang ){
                matchedRowSet.add(ROAD_QIANYIN_JIEFANG);
            }
            if( sellerAllBandInfo.isRoadNetwork && sellerAllBandInfo.isQianyin && sellerAllBandInfo.isOthers ){
                matchedRowSet.add(ROAD_QIANYIN_OTHER);
            }
             //-----------
            if( sellerAllBandInfo.isRoadNetwork && sellerAllBandInfo.isPinban && sellerAllBandInfo.isOuman ){
                matchedRowSet.add(ROAD_PINBAN_OUMAN);
            }
            if( sellerAllBandInfo.isRoadNetwork && sellerAllBandInfo.isPinban && sellerAllBandInfo.isDongfeng ){
                matchedRowSet.add(ROAD_PINBAN_DONGFENG);
            }
            if( sellerAllBandInfo.isRoadNetwork && sellerAllBandInfo.isPinban && sellerAllBandInfo.isJiefang ){
                matchedRowSet.add(ROAD_PINBAN_JIEFANG);
            }
            if( sellerAllBandInfo.isRoadNetwork && sellerAllBandInfo.isPinban && sellerAllBandInfo.isOthers ){
                matchedRowSet.add(ROAD_PINBAN_OTHER);
            }
            //----------------

            if( sellerAllBandInfo.isMachineNetwork && sellerAllBandInfo.isZixie && sellerAllBandInfo.isOuman ){
                matchedRowSet.add(MACHINE_ZIXIE_OUMAN);
            }
            if( sellerAllBandInfo.isMachineNetwork && sellerAllBandInfo.isZixie && sellerAllBandInfo.isDongfeng ){
                matchedRowSet.add(MACHINE_ZIXIE_DONGFENG);
            }
            if( sellerAllBandInfo.isMachineNetwork && sellerAllBandInfo.isZixie && sellerAllBandInfo.isJiefang ){
                matchedRowSet.add(MACHINE_ZIXIE_JIEFANG);
            }
            if( sellerAllBandInfo.isMachineNetwork && sellerAllBandInfo.isZixie && sellerAllBandInfo.isOthers ){
                matchedRowSet.add(MACHINE_ZIXIE_OTHER);
            }

            String cityName = sellerAllBandInfo.cityName;
            CityBasicInfoPerYear cityBasicInfo = BasicCommonValues.cityBasicInfoMap.get(cityName);
            if( cityBasicInfo==null){
                log.error("Cannot run!!!");
                continue;
            }
            CityPriority cityPriority = cityBasicInfo.getYearPriority().get(2015);
            if( cityPriority == null){
                log.error("Cannot find city priority for 2015.");
                continue;
            }


            for( String matchedRow : matchedRowSet  ){

                //Output.. This key is a,b,c,d.
                Map<String, CalculatedOutputItem> calculateMap = outputObjectMap.get(matchedRow);
                if( calculateMap == null ){
                    calculateMap = new HashMap<>();
                    outputObjectMap.put( matchedRow, calculateMap);
                }

                String functionType = getFunctionType( matchedRow );
                String currentPriority = null;
                switch (functionType){
                    case TYPE_PINBAN:
                        currentPriority = cityPriority.getCityPriorityOfPinbanNetwork();
                        break;
                    case TYPE_QIANYIN:
                        currentPriority = cityPriority.getCityPriorityOfQianyinNetwork();
                        break;
                    case TYPE_ZIXIE:
                        currentPriority = cityPriority.getCityPriorityOfZixieNetwork();
                        break;
                }
                CalculatedOutputItem calculatedOutputItem = calculateMap.get(currentPriority);
                if( calculatedOutputItem == null ){
                    calculatedOutputItem = new CalculatedOutputItem();
                    calculateMap.put(currentPriority, calculatedOutputItem);
                }
                calculatedOutputItem.addCityName( sellerAllBandInfo.cityName );
                calculatedOutputItem.addSellerName( sellerAllBandInfo.sellerName );
                int saleNumber  = sellerAllBandInfo.saleNumberPerTypes.get(matchedRow)==null?0:sellerAllBandInfo.saleNumberPerTypes.get(matchedRow);
                calculatedOutputItem.totalSaleNumber_Mid  +=  saleNumber;
                calculatedOutputItem.coveredCityNumber +=1;
            }
        }

        //---------------- Finish the statstic. Begin to calculate the percentage.
        for( String rowTileKey : rowTitleList ){
            //input
            Map<String,CalculatedOutputItem> calculatedOutputMap = outputObjectMap.get(rowTileKey);
            if( calculatedOutputMap == null ){
                continue;
            }
            String functionType = getFunctionType( rowTileKey);
            for(String priorityString : BasicConstants.priorityList ){

                //Input
                CalculatedOutputItem detailItem = calculatedOutputMap.get( priorityString);
                if( detailItem ==null){
                    detailItem = new CalculatedOutputItem();
                    calculatedOutputMap.put(priorityString, detailItem);
                    continue;
                }
                double totalSaleNumber = 0;
                int totalCityNumber = 0;
                switch ( functionType ){
                    case TYPE_QIANYIN:
                        totalSaleNumber =(double) qianyinSaleNumPerPriority.get( priorityString);
                        totalCityNumber = qianyinCityNumberPerPriority.get(priorityString);
                        break;
                    case TYPE_ZIXIE:
                        totalSaleNumber =(double)zixieCityNumberPerPriority.get( priorityString);
                        totalCityNumber = zixieCityNumberPerPriority.get(priorityString);
                        break;
                    case TYPE_PINBAN:
                        totalSaleNumber =(double)pinbanCityNumberPerPriority.get( priorityString);
                        totalCityNumber = pinbanCityNumberPerPriority.get(priorityString);
                        break;
                }
                if(totalSaleNumber >0){
                    double salePercentage =   (double)detailItem.totalSaleNumber_Mid/ totalSaleNumber;
                    detailItem.salePercentage = Double.parseDouble(BasicConstants.DecimalFormat.format(salePercentage));
                }else{
                    detailItem.salePercentage = 0;
                }
                if( detailItem.getSellerNumber() !=0){
                    double averageSaleNumber =  (double)detailItem.totalSaleNumber_Mid / (double) detailItem.getSellerNumber();
                    detailItem.averageSaleNumber =  Double.parseDouble(BasicConstants.DecimalFormat.format(averageSaleNumber));
                }else{
                    detailItem.averageSaleNumber = 0;
                }

                detailItem.totalCityNumber = totalCityNumber;

                if( totalCityNumber== 0 ){
                    detailItem.cityPercentage = 0;
                }else{
                    double cityPercentage = (double) detailItem.coveredCityNumber / (double) totalCityNumber;
                    detailItem.cityPercentage =  Double.parseDouble(BasicConstants.DecimalFormat.format(cityPercentage));
                }

            }
        }
    }

    /*
    Map<String, Integer> qianyinCityNumberPerPriority = new HashMap<>();
    Map<String, Integer> pinbanCityNumberPerPriority = new HashMap<>();
    Map<String, Integer> zixieCityNumberPerPriority = new HashMap<>();
    */
    private void initCityNumberMap() {
        for(Map.Entry<String, CityBasicInfoPerYear> item: BasicCommonValues.cityBasicInfoMap.entrySet()){
            CityBasicInfoPerYear cityBasicInfo = item.getValue();

            CityPriority cityPriority = cityBasicInfo.getYearPriority().get(2015);
            if( cityPriority == null){
                continue;
            }
            String qianyinPriority = cityPriority.getCityPriorityOfQianyinNetwork();
            Integer totalCityNumber = qianyinCityNumberPerPriority.get(qianyinPriority);
            totalCityNumber = totalCityNumber==null?1:totalCityNumber+1;
            qianyinCityNumberPerPriority.put( qianyinPriority, totalCityNumber );

            String pinbanPriority = cityPriority.getCityPriorityOfPinbanNetwork();
            Integer totalCityNumberOfPinban = pinbanCityNumberPerPriority.get(pinbanPriority);
            totalCityNumberOfPinban = totalCityNumberOfPinban==null?1:totalCityNumberOfPinban+1;
            pinbanCityNumberPerPriority.put( pinbanPriority, totalCityNumberOfPinban );

            String zixiePriority = cityPriority.getCityPriorityOfZixieNetwork();
            Integer totalCityNumberOfZixie = zixieCityNumberPerPriority.get(zixiePriority);
            totalCityNumberOfZixie = totalCityNumberOfZixie==null?1:totalCityNumberOfZixie+1;
            zixieCityNumberPerPriority.put( zixiePriority, totalCityNumberOfZixie );
        }
    }


    class SellerAllBandInfo{
        public String sellerName;
        public String cityName;
        public int sellerNumber;
        //key is network, value is priority
        public Map<String, String> priorityPerNetwork = new HashMap<>();
        //key is function, value is priority
        public Map<String, String> priorityPerFunction = new HashMap<>();

        //the complicated key of brand_function_network
        public Map<String, Integer> saleNumberPerTypes = new HashMap<>();
        public boolean isOuman = false;
        public boolean isDongfeng = false;
        public boolean isJiefang = false;
        public boolean isOthers = false;

        public boolean isQianyin = false;
        public boolean isPinban = false;
        public boolean isZixie = false;

        public boolean isRoadNetwork = false;
        public boolean isMachineNetwork = false;




        public SellerAllBandInfo(String sellerName, String cityName){
            this.sellerName = sellerName;
            this.cityName = cityName;
        }
    }




    class CalculatedOutputItem{
        // 城市数量
        int totalCityNumber;
        //   覆盖城市数量
        private int coveredCityNumber;


        // 网络覆盖度
        double cityPercentage;
        //    total 销量
        int totalSaleNumber_Mid;
        //  销量
        int coveredSaleNumber;
        //   销量占比
        double salePercentage;
        //       点均销量
        double averageSaleNumber;

        Set<String> cityNameSet = new HashSet<>();
        Set<String> sellerNameSet = new HashSet<>();

        public void addCityName( String cityName ){
            cityNameSet.add( cityName );
            this.totalCityNumber = cityNameSet.size();
        }
        // 渠道数量
        public int getSellerNumber(){
            return sellerNameSet.size();
        }

        public void addSellerName(String sellerName ){
            sellerNameSet.add(sellerName);
        }

        public int getCoveredCityNumber(){
            return coveredCityNumber;
        }



        public int getTotalSaleNumber_Mid(){
            return totalSaleNumber_Mid;
        }
    }



    /*
    class CalculatedKey{
        public String functionType;
        public String networkType;
        public String brand;

        public CalculatedKey(String networkType, String functionType, String brand ) {
            this.functionType = functionType;
            this.brand = brand;
            this.networkType = networkType;
        }

        public int hashCode(){
            return (networkType+ functionType + brand).hashCode();
        }

        public boolean equals(Object obj ){
            if( obj instanceof  CalculatedKey ){
                CalculatedKey ano = (CalculatedKey) obj;
                if(ano.functionType.equals( this.functionType) && ano.brand.equals(this.brand) && this.networkType.equals(ano.networkType)){
                    return true;
                }
            }
            return false;
        }

        public String toString(){
            return networkType+"_"+ functionType +" _"  + brand;
        }
    }
    */


}
