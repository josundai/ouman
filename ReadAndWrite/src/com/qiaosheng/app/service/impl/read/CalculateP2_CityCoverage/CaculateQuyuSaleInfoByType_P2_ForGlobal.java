package com.qiaosheng.app.service.impl.read.CalculateP2_CityCoverage;

import com.qiaosheng.app.service.IBasicServiceInterface;
import com.qiaosheng.app.service.impl.read.ReadJinpinOneLineFromSource;
import com.qiaosheng.common.pojo.model.JinpinOneLinePOJO;
import com.qiaosheng.common.utils.BasicCommonValues;
import com.qiaosheng.common.utils.BasicConfiguredValues;
import com.qiaosheng.common.utils.BasicConstants;
import com.qiaosheng.common.utils.OutputToTxtUtil;
import com.qiaosheng.common.utils.QuyuBasicInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 覆盖区域其实是区域内的城市覆盖度。
 */

@Component

public class CaculateQuyuSaleInfoByType_P2_ForGlobal implements IBasicServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(CaculateQuyuSaleInfoByType_P2_ForGlobal.class);

    public void run() {
        try {
            generateText(ReadJinpinOneLineFromSource.readJinpinInputData);
        } catch (Exception e) {
            log.error("Failed to generate report.", e);
        }
    }

    public void generateText(List<JinpinOneLinePOJO> jinpinOneList) {
        Map<String, Map<Integer, Map<String, Integer>>> cityNumberResult = readByNetwork(jinpinOneList);

        Map<Integer, StringBuffer> outputStringBuffer = new HashMap<>();
        int lineIndex = 0;
        for( QuyuBasicInfo quyuBasicInfo : BasicCommonValues.quYuBasicInfoListWithOrder){
            String quyuName = quyuBasicInfo.getQuYuName();
            Map<Integer, Map<String, Integer>> cityNumberPerYearPri = cityNumberResult.get(quyuName);
            StringBuffer oneLine = new StringBuffer(quyuBasicInfo.getWarQu()).append(BasicConstants.TAB_WORD_BREAK);
            oneLine.append(quyuName).append(BasicConstants.TAB_WORD_BREAK);

            Map<String, Integer> cityNumberMap =  BasicCommonValues.quyuCityNumberPerPriority.get(quyuName).get(BasicConstants.GLOBAL);


            int pNumberA = cityNumberMap.get(BasicConstants.SPECIAL_SELL_TYPE_A)==null?0:cityNumberMap.get(BasicConstants.SPECIAL_SELL_TYPE_A);
            oneLine.append(pNumberA).append(BasicConstants.TAB_WORD_BREAK);
            int pNumberB = cityNumberMap.get(BasicConstants.SPECIAL_SELL_TYPE_B)==null?0:cityNumberMap.get(BasicConstants.SPECIAL_SELL_TYPE_B);
            oneLine.append(pNumberB).append(BasicConstants.TAB_WORD_BREAK);
            int pNumberC = cityNumberMap.get(BasicConstants.SPECIAL_SELL_TYPE_C)==null?0:cityNumberMap.get(BasicConstants.SPECIAL_SELL_TYPE_C);
            oneLine.append(pNumberC).append(BasicConstants.TAB_WORD_BREAK);
            int pNumberD = cityNumberMap.get(BasicConstants.SPECIAL_SELL_TYPE_D)==null?0:cityNumberMap.get(BasicConstants.SPECIAL_SELL_TYPE_D);
            oneLine.append(pNumberD).append(BasicConstants.TAB_WORD_BREAK);
            int pNumberTotal = pNumberA + pNumberB + pNumberC + pNumberD;
            oneLine.append(pNumberTotal).append(BasicConstants.TAB_WORD_BREAK);

            if( cityNumberPerYearPri==null){
                cityNumberPerYearPri= new HashMap<>();
            }
            for(int year = 2012; year<=2018; year++ ){
                Map<String, Integer> cityNumberPerPriority = cityNumberPerYearPri.get(year);
                if( cityNumberPerPriority == null ){
                    cityNumberPerPriority = new HashMap<>();
                }


                int cityNumberOfA = cityNumberPerPriority.get(BasicConstants.SPECIAL_SELL_TYPE_A)==null?0:cityNumberPerPriority.get(BasicConstants.SPECIAL_SELL_TYPE_A);
                int cityNumberOfB = cityNumberPerPriority.get(BasicConstants.SPECIAL_SELL_TYPE_B)==null?0:cityNumberPerPriority.get(BasicConstants.SPECIAL_SELL_TYPE_B);
                int cityNumberOfC = cityNumberPerPriority.get(BasicConstants.SPECIAL_SELL_TYPE_C)==null?0:cityNumberPerPriority.get(BasicConstants.SPECIAL_SELL_TYPE_C);
                int cityNumberOfD = cityNumberPerPriority.get(BasicConstants.SPECIAL_SELL_TYPE_D)==null?0:cityNumberPerPriority.get(BasicConstants.SPECIAL_SELL_TYPE_D);
                int cityNumberOfTotal = cityNumberOfA+cityNumberOfB+cityNumberOfC+cityNumberOfD;
                oneLine.append(cityNumberOfA).append(BasicConstants.TAB_WORD_BREAK).append(cityNumberOfB).append(BasicConstants.TAB_WORD_BREAK)
                        .append(cityNumberOfC).append(BasicConstants.TAB_WORD_BREAK).append(cityNumberOfD).append(BasicConstants.TAB_WORD_BREAK)
                        .append(cityNumberOfTotal).append(BasicConstants.TAB_WORD_BREAK);

                double cityPercentageOfA = pNumberA==0?0:(((double) cityNumberOfA)/((double)pNumberA));
                double cityPercentageOfB = pNumberB==0?0:(((double) cityNumberOfB)/((double)pNumberB));
                double cityPercentageOfC = pNumberC==0?0:(((double) cityNumberOfC)/((double)pNumberC));
                double cityPercentageOfD = pNumberD==0?0:(((double) cityNumberOfD)/((double)pNumberD));
                double cityPercentageOfTotal = pNumberTotal==0?0:(((double) cityNumberOfTotal)/((double)pNumberTotal));

                oneLine.append( BasicConstants.DecimalFormat.format(cityPercentageOfA)).append(BasicConstants.TAB_WORD_BREAK)
                        .append(BasicConstants.DecimalFormat.format(cityPercentageOfB)).append(BasicConstants.TAB_WORD_BREAK)
                        .append(BasicConstants.DecimalFormat.format(cityPercentageOfC)).append(BasicConstants.TAB_WORD_BREAK)
                        .append(BasicConstants.DecimalFormat.format(cityPercentageOfD)).append(BasicConstants.TAB_WORD_BREAK)
                        .append(BasicConstants.DecimalFormat.format(cityPercentageOfTotal)).append(BasicConstants.TAB_WORD_BREAK);
            }

            outputStringBuffer.put(lineIndex, oneLine);
            lineIndex++;
        }

        String fileName = BasicConfiguredValues.PAGE2And3_OUTPUT_DIRECTORY +  "_总体城市覆盖程度.txt";
        OutputToTxtUtil.writeTo( outputStringBuffer, fileName);
        log.info("Write the       总体覆盖度 to file: " + fileName);

//        OutputToTxtUtil.writeTo(outputStringBuffer, "");
    }



    private Map<String, Map<Integer, Map<String, Integer>>>  readByNetwork(List<JinpinOneLinePOJO> jinpinOneList) {
        //key is: quYu-year-priority
        Map<String, Map<Integer, Map<String, Set<String>>>> existingCityOfGlobal = new HashMap<>();

//        第一遍是为了读取： 销量 跟城市列表 / city and year
        for (JinpinOneLinePOJO oneLine : jinpinOneList) {
            Map<Integer, Map<String, Set<String>>> citySetPerYearPriority = existingCityOfGlobal.get(oneLine.quYu);
            if(citySetPerYearPriority==null){
                citySetPerYearPriority = new HashMap<>();
                existingCityOfGlobal.put(oneLine.quYu, citySetPerYearPriority);
            }
            Map<String,Set<String>>  citySetPerPriority = citySetPerYearPriority.get(oneLine.year);
            if( citySetPerPriority == null ){
                citySetPerPriority = new HashMap<>();
                citySetPerYearPriority.put(oneLine.year, citySetPerPriority);
            }
            Set<String> citySet = citySetPerPriority.get( oneLine.globalNetworkTypePriority);
            if( citySet==null){
                citySet = new HashSet<>();
                citySetPerPriority.put(oneLine.globalNetworkTypePriority, citySet );
            }
            citySet.add(oneLine.city);
        }

        Map<String, Map<Integer, Map<String, Integer>>> output_cityNumberOfGlobal = new HashMap<>();
        for( Map.Entry<String, Map<Integer, Map<String, Set<String>>>> entry : existingCityOfGlobal.entrySet() ){
            String quyu = entry.getKey();
            Map<Integer, Map<String, Set<String>>> citSetPerYearPriority = entry.getValue();

            Map<Integer, Map<String, Integer>> output_cityNumberPerPriorityYear = new HashMap<>();
            for( int i=2012; i<=2018; i++){
                Map<String, Set<String>> citySetPerPriority = citSetPerYearPriority.get(i);
                if(citySetPerPriority == null ){
                    citySetPerPriority = new HashMap<>();
                }
                Map<String, Integer> cityNumberPerPriority  = new HashMap<>();
                for( Map.Entry<String,Set<String>> citySetEntry : citySetPerPriority.entrySet()){
                    String priority = citySetEntry.getKey();
                    int cityNumber = citySetEntry.getValue()==null?0:citySetEntry.getValue().size();
                    cityNumberPerPriority.put(priority, cityNumber);
                }
                output_cityNumberPerPriorityYear.put(i, cityNumberPerPriority);
            }

            output_cityNumberOfGlobal.put(quyu, output_cityNumberPerPriorityYear);
        }
        return output_cityNumberOfGlobal;
    }


}
