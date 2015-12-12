package com.qiaosheng.common.utils;

import java.util.HashSet;
import java.util.Set;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午7:45
 * To change this template use File | Settings | File Templates.
 */
public class QuyuBasicInfo {

    private String quYuName;
    private String warQu;

    private Set<String> cityNameSet = new HashSet<>();

    public String getWarQu() {
        return warQu;
    }

    public void setWarQu(String warQu) {
        this.warQu = warQu;
    }

    public QuyuBasicInfo(String quYu, String warQu) {
        this.quYuName = quYu;
        this.warQu = warQu;
    }

    public String getQuYuName() {
        return quYuName;
    }

    public void setQuYuName(String quYuName) {
        this.quYuName = quYuName;
    }

    public Set<String> getCityNameSet() {
        return cityNameSet;
    }

    public void setCityNameSet(Set<String> cityNameSet) {
        this.cityNameSet = cityNameSet;
    }

    public void addCityName(String cityName ){
        getCityNameSet().add(cityName);
    }
}
