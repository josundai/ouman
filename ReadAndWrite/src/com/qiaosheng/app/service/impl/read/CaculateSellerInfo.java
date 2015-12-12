package com.qiaosheng.app.service.impl.read;

import com.qiaosheng.common.utils.BasicCommonValues;
import com.qiaosheng.app.service.IBasicServiceInterface;
import com.qiaosheng.common.pojo.model.JinpinOneLinePOJO;
import com.qiaosheng.common.utils.*;
import com.qiaosheng.common.utils.SellerSumInfo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CaculateSellerInfo  implements IBasicServiceInterface {


    /**
     * Return:     key is Seller Name. Value is the Seller details.
     * @param sourceLines .
     * @return
     */
    public Map<String, SellerSumInfo>  caculate(List<JinpinOneLinePOJO> sourceLines ) {

        //Key is seller Name.
        Map<String, SellerSumInfo> sellerSumInfoMap = new HashMap<>();

        for( JinpinOneLinePOJO oneLine : sourceLines ){
            SellerSumInfo sellerSumInfo = sellerSumInfoMap.get(oneLine.sellerName);
            if( sellerSumInfo == null ){
                sellerSumInfo = new SellerSumInfo();
            }
            Map<Integer, Integer> saleNumberPerYear = sellerSumInfo.totalSaleNumberMap;

            int year = oneLine.year;
            int total = saleNumberPerYear.get( year)==null?0:saleNumberPerYear.get(year);
            saleNumberPerYear.put( year, total + oneLine.saleNumber );

            // add sum for each type.

            Map<Integer, SaleInfo> saleInfoMap = sellerSumInfo.saleNumberPerTypeMap;

            SaleInfo saleInfo1 =  saleInfoMap.get( year );
            if( saleInfo1==null){
                saleInfo1 = new SaleInfo();
                saleInfoMap.put( year,saleInfo1);
            }
            sellerSumInfo.sellerName = oneLine.getSellerName();
            sellerSumInfo.city = oneLine.city;

            int totalBrand = saleInfo1.brandSaleNumMap.get( oneLine.brand )==null?0:saleInfo1.brandSaleNumMap.get( oneLine.brand );
            saleInfo1.brandSaleNumMap.put(oneLine.brand, totalBrand+oneLine.saleNumber);

            int totalSubBrand = saleInfo1.subBrandSaleNumMap.get( oneLine.subBrand )==null?0:saleInfo1.subBrandSaleNumMap.get( oneLine.subBrand );
            saleInfo1.subBrandSaleNumMap.put(oneLine.subBrand, totalSubBrand+oneLine.saleNumber);

            int totalFunctionType = saleInfo1.functionSaleNumMap.get( oneLine.functionType )==null?0:saleInfo1.functionSaleNumMap.get( oneLine.functionType );
            saleInfo1.functionSaleNumMap.put(oneLine.brand, totalFunctionType+oneLine.saleNumber);

            if( oneLine.functionType.equals("平板车")){
                sellerSumInfo.isPinban = true;
            }else if( oneLine.functionType.equals("牵引车")){
                sellerSumInfo.isQianyin = true;
            }else if( oneLine.functionType.equals("自卸车")){
                sellerSumInfo.isZixie = true;
            }else {
                System.out.println("Find other types: " + oneLine.functionType);
            }
            sellerSumInfoMap.put( oneLine.sellerName, sellerSumInfo);
        }
        BasicCommonValues.sellerSumInfoMap = sellerSumInfoMap;

        return sellerSumInfoMap;
    }


}
