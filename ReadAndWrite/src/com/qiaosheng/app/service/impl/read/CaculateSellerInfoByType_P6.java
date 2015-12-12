package com.qiaosheng.app.service.impl.read;

import com.qiaosheng.app.service.IBasicServiceInterface;
import com.qiaosheng.common.utils.BasicCommonValues;
import com.qiaosheng.common.utils.BasicConfiguredValues;
import com.qiaosheng.common.utils.BasicConstants;
import com.qiaosheng.common.pojo.model.JinpinOneLinePOJO;
import com.qiaosheng.common.pojo.model.QuyuSaleInfoPOJO;
import com.qiaosheng.common.utils.OutputToTxtUtil;
import com.qiaosheng.common.utils.QuyuBasicInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午5:03
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CaculateSellerInfoByType_P6  implements IBasicServiceInterface {
    private static final Logger log = LoggerFactory.getLogger(CaculateSellerInfoByType_P6.class);
    // key is functionType, such as "公路车网络， 工程车网" or "牵引车    平板车      自卸车
    //String is Type as above. Value is           销售情况per每个地区每年
    Map<String, SaleInfoPerCityYear> outputSellInfoPerType = new HashMap<>();



    public void run(){
        try {
            generateText(ReadJinpinOneLineFromSource.readJinpinInputData);
        } catch (Exception e) {
            log.error("Failed to generate report.", e);
        }
    }

    public void generateText(List<JinpinOneLinePOJO> jinpinOneList) {

        Map<String, CaculateSellerInfoByType_P6.SaleInfoPerCityYear> saleInfoPerCityYearMap= this.readSubValues(jinpinOneList);

        for( String type : saleInfoPerCityYearMap.keySet()){
            String fileName = BasicConfiguredValues.PAGE6And7_OUTPUT_DIRECTORY + type + "_渠道点均销量.txt";
            outputFile( fileName,saleInfoPerCityYearMap.get(type) );
        }
    }

    private void outputFile(String fileName, CaculateSellerInfoByType_P6.SaleInfoPerCityYear saleInfoPerCityYear ){
        //Output lines;
        Map<Integer,StringBuffer> allLineMap = new HashMap<>();

        List<StringBuffer> beiFangOutputMap = new ArrayList<>();
        List<StringBuffer> nanFangOutputMap = new ArrayList<>();
        List<StringBuffer> xiBuOutputMap = new ArrayList<>();
        List<StringBuffer> othersOutputMap = new ArrayList<>();



        for(String quyuName : saleInfoPerCityYear.saleNumberPerCityYear.keySet()) {
            String warQu = "Unknown";
            for(QuyuBasicInfo quyuBasicInfo : BasicCommonValues.quYuBasicInfoListWithOrder){
                if( quyuBasicInfo.getQuYuName().equals(quyuName)){
                    warQu = quyuBasicInfo.getWarQu();
                }
            }

            StringBuffer oneLine = new StringBuffer(warQu ).append( BasicConstants.TAB_WORD_BREAK).append(quyuName).append(BasicConstants.TAB_WORD_BREAK);
            if( warQu.equals("北方")){
                beiFangOutputMap.add( oneLine );
            }else if( warQu.equals("南方")){
                nanFangOutputMap.add( oneLine );
            }else if( warQu.equals("西部")){
                xiBuOutputMap.add( oneLine );
            }else{
                othersOutputMap.add( oneLine );
            }

            Map<Integer, Map<String, Integer>> yearSaleMap =saleInfoPerCityYear.saleNumberPerCityYear.get(quyuName);

            Map<Integer, Map<String, Double>> yearAverageSaleMap = saleInfoPerCityYear.averageSaleNumberPerCityYear.get(quyuName);

            for(int i=2012; i<=2018; i++ ){
                Map<String, Integer> saleNumberPerPriority = yearSaleMap.get(i);
                Map<String, Double> averageSalePerPriority = yearAverageSaleMap.get(i);

                if(saleNumberPerPriority==null){
                    saleNumberPerPriority = new HashMap<>();
                }
                if(averageSalePerPriority==null){
                    averageSalePerPriority = new HashMap<>();
                }

                int aValue = saleNumberPerPriority.get("a")==null?0:saleNumberPerPriority.get("a");
                int bValue = saleNumberPerPriority.get("b")==null?0:saleNumberPerPriority.get("b");
                int cValue = saleNumberPerPriority.get("c")==null?0:saleNumberPerPriority.get("c");
                int dValue = saleNumberPerPriority.get("d")==null?0:saleNumberPerPriority.get("d");
                int totalV = aValue + bValue + cValue + dValue;

                double aValueD = averageSalePerPriority.get("a")==null?0:averageSalePerPriority.get("a");
                double bValueD = averageSalePerPriority.get("b")==null?0:averageSalePerPriority.get("b");
                double cValueD = averageSalePerPriority.get("c")==null?0:averageSalePerPriority.get("c");
                double dValueD = averageSalePerPriority.get("d")==null?0:averageSalePerPriority.get("d");
                double totalVD = aValueD + bValueD + cValueD + dValueD;


                oneLine.append(aValue ).append(BasicConstants.TAB_WORD_BREAK)
                        .append( bValue).append(BasicConstants.TAB_WORD_BREAK)
                        .append( cValue).append(BasicConstants.TAB_WORD_BREAK)
                        .append( dValue).append(BasicConstants.TAB_WORD_BREAK)
                        .append( totalV ).append(BasicConstants.TAB_WORD_BREAK);
                oneLine.append(aValueD).append(BasicConstants.TAB_WORD_BREAK)
                        .append(bValueD).append(BasicConstants.TAB_WORD_BREAK)
                        .append(cValueD).append(BasicConstants.TAB_WORD_BREAK)
                        .append(dValueD).append(BasicConstants.TAB_WORD_BREAK)
                        .append(totalVD ).append(BasicConstants.TAB_WORD_BREAK);
            }
        }

        int lineIndex = 0;
        for(StringBuffer sbLine : beiFangOutputMap){
            allLineMap.put(lineIndex, sbLine);
            lineIndex++;
        }
        for(StringBuffer sbLine : nanFangOutputMap){
            allLineMap.put(lineIndex, sbLine);
            lineIndex++;
        }
        for(StringBuffer sbLine : xiBuOutputMap){
            allLineMap.put(lineIndex, sbLine);
            lineIndex++;
        }
        for(StringBuffer sbLine : othersOutputMap){
            allLineMap.put(lineIndex, sbLine);
            lineIndex++;
        }
        OutputToTxtUtil.writeTo(allLineMap, fileName);
    }


    public  Map<String, SaleInfoPerCityYear> readSubValues(List<JinpinOneLinePOJO> jinpinOneList){
        outputSellInfoPerType = readByNetwork(  jinpinOneList );
        outputSellInfoPerType.putAll(readByFunction(jinpinOneList));
        persisQuyuSaleInfo(outputSellInfoPerType);
        return outputSellInfoPerType;
    }

    //String is Type as above. Value is           销售情况per每个地区每年
    private void persisQuyuSaleInfo( Map<String, SaleInfoPerCityYear> outputSellInfoPerType ){
        if(outputSellInfoPerType==null || outputSellInfoPerType.size() ==0){
            return;
        }

        Map<String, QuyuSaleInfoPOJO> cacheResult = new HashMap<>();

        for( Map.Entry<String, SaleInfoPerCityYear> e : outputSellInfoPerType.entrySet()){
            String type = e.getKey();
            SaleInfoPerCityYear saleInfoPerCityYear = e.getValue();

            for(Map.Entry<String, Map<Integer, Map<String,Integer>>> inerE : saleInfoPerCityYear.saleNumberPerCityYear.entrySet()){
                String quYuName = inerE.getKey();
                for( Map.Entry<Integer, Map<String,Integer>> yearInfo : inerE.getValue().entrySet() ){
                    int year = yearInfo.getKey();

                    for( Map.Entry<String, Integer> saleNumberPerPriority : yearInfo.getValue().entrySet()){
                        String priority = saleNumberPerPriority.getKey();
                        int saleNumber = saleNumberPerPriority.getValue();
                        QuyuSaleInfoPOJO quyuSaleInfoPOJO = new QuyuSaleInfoPOJO();
                        quyuSaleInfoPOJO.setSaleNumber( saleNumber );
                        quyuSaleInfoPOJO.setPriority(priority);
                        quyuSaleInfoPOJO.setQuyuName(quYuName);
                        quyuSaleInfoPOJO.setType(type);
                        quyuSaleInfoPOJO.setYear(year);
                        cacheResult.put( quYuName + type + year + priority , quyuSaleInfoPOJO );
                    }
                }
            }

            for(Map.Entry<String, Map<Integer, Map<String,Integer>>> inerE : saleInfoPerCityYear.sellerNumberPerCityYear.entrySet()){
                String quYuName = inerE.getKey();
                for( Map.Entry<Integer, Map<String,Integer>> yearInfo : inerE.getValue().entrySet() ){
                    int year = yearInfo.getKey();

                    for( Map.Entry<String, Integer> sellerNumberPerPriority : yearInfo.getValue().entrySet()){
                        String priority = sellerNumberPerPriority.getKey();
                        int sellerNumber = sellerNumberPerPriority.getValue();
                        QuyuSaleInfoPOJO quyuSaleInfoPOJO =   cacheResult.get(quYuName + type + year + priority);
                        if( quyuSaleInfoPOJO == null ){
                            log.error("Strange issue! Cannot get the QuyuSaleInfo!");
                            quyuSaleInfoPOJO = new QuyuSaleInfoPOJO();
                            quyuSaleInfoPOJO.setPriority(priority);
                            quyuSaleInfoPOJO.setQuyuName(quYuName);
                            quyuSaleInfoPOJO.setType(type);
                            quyuSaleInfoPOJO.setYear(year);
                        }
                        quyuSaleInfoPOJO.setSellerNumber(sellerNumber);
                        cacheResult.put( quYuName + type + year + priority , quyuSaleInfoPOJO );
                    }
                }
            }


            for(Map.Entry<String, Map<Integer, Map<String,Double>>> inerE : saleInfoPerCityYear.averageSaleNumberPerCityYear.entrySet()){
                String quYuName = inerE.getKey();
                for( Map.Entry<Integer, Map<String,Double>> yearInfo : inerE.getValue().entrySet() ){
                    int year = yearInfo.getKey();

                    for( Map.Entry<String, Double> averageSellerNumberPerPriority : yearInfo.getValue().entrySet()){
                        String priority = averageSellerNumberPerPriority.getKey();
                        double averageSellerNumber = averageSellerNumberPerPriority.getValue();
                        QuyuSaleInfoPOJO quyuSaleInfoPOJO =   cacheResult.get(quYuName + type + year + priority);
                        if( quyuSaleInfoPOJO == null ){
                            log.error("Strange issue! Cannot get the QuyuSaleInfo!");
                            quyuSaleInfoPOJO = new QuyuSaleInfoPOJO();
                            quyuSaleInfoPOJO.setPriority(priority);
                            quyuSaleInfoPOJO.setQuyuName(quYuName);
                            quyuSaleInfoPOJO.setType(type);
                            quyuSaleInfoPOJO.setYear(year);
                        }
                        quyuSaleInfoPOJO.setAverageSaleNumber( averageSellerNumber );
                        cacheResult.put( quYuName + type + year + priority, quyuSaleInfoPOJO );
                    }
                }
            }
        }

        BasicCommonValues.quyuSaleInfoPerTypes = cacheResult;
        log.info("Saved {} QuyuSaleInfoPOJO to DB", cacheResult.size());
    }



    private String getPriorityFromInput(String networkType, JinpinOneLinePOJO oneLine ){
        if (networkType.equals(BasicConstants.GLOBAL)) {
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


    private Map<String, SaleInfoPerCityYear>  readByNetwork(List<JinpinOneLinePOJO> jinpinOneList) {
        Map<String, SaleInfoPerCityYear> typeSellInfo = new HashMap<>();

//        第一遍是为了读取： 销量 跟城市列表 / city and year

        for(JinpinOneLinePOJO oneLine : jinpinOneList ){

            String networkType = oneLine.networkType;
            //得到当前NetworkType类型的分类值
            String inputPriority = getPriorityFromInput( networkType, oneLine  );

            SaleInfoPerCityYear saleInfoPerCityYear = typeSellInfo.get(networkType);
            if( saleInfoPerCityYear == null ){
                saleInfoPerCityYear = new SaleInfoPerCityYear();
                typeSellInfo.put(networkType, saleInfoPerCityYear);
            }

            Map<Integer, Map<String,Integer>> saleNumberPerYear = saleInfoPerCityYear.saleNumberPerCityYear.get( oneLine.quYu );
            if( saleNumberPerYear == null){
                saleNumberPerYear = new HashMap<>();
                saleInfoPerCityYear.saleNumberPerCityYear.put(oneLine.quYu, saleNumberPerYear);
            }

            Map<String, Integer> saleNumberMap = saleNumberPerYear.get(oneLine.year);
            if( saleNumberMap == null ){
                saleNumberMap = new HashMap<> ();
                saleNumberPerYear.put( oneLine.year, saleNumberMap);
            }

            int saleNumber =  saleNumberMap.get(inputPriority)== null?0:  saleNumberMap.get(inputPriority);
            saleNumberMap.put(inputPriority, saleNumber+ oneLine.saleNumber );

            //Insert total SaleNumber
            Map<Integer, Map<String,Set<String>>> sellerNamePerYear = saleInfoPerCityYear.sellerNameSetPerCityYear.get( oneLine.quYu );
            if( sellerNamePerYear == null ){
                sellerNamePerYear = new HashMap<>();
                saleInfoPerCityYear.sellerNameSetPerCityYear.put(oneLine.quYu, sellerNamePerYear);
            }

            //Insert total seller Number;
            Map<String, Set<String>> namesPerPriority =  sellerNamePerYear.get( oneLine.year );
            if( namesPerPriority==null){
                namesPerPriority = new HashMap<>();
                sellerNamePerYear.put(oneLine.year, namesPerPriority);
            }

            Set<String> names = namesPerPriority.get( inputPriority );
            if( names==null){
                names = new HashSet<>();
                namesPerPriority.put( inputPriority, names);
            }
            names.add( oneLine.sellerName );
        }
//        Second: calculate the SellerNumber, and average SellerNumber.
        for( String networkType : typeSellInfo.keySet()){
            SaleInfoPerCityYear saleInfoPerCityYear = typeSellInfo.get(networkType);
            if( saleInfoPerCityYear == null ){
                continue;
            }
            Map<String, Map<Integer, Map<String,Set<String>>>> sellerNamePerCityYear = saleInfoPerCityYear.sellerNameSetPerCityYear;

            for( String cityName : sellerNamePerCityYear.keySet()){
                //parse the input.
                Map<Integer, Map<String,Set<String>>> sellerNameSetPerYear =  saleInfoPerCityYear.sellerNameSetPerCityYear.get(cityName);
                if( sellerNameSetPerYear==null || sellerNameSetPerYear.size()==0){
                    continue;
                }
                //prepare for output
                Map<Integer, Map<String,Integer>> sellerNumberYearMap = saleInfoPerCityYear.sellerNumberPerCityYear.get(cityName);
                if( sellerNumberYearMap ==null ){
                    sellerNumberYearMap = new HashMap<>();
                    saleInfoPerCityYear.sellerNumberPerCityYear.put(cityName, sellerNumberYearMap);
                }
                Map<Integer, Map<String,Double>> averageSaleNumberYearMap = saleInfoPerCityYear.averageSaleNumberPerCityYear.get(cityName);
                if( averageSaleNumberYearMap ==null ){
                    averageSaleNumberYearMap = new HashMap<>();
                    saleInfoPerCityYear.averageSaleNumberPerCityYear.put(cityName, averageSaleNumberYearMap);
                }

                Map<Integer, Map<String,Integer>> saleNumberPerYear =  saleInfoPerCityYear.saleNumberPerCityYear.get(cityName);
                if( saleNumberPerYear==null){
                    System.err.println("This should be impossible!");
                    saleNumberPerYear = new HashMap<>();
                }


                for( int i=2012; i<=2018; i++){
                    Map<String,Set<String>> sellerNameSetPerPriority = sellerNameSetPerYear.get(i);
                    if( sellerNameSetPerPriority == null ){
                        System.err.print("miss year " + i + "  data for city " + cityName);
                        continue;
                    }

                    //Input source for totalSaleNumberMap
                    Map<String, Integer> saleNumberPerPriority =  saleNumberPerYear.get(i);
                    if( saleNumberPerPriority == null ){
                        saleNumberPerPriority = new HashMap<>();
                        saleNumberPerYear.put(i, saleNumberPerPriority);
                    }

                    //output for sellerNumber
                    Map<String,Integer> sellerNumberPerPriority = sellerNumberYearMap.get(i);
                    if( sellerNumberPerPriority ==null ){
                        sellerNumberPerPriority = new HashMap<>();
                        sellerNumberYearMap.put(i, sellerNumberPerPriority);
                    }



                    //output for average SaleNumber
                    Map<String,Double> averageSaleNumberPerPriority =  averageSaleNumberYearMap.get(i);
                    if(averageSaleNumberPerPriority==null){
                        averageSaleNumberPerPriority = new HashMap<>();
                        averageSaleNumberYearMap.put(i, averageSaleNumberPerPriority);
                    }

                    for(String priority : sellerNameSetPerPriority.keySet() ){
                        Set<String> names =  sellerNameSetPerPriority.get(priority);
                        sellerNumberPerPriority.put( priority, names.size());

                        int saleNumberForPriority = saleNumberPerPriority.get(priority)==null?0:saleNumberPerPriority.get(priority);
                        double averageSaleNumber =  (double)saleNumberForPriority/(double) names.size();
                        averageSaleNumber = Double.parseDouble(BasicConstants.DecimalFormat.format(averageSaleNumber));
                        averageSaleNumberPerPriority.put(priority, averageSaleNumber);
                    }

                }
            }
        }
        return typeSellInfo;
    }

    private Map<String, SaleInfoPerCityYear>  readByFunction(List<JinpinOneLinePOJO> jinpinOneList) {
        Map<String, SaleInfoPerCityYear> typeSellInfo = new HashMap<>();

//        第一遍是为了读取： 销量 跟城市列表 / city and year

        for(JinpinOneLinePOJO oneLine : jinpinOneList ){

            String networkType = oneLine.functionType;
            //得到当前NetworkType类型的分类值
            String inputPriority = getPriorityFromInput( networkType, oneLine  );

            SaleInfoPerCityYear saleInfoPerCityYear = typeSellInfo.get(networkType);
            if( saleInfoPerCityYear == null ){
                saleInfoPerCityYear = new SaleInfoPerCityYear();
                typeSellInfo.put(networkType, saleInfoPerCityYear);
            }

            Map<Integer, Map<String,Integer>> saleNumberPerYear = saleInfoPerCityYear.saleNumberPerCityYear.get( oneLine.quYu );
            if( saleNumberPerYear == null){
                saleNumberPerYear = new HashMap<>();
                saleInfoPerCityYear.saleNumberPerCityYear.put(oneLine.quYu, saleNumberPerYear);
            }

            Map<String, Integer> saleNumberMap = saleNumberPerYear.get(oneLine.year);
            if( saleNumberMap == null ){
                saleNumberMap = new HashMap<> ();
                saleNumberPerYear.put( oneLine.year, saleNumberMap);
            }

            int saleNumber =  saleNumberMap.get(inputPriority)== null?0:  saleNumberMap.get(inputPriority);
            saleNumberMap.put(inputPriority, saleNumber+ oneLine.saleNumber );

            //Insert total SaleNumber
            Map<Integer, Map<String,Set<String>>> sellerNamePerYear = saleInfoPerCityYear.sellerNameSetPerCityYear.get( oneLine.quYu );
            if( sellerNamePerYear == null ){
                sellerNamePerYear = new HashMap<>();
                saleInfoPerCityYear.sellerNameSetPerCityYear.put(oneLine.quYu, sellerNamePerYear);
            }

            //Insert total seller Number;
            Map<String, Set<String>> namesPerPriority =  sellerNamePerYear.get( oneLine.year );
            if( namesPerPriority==null){
                namesPerPriority = new HashMap<>();
                sellerNamePerYear.put(oneLine.year, namesPerPriority);
            }

            Set<String> names = namesPerPriority.get( inputPriority );
            if( names==null){
                names = new HashSet<>();
                namesPerPriority.put( inputPriority, names);
            }
            names.add( oneLine.sellerName );
        }
//        Second: calculate the SellerNumber, and average SellerNumber.
        for( String networkType : typeSellInfo.keySet()){
            SaleInfoPerCityYear saleInfoPerCityYear = typeSellInfo.get(networkType);
            if( saleInfoPerCityYear == null ){
                continue;
            }
            Map<String, Map<Integer, Map<String,Set<String>>>> sellerNamePerCityYear = saleInfoPerCityYear.sellerNameSetPerCityYear;

            for( String cityName : sellerNamePerCityYear.keySet()){
                //parse the input.
                Map<Integer, Map<String,Set<String>>> sellerNameSetPerYear =  saleInfoPerCityYear.sellerNameSetPerCityYear.get(cityName);
                if( sellerNameSetPerYear==null || sellerNameSetPerYear.size()==0){
                    continue;
                }
                //prepare for output
                Map<Integer, Map<String,Integer>> sellerNumberYearMap = saleInfoPerCityYear.sellerNumberPerCityYear.get(cityName);
                if( sellerNumberYearMap ==null ){
                    sellerNumberYearMap = new HashMap<>();
                    saleInfoPerCityYear.sellerNumberPerCityYear.put(cityName, sellerNumberYearMap);
                }
                Map<Integer, Map<String,Double>> averageSaleNumberYearMap = saleInfoPerCityYear.averageSaleNumberPerCityYear.get(cityName);
                if( averageSaleNumberYearMap ==null ){
                    averageSaleNumberYearMap = new HashMap<>();
                    saleInfoPerCityYear.averageSaleNumberPerCityYear.put(cityName, averageSaleNumberYearMap);
                }

                Map<Integer, Map<String,Integer>> saleNumberPerYear =  saleInfoPerCityYear.saleNumberPerCityYear.get(cityName);
                if( saleNumberPerYear==null){
                    System.err.println("This should be impossible!");
                    saleNumberPerYear = new HashMap<>();
                }


                for( int i=2012; i<=2018; i++){
                    int sellerNumber = 0;
                    Map<String,Set<String>> sellerNameSetPerPriority = sellerNameSetPerYear.get(i);
                    if( sellerNameSetPerPriority == null ){
                        System.err.print(" miss year " + i + "  data for city " + cityName);
                        continue;
                    }

                    //Input source for totalSaleNumberMap
                    Map<String, Integer> saleNumberPerPriority =  saleNumberPerYear.get(i);
                    if( saleNumberPerPriority == null ){
                        saleNumberPerPriority = new HashMap<>();
                        saleNumberPerYear.put(i, saleNumberPerPriority);
                    }

                    //output for sellerNumber
                    Map<String,Integer> sellerNumberPerPriority = sellerNumberYearMap.get(i);
                    if( sellerNumberPerPriority ==null ){
                        sellerNumberPerPriority = new HashMap<>();
                        sellerNumberYearMap.put(i, sellerNumberPerPriority);
                    }



                    //output for average SaleNumber
                    Map<String,Double> averageSaleNumberPerPriority =  averageSaleNumberYearMap.get(i);
                    if(averageSaleNumberPerPriority==null){
                        averageSaleNumberPerPriority = new HashMap<>();
                        averageSaleNumberYearMap.put(i, averageSaleNumberPerPriority);
                    }

                    for(String priority : sellerNameSetPerPriority.keySet() ){
                        Set<String> names =  sellerNameSetPerPriority.get(priority);
                        sellerNumberPerPriority.put( priority, names.size());

                        int saleNumberForPriority = saleNumberPerPriority.get(priority)==null?0:saleNumberPerPriority.get(priority);
                        double averageSaleNumber =  (double)saleNumberForPriority/(double) names.size();
                        averageSaleNumber = Double.parseDouble(BasicConstants.DecimalFormat.format(averageSaleNumber));
                        averageSaleNumberPerPriority.put(priority, averageSaleNumber);
                    }

                }
            }
        }
        return typeSellInfo;

    }

    public class SaleInfoPerCityYear {
        //all String key is "quyuName"
        public Map<String, Map<Integer, Map<String, Integer>>> saleNumberPerCityYear = new HashMap<>();
        public Map<String, Map<Integer, Map<String,Set<String>>>> sellerNameSetPerCityYear = new HashMap<>();
        public Map<String, Map<Integer, Map<String,Integer>>> sellerNumberPerCityYear = new HashMap<>();
        public Map<String, Map<Integer, Map<String,Double>>> averageSaleNumberPerCityYear = new HashMap<>();
    }
}
