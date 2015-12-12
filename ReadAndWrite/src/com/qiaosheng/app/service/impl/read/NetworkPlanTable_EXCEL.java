package com.qiaosheng.app.service.impl.read;

import com.qiaosheng.common.pojo.model.JinpinOneLinePOJO;
import com.qiaosheng.common.pojo.model.OumanPOJO;
import com.qiaosheng.common.utils.BasicCommonValues;
import com.qiaosheng.common.utils.BasicConfiguredValues;
import com.qiaosheng.common.utils.BasicConstants;
import com.qiaosheng.common.utils.CityBasicInfoPerYear;
import com.qiaosheng.common.utils.OutputToTxtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
  * User: dai
 * Time: 下午9:40
  */
public class NetworkPlanTable_EXCEL {

    private static final Logger log = LoggerFactory.getLogger(NetworkPlanTable_EXCEL.class);

    private static List<String> outputSequence  = new ArrayList<>();
    static{
        outputSequence.add(BasicConstants.NETWORKTYPE_ROAD);
        outputSequence.add(BasicConstants.NETWORKTYPE_MACHINE);
        outputSequence.add(BasicConstants.TYPE_PINBAN);
        outputSequence.add(BasicConstants.TYPE_QIANYIN);
        outputSequence.add(BasicConstants.TYPE_ZIXIE);

    }


    public void run(){
        try {
            Map<Integer, StringBuffer> output = generateText(ReadJinpinOneLineFromSource.readJinpinInputData, ReadOumanFromSource.oumanInputData);
            OutputToTxtUtil.writeTo(output, BasicConfiguredValues.NETWORKPLAN_OUTPUT_DIRECTORY + "网络规划数据.txt");
        } catch (Exception e) {
            log.error("Failed to generate report.", e);
        }
    }


    public Map<Integer, StringBuffer> generateText(List<JinpinOneLinePOJO> jinpinOneList, List<OumanPOJO> oumanInputData) {
        Map<String, StringBuffer> cityPercentage = calculatePercentage(jinpinOneList);
        Map<Integer, StringBuffer> outputMap = new HashMap<>();
        int lineIndex=0;
        for( OumanPOJO oumanPOJO: oumanInputData ){
            String cityName = oumanPOJO.localCity;
            StringBuffer sb = new StringBuffer(lineIndex+1).append(BasicConstants.TAB_WORD_BREAK);

            CityBasicInfoPerYear cityBasicInfo = BasicCommonValues.cityBasicInfoMap.get(cityName);
            String warQu = null;
            String cityPriorityOfGlobalNetwork = "unknown";
            String cityPriorityOfRoadNetwork = "unknown";
            String cityPriorityOfMachineNetwork = "unknown";
            String cityPriorityOfPinbanNetwork = "unknown";
            String cityPriorityOfQianyinNetwork = "unknown";
            String cityPriorityOfZixieNetwork = "unknown";
            if(cityBasicInfo==null){
                log.warn("Cannot find the city {} from 竞品渠道开票汇总,", cityName);
                warQu = oumanPOJO.daQu;
            }else{
                warQu = cityBasicInfo.warQu;
                try {
                    cityPriorityOfGlobalNetwork= cityBasicInfo.getYearPriority().get(2012).getCityPriorityOfGlobalNetwork();
                    cityPriorityOfRoadNetwork = cityBasicInfo.getYearPriority().get(2012).getCityPriorityOfRoadNetwork();
                    cityPriorityOfMachineNetwork = cityBasicInfo.getYearPriority().get(2012).getCityPriorityOfMachineNetwork();
                    cityPriorityOfPinbanNetwork= cityBasicInfo.getYearPriority().get(2012).getCityPriorityOfPinbanNetwork();
                    cityPriorityOfQianyinNetwork= cityBasicInfo.getYearPriority().get(2012).getCityPriorityOfQianyinNetwork();
                    cityPriorityOfZixieNetwork= cityBasicInfo.getYearPriority().get(2012).getCityPriorityOfZixieNetwork();
                } catch (Exception e) {
                    log.warn("City {} has no priority defined at current year: 2015.", cityName, e);
                }
            }


            // num 2.
            sb.append(warQu).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.daQu).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.market).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.province).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.localCity).append(BasicConstants.TAB_WORD_BREAK);
            //7
            sb.append(oumanPOJO.countryTown).append(BasicConstants.TAB_WORD_BREAK);

            sb.append(cityPriorityOfGlobalNetwork).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(cityPriorityOfRoadNetwork).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(cityPriorityOfMachineNetwork).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(cityPriorityOfPinbanNetwork).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(cityPriorityOfQianyinNetwork).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(cityPriorityOfZixieNetwork).append(BasicConstants.TAB_WORD_BREAK);

            StringBuffer cityPercentageString = cityPercentage.get(cityName);
            if( cityPercentageString ==null){
                log.warn("找不到城市{} 的竞品计算结果", cityName);
                cityPercentageString = new StringBuffer();
                for( int j=0; j<42; j++){
                    cityPercentageString.append(0).append(BasicConstants.TAB_WORD_BREAK);
                }
            }
            sb.append(cityPercentageString);

            for( int emptyColumn = 0; emptyColumn<270; emptyColumn++){
                sb.append(BasicConstants.TAB_WORD_BREAK);
            }
            //126
            sb.append(oumanPOJO.sellerName).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.sellerSimpleName).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.sellerJoinTime).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.sellerUsedName).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.sellerUsedJoinTime).append(BasicConstants.TAB_WORD_BREAK);
            //131
            sb.append(oumanPOJO.businessType).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.joinType).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.manageType).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.businessMode).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.secondFirstSeller).append(BasicConstants.TAB_WORD_BREAK);
            //
            //136
            sb.append(oumanPOJO.oumanRoadNetwork).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.oumanMachineNetwork).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.sellerRoadNetwork).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.sellerMachineNetwork).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.authedProductLine).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.authedArea).append(BasicConstants.TAB_WORD_BREAK);

            //142
            sb.append(oumanPOJO.oumanShopOldStandard).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.oumanShopNewStandard).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.oumanShopAddress).append(BasicConstants.TAB_WORD_BREAK);

            //145
            sb.append(oumanPOJO.isSellAndService).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.isFinance).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.isSellingReplacement).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.isSecondHandBusiness).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.isCarTeamManage).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.isInsuranceBusiness).append(BasicConstants.TAB_WORD_BREAK);
            sb.append(oumanPOJO.isSecondSustaining).append(BasicConstants.TAB_WORD_BREAK);
            outputMap.put(lineIndex, sb);
            lineIndex++;
        }
        return  outputMap;
    }

    public Map<String, StringBuffer> calculatePercentage(List<JinpinOneLinePOJO> jinpinOneList) {
        //key is: cityName->Year-> typeOfNetwork/typeOfFunction: saleNumber;
        Map<String, Map<Integer, Map<String, Integer>>> saleNumberPerYearCityType = new HashMap<>();
        //key is:Year->type:  value is totalSaleNumber;
        Map<Integer, Map<String,Integer>> countrySaleNumberPerYearType = new HashMap<>();
        //key is cityName->year, value is total SaleNumber;
        Map<String, Map<Integer, Integer>> saleNumberPerYearCity = new HashMap<>();

        Map<Integer, Integer> totalSalNumberPerYear = new HashMap<>();


        for( JinpinOneLinePOJO jinpinOneLinePOJO : jinpinOneList){
            String cityName = jinpinOneLinePOJO.city;
            Map<Integer, Map<String, Integer>> saleNPerYearPerT = saleNumberPerYearCityType.get(cityName);
            if( saleNPerYearPerT == null){
                saleNPerYearPerT = new HashMap<>();
                saleNumberPerYearCityType.put(cityName, saleNPerYearPerT);
            }
            Map<String,Integer> saleNPerT = saleNPerYearPerT.get(jinpinOneLinePOJO.year);
            if(saleNPerT == null){
                saleNPerT = new HashMap<>();
                saleNPerYearPerT.put(jinpinOneLinePOJO.year, saleNPerT);
            }
            int saleNofCityForFunction = saleNPerT.get( jinpinOneLinePOJO.functionType)==null?0: saleNPerT.get(jinpinOneLinePOJO.functionType);
            saleNPerT.put(jinpinOneLinePOJO.functionType, saleNofCityForFunction + jinpinOneLinePOJO.saleNumber);
            int saleNofCityForNetwork = saleNPerT.get( jinpinOneLinePOJO.networkType)==null?0: saleNPerT.get(jinpinOneLinePOJO.networkType);
            saleNPerT.put(jinpinOneLinePOJO.networkType,saleNofCityForNetwork + jinpinOneLinePOJO.saleNumber);
            //---- above is for the city sale number. below is total salenumber
            Map<String,Integer> saleNumberPerType = countrySaleNumberPerYearType.get(jinpinOneLinePOJO.year);
            if( saleNumberPerType==null ){
                saleNumberPerType = new HashMap<>();
                countrySaleNumberPerYearType.put(jinpinOneLinePOJO.year, saleNumberPerType);
            }

            int saleNofYearForFunction = saleNumberPerType.get( jinpinOneLinePOJO.functionType)==null?0: saleNumberPerType.get(jinpinOneLinePOJO.functionType);
            saleNumberPerType.put(jinpinOneLinePOJO.functionType, saleNofYearForFunction + jinpinOneLinePOJO.saleNumber);
            int saleNofYearForNetwork = saleNumberPerType.get( jinpinOneLinePOJO.networkType)==null?0: saleNumberPerType.get(jinpinOneLinePOJO.networkType);
            saleNumberPerType.put(jinpinOneLinePOJO.networkType,  saleNofYearForNetwork + jinpinOneLinePOJO.saleNumber);

            //---- above is  is total sale number per Type. below is total sale number per city.

            Map<Integer, Integer> totalSaleNPerYear =  saleNumberPerYearCity.get(cityName);
            if(totalSaleNPerYear==null){
                totalSaleNPerYear = new HashMap<>();
                saleNumberPerYearCity.put(cityName, totalSaleNPerYear);
            }
            int saleNOfYear = totalSaleNPerYear.get(jinpinOneLinePOJO.year)==null?0:totalSaleNPerYear.get(jinpinOneLinePOJO.year);
            totalSaleNPerYear.put(jinpinOneLinePOJO.year, saleNOfYear + jinpinOneLinePOJO.saleNumber);

            //Below is the saleNumber of the country year;
            int totalN = totalSalNumberPerYear.get(jinpinOneLinePOJO.year)==null?0:totalSalNumberPerYear.get(jinpinOneLinePOJO.year);
            totalSalNumberPerYear.put(jinpinOneLinePOJO.year, totalN+jinpinOneLinePOJO.saleNumber);
         }

        // ==== Then for output=====
        Map<String, StringBuffer> percentageStringPerCityName = new HashMap<>();
        for( String cityName : BasicCommonValues.cityBasicInfoMap.keySet()){

            StringBuffer sb = new StringBuffer();

            Map<Integer, Map<String, Integer>> detailedSaleNumber =  saleNumberPerYearCityType.get(cityName);
            if( detailedSaleNumber ==null ){
                detailedSaleNumber = new HashMap<>();
            }
            Map<Integer, Integer> totalSaleNumberOftheCity = saleNumberPerYearCity.get(cityName);
            if( totalSaleNumberOftheCity==null){
                totalSaleNumberOftheCity = new HashMap<>();
            }


            for( int year=2012; year<=2018; year++ ){
                double totalSaleNumberOfCityYear =(double) (totalSaleNumberOftheCity.get(year)==null?0:totalSaleNumberOftheCity.get(year));
                double totalSaleNumberOfAllCities =(double) (totalSalNumberPerYear.get(year)==null?0:totalSalNumberPerYear.get(year));


                Map<String,Integer> typeSaleNumberOfCountryYear = countrySaleNumberPerYearType.get(year);
                if(typeSaleNumberOfCountryYear==null){
                    typeSaleNumberOfCountryYear = new HashMap<>();
                }

                Map<String, Integer> saleNPerType = detailedSaleNumber.get(year);
                if(saleNPerType==null){
                    saleNPerType = new HashMap<>();
                }

                double hejiPercentage =(totalSaleNumberOfAllCities<=0)?0: totalSaleNumberOfCityYear/totalSaleNumberOfAllCities;

                sb.append(BasicConstants.DecimalFormat.format(hejiPercentage)).append(BasicConstants.TAB_WORD_BREAK);

                for( String type: outputSequence){
                    double saleNOfCityTypeYear = (double) (saleNPerType.get(type)==null?0:saleNPerType.get(type));
                    double countryTotalSaleNumOfType =(double)( typeSaleNumberOfCountryYear.get(type)==null?0:typeSaleNumberOfCountryYear.get(type));
                    double salePerc = countryTotalSaleNumOfType<=0?0:(saleNOfCityTypeYear/countryTotalSaleNumOfType);
                    sb.append( BasicConstants.DecimalFormat.format(salePerc)).append(BasicConstants.TAB_WORD_BREAK);
                }

            }
            percentageStringPerCityName.put(cityName, sb);
        }
        return percentageStringPerCityName;
    }
}
