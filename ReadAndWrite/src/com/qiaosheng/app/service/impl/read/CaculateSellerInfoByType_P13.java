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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *    不是所有的经销商都是形象店！所以，形象店级别可以是空！
 *
 */
@Component
public class CaculateSellerInfoByType_P13 implements IBasicServiceInterface {
    private static final Logger log = LoggerFactory.getLogger(CaculateSellerInfoByType_P13.class);

    private static List<String> outputTitleSequence = new ArrayList<>();

    static{
        outputTitleSequence.add(BasicConstants.SPECIAL_SELL_TYPE_A);
        outputTitleSequence.add(BasicConstants.SPECIAL_SELL_TYPE_B);
        outputTitleSequence.add(BasicConstants.SPECIAL_SELL_TYPE_C);
        outputTitleSequence.add(BasicConstants.SPECIAL_SELL_TYPE_D);
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
    }

    //String is Type as above. Value is           销售情况per每个地区每年
    private void readJinpinList( List<JinpinOneLinePOJO> jinpinOneList){
        //Seller+daqu: specialLevel Per year.
        Set<SellSpecialInfo> specialSellersSet = new HashSet<>();

        for( JinpinOneLinePOJO jinpinOneLinePOJO : jinpinOneList ){
            SellSpecialInfo sellSpecialInfo = new SellSpecialInfo( jinpinOneLinePOJO.sellerName, jinpinOneLinePOJO.quYu);
            for( SellSpecialInfo existed : specialSellersSet){
                if( existed.equals(sellSpecialInfo)){
                    sellSpecialInfo = existed;
                }
            }
            sellSpecialInfo.levelPerYearMap.put( jinpinOneLinePOJO.year, jinpinOneLinePOJO.sellSpecialLevel);
            sellSpecialInfo.globalPriorityPerYearMap.put(jinpinOneLinePOJO.year, jinpinOneLinePOJO.globalNetworkTypePriority);
            specialSellersSet.add(sellSpecialInfo);
            if(!( jinpinOneLinePOJO.sellSpecialLevel.equals(BasicConstants.SPECIAL_SELL_TYPE_A) ||jinpinOneLinePOJO.sellSpecialLevel.equals(BasicConstants.SPECIAL_SELL_TYPE_B) ||jinpinOneLinePOJO.sellSpecialLevel.equals(BasicConstants.SPECIAL_SELL_TYPE_C) ||jinpinOneLinePOJO.sellSpecialLevel.equals(BasicConstants.SPECIAL_SELL_TYPE_D)||jinpinOneLinePOJO.sellSpecialLevel.equals(BasicConstants.SPECIAL_SELL_TYPE_Empty))){
                log.warn("Unknown special level {}, ", jinpinOneLinePOJO);
                jinpinOneLinePOJO.sellSpecialLevel = BasicConstants.SPECIAL_SELL_TYPE_Empty;
            }
        }
        //上面获得了 每个经销商每年的形象店级别，下面开始统计
        //Keys of below all three are :Quyu.
        Map<String, Map<Integer, Map<String, Integer>>> quyuAllSellerNumberPer = new HashMap<>();
        Map<String, Map<Integer, Integer>> quyuAllSellerNumberPerYear = new HashMap<>();
        Map<String, Map<Integer, Integer>> quyuAllSpecialSellerNumberPerYear = new HashMap<>();


        for(SellSpecialInfo sellSpecialInfo : specialSellersSet  ){
            String quYu = sellSpecialInfo.quYu;

            //for output
            Map<Integer, Map<String, Integer>> sellerNumberPerLevelPerYear =  quyuAllSellerNumberPer.get(quYu);
            if( sellerNumberPerLevelPerYear == null ){
                sellerNumberPerLevelPerYear = new HashMap<>();
                quyuAllSellerNumberPer.put(quYu, sellerNumberPerLevelPerYear);
            }
            Map<Integer, Integer> quYuAllSellerNumberMap = quyuAllSellerNumberPerYear.get(quYu);
            if( quYuAllSellerNumberMap==null ){
                quYuAllSellerNumberMap = new HashMap<>();
                quyuAllSellerNumberPerYear.put(quYu, quYuAllSellerNumberMap);
            }
            Map<Integer, Integer> quYuSpecialSellerNumberMap = quyuAllSpecialSellerNumberPerYear.get(quYu);
            if( quYuSpecialSellerNumberMap==null ){
                quYuSpecialSellerNumberMap = new HashMap<>();
                quyuAllSpecialSellerNumberPerYear.put(quYu, quYuSpecialSellerNumberMap);
            }

            for( int year=2012; year<=2018; year++ ){
                //input
                //If null, the seller is not exist in this year! so, jump out to next.
                if( sellSpecialInfo.levelPerYearMap.get(year)==null ){
                    continue;
                }
                String level = sellSpecialInfo.levelPerYearMap.get(year) ;

                //prepare for output
                Map<String, Integer> sellerNumberPerLevel = sellerNumberPerLevelPerYear.get(year);
                if( sellerNumberPerLevel == null ){
                    sellerNumberPerLevel = new HashMap<>();
                    sellerNumberPerLevelPerYear.put( year, sellerNumberPerLevel);
                }
                int totalNumberPerLevel = sellerNumberPerLevel.get(level)==null?0:sellerNumberPerLevel.get(level);
                sellerNumberPerLevel.put(level, totalNumberPerLevel+1);

                // sum 形象店
                if( !level.equals(BasicConstants.SPECIAL_SELL_TYPE_Empty)){
                    // 非形象店
                    int typeSummary =   quYuSpecialSellerNumberMap.get(year)==null?0:quYuSpecialSellerNumberMap.get(year);
                    quYuSpecialSellerNumberMap.put( year, typeSummary+1);
                }
                //sum all
                int totalSellerNumber = quYuAllSellerNumberMap.get(year)==null?0:quYuAllSellerNumberMap.get(year);
                quYuAllSellerNumberMap.put(year, totalSellerNumber+1);
            }
        }
        //finish all the calculation
        //=================Output=============
        Map<Integer, StringBuffer> outputLines = new HashMap<>();

        int lineIndex = 0;
        for( QuyuBasicInfo quyuBasicInfo: BasicCommonValues.quYuBasicInfoListWithOrder){
            String warQu = quyuBasicInfo.getWarQu();
            String quYuName = quyuBasicInfo.getQuYuName();
            StringBuffer sb = new StringBuffer(warQu).append(BasicConstants.TAB_WORD_BREAK).append(quYuName).append(BasicConstants.TAB_WORD_BREAK);

            //Prepare the input
            Map<Integer,Integer> totalSellerNumberPerYear = quyuAllSellerNumberPerYear.get(quYuName);
            Map<Integer,Integer> totalSpecailSellerNumberPerYear = quyuAllSpecialSellerNumberPerYear.get(quYuName);
            Map<Integer, Map<String, Integer>> quyuSellerNumberPerTypePerYear =  quyuAllSellerNumberPer.get(quYuName);
            if( totalSellerNumberPerYear==null ){
                totalSellerNumberPerYear = new HashMap<>();
            }
            if( totalSpecailSellerNumberPerYear==null ){
                totalSpecailSellerNumberPerYear = new HashMap<>();
            }
            if( quyuSellerNumberPerTypePerYear==null ){
                quyuSellerNumberPerTypePerYear = new HashMap<>();
            }


            for( int i=2012;i<=2018; i++){

                int totalSellerNumber = totalSellerNumberPerYear.get(i)==null?0:totalSellerNumberPerYear.get(i);
                sb.append( totalSellerNumber ).append(BasicConstants.TAB_WORD_BREAK);

                Map<String, Integer> sellerNumberPerType = quyuSellerNumberPerTypePerYear.get(i);
                if( sellerNumberPerType==null){
                    sellerNumberPerType = new HashMap<>();
                }

                for( String type : outputTitleSequence ){
                    sb.append( sellerNumberPerType.get(type)==null?0:sellerNumberPerType.get(type)).append(BasicConstants.TAB_WORD_BREAK);
                }

                double totalSpecialNumber = (double)  (totalSpecailSellerNumberPerYear.get(i)==null?0:totalSpecailSellerNumberPerYear.get(i));
                sb.append( totalSpecialNumber ).append(BasicConstants.TAB_WORD_BREAK);
                double specialPercentage = totalSellerNumber<=0?0: totalSpecialNumber / (double) totalSellerNumber;
                sb.append( BasicConstants.DecimalFormat.format(specialPercentage)).append(BasicConstants.TAB_WORD_BREAK);
            }
//            sb.append(BasicConstants.ENTER_LINE_BREAK);
            outputLines.put(lineIndex, sb );
            lineIndex ++;
        }

        OutputToTxtUtil.writeTo( outputLines, BasicConfiguredValues.PAGE13_OUTPUT_DIRECTORY+"各区域形象店占比情况.txt" );

    }






    class SellSpecialInfo {
        public String sellerName;

        public String quYu;

        //Key is Year. Value is the levelType
        public Map<Integer, String> levelPerYearMap = new HashMap<>();
        // key is year. value is the global priority per year.
        public Map<Integer, String> globalPriorityPerYearMap = new HashMap<>();

        public SellSpecialInfo(String sellerName, String quYu) {
            this.sellerName = sellerName;
            this.quYu = quYu;
        }

        public int hashCode(){
            return (sellerName + quYu).hashCode();
        }

        public boolean equals(Object obj ){
            if( obj instanceof  SellSpecialInfo ){
                SellSpecialInfo ano = (SellSpecialInfo) obj;
                if(ano.sellerName.equals( this.sellerName )){
                    return true;
                }
            }
            return false;
        }

        public String toString(){
            return sellerName  + quYu;
        }


    }
}
