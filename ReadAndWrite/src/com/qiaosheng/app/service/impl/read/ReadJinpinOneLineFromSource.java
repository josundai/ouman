package com.qiaosheng.app.service.impl.read;

import com.qiaosheng.common.utils.BasicCommonValues;
import com.qiaosheng.common.utils.BasicConfiguredValues;
import com.qiaosheng.common.utils.BasicConstants;
import com.qiaosheng.app.service.IBasicServiceInterface;
import com.qiaosheng.common.pojo.model.JinpinOneLinePOJO;
import com.qiaosheng.common.utils.CityBasicInfoPerYear;
import com.qiaosheng.common.utils.CityPriority;
import com.qiaosheng.common.utils.QuyuBasicInfo;
import com.qiaosheng.common.utils.ReadParseHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午2:57
 * To change this template use File | Settings | File Templates.
 * This must be after ReadAllQuyuInfoFromSource.
 */
@Component
public class ReadJinpinOneLineFromSource implements IBasicServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(ReadJinpinOneLineFromSource.class);



    public static List<JinpinOneLinePOJO> readJinpinInputData= new ArrayList<>();
    public static Set<String> sellerNameSet = new HashSet<>();

    public static Map<String, CityBasicInfoPerYear> readCityBasicInfoMap = new HashMap<>();
    // warQu: <quYuName: quYu>
    public static Map<String,Map<String, QuyuBasicInfo>> quyuBasicInfoPerWarQuMap = new HashMap<>();




    private void generateJinpinRockData(String line){
        String oneLine = line.trim();
        if( StringUtils.isEmpty(oneLine))
            return;
        if( oneLine.startsWith("年度") || oneLine.startsWith("挂靠")){
            //第一行
            return;
        }
        /*
        年度	月份	战区分类	业务分类	网络分类	大区	区域	品牌	渠道	地区
        城市类别	分系	驱动	功能	子品牌	销量	档次	中重型分类	总体网络分类	公路车网分类
        工程车网分类	平板分类	牵引分类	自卸分类	形象店级别	销服一体情况	金融服务情况	保险代办	配件销售	车队
        挂靠"	二手车业务	二级维护	实体物流	钢材/煤炭	房地产	餐饮娱乐	证券投资
         */

        String[] jinpinSaleData = oneLine.split("\t");
        JinpinOneLinePOJO oneJinpinInputLine = new JinpinOneLinePOJO();

        try {
            oneJinpinInputLine.year =  Integer.valueOf(jinpinSaleData[0]);
        } catch (NumberFormatException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        oneJinpinInputLine.month =  Integer.valueOf(jinpinSaleData[1]);
        oneJinpinInputLine.warQu =  jinpinSaleData[2];
        oneJinpinInputLine.businessType =  jinpinSaleData[3];
        oneJinpinInputLine.networkType =  jinpinSaleData[4];
        oneJinpinInputLine.daQu =  jinpinSaleData[5];
        oneJinpinInputLine.quYu  =  jinpinSaleData[6];
        oneJinpinInputLine.brand  =  jinpinSaleData[7];
        oneJinpinInputLine.sellerName =  jinpinSaleData[8];
        oneJinpinInputLine.city =  jinpinSaleData[9];

        oneJinpinInputLine.cityType =  jinpinSaleData[10];
        oneJinpinInputLine.subType =  jinpinSaleData[11];
        oneJinpinInputLine.driveMode =  jinpinSaleData[12];
        oneJinpinInputLine.functionType =  jinpinSaleData[13];
        oneJinpinInputLine.subBrand =  jinpinSaleData[14];
        oneJinpinInputLine.saleNumber =  Integer.parseInt(jinpinSaleData[15]);
        oneJinpinInputLine.grade =  jinpinSaleData[16];
        oneJinpinInputLine.heavyOrNot =  jinpinSaleData[17];

        try {
            oneJinpinInputLine.globalNetworkTypePriority =  ReadParseHelper.parseForPriority(jinpinSaleData[18].toLowerCase());
        } catch (Exception e) {
            oneJinpinInputLine.globalNetworkTypePriority = BasicConstants.SPECIAL_SELL_TYPE_D;
        }
        try {
            oneJinpinInputLine.roadNetworkTypePriority =  ReadParseHelper.parseForPriority(jinpinSaleData[19].toLowerCase());
        } catch (Exception e) {
            oneJinpinInputLine.roadNetworkTypePriority =  BasicConstants.SPECIAL_SELL_TYPE_D;
        }

        try {
            oneJinpinInputLine.machineNetworkTypePriority =  ReadParseHelper.parseForPriority(jinpinSaleData[20].toLowerCase());
        } catch (Exception e) {
            oneJinpinInputLine.machineNetworkTypePriority = BasicConstants.SPECIAL_SELL_TYPE_D;
        }
        try {
            oneJinpinInputLine.pinbanTypePriority =  ReadParseHelper.parseForPriority(jinpinSaleData[21].toLowerCase());
        } catch (Exception e) {
            oneJinpinInputLine.pinbanTypePriority = BasicConstants.SPECIAL_SELL_TYPE_D;
        }
        try {
            oneJinpinInputLine.qianyinTypePriority = ReadParseHelper.parseForPriority( jinpinSaleData[22].toLowerCase());
        } catch (Exception e) {
            oneJinpinInputLine.qianyinTypePriority = BasicConstants.SPECIAL_SELL_TYPE_D;
        }
        try {
            oneJinpinInputLine.zixieTypePriority =  ReadParseHelper.parseForPriority(jinpinSaleData[23].toLowerCase());
        } catch (Exception e) {
            oneJinpinInputLine.zixieTypePriority = BasicConstants.SPECIAL_SELL_TYPE_D;
        }

        logWarnForPriority(oneJinpinInputLine.globalNetworkTypePriority);
        logWarnForPriority(oneJinpinInputLine.roadNetworkTypePriority);
        logWarnForPriority(oneJinpinInputLine.machineNetworkTypePriority);
        logWarnForPriority(oneJinpinInputLine.pinbanTypePriority);
        logWarnForPriority(oneJinpinInputLine.qianyinTypePriority);
        logWarnForPriority(oneJinpinInputLine.zixieTypePriority);

        try {
            if( StringUtils.isEmpty( jinpinSaleData[24])){
                oneJinpinInputLine.sellSpecialLevel = BasicConstants.SPECIAL_SELL_TYPE_Empty;
            }else{
                oneJinpinInputLine.sellSpecialLevel = jinpinSaleData[24].trim();
            }
        } catch (Exception e) {
        }
        try {
            oneJinpinInputLine.sellAndService =  ReadParseHelper.parseForBoolean(jinpinSaleData[25]);
        } catch (Exception e) {   }
        try {
            oneJinpinInputLine.finance = ReadParseHelper.parseForBoolean( jinpinSaleData[26]);
        } catch (Exception e) {    }
        try {
            oneJinpinInputLine.insuranceBusiness =  ReadParseHelper.parseForBoolean(jinpinSaleData[27]);
        } catch (Exception e) {  }
        try {
            oneJinpinInputLine.sellingReplacement =  ReadParseHelper.parseForBoolean(jinpinSaleData[28]);
        } catch (Exception e) { }
        try {
            oneJinpinInputLine.carTeamManage =  ReadParseHelper.parseForBoolean(jinpinSaleData[29]);
        } catch (Exception e) {    }
        try {
            oneJinpinInputLine.secondHandBusiness = ReadParseHelper.parseForBoolean( jinpinSaleData[30]);
        } catch (Exception e) { }
        try {
            oneJinpinInputLine.secondSustaining =  ReadParseHelper.parseForBoolean(jinpinSaleData[31]);
        } catch (Exception e) {  }
        try {
            oneJinpinInputLine.logistics = ReadParseHelper.parseForBoolean( jinpinSaleData[32]);
        } catch (Exception e) { }
        try {
            oneJinpinInputLine.ironCoal = ReadParseHelper.parseForBoolean( jinpinSaleData[33]);
        } catch (Exception e) { }
        try {
            oneJinpinInputLine.house =  ReadParseHelper.parseForBoolean(jinpinSaleData[34]);
        } catch (Exception e) { }
        try {
            oneJinpinInputLine.restaurant = ReadParseHelper.parseForBoolean( jinpinSaleData[35]);
        } catch (Exception e) {  }
        try {
            oneJinpinInputLine.investment =  ReadParseHelper.parseForBoolean(jinpinSaleData[36]);
        } catch (Exception e) { }

        // 数据的再加工
        BasicCommonValues.brandList.add(oneJinpinInputLine.brand) ;
        verifyWhetherInCityNumberMap(oneJinpinInputLine);


        Map<String, QuyuBasicInfo> quyuBasicInfoMapofWarQu = quyuBasicInfoPerWarQuMap.get(oneJinpinInputLine.warQu);
        if( quyuBasicInfoMapofWarQu == null ){
            quyuBasicInfoMapofWarQu = new HashMap<>();
            quyuBasicInfoPerWarQuMap.put(oneJinpinInputLine.warQu, quyuBasicInfoMapofWarQu);
        }

        QuyuBasicInfo quyuBasicInfo = quyuBasicInfoMapofWarQu.get(oneJinpinInputLine.quYu);
        if( quyuBasicInfo == null){
            quyuBasicInfo = new QuyuBasicInfo( oneJinpinInputLine.quYu, oneJinpinInputLine.warQu);
            quyuBasicInfoMapofWarQu.put( oneJinpinInputLine.quYu, quyuBasicInfo);
        }
        quyuBasicInfo.addCityName(oneJinpinInputLine.city);

        CityBasicInfoPerYear cityBasicInfo = readCityBasicInfoMap.get(oneJinpinInputLine.city);
        if( cityBasicInfo == null ){
            cityBasicInfo = new CityBasicInfoPerYear(oneJinpinInputLine.city);
            cityBasicInfo.cityType = oneJinpinInputLine.cityType;
            cityBasicInfo.daQu = oneJinpinInputLine.daQu;
            cityBasicInfo.quYu = oneJinpinInputLine.quYu;
            cityBasicInfo.warQu = oneJinpinInputLine.warQu;

//            Map<Integer, CityPriority> yearPriority = cityBasicInfo.
            Map<Integer, CityPriority> yearPriority = cityBasicInfo.getYearPriority();
            CityPriority cityPriority = new CityPriority( oneJinpinInputLine.getGlobalNetworkTypePriority(), oneJinpinInputLine.getRoadNetworkTypePriority(),oneJinpinInputLine.getMachineNetworkTypePriority(),oneJinpinInputLine.getPinbanTypePriority() , oneJinpinInputLine.getQianyinTypePriority(), oneJinpinInputLine.getZixieTypePriority()  );
            yearPriority.put( oneJinpinInputLine.year,cityPriority );
            readCityBasicInfoMap.put( oneJinpinInputLine.city, cityBasicInfo);
        }else{
            //            Map<Integer, CityPriority> yearPriority = cityBasicInfo.
            Map<Integer, CityPriority> yearPriority = cityBasicInfo.getYearPriority();
            CityPriority cityPriority = new CityPriority( oneJinpinInputLine.getGlobalNetworkTypePriority(), oneJinpinInputLine.getRoadNetworkTypePriority(),oneJinpinInputLine.getMachineNetworkTypePriority(),oneJinpinInputLine.getPinbanTypePriority() , oneJinpinInputLine.getQianyinTypePriority(), oneJinpinInputLine.getZixieTypePriority()  );
            yearPriority.put( oneJinpinInputLine.year,cityPriority );
            readCityBasicInfoMap.put( oneJinpinInputLine.city, cityBasicInfo);
        }

        sellerNameSet.add(oneJinpinInputLine.getSellerName());
        readJinpinInputData.add(oneJinpinInputLine);
    }

    private void verifyWhetherInCityNumberMap(JinpinOneLinePOJO oneJinpinInputLine){
        if( BasicCommonValues.cityNameSetInQuyuTable.contains(oneJinpinInputLine.city)){
            return;
        }
        log.warn("City {} is not defined in table 区域分类明细. Need update cityNumber map", oneJinpinInputLine.city );

        Map<String,Map<String,  Integer>> cityNumberPerTypePriority = BasicCommonValues.quyuCityNumberPerPriority.get(oneJinpinInputLine.quYu);
        if( cityNumberPerTypePriority==null ){
            cityNumberPerTypePriority = new HashMap<>();
            BasicCommonValues.quyuCityNumberPerPriority.put(oneJinpinInputLine.quYu, cityNumberPerTypePriority);
            log.error("Quyu {} is not defined in table 区域分类明细!! Need check what happens!", oneJinpinInputLine.quYu );
        }

        Map<String,  Integer> cityNumPerPriorityOfGlobal = cityNumberPerTypePriority.get( BasicConstants.GLOBAL);
        if(cityNumPerPriorityOfGlobal==null){
            cityNumPerPriorityOfGlobal = new HashMap<>();
            cityNumberPerTypePriority.put(BasicConstants.GLOBAL, cityNumPerPriorityOfGlobal);
        }
        int numOfGlobal = cityNumPerPriorityOfGlobal.get(oneJinpinInputLine.globalNetworkTypePriority)==null?0:cityNumPerPriorityOfGlobal.get(oneJinpinInputLine.globalNetworkTypePriority);
        cityNumPerPriorityOfGlobal.put(oneJinpinInputLine.globalNetworkTypePriority, numOfGlobal+1);

        Map<String,  Integer> cityNumPerPriorityOfZixie = cityNumberPerTypePriority.get( BasicConstants.TYPE_ZIXIE);
        if(cityNumPerPriorityOfZixie==null){
            cityNumPerPriorityOfZixie = new HashMap<>();
            cityNumberPerTypePriority.put(BasicConstants.TYPE_ZIXIE, cityNumPerPriorityOfZixie);
        }
        int numOfZixie = cityNumPerPriorityOfZixie.get(oneJinpinInputLine.zixieTypePriority)==null?0:cityNumPerPriorityOfZixie.get(oneJinpinInputLine.zixieTypePriority);
        cityNumPerPriorityOfZixie.put(oneJinpinInputLine.zixieTypePriority, numOfZixie+1);

        Map<String,  Integer> cityNumPerPriorityOfPinban = cityNumberPerTypePriority.get( BasicConstants.TYPE_PINBAN);
        if(cityNumPerPriorityOfPinban==null){
            cityNumPerPriorityOfPinban = new HashMap<>();
            cityNumberPerTypePriority.put(BasicConstants.TYPE_PINBAN, cityNumPerPriorityOfPinban);
        }
        int numOfPinban = cityNumPerPriorityOfPinban.get(oneJinpinInputLine.pinbanTypePriority)==null?0:cityNumPerPriorityOfPinban.get(oneJinpinInputLine.pinbanTypePriority);
        cityNumPerPriorityOfPinban.put(oneJinpinInputLine.pinbanTypePriority, numOfPinban+1);

        Map<String,  Integer> cityNumPerPriorityOfQianyin = cityNumberPerTypePriority.get( BasicConstants.TYPE_QIANYIN);
        if(cityNumPerPriorityOfQianyin==null){
            cityNumPerPriorityOfQianyin = new HashMap<>();
            cityNumberPerTypePriority.put(BasicConstants.TYPE_QIANYIN, cityNumPerPriorityOfQianyin);
        }
        int numOfQianyin = cityNumPerPriorityOfQianyin.get(oneJinpinInputLine.qianyinTypePriority)==null?0:cityNumPerPriorityOfQianyin.get(oneJinpinInputLine.qianyinTypePriority);
        cityNumPerPriorityOfQianyin.put(oneJinpinInputLine.qianyinTypePriority, numOfQianyin+1);

        Map<String,  Integer> cityNumPerPriorityOfMachine = cityNumberPerTypePriority.get( BasicConstants.NETWORKTYPE_MACHINE);
        if(cityNumPerPriorityOfMachine==null){
            cityNumPerPriorityOfMachine = new HashMap<>();
            cityNumberPerTypePriority.put(BasicConstants.NETWORKTYPE_MACHINE, cityNumPerPriorityOfMachine);
        }
        int numOfMachine = cityNumPerPriorityOfMachine.get(oneJinpinInputLine.machineNetworkTypePriority)==null?0:cityNumPerPriorityOfMachine.get(oneJinpinInputLine.machineNetworkTypePriority);
        cityNumPerPriorityOfMachine.put(oneJinpinInputLine.machineNetworkTypePriority, numOfMachine+1);


        Map<String,  Integer> cityNumPerPriorityOfRoad = cityNumberPerTypePriority.get( BasicConstants.NETWORKTYPE_ROAD);
        if(cityNumPerPriorityOfRoad==null){
            cityNumPerPriorityOfRoad = new HashMap<>();
            cityNumberPerTypePriority.put(BasicConstants.NETWORKTYPE_ROAD, cityNumPerPriorityOfRoad);
        }
        int numOfRoad = cityNumPerPriorityOfRoad.get(oneJinpinInputLine.roadNetworkTypePriority)==null?0:cityNumPerPriorityOfRoad.get(oneJinpinInputLine.roadNetworkTypePriority);
        cityNumPerPriorityOfRoad.put(oneJinpinInputLine.roadNetworkTypePriority, numOfRoad+1);

        BasicCommonValues.cityNameSetInQuyuTable.add(oneJinpinInputLine.city);

    }



    private void logWarnForPriority(String priority){
        if( priority.contains("类")){
            log.warn("源数据的分类不是程序期望的a，b，c，d，而是含有\"类\"");
            priority.replaceAll("类","");
        }
    }

    private void readJinpin(String fileName) throws Exception{

        FileInputStream f = new FileInputStream(fileName);

        // DataInputStream dr = new DataInputStream(f);
        BufferedReader dr=new BufferedReader(new InputStreamReader(f ,"utf-16"));
        String line = null;
        int i = 0;
        while((line=dr.readLine())!=null){
            i++;
            try {
                generateJinpinRockData(line.trim());
            } catch (Exception e) {
                System.out.println("Failed to parse   第" + i + " 行，内容是：" + line.trim());
                log.error("Failed to parse line {} of file {} with content {}", i, fileName, line.trim());
                e.printStackTrace();
            }
        }

        BasicCommonValues.cityBasicInfoMap.putAll(readCityBasicInfoMap);
        BasicCommonValues.jinpinInputData.addAll(readJinpinInputData);

        List<QuyuBasicInfo> quyuBasicInfoList = new ArrayList<>();
        for(Map.Entry<String, Map<String, QuyuBasicInfo>> items:  quyuBasicInfoPerWarQuMap.entrySet() ){
            quyuBasicInfoList.addAll(items.getValue().values());
        }
        //TODO:
        BasicCommonValues.quYuBasicInfoListWithOrder = quyuBasicInfoList;
    }


    public void readAllJinpin(){
        for( int i=2012; i<= 2018; i++ ){
            String fileName = BasicConfiguredValues.JINPIN_DIRECTORY + i + "年竞品渠道开票汇总.txt";
            try {
                readJinpin( fileName );
            } catch (Exception e) {
                System.out.println( "cannot read the file " + fileName );
                e.printStackTrace();
            }
        }
    }



    public static Set<String> getSellerNameSet() {
        return sellerNameSet;
    }

    public static void setSellerNameSet(Set<String> sellerNameSet) {
        ReadJinpinOneLineFromSource.sellerNameSet = sellerNameSet;
    }


}

