package com.qiaosheng.app.service.impl.read;

import com.qiaosheng.app.service.IBasicServiceInterface;
import com.qiaosheng.app.service.impl.read.CalculateP2_CityCoverage.CaculateQuyuSaleInfoByType_P2_ForGlobal;
import com.qiaosheng.app.service.impl.read.CalculateP2_CityCoverage.CaculateQuyuSaleInfoByType_P2_ForMachine;
import com.qiaosheng.app.service.impl.read.CalculateP2_CityCoverage.CaculateQuyuSaleInfoByType_P2_ForPinban;
import com.qiaosheng.app.service.impl.read.CalculateP2_CityCoverage.CaculateQuyuSaleInfoByType_P2_ForQianyin;
import com.qiaosheng.app.service.impl.read.CalculateP2_CityCoverage.CaculateQuyuSaleInfoByType_P2_ForRoad;
import com.qiaosheng.app.service.impl.read.CalculateP2_CityCoverage.CaculateQuyuSaleInfoByType_P2_ForZixie;
import com.qiaosheng.common.utils.BasicCommonValues;
import com.qiaosheng.common.utils.BasicConfiguredValues;
import com.qiaosheng.common.utils.BasicConstants;
import com.qiaosheng.common.pojo.model.JinpinOneLinePOJO;
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
 * 覆盖区域其实是              区域内的城市覆盖度。
 */

@Component

public class CaculateQuyuSaleInfoByType_P2 implements IBasicServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(CaculateQuyuSaleInfoByType_P2.class);

    //String is Type as above. Value is           销售情况per每个地区每年
    Map<String, CityCoveragePerQuyuYear> outputQuyuSellInfoPerType = new HashMap<>();

    public void run() {

        CaculateQuyuSaleInfoByType_P2_ForGlobal caculateQuyuSaleInfoByType_p2_forGlobal = new CaculateQuyuSaleInfoByType_P2_ForGlobal();
        caculateQuyuSaleInfoByType_p2_forGlobal.run();

        CaculateQuyuSaleInfoByType_P2_ForRoad c2 = new CaculateQuyuSaleInfoByType_P2_ForRoad();
        c2.run();

        CaculateQuyuSaleInfoByType_P2_ForMachine c3 = new CaculateQuyuSaleInfoByType_P2_ForMachine();
        c3.run();

        CaculateQuyuSaleInfoByType_P2_ForPinban c4 = new CaculateQuyuSaleInfoByType_P2_ForPinban();
        c4.run();

        CaculateQuyuSaleInfoByType_P2_ForZixie c5 = new CaculateQuyuSaleInfoByType_P2_ForZixie();
        c5.run();

        CaculateQuyuSaleInfoByType_P2_ForQianyin c6 = new CaculateQuyuSaleInfoByType_P2_ForQianyin();
        c6.run();

//        generateText(ReadJinpinOneLineFromSource.readJinpinInputData);
    }

    public void generateText(List<JinpinOneLinePOJO> jinpinOneList) {

        Map<String, CityCoveragePerQuyuYear> cityInfoPerQuyuYearMap = this.readSubValues(jinpinOneList);

        for (String type : cityInfoPerQuyuYearMap.keySet()) {
            String fileName = BasicConfiguredValues.PAGE2And3_OUTPUT_DIRECTORY + type + "_城市覆盖程度.txt";
            OutputToTxtUtil.writeTo( lineMap(cityInfoPerQuyuYearMap.get(type), type), fileName);
            log.info("Write the 各区域渠道覆盖度 to file: " + fileName);
        }
    }

    private Map<Integer, StringBuffer> lineMap(CityCoveragePerQuyuYear  cityCoveragePerQuyuYear, String type ){
        Map<Integer, StringBuffer> result = new HashMap<>();

        int lineIndex = 0;

        for( QuyuBasicInfo quyuBasicInfo : BasicCommonValues.quYuBasicInfoListWithOrder){
            String quyuName = quyuBasicInfo.getQuYuName();

            Map<Integer,Map<String,Integer>> cityNumberPerYearPriority  = cityCoveragePerQuyuYear.cityNumberPerQuyuYear.get(quyuName);
            if( cityNumberPerYearPriority == null || cityNumberPerYearPriority.size()==0 ){
                continue;
            }
            StringBuffer oneLine = new StringBuffer(quyuBasicInfo.getWarQu()).append(BasicConstants.TAB_WORD_BREAK);
            oneLine.append(quyuName).append(BasicConstants.TAB_WORD_BREAK);
            appendDataForAllCityNumber(oneLine, quyuName, type);
            for( int i=2012; i<=2018; i++ ){
                Map<String,Integer> cityNumberPerPriority = cityNumberPerYearPriority.get(i);
                if( cityNumberPerPriority==null){
                    cityNumberPerPriority = new HashMap<>();
                }

                for(String priority : BasicConstants.priorityListAndAll ){
                    int cityNumber = cityNumberPerPriority.get(priority)==null?0:cityNumberPerPriority.get(priority);
                    oneLine.append(cityNumber).append(BasicConstants.TAB_WORD_BREAK);
                }
            }
//            oneLine.append(BasicConstants.ENTER_LINE_BREAK);
            lineIndex++;
            result.put(lineIndex, oneLine);
        }
        return result;
    }



    public Map<String, CityCoveragePerQuyuYear> readSubValues(List<JinpinOneLinePOJO> jinpinOneList) {
        outputQuyuSellInfoPerType = readByNetwork(jinpinOneList);
        outputQuyuSellInfoPerType.putAll(readByFunction(jinpinOneList));
        return outputQuyuSellInfoPerType;
    }

    private String getPriorityFromInput(String networkType, JinpinOneLinePOJO oneLine) {
        if (networkType.equals(BasicConstants.GLOBAL)||networkType.startsWith("总体") ) {
            return oneLine.globalNetworkTypePriority;
        } else if (networkType.equals(BasicConstants.NETWORKTYPE_ROAD)||networkType.startsWith("公路车")) {
            return oneLine.roadNetworkTypePriority;
        } else if (networkType.equals(BasicConstants.NETWORKTYPE_MACHINE)||networkType.startsWith("工程车")) {
            return oneLine.machineNetworkTypePriority;
        } else if (networkType.equals(BasicConstants.TYPE_QIANYIN)||networkType.startsWith("牵引")) {
            return oneLine.qianyinTypePriority;
        } else if (networkType.equals(BasicConstants.TYPE_PINBAN)||networkType.startsWith("平板")) {
            return oneLine.pinbanTypePriority;
        } else if (networkType.equals(BasicConstants.TYPE_ZIXIE)||networkType.startsWith("自卸")) {
            return oneLine.zixieTypePriority;
        }
        System.err.println("CANNOT FIND THE PRIORITY with networkType " + networkType);
        return oneLine.zixieTypePriority;
    }


    private Map<String, CityCoveragePerQuyuYear> readByNetwork(List<JinpinOneLinePOJO> jinpinOneList) {
        Map<String, CityCoveragePerQuyuYear> typeSellInfo = new HashMap<>();

//        第一遍是为了读取： 销量 跟城市列表 / city and year

        for (JinpinOneLinePOJO oneLine : jinpinOneList) {

            String networkType = oneLine.networkType;
            //得到当前NetworkType类型的分类值
            String inputPriority = getPriorityFromInput(networkType, oneLine);

            CityCoveragePerQuyuYear cityCoveragePerQuyuYear = typeSellInfo.get(networkType);
            if (cityCoveragePerQuyuYear == null) {
                cityCoveragePerQuyuYear = new CityCoveragePerQuyuYear();
                typeSellInfo.put(networkType, cityCoveragePerQuyuYear);
            }


            //Insert total SaleNumber
            Map<Integer, Map<String, Set<String>>> cityNameSetPerYear = cityCoveragePerQuyuYear.cityNameSetPerQuyuYear.get(oneLine.quYu);
            if (cityNameSetPerYear == null) {
                cityNameSetPerYear = new HashMap<>();
                cityCoveragePerQuyuYear.cityNameSetPerQuyuYear.put(oneLine.quYu, cityNameSetPerYear);
            }

            //Insert total seller Number;
            Map<String, Set<String>> namesPerPriority = cityNameSetPerYear.get(oneLine.year);
            if (namesPerPriority == null) {
                namesPerPriority = new HashMap<>();
                cityNameSetPerYear.put(oneLine.year, namesPerPriority);
            }

            Set<String> cityNames = namesPerPriority.get(inputPriority);
            if (cityNames == null) {
                cityNames = new HashSet<>();
                namesPerPriority.put(inputPriority, cityNames);
            }
            cityNames.add(oneLine.city);
        }
//        Second: calculate the SellerNumber, and average SellerNumber.
        for (String networkType : typeSellInfo.keySet()) {
            CityCoveragePerQuyuYear cityCoveragePerQuyuYear = typeSellInfo.get(networkType);
            if (cityCoveragePerQuyuYear == null) {
                continue;
            }
            Map<String, Map<Integer, Map<String, Set<String>>>> cityNameSetPerQuyuYear = cityCoveragePerQuyuYear.cityNameSetPerQuyuYear;

            for (String quyuName : cityNameSetPerQuyuYear.keySet()) {
                //parse the input.
                Map<Integer, Map<String, Set<String>>> cityNameCollectionPerPriorityYear = cityCoveragePerQuyuYear.cityNameSetPerQuyuYear.get(quyuName);
                if (cityNameCollectionPerPriorityYear == null || cityNameCollectionPerPriorityYear.size() == 0) {
                    continue;
                }
                //prepare for output
                Map<Integer, Map<String, Integer>> cityNumberPerPriorityYear = cityCoveragePerQuyuYear.cityNumberPerQuyuYear.get(quyuName);
                if (cityNumberPerPriorityYear == null) {
                    cityNumberPerPriorityYear = new HashMap<>();
                    cityCoveragePerQuyuYear.cityNumberPerQuyuYear.put(quyuName, cityNumberPerPriorityYear);
                }

                String logmessage = "";
                for (int i = 2012; i <= 2018; i++) {

                    Map<String, Set<String>> cityNamesPerPriority = cityNameCollectionPerPriorityYear.get(i);
                    if (cityNamesPerPriority == null) {
                        logmessage +=  " miss year " + i + "  data for quYu " + quyuName + " ; ";
                        continue;
                    }
                    //output for city number
                    Map<String, Integer> cityNumberPerPriority = cityNumberPerPriorityYear.get(i);
                    if (cityNumberPerPriority == null) {
                        cityNumberPerPriority = new HashMap<>();
                        cityNumberPerPriorityYear.put(i, cityNumberPerPriority);
                    }
                    int summary = 0;
                    for (String priority : cityNamesPerPriority.keySet()) {
                        Set<String> names = cityNamesPerPriority.get(priority);
                        cityNumberPerPriority.put(priority, names.size());
                        summary += names.size();
                    }
                    cityNumberPerPriority.put(BasicConstants.PRIORITY_SUMMARY, summary);
                }
                if(!logmessage.equals("")){
                    log.info("While analysis the Basic sale info for NETWORK TYPE,  Not all Quyu has all year sale info: "  + logmessage);
                }

            }
        }
        return typeSellInfo;
    }

    private Map<String, CityCoveragePerQuyuYear> readByFunction(List<JinpinOneLinePOJO> jinpinOneList) {
        Map<String, CityCoveragePerQuyuYear> typeSellInfo = new HashMap<>();

//        第一遍是为了读取： 销量 跟城市列表 / city and year

        for (JinpinOneLinePOJO oneLine : jinpinOneList) {

            String networkType = oneLine.functionType;
            //得到当前NetworkType类型的分类值
            String inputPriority = getPriorityFromInput(networkType, oneLine);

            CityCoveragePerQuyuYear cityCoveragePerQuyuYear = typeSellInfo.get(networkType);
            if (cityCoveragePerQuyuYear == null) {
                cityCoveragePerQuyuYear = new CityCoveragePerQuyuYear();
                typeSellInfo.put(networkType, cityCoveragePerQuyuYear);
            }


            //Insert total SaleNumber
            Map<Integer, Map<String, Set<String>>> cityNameSetPerYear = cityCoveragePerQuyuYear.cityNameSetPerQuyuYear.get(oneLine.quYu);
            if (cityNameSetPerYear == null) {
                cityNameSetPerYear = new HashMap<>();
                cityCoveragePerQuyuYear.cityNameSetPerQuyuYear.put(oneLine.quYu, cityNameSetPerYear);
            }

            //Insert total seller Number;
            Map<String, Set<String>> namesPerPriority = cityNameSetPerYear.get(oneLine.year);
            if (namesPerPriority == null) {
                namesPerPriority = new HashMap<>();
                cityNameSetPerYear.put(oneLine.year, namesPerPriority);
            }

            Set<String> cityNames = namesPerPriority.get(inputPriority);
            if (cityNames == null) {
                cityNames = new HashSet<>();
                namesPerPriority.put(inputPriority, cityNames);
            }
            cityNames.add(oneLine.city);
        }
//        Second: calculate the SellerNumber, and average SellerNumber.
        for (String networkType : typeSellInfo.keySet()) {
            CityCoveragePerQuyuYear cityCoveragePerQuyuYear = typeSellInfo.get(networkType);
            if (cityCoveragePerQuyuYear == null) {
                continue;
            }
            Map<String, Map<Integer, Map<String, Set<String>>>> cityNameSetPerQuyuYear = cityCoveragePerQuyuYear.cityNameSetPerQuyuYear;

            for (String quyuName : cityNameSetPerQuyuYear.keySet()) {
                //parse the input.
                Map<Integer, Map<String, Set<String>>> cityNameCollectionPerPriorityYear = cityCoveragePerQuyuYear.cityNameSetPerQuyuYear.get(quyuName);
                if (cityNameCollectionPerPriorityYear == null || cityNameCollectionPerPriorityYear.size() == 0) {
                    continue;
                }
                //prepare for output
                Map<Integer, Map<String, Integer>> cityNumberPerPriorityYear = cityCoveragePerQuyuYear.cityNumberPerQuyuYear.get(quyuName);
                if (cityNumberPerPriorityYear == null) {
                    cityNumberPerPriorityYear = new HashMap<>();
                    cityCoveragePerQuyuYear.cityNumberPerQuyuYear.put(quyuName, cityNumberPerPriorityYear);
                }

                String logmessage = "";
                for (int i = 2012; i <= 2018; i++) {

                    Map<String, Set<String>> cityNamesPerPriority = cityNameCollectionPerPriorityYear.get(i);
                    if (cityNamesPerPriority == null) {
                        logmessage += ("miss year " + i + "  data for quYu " + quyuName);
                        continue;
                    }
                    //output for city number
                    Map<String, Integer> cityNumberPerPriority = cityNumberPerPriorityYear.get(i);
                    if (cityNumberPerPriority == null) {
                        cityNumberPerPriority = new HashMap<>();
                        cityNumberPerPriorityYear.put(i, cityNumberPerPriority);
                    }
                    int summary = 0;
                    for (String priority : cityNamesPerPriority.keySet()) {
                        Set<String> names = cityNamesPerPriority.get(priority);
                        cityNumberPerPriority.put(priority, names.size());
                        summary += names.size();
                    }
                    cityNumberPerPriority.put(BasicConstants.PRIORITY_SUMMARY, summary);
                }
                if(!logmessage.equals("")){
                    log.info("While analysis the Basic sale info for FUNCTION TYPE,  Not all Quyu has all year sale info: "  + logmessage);
                }
            }
        }
        return typeSellInfo;
    }

    private void appendDataForAllCityNumber(StringBuffer sb, String quyuName, String type ) {
        Map<String,Map<String,  Integer>> cityNumber = BasicCommonValues.quyuCityNumberPerPriority.get(quyuName);
        if( cityNumber==null ){
            cityNumber = new HashMap<>();
        }

        Map<String,  Integer> cityNumberOfPriority = cityNumber.get(type);
        if( cityNumberOfPriority==null ){
            cityNumberOfPriority = new HashMap<>();
        }

        int totalN = 0;
        for(String priority: BasicConstants.priorityList ){
            int number = cityNumberOfPriority.get(priority)==null?0:cityNumberOfPriority.get(priority);
            sb.append(number).append(BasicConstants.TAB_WORD_BREAK);
            totalN += number;
        }
        sb.append(totalN).append(BasicConstants.TAB_WORD_BREAK);
//        sb.append("待定数据").append(BasicConstants.TAB_WORD_BREAK).append("待定数据").append(BasicConstants.TAB_WORD_BREAK).append("待定数据").append(BasicConstants.TAB_WORD_BREAK).append("待定数据").append(BasicConstants.TAB_WORD_BREAK).append("待定数据").append(BasicConstants.TAB_WORD_BREAK);
    }


    public class CityCoveragePerQuyuYear {
        //all String key is "quYuName"
        //中间变量
        public Map<String, Map<Integer, Map<String, Set<String>>>> cityNameSetPerQuyuYear = new HashMap<>();
        public Map<String, Map<Integer, Map<String, Integer>>> cityNumberPerQuyuYear = new HashMap<>();
    }
}
