package com.qiaosheng.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 */

public class CityBasicInfoPerYear {
    //key is Year. Value is CityPriority
    private Map<Integer, CityPriority> yearPriority = new HashMap<>();

    public String warQu;          //战区
    public String daQu ;          //大区
    public String quYu  ;         //区域 （ 省份）
    public String city  ;         //     地区
    public String cityType ;     //县级城市

    public CityBasicInfoPerYear(String cityName){
        this.city = cityName;
    }



    public Map<Integer, CityPriority> getYearPriority() {
        return yearPriority;
    }

    public void setYearPriority(Map<Integer, CityPriority> yearPriority) {
        this.yearPriority = yearPriority;
    }
}
