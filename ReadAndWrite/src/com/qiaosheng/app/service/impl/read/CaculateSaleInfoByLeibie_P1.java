package com.qiaosheng.app.service.impl.read;

import com.qiaosheng.app.service.IBasicServiceInterface;
import com.qiaosheng.common.utils.BasicConfiguredValues;
import com.qiaosheng.common.utils.BasicConstants;
import com.qiaosheng.common.utils.OutputToTxtUtil;
import com.qiaosheng.common.utils.SaleInfo;
import com.qiaosheng.common.utils.SellerSumInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 *  生成PPT第一页
 *  共计306行
 */
@Component
public class CaculateSaleInfoByLeibie_P1 implements IBasicServiceInterface {

    String SELLER_PERCENTAGE = "渠道占比";
    String SALE_PERCENTAGE = "销售贡献度";
    String SELLER_NUMBER = "渠道数量";
    String SALE_NUMBER = "销量";

    String BRAND_DONGFENG = "东风";
    String BRAND_LIUQI = "柳汽";
    String BRAND_OUMAN = "欧曼";
    String BRAND_OTHERS = "其他竞品";

    //类型：每年的统计情况
    // Map<String, YearSaleInfo> typeSaleInfoMap = new HashMap<>();

    String ALL_TYPE = "牵引+平板+自卸";
    String PINBAN_ZIXIE= "平板+自卸";
    String QIANYIN_ZIXIE= "牵引+自卸";
    String QIANYIN_PINBAN= "牵引+平板";
    String ONLY_QIANYIN= "仅牵引";
    String ONLY_ZIXIE= "仅自卸";
    String ONLY_PINBAN= "仅平板";
    String SUMMARY = "合计";


    private static final Logger log = LoggerFactory.getLogger(CaculateQuyuSaleInfoByType_P2.class);

    public void run(){
        try {
            CaculateSellerInfo caculateSellerInfo = new CaculateSellerInfo();
            Map<String, SellerSumInfo> sellerSumInfoMap = caculateSellerInfo.caculate(ReadJinpinOneLineFromSource.readJinpinInputData);
            generateText(sellerSumInfoMap);
        } catch (Exception e) {
            log.error("Failed to generate report.", e);

        }
    }



    public void generateText(Map<String, SellerSumInfo> sellerSumInfoMap) {
        Map<String, BrandSaleInfo> output =  calculateResult(sellerSumInfoMap);

        Map<Integer,StringBuffer> allLineMap = new HashMap<>();
        allLineMap.put(0, new StringBuffer(ONLY_QIANYIN).append(BasicConstants.TAB_WORD_BREAK).append(SELLER_PERCENTAGE));
        allLineMap.put(1, new StringBuffer(ONLY_QIANYIN).append(BasicConstants.TAB_WORD_BREAK).append(SALE_PERCENTAGE));
        allLineMap.put(2, new StringBuffer(ONLY_QIANYIN).append(BasicConstants.TAB_WORD_BREAK).append(SELLER_NUMBER));
        allLineMap.put(3, new StringBuffer(ONLY_QIANYIN).append(BasicConstants.TAB_WORD_BREAK).append(SALE_NUMBER));

        allLineMap.put(4, new StringBuffer(ONLY_PINBAN).append(BasicConstants.TAB_WORD_BREAK).append(SELLER_PERCENTAGE));
        allLineMap.put(5, new StringBuffer(ONLY_PINBAN).append(BasicConstants.TAB_WORD_BREAK).append(SALE_PERCENTAGE));
        allLineMap.put(6, new StringBuffer(ONLY_PINBAN).append(BasicConstants.TAB_WORD_BREAK).append(SELLER_NUMBER));
        allLineMap.put(7, new StringBuffer(ONLY_PINBAN).append(BasicConstants.TAB_WORD_BREAK).append(SALE_NUMBER));


        allLineMap.put(8, new StringBuffer(ONLY_ZIXIE).append(BasicConstants.TAB_WORD_BREAK).append(SELLER_PERCENTAGE));
        allLineMap.put(9, new StringBuffer(ONLY_ZIXIE).append(BasicConstants.TAB_WORD_BREAK).append(SALE_PERCENTAGE));
        allLineMap.put(10, new StringBuffer(ONLY_ZIXIE).append(BasicConstants.TAB_WORD_BREAK).append(SELLER_NUMBER));
        allLineMap.put(11, new StringBuffer(ONLY_ZIXIE).append(BasicConstants.TAB_WORD_BREAK).append(SALE_NUMBER));



        allLineMap.put(12, new StringBuffer(QIANYIN_PINBAN).append(BasicConstants.TAB_WORD_BREAK).append(SALE_PERCENTAGE));
        allLineMap.put(13, new StringBuffer(QIANYIN_PINBAN).append(BasicConstants.TAB_WORD_BREAK).append(SELLER_NUMBER));
        allLineMap.put(14, new StringBuffer(QIANYIN_PINBAN).append(BasicConstants.TAB_WORD_BREAK).append(SALE_NUMBER));
        allLineMap.put(15, new StringBuffer(QIANYIN_PINBAN).append(BasicConstants.TAB_WORD_BREAK).append(SALE_NUMBER));


        allLineMap.put(16, new StringBuffer(QIANYIN_ZIXIE).append(BasicConstants.TAB_WORD_BREAK).append(SELLER_PERCENTAGE));
        allLineMap.put(17, new StringBuffer(QIANYIN_ZIXIE).append(BasicConstants.TAB_WORD_BREAK).append(SALE_PERCENTAGE));
        allLineMap.put(18, new StringBuffer(QIANYIN_ZIXIE).append(BasicConstants.TAB_WORD_BREAK).append(SELLER_NUMBER));
        allLineMap.put(19, new StringBuffer(QIANYIN_ZIXIE).append(BasicConstants.TAB_WORD_BREAK).append(SALE_NUMBER));


        allLineMap.put(20, new StringBuffer(PINBAN_ZIXIE).append(BasicConstants.TAB_WORD_BREAK).append(SELLER_PERCENTAGE));
        allLineMap.put(21, new StringBuffer(PINBAN_ZIXIE).append(BasicConstants.TAB_WORD_BREAK).append(SALE_PERCENTAGE));
        allLineMap.put(22, new StringBuffer(PINBAN_ZIXIE).append(BasicConstants.TAB_WORD_BREAK).append(SELLER_NUMBER));
        allLineMap.put(23, new StringBuffer(PINBAN_ZIXIE).append(BasicConstants.TAB_WORD_BREAK).append(SALE_NUMBER));


        allLineMap.put(24, new StringBuffer(ALL_TYPE).append(BasicConstants.TAB_WORD_BREAK).append(SELLER_PERCENTAGE));
        allLineMap.put(25, new StringBuffer(ALL_TYPE).append(BasicConstants.TAB_WORD_BREAK).append(SALE_PERCENTAGE));
        allLineMap.put(26, new StringBuffer(ALL_TYPE).append(BasicConstants.TAB_WORD_BREAK).append(SELLER_NUMBER));
        allLineMap.put(27, new StringBuffer(ALL_TYPE).append(BasicConstants.TAB_WORD_BREAK).append(SALE_NUMBER));

        updateLines( 0, output, ONLY_QIANYIN,  allLineMap );
        updateLines( 4, output, ONLY_PINBAN,  allLineMap );
        updateLines( 8, output, ONLY_ZIXIE,  allLineMap );
        updateLines( 12, output, QIANYIN_PINBAN,  allLineMap );
        updateLines( 16, output, QIANYIN_ZIXIE,  allLineMap );
        updateLines( 20, output, PINBAN_ZIXIE,  allLineMap );
        updateLines( 24, output, ALL_TYPE,  allLineMap );

        OutputToTxtUtil.writeTo(allLineMap, BasicConfiguredValues.PAGE1_OUTPUT_TXTName);

    }


    private void calculateOutputForType(String functionType, SellerSumInfo sellerSumInfo, Map<String, BrandSaleInfo>  output ){
        //For output
        BrandSaleInfo brandSaleInfo = output.get(functionType);
        if( brandSaleInfo == null ){
            brandSaleInfo = new BrandSaleInfo();
            output.put(functionType, brandSaleInfo);
        }
        for( int year=2012; year<=2018; year++ ){
            //过滤所有的input
            SaleInfo saleInfo = sellerSumInfo.saleNumberPerTypeMap.get(year);
            if( saleInfo == null){
                continue;
            }

            for( Map.Entry<String, Integer> saleNumberPerBrandEntity : saleInfo.brandSaleNumMap.entrySet()){
                String inputBrand = saleNumberPerBrandEntity.getKey();
                //get output of the brand:
                YearSaleInfo yearSaleInfo = brandSaleInfo.getBrandSaleInfoMap().get(inputBrand);
                if( yearSaleInfo ==null ){
                    yearSaleInfo = new YearSaleInfo();
                    brandSaleInfo.getBrandSaleInfoMap().put(inputBrand, yearSaleInfo);
                }
//            YearSummarySaleInfo yearSummarySaleInfo = outputSummary.get(i);
                yearSaleInfo.update(year, saleNumberPerBrandEntity.getValue() );
            }
        }
    }

    private void updateLines(int lineIndex, Map<String, BrandSaleInfo> output, String type, Map<Integer,StringBuffer> allLineMap  ){

        BrandSaleInfo brandSaleInfoForOneType = output.get(type);
        if( brandSaleInfoForOneType == null){
           System.out.println(type + "没有数据.  ");
            return;
        }
        YearSaleInfo yearSaleInfoForBrandAndType = brandSaleInfoForOneType.getBrandSaleInfoMap().get(BRAND_DONGFENG);
        forOneType( lineIndex, yearSaleInfoForBrandAndType, allLineMap);
        yearSaleInfoForBrandAndType = brandSaleInfoForOneType.getBrandSaleInfoMap().get(BRAND_LIUQI);
        forOneType( lineIndex, yearSaleInfoForBrandAndType, allLineMap);
        yearSaleInfoForBrandAndType = brandSaleInfoForOneType.getBrandSaleInfoMap().get(BRAND_OUMAN);
        forOneType( lineIndex, yearSaleInfoForBrandAndType, allLineMap);
        yearSaleInfoForBrandAndType = brandSaleInfoForOneType.getBrandSaleInfoMap().get(BRAND_OTHERS);
        forOneType( lineIndex, yearSaleInfoForBrandAndType, allLineMap);


    }

    private void forOneType( int lineIndex, YearSaleInfo yearSaleInfoForBrandAndType, Map<Integer,StringBuffer> allLineMap   ){
        for( int d = 2012;d<=2018; d++){
            if( yearSaleInfoForBrandAndType== null){
                allLineMap.get(lineIndex).append(BasicConstants.TAB_WORD_BREAK).append( BasicConstants.ZERO_WORD_FILL);
                allLineMap.get(lineIndex+1).append(BasicConstants.TAB_WORD_BREAK).append( BasicConstants.ZERO_WORD_FILL);
                allLineMap.get(lineIndex+2).append(BasicConstants.TAB_WORD_BREAK).append( BasicConstants.ZERO_WORD_FILL);
                allLineMap.get(lineIndex+3).append(BasicConstants.TAB_WORD_BREAK).append( BasicConstants.ZERO_WORD_FILL);

            }else{
                allLineMap.get(lineIndex).append(BasicConstants.TAB_WORD_BREAK).append( yearSaleInfoForBrandAndType.sellerPercentage.get(d)==null?"0": yearSaleInfoForBrandAndType.sellerPercentage.get(d));
                allLineMap.get(lineIndex+1).append(BasicConstants.TAB_WORD_BREAK).append(yearSaleInfoForBrandAndType.salePercentage.get(d)==null?"0": yearSaleInfoForBrandAndType.salePercentage.get(d));
                allLineMap.get(lineIndex+2).append(BasicConstants.TAB_WORD_BREAK).append(yearSaleInfoForBrandAndType.brandYearSellerNumber.get(d)==null?"0": yearSaleInfoForBrandAndType.brandYearSellerNumber.get(d));
                allLineMap.get(lineIndex+3).append(BasicConstants.TAB_WORD_BREAK).append(yearSaleInfoForBrandAndType.saleNumber.get(d)==null?"0":yearSaleInfoForBrandAndType.saleNumber.get(d) );
            }
        }
    }

    protected Map<String, BrandSaleInfo>  calculateResult(Map<String, SellerSumInfo> sellerSumInfoMap) {
        // key is functionType. subKey is Brand. Then Value.
        Map<String, BrandSaleInfo>  output = new HashMap<>();

        //key is brand. Value is total Seller Number and total totalSaleNumberMap per year.
        Map<String, YearSummarySaleInfo> outputSummary = new HashMap<>();

        //下面的循环获取了：按照品牌划分的，每年的销量跟经销商数量之和
        for( SellerSumInfo sellerSumInfo : sellerSumInfoMap.values()){
            for( int i=2012; i<=2018; i++){
                SaleInfo saleInfo = sellerSumInfo.saleNumberPerTypeMap.get(i);
                if( saleInfo == null ){
                    continue;
                }
                for(Map.Entry<String, Integer> saleNumberPerBrand : saleInfo.brandSaleNumMap.entrySet()){
                    //input
                    String brand = saleNumberPerBrand.getKey();
                    int saleNumberOfBrand = saleNumberPerBrand.getValue()==null?0:saleNumberPerBrand.getValue();
                    //output
                    YearSummarySaleInfo yearSummarySaleInfo = outputSummary.get(brand);
                    if( yearSummarySaleInfo == null ){
                        yearSummarySaleInfo = new YearSummarySaleInfo();
                        outputSummary.put(brand, yearSummarySaleInfo );
                    }

                    int saleSummary = yearSummarySaleInfo.saleNumberSummary.get(i)==null?0:yearSummarySaleInfo.saleNumberSummary.get(i) ;
                    // Sale Summary  Per one Year + one Brand
                    yearSummarySaleInfo.saleNumberSummary.put(i, saleSummary+saleNumberOfBrand);

                    int sellerSummary = yearSummarySaleInfo.sellerNumberSummary.get(i)==null?0:yearSummarySaleInfo.sellerNumberSummary.get(i) ;
                    yearSummarySaleInfo.sellerNumberSummary.put(i, sellerSummary +1);
                }
            }
        }

        //下面的循环获取了：每个分类下的，按照品牌划分的，每年的销量跟经销商数量。
        for( SellerSumInfo sellerSumInfo : sellerSumInfoMap.values()){
            if( sellerSumInfo.isQianyin && sellerSumInfo.isZixie && sellerSumInfo.isPinban ){
                calculateOutputForType(ALL_TYPE, sellerSumInfo, output );
            }else if( sellerSumInfo.isQianyin && sellerSumInfo.isZixie && !sellerSumInfo.isPinban  ){
                calculateOutputForType(QIANYIN_ZIXIE, sellerSumInfo, output  );
            }else if( sellerSumInfo.isQianyin && sellerSumInfo.isPinban && !sellerSumInfo.isZixie  ){
                calculateOutputForType(QIANYIN_PINBAN, sellerSumInfo, output  );
            }else if( sellerSumInfo.isPinban && sellerSumInfo.isZixie && !sellerSumInfo.isQianyin  ){
                calculateOutputForType(PINBAN_ZIXIE, sellerSumInfo, output );
            }else if( sellerSumInfo.isPinban && (!sellerSumInfo.isZixie) && !sellerSumInfo.isQianyin  ){
                calculateOutputForType(ONLY_PINBAN, sellerSumInfo, output  );
            }else if( (!sellerSumInfo.isPinban) && (sellerSumInfo.isZixie) && !sellerSumInfo.isQianyin  ){
                calculateOutputForType(ONLY_ZIXIE, sellerSumInfo, output  );
            }else if( (!sellerSumInfo.isPinban) && (!sellerSumInfo.isZixie) && sellerSumInfo.isQianyin  ){
                calculateOutputForType(ONLY_QIANYIN, sellerSumInfo, output  );
            }
        }

        //最后计算percentage
        for( BrandSaleInfo brandSaleInfo : output.values()){
            for( String brand : brandSaleInfo.getBrandSaleInfoMap().keySet()){

                YearSummarySaleInfo yearSummarySaleInfo = outputSummary.get(brand);
                if( yearSummarySaleInfo ==null){
                    yearSummarySaleInfo = new YearSummarySaleInfo();
                    yearSummarySaleInfo.saleNumberSummary.put(2012, 0);
                    yearSummarySaleInfo.sellerNumberSummary.put(2012,0);
                    outputSummary.put(brand, yearSummarySaleInfo);
                }

                YearSaleInfo yearSaleInfo = brandSaleInfo.getBrandSaleInfoMap().get(brand);

                for( int i=2012; i<=2018; i++){
                    int yearSaleNumber = yearSaleInfo.saleNumber.get(i)==null?0 : yearSaleInfo.saleNumber.get(i);
                    int yearSummarSaleNumber =  yearSummarySaleInfo.saleNumberSummary.get(i)==null?0:  yearSummarySaleInfo.saleNumberSummary.get(i);
                    double salePercentage = 0d;
                    if( yearSummarSaleNumber!=0 ){
                        salePercentage =    ((double) yearSaleNumber) /  ((double)yearSummarSaleNumber);
                        salePercentage = Double.parseDouble(BasicConstants.DecimalFormat.format(salePercentage));
                    }
                    int yearSellerNumber = yearSaleInfo.brandYearSellerNumber.get(i)==null?0:yearSaleInfo.brandYearSellerNumber.get(i);
                    int yearSellerSumNumber = yearSummarySaleInfo.sellerNumberSummary.get(i)==null?0:yearSummarySaleInfo.sellerNumberSummary.get(i);
                    double sellerPercentage = 0d;
                    if( yearSellerSumNumber!=0){
                       sellerPercentage = ((double)yearSellerNumber)/((double)yearSellerSumNumber);
                       sellerPercentage = Double.parseDouble(BasicConstants.DecimalFormat.format(sellerPercentage));
                    }
                    yearSaleInfo.salePercentage.put(i, salePercentage);
                    yearSaleInfo.sellerPercentage.put(i, sellerPercentage);
                }
            }
        }
        return  output;
    }


    private class YearSaleInfo {
        //        public int year;
        public Map<Integer, Integer> brandYearSellerNumber = new HashMap<>();
        public Map<Integer, Double> sellerPercentage = new HashMap<>();
        public Map<Integer, Integer> saleNumber = new HashMap<>();
        public Map<Integer, Double> salePercentage = new HashMap<>();

        public void update(int year, int saleNumberOfOneBrandYear) {
            if( brandYearSellerNumber.get(year)==null ){
                brandYearSellerNumber.put(year, 0);
            }
            if( sellerPercentage.get(year)==null ){
                sellerPercentage.put(year, 0d);
            }
            if( saleNumber.get(year)==null ){
                saleNumber.put(year, 0);
            }
            if( salePercentage.get(year)==null ){
                salePercentage.put(year, 0d);
            }
            brandYearSellerNumber.put(year, brandYearSellerNumber.get(year) + 1);
            saleNumber.put(year, saleNumber.get(year) + saleNumberOfOneBrandYear);

        }
    }

    private class BrandSaleInfo{

        //key is brand.
        private Map<String, YearSaleInfo> brandSaleInfoMap = new HashMap<>() ; //key is 品牌：         销售情况

        Map<String, YearSaleInfo> getBrandSaleInfoMap() {
            return brandSaleInfoMap;
        }

        void setBrandSaleInfoMap(Map<String, YearSaleInfo> brandSaleInfoMap) {
            this.brandSaleInfoMap = brandSaleInfoMap;
        }
    }

    private class YearSummarySaleInfo{
        //key is Year; Value is total Seller number
        public Map<Integer, Integer> sellerNumberSummary = new HashMap<>();
        //key is Year; Value is total Sale number
        public Map<Integer, Integer> saleNumberSummary = new HashMap<>();
    }
}
