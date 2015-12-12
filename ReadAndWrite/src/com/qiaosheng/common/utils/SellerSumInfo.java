package com.qiaosheng.common.utils;


import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class SellerSumInfo {

    public String sellerName;
    public String city;
    //key: year.
    public Map<Integer, SaleInfo> saleNumberPerTypeMap = new HashMap<>();

    public Map<Integer, Integer> totalSaleNumberMap = new HashMap();

    public boolean isPinban;
    public boolean isZixie;
    public boolean isQianyin;

    public void cleanType(){
        isPinban = false;
        isZixie = false;
        isQianyin = false;
    }

}
