package com.qiaosheng.app.service.impl.read;

import com.qiaosheng.app.service.IBasicServiceInterface;
import com.qiaosheng.common.pojo.model.OumanPOJO;
import com.qiaosheng.common.utils.BasicCommonValues;
import com.qiaosheng.common.utils.BasicConfiguredValues;
import com.qiaosheng.common.utils.ReadParseHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午2:57
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ReadOumanFromSource implements IBasicServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(ReadOumanFromSource.class);



    public static List<OumanPOJO> oumanInputData= new ArrayList<>();
//    public static Set<String> sellerNameSet = new HashSet<>();


    // warQu: <quYuName: quYu>


    public static void main(String[] args){
        ReadOumanFromSource readOumanFromSource = new ReadOumanFromSource();
        readOumanFromSource.readOuman();
        int a=0;

    }

    public void readOuman(){
//        for( int i=2012; i< 2018; i++ ){
        String fileName = BasicConfiguredValues.JINPIN_DIRECTORY + "欧曼经销商数据.txt";
        try {
            readOumanFile(fileName);
        } catch (Exception e) {
            log.error("Failed to read the file {}", fileName, e );
        }
//        }
    }

    private void readOumanFile(String fileName) throws Exception{

        FileInputStream f = new FileInputStream(fileName);

        // DataInputStream dr = new DataInputStream(f);
        BufferedReader dr=new BufferedReader(new InputStreamReader(f ,"utf-16"));
        String line = null;
        int i = 0;
        while((line=dr.readLine())!=null){
            i++;
            try {
//                if( i>=7)
                    generateOumanData(line.trim());
            } catch (Exception e) {

                log.error("Failed to parse line {} of file {} with content {}", i, fileName, line.trim());
                e.printStackTrace();
            }
        }
        log.info("Has read {} lines", i);

//        BasicCommonValues.cityBasicInfoMap = cityBasicInfoMap;
        BasicCommonValues.oumanInputData = oumanInputData;


    }

    private boolean parsetToBoolean(String input){
        if( input==null)
            return false;
        if( input.equals("Y") || input.equals("是")){
            return true;
        }
        return false;
    }

    private void generateOumanData(String line){
        String oneLine = line.trim();
        if( StringUtils.isEmpty(oneLine))
            return;
        if( oneLine.startsWith("欧曼品牌") || oneLine.startsWith("序号")|| oneLine.startsWith("汽车业务")||oneLine.startsWith("自己放款做分期") || oneLine.startsWith("挂靠")|| oneLine.startsWith("大区")|| oneLine.startsWith("曾用名")){
            //第一行
            return;
        }


        String[] jinpinSaleData = oneLine.split("\t");
        try{
            Integer.parseInt(jinpinSaleData[0]);
        }catch (Exception e){
            return;
        }
        OumanPOJO oumanPOJO = new OumanPOJO();
        String index = jinpinSaleData[0];
        oumanPOJO.sellerName = jinpinSaleData[7];
        oumanPOJO.sellerSimpleName = jinpinSaleData[8];
        oumanPOJO.sellerUsedName = jinpinSaleData[9];
        oumanPOJO.sellerUsedJoinTime = jinpinSaleData[10];
        oumanPOJO.sellerJoinTime = jinpinSaleData[11];

        oumanPOJO.businessType = jinpinSaleData[13];
        oumanPOJO.joinType = jinpinSaleData[14];
        oumanPOJO.manageType = jinpinSaleData[15];
        oumanPOJO.businessMode = jinpinSaleData[16];
        oumanPOJO.secondFirstSeller = jinpinSaleData[17];

        oumanPOJO.oumanRoadNetwork = jinpinSaleData[18];
        oumanPOJO.oumanMachineNetwork = jinpinSaleData[19];

        oumanPOJO.sellerRoadNetwork = jinpinSaleData[20];
        oumanPOJO.sellerMachineNetwork = jinpinSaleData[21];
        oumanPOJO.authedProductLine = jinpinSaleData[22];
        oumanPOJO.authedArea = jinpinSaleData[23];

        oumanPOJO.onlySellSpecial = jinpinSaleData[24];

        oumanPOJO.oumanShopOldStandard = jinpinSaleData[25];
        oumanPOJO.oumanShopNewStandard = jinpinSaleData[26];
        oumanPOJO.oumanShopAddress = jinpinSaleData[27];
        oumanPOJO.isSellAndService = ReadParseHelper.parseForBoolean(jinpinSaleData[28]);
        oumanPOJO.serviceStar = jinpinSaleData[29];
        //TODO: make sure the "source" of is finance.
        oumanPOJO.isFinance   = ReadParseHelper.parseForBoolean(jinpinSaleData[30])||ReadParseHelper.parseForBoolean(jinpinSaleData[31])||ReadParseHelper.parseForBoolean(jinpinSaleData[32])||ReadParseHelper.parseForBoolean(jinpinSaleData[33])||ReadParseHelper.parseForBoolean(jinpinSaleData[34]);
        oumanPOJO.isSellingReplacement = ReadParseHelper.parseForBoolean(jinpinSaleData[70]);
        oumanPOJO.isSecondHandBusiness = ReadParseHelper.parseForBoolean(jinpinSaleData[72]);
        oumanPOJO.isCarTeamManage =ReadParseHelper.parseForBoolean( jinpinSaleData[71]);
        oumanPOJO.isInsuranceBusiness = ReadParseHelper.parseForBoolean(jinpinSaleData[69]);
        oumanPOJO.isSecondSustaining = ReadParseHelper.parseForBoolean(jinpinSaleData[73]);


        oumanPOJO.daQu = jinpinSaleData[1];
        oumanPOJO.market = jinpinSaleData[2];
        oumanPOJO.province = jinpinSaleData[3];
        oumanPOJO.localCity = jinpinSaleData[4];
//        oumanPOJO.city = jinpinSaleData[0];
        oumanPOJO.countryTown = jinpinSaleData[5];
        oumanPOJO.cityType = jinpinSaleData[6];

//        sellerNameSet.add(oumanPOJO.sellerName);
        oumanInputData.add(oumanPOJO);
    }








}

