package com.qiaosheng.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午5:12
 * To change this template use File | Settings | File Templates.
 */
public class SaleInfo {
    //Key is Brand.
    public Map<String, Integer> brandSaleNumMap = new HashMap<>();

    //Key is subBrand
    public Map<String, Integer> subBrandSaleNumMap = new HashMap<>();

    //Key is function Type;
    public Map<String, Integer> functionSaleNumMap = new HashMap<>();
}

