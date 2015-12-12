package com.qiaosheng.app.service.impl.read;

import com.qiaosheng.common.utils.BasicCommonValues;
import com.qiaosheng.common.utils.BasicConfiguredValues;
import com.qiaosheng.common.utils.BasicConstants;
import com.qiaosheng.common.utils.CityInfoWithPriority;
import com.qiaosheng.common.utils.CityPriority;
import com.qiaosheng.common.utils.ReadParseHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This must be before the "ReadJinpinOneLineFromSource".
 */
public class ReadAllQuyuInfoFromSource {

    private static final Logger log = LoggerFactory.getLogger(ReadAllQuyuInfoFromSource.class);

    public static void main(String [] args){
        ReadAllQuyuInfoFromSource readAllQuyuInfoFromSource = new ReadAllQuyuInfoFromSource();
        try {
            readAllQuyuInfoFromSource.parseGlobalQuyuPriorities("d:\\abc.txt");
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void readQuyuPriority(){
        String quyuPriorityFileName = BasicConfiguredValues.JINPIN_DIRECTORY + "区域分类明细.txt";

        try {
            parseGlobalQuyuPriorities(quyuPriorityFileName);
        } catch (Exception e) {
            log.error("Failed to read Quyu priority file.", e);
        }
    }

    private void parseGlobalQuyuPriorities(String fileName) throws Exception{

        FileInputStream f = new FileInputStream(fileName);

        // DataInputStream dr = new DataInputStream(f);
        BufferedReader dr=new BufferedReader(new InputStreamReader(f ,"UTF-16"));
        String line = null;
        int i = 0;
        List<CityInfoWithPriority> cityPriorityList = new ArrayList<>();
        while((line=dr.readLine())!=null){
            i++;
            try {
                CityInfoWithPriority cityInfoWithPriority = parseQuyu(line.trim());
                if( cityInfoWithPriority!=null )
                    cityPriorityList.add(cityInfoWithPriority);
            } catch (Exception e) {
                System.out.println("Failed to parse   第" + i + " 行，内容是：" + line.trim());
                log.error("Failed to parse line {} of file {} with content {}", i, fileName, line.trim());
                e.printStackTrace();
            }
        }
        parseCityNumberPerPriority( cityPriorityList );
        log.info("Has read {} lines for All City Priority", i);
        BasicCommonValues.allCityInfowithPriority = cityPriorityList;

    }

    private CityInfoWithPriority parseQuyu(String line) {
        if(StringUtils.isEmpty(line)){
            return null;
        }
        if( line.startsWith("战区")){
            return null;
        }
        String[] values = line.split("\t");
        CityInfoWithPriority cityPriorityInfo = null;
        try {
            cityPriorityInfo = new CityInfoWithPriority();
            cityPriorityInfo.warqu = values[0];
            cityPriorityInfo.daQu = values[1];
            cityPriorityInfo.quYu = values[2];
            cityPriorityInfo.city = values[3];
            //The cache in order to add city from JinpinTable which is not defined in this table.
            BasicCommonValues.cityNameSetInQuyuTable.add(cityPriorityInfo.city);

            CityPriority cityPriority = new CityPriority();
            cityPriorityInfo.cityPriority = cityPriority;
            cityPriority.setCityPriorityOfGlobalNetwork(values[4]);

            try {
                cityPriority.setCityPriorityOfPinbanNetwork(ReadParseHelper.parseForPriority(values[5]));
            } catch (Exception e) {
                cityPriority.setCityPriorityOfPinbanNetwork("d");
            }
            try {
                cityPriority.setCityPriorityOfQianyinNetwork(ReadParseHelper.parseForPriority(values[6]));
            } catch (Exception e) {
                cityPriority.setCityPriorityOfQianyinNetwork("d");
            }
            try {
                cityPriority.setCityPriorityOfZixieNetwork(ReadParseHelper.parseForPriority(values[7]));
            } catch (Exception e) {
                cityPriority.setCityPriorityOfZixieNetwork("d");
            }
            try {
                cityPriority.setCityPriorityOfRoadNetwork(ReadParseHelper.parseForPriority(values[8]));
            } catch (Exception e) {
                cityPriority.setCityPriorityOfRoadNetwork("d");
            }
            try {
                cityPriority.setCityPriorityOfMachineNetwork(ReadParseHelper.parseForPriority(values[9]));
            } catch (ArrayIndexOutOfBoundsException e) {
                cityPriority.setCityPriorityOfMachineNetwork("d");
            }
        } catch (Exception e) {
            log.error("Failed to parse the file of all city Info with priority. Line: {}",line, e);
        }
        return cityPriorityInfo;
    }


    private void parseCityNumberPerPriority(List<CityInfoWithPriority> cityInfoWithPriorityList){

        //key is: quYu -> type -> priority
        Map<String, Map<String,Map<String,  Integer>>> quyuCityNumberPerPriority = new HashMap<>();

        for( CityInfoWithPriority cityInfoWithPriority : cityInfoWithPriorityList ){
            Map<String,Map<String,  Integer>> cityNumberPerTypePriority = quyuCityNumberPerPriority.get( cityInfoWithPriority.quYu );
            if( cityNumberPerTypePriority ==null ){
                cityNumberPerTypePriority = new HashMap<>();
                quyuCityNumberPerPriority.put(cityInfoWithPriority.quYu, cityNumberPerTypePriority);
            }

            Map<String, Integer> cityNumberPerPriorityOfGlobal = cityNumberPerTypePriority.get( BasicConstants.GLOBAL);
            if( cityNumberPerPriorityOfGlobal==null){
                cityNumberPerPriorityOfGlobal = new HashMap<>();
                cityNumberPerTypePriority.put(BasicConstants.GLOBAL, cityNumberPerPriorityOfGlobal );
            }
            Integer cityNumber = cityNumberPerPriorityOfGlobal.get(cityInfoWithPriority.cityPriority.getCityPriorityOfGlobalNetwork());
            cityNumberPerPriorityOfGlobal.put(cityInfoWithPriority.cityPriority.getCityPriorityOfGlobalNetwork(), cityNumber==null?1:(cityNumber+1) );


            Map<String, Integer> cityNumberPerPriorityOfZixie = cityNumberPerTypePriority.get( BasicConstants.TYPE_ZIXIE);
            if( cityNumberPerPriorityOfZixie==null){
                cityNumberPerPriorityOfZixie = new HashMap<>();
                cityNumberPerTypePriority.put(BasicConstants.TYPE_ZIXIE, cityNumberPerPriorityOfZixie );
            }
            Integer cityNumberOfZixie = cityNumberPerPriorityOfZixie.get(cityInfoWithPriority.cityPriority.getCityPriorityOfZixieNetwork());
            cityNumberPerPriorityOfZixie.put(cityInfoWithPriority.cityPriority.getCityPriorityOfZixieNetwork(), cityNumberOfZixie==null?1:(cityNumberOfZixie+1) );


            Map<String, Integer> cityNumberPerPriorityOfQianyin = cityNumberPerTypePriority.get( BasicConstants.TYPE_QIANYIN);
            if( cityNumberPerPriorityOfQianyin==null){
                cityNumberPerPriorityOfQianyin = new HashMap<>();
                cityNumberPerTypePriority.put(BasicConstants.TYPE_QIANYIN, cityNumberPerPriorityOfQianyin );
            }
            Integer cityNumberOfQianyin = cityNumberPerPriorityOfQianyin.get(cityInfoWithPriority.cityPriority.getCityPriorityOfQianyinNetwork());
            cityNumberPerPriorityOfQianyin.put(cityInfoWithPriority.cityPriority.getCityPriorityOfQianyinNetwork(), cityNumberOfQianyin==null?1:(cityNumberOfQianyin+1) );


            Map<String, Integer> cityNumberPerPriorityOfPianban = cityNumberPerTypePriority.get( BasicConstants.TYPE_PINBAN);
            if( cityNumberPerPriorityOfPianban==null){
                cityNumberPerPriorityOfPianban = new HashMap<>();
                cityNumberPerTypePriority.put(BasicConstants.TYPE_PINBAN, cityNumberPerPriorityOfPianban );
            }
            Integer cityNumberOfPinban = cityNumberPerPriorityOfPianban.get(cityInfoWithPriority.cityPriority.getCityPriorityOfPinbanNetwork());
            cityNumberPerPriorityOfPianban.put(cityInfoWithPriority.cityPriority.getCityPriorityOfPinbanNetwork(), cityNumberOfPinban==null?1:(cityNumberOfPinban+1) );


            Map<String, Integer> cityNumberPerPriorityOfRoad = cityNumberPerTypePriority.get( BasicConstants.NETWORKTYPE_ROAD);
            if( cityNumberPerPriorityOfRoad==null){
                cityNumberPerPriorityOfRoad = new HashMap<>();
                cityNumberPerTypePriority.put(BasicConstants.NETWORKTYPE_ROAD, cityNumberPerPriorityOfRoad );
            }
            Integer cityNumberOfRoad = cityNumberPerPriorityOfRoad.get(cityInfoWithPriority.cityPriority.getCityPriorityOfRoadNetwork());
            cityNumberPerPriorityOfRoad.put(cityInfoWithPriority.cityPriority.getCityPriorityOfRoadNetwork(), cityNumberOfRoad==null?1:(cityNumberOfRoad+1) );


            Map<String, Integer> cityNumberPerPriorityOfMachine = cityNumberPerTypePriority.get( BasicConstants.NETWORKTYPE_MACHINE);
            if( cityNumberPerPriorityOfMachine==null){
                cityNumberPerPriorityOfMachine = new HashMap<>();
                cityNumberPerTypePriority.put(BasicConstants.NETWORKTYPE_MACHINE, cityNumberPerPriorityOfMachine );
            }
            Integer cityNumberOfMachine = cityNumberPerPriorityOfMachine.get(cityInfoWithPriority.cityPriority.getCityPriorityOfMachineNetwork());
            cityNumberPerPriorityOfMachine.put(cityInfoWithPriority.cityPriority.getCityPriorityOfMachineNetwork(), cityNumberOfMachine==null?1:(cityNumberOfMachine+1) );

        }
        BasicCommonValues.quyuCityNumberPerPriority =  quyuCityNumberPerPriority;

    }



}
