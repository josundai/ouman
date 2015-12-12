package com.qiaosheng.app.service.impl.read;

import com.qiaosheng.app.service.IBasicServiceInterface;
import com.qiaosheng.common.pojo.model.JinpinOneLinePOJO;
import com.qiaosheng.common.utils.BasicCommonValues;
import com.qiaosheng.common.utils.BasicConfiguredValues;
import com.qiaosheng.common.utils.BasicConstants;
import com.qiaosheng.common.utils.CityBasicInfoPerYear;
import com.qiaosheng.common.utils.OutputToTxtUtil;
import com.qiaosheng.common.utils.SaleInfo;
import com.qiaosheng.common.utils.SellerSumInfo;
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
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午5:03
 * For page 13 and 14.
 */
@Component
public class CaculateBrandCoverage_P19 implements IBasicServiceInterface {
    private static final Logger log = LoggerFactory.getLogger(CaculateBrandCoverage_P19.class);

    private static List<String> outputBrandList = new ArrayList<>();

    static{
        outputBrandList.add(BasicConstants.BRAND_DONGFENG);
        outputBrandList.add(BasicConstants.BRAND_OUMAN);
        outputBrandList.add(BasicConstants.BRAND_JIEFANG);
    }



    public void run(){
        try {
            generateText(ReadJinpinOneLineFromSource.readJinpinInputData);
        } catch (Exception e) {
            log.error("Failed to generate report.", e);
        }

    }

    public void  generateText(List<JinpinOneLinePOJO> jinpinOneList) {
        try {
            readAndGenerate( jinpinOneList );
        } catch (Exception e) {
            log.error("Failed to generate report.", e);
        }
    }

    //String is Type as above. Value is           销售情况per每个地区每年
    private void readAndGenerate( List<JinpinOneLinePOJO> jinpinOneList){

        int lineIndex = 0;
        Map<Integer, StringBuffer> outputLines = new HashMap<>();

        for( String brand : outputBrandList ){
            Map<Integer, Map<String, CityOutputNumberOfBrand_Mid>> outputMap = new HashMap<>();


            Map<Integer,Integer> totalSaleNumberPerYear = new HashMap<>();
            Map<Integer,Integer> totalSellerNumberPerYear = new HashMap<>();


            for( Map.Entry<String, SellerSumInfo> sellerInfo : BasicCommonValues.sellerSumInfoMap.entrySet() ){
                //input
                SellerSumInfo sellerSumInfo = sellerInfo.getValue();
                String city = sellerSumInfo.city;

                CityBasicInfoPerYear cityBasicInfo = BasicCommonValues.cityBasicInfoMap.get(city);
                String cityType = cityBasicInfo.cityType;

                //output:
                for( Integer year = 2012; year<=2018; year ++){
                    //prepare output:
                    //String key is CityType.
                    int totalSaleN = totalSaleNumberPerYear.get(year)==null?0:totalSaleNumberPerYear.get(year);
                    int totalSellerN = totalSellerNumberPerYear.get(year)==null?0: totalSellerNumberPerYear.get(year);


                    Map<String, CityOutputNumberOfBrand_Mid> numberPerCitType = outputMap.get(year);
                    if( numberPerCitType==null ){
                        numberPerCitType = new HashMap<>();
                        outputMap.put(year, numberPerCitType);
                    }
                    CityOutputNumberOfBrand_Mid cityOutputNumberOfBrandMid = numberPerCitType.get(cityType);
                    if(cityOutputNumberOfBrandMid ==null){
                        cityOutputNumberOfBrandMid = new CityOutputNumberOfBrand_Mid();
                        numberPerCitType.put(cityType, cityOutputNumberOfBrandMid);
                    }
                    //output is ready: cityOutputNumberOfBrandMid

                    //check Input
                    SaleInfo yearSellerInfo = sellerSumInfo.saleNumberPerTypeMap.get(year);
                    if( yearSellerInfo == null ){
//                        The seller doesn't sale anything.
//                        cityOutputNumberOfBrandMid.numberOfSeller=cityOutputNumberOfBrandMid.numberOfSeller+1;
//                        cityOutputNumberOfBrandMid.numberOfCityWithSeller=0;
                        continue;
                    }
                    if(yearSellerInfo.brandSaleNumMap.get(brand)!=null ){
                        cityOutputNumberOfBrandMid.totalSaleNumber = cityOutputNumberOfBrandMid.totalSaleNumber + yearSellerInfo.brandSaleNumMap.get(brand);
                        cityOutputNumberOfBrandMid.numberOfSeller = cityOutputNumberOfBrandMid.numberOfSeller +1;
                        cityOutputNumberOfBrandMid.addCity( city );
                        totalSaleN = totalSaleN + yearSellerInfo.brandSaleNumMap.get(brand);//cityOutputNumberOfBrandMid.totalSaleNumber;
                        totalSellerN ++;
                    }
                    totalSaleNumberPerYear.put(year, totalSaleN);
                    totalSellerNumberPerYear.put(year, totalSellerN);
                }
            }


            //The last output
            for( int i = 2012; i<=2018; i++ ){
                //key is cityType;
                Map<String, CityOutputNumberOfBrand_Mid> cityOutputNumberOfBrand_midMap = outputMap.get(i);
                double totalSaleNumber = (double) (totalSaleNumberPerYear.get(i)==null?0:totalSaleNumberPerYear.get(i));
                double totalSellerNumber =  (double) (totalSellerNumberPerYear.get(i)==null?0:totalSellerNumberPerYear.get(i));
                for( String cityType : BasicConstants.cityTypeList){
                    StringBuffer sb = new StringBuffer(brand).append(BasicConstants.TAB_WORD_BREAK).append(i).append(BasicConstants.TAB_WORD_BREAK).append(cityType).append(BasicConstants.TAB_WORD_BREAK);
                    int totalCityNumber = 0;
                    switch (cityType){
                        case BasicConstants.CITY_TYPE_SHENGHUI:
                            totalCityNumber = BasicConstants.NUM_CITY_SHENGHUI;
                            break;
                        case BasicConstants.CITY_TYPE_DIJI:
                            totalCityNumber = BasicConstants.NUM_CITY_DIJI;
                            break;
                        case BasicConstants.CITY_TYPE_XIANGJI:
                            totalCityNumber = BasicConstants.NUM_CITY_XIANGJI;
                            break;
                    }
                    sb.append( totalCityNumber ).append(BasicConstants.TAB_WORD_BREAK);


                    CityOutputNumberOfBrand_Mid cityOutputNumberOfBrand_mid = cityOutputNumberOfBrand_midMap.get(cityType);
                    if( cityOutputNumberOfBrand_mid==null){
                        sb.append(0).append(BasicConstants.TAB_WORD_BREAK).append(0).append(BasicConstants.TAB_WORD_BREAK).append(0).append(BasicConstants.TAB_WORD_BREAK).append(0).append(BasicConstants.TAB_WORD_BREAK).append(0).append(BasicConstants.TAB_WORD_BREAK).append(0).append(BasicConstants.TAB_WORD_BREAK).append(0).append(BasicConstants.TAB_WORD_BREAK);
                        continue;
                    }
                    int sellerNumber = cityOutputNumberOfBrand_mid.numberOfSeller;
                    double qudaoPercentage = totalSellerNumber==0?0:((double)sellerNumber/ totalSellerNumber);

                    int cityNumber = cityOutputNumberOfBrand_mid.getCityNumber();
                    double cityPercentage = totalCityNumber==0?0:((double)cityNumber/ totalCityNumber);

                    int saleNum= cityOutputNumberOfBrand_mid.totalSaleNumber;
                    double saleCPercentage =  totalSaleNumber==0?0: ((double) saleNum/ totalSaleNumber);

                    sb.append(sellerNumber).append(BasicConstants.TAB_WORD_BREAK)
                            .append(BasicConstants.DecimalFormat.format(qudaoPercentage)).append(BasicConstants.TAB_WORD_BREAK)
                            .append(cityNumber).append(BasicConstants.TAB_WORD_BREAK)
                            .append(BasicConstants.DecimalFormat.format(cityPercentage)).append(BasicConstants.TAB_WORD_BREAK)
                            .append(saleNum).append(BasicConstants.TAB_WORD_BREAK)
                            .append(BasicConstants.DecimalFormat.format(saleCPercentage)).append(BasicConstants.TAB_WORD_BREAK);

                    outputLines.put(lineIndex, sb );
                    lineIndex++;
                }
            }


        }

        OutputToTxtUtil.writeTo( outputLines, BasicConfiguredValues.PAGE19_OUTPUT_DIRECTORY+"城市类别覆盖度_所有品牌.txt");
    }




    class CityOutputNumberOfBrand_Mid {
        //totalNum of seller
        public int numberOfSeller;
        // seller

        public  int totalSaleNumber;
        private Set<String> matchedCitySet = new HashSet<>();

        public int getCityNumber(){
            return  matchedCitySet.size();
        }


        public void addCity(String cityName ){
            matchedCitySet.add(cityName);

        }

    }
}
