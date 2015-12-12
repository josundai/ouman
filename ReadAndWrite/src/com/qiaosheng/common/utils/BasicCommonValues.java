package com.qiaosheng.common.utils;

import com.qiaosheng.common.pojo.model.JinpinOneLinePOJO;
import com.qiaosheng.common.pojo.model.OumanPOJO;
import com.qiaosheng.common.pojo.model.QuyuSaleInfoPOJO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 上午3:26
 * To change this template use File | Settings | File Templates.
 */
public class BasicCommonValues {
    public static Set<String> brandList = new HashSet<>();

    //key is sellerName.
    public static Map<String, SellerSumInfo> sellerSumInfoMap = null;
    //key is city Name.
    public static Map<String, CityBasicInfoPerYear> cityBasicInfoMap = new HashMap<>();
    public static List<JinpinOneLinePOJO> jinpinInputData = new ArrayList<>();
    public static List<OumanPOJO> oumanInputData = new ArrayList<>();

    public static List<QuyuBasicInfo> quYuBasicInfoListWithOrder =  new ArrayList<>();

    public static List<CityInfoWithPriority> allCityInfowithPriority = null;

    //key is: quyu->type->priority
    public static Map<String, Map<String,Map<String,  Integer>>> quyuCityNumberPerPriority = null;
    public static Set<String> cityNameSetInQuyuTable = new HashSet<>();

    // key is quYuName + type + year + priority.
    public static Map<String, QuyuSaleInfoPOJO> quyuSaleInfoPerTypes = null;




}
