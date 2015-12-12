package com.qiaosheng.common.pojo.model;


import com.qiaosheng.common.utils.BasicConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *读取数据
 */
@Entity
@Table
@XmlAccessorType(XmlAccessType.NONE)
public class JinpinOneLinePOJO extends BasicPOJO {



    public Integer year=2002;
    public Integer month=2;

    public String sellerName;
    public String warQu;
    public String daQu;
    public String quYu;
    public String city;
    public String cityType;

    public String brand;
    public String businessType;
    public String networkType;
    public String subType;
    public String driveMode;
    public String functionType;
    public String subBrand;
    public int saleNumber;
    public String grade;
    public String heavyOrNot;
    public String globalNetworkTypePriority;
    public String roadNetworkTypePriority;
    public String machineNetworkTypePriority;
    public String pinbanTypePriority;
    public String qianyinTypePriority;
    public String zixieTypePriority;
    public String sellSpecialLevel = BasicConstants.SPECIAL_SELL_TYPE_Empty;
    public Boolean sellAndService = false;
    public Boolean finance= false;
    public Boolean insuranceBusiness= false;
    public Boolean sellingReplacement= false;
    public Boolean carTeamManage= false;
    public Boolean secondHandBusiness= false;
    public Boolean secondSustaining= false;
    public Boolean logistics= false;
    public Boolean ironCoal= false;
    public Boolean house= false;
    public Boolean restaurant= false;
    public Boolean investment= false;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
    @Column
    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    @Column
    public int getYear() {
        return year;
    }


    @Column
    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
    @Column
    public String getWarQu() {
        return warQu;
    }

    public void setWarQu(String warQu) {
        this.warQu = warQu;
    }
    @Column
    public String getDaQu() {
        return daQu;
    }

    public void setDaQu(String daQu) {
        this.daQu = daQu;
    }
    @Column
    public String getQuYu() {
        return quYu;
    }

    public void setQuYu(String quYu) {
        this.quYu = quYu;
    }
    @Column
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    @Column
    public String getCityType() {
        return cityType;
    }

    public void setCityType(String cityType) {
        this.cityType = cityType;
    }
    @Column
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    @Column
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
    @Column
    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }
    @Column
    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }
    @Column
    public String getDriveMode() {
        return driveMode;
    }

    public void setDriveMode(String driveMode) {
        this.driveMode = driveMode;
    }
    @Column
    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }
    @Column
    public String getSubBrand() {
        return subBrand;
    }

    public void setSubBrand(String subBrand) {
        this.subBrand = subBrand;
    }
    @Column
    public int getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(int saleNumber) {
        this.saleNumber = saleNumber;
    }
    @Column
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
    @Column
    public String getHeavyOrNot() {
        return heavyOrNot;
    }

    public void setHeavyOrNot(String heavyOrNot) {
        this.heavyOrNot = heavyOrNot;
    }
    @Column
    public String getGlobalNetworkTypePriority() {
        return globalNetworkTypePriority;
    }

    public void setGlobalNetworkTypePriority(String globalNetworkTypePriority) {
        this.globalNetworkTypePriority = globalNetworkTypePriority;
    }
    @Column
    public String getRoadNetworkTypePriority() {
        return roadNetworkTypePriority;
    }

    public void setRoadNetworkTypePriority(String roadNetworkTypePriority) {
        this.roadNetworkTypePriority = roadNetworkTypePriority;
    }
    @Column
    public String getMachineNetworkTypePriority() {
        return machineNetworkTypePriority;
    }

    public void setMachineNetworkTypePriority(String machineNetworkTypePriority) {
        this.machineNetworkTypePriority = machineNetworkTypePriority;
    }
    @Column
    public String getPinbanTypePriority() {
        return pinbanTypePriority;
    }

    public void setPinbanTypePriority(String pinbanTypePriority) {
        this.pinbanTypePriority = pinbanTypePriority;
    }
    @Column
    public String getQianyinTypePriority() {
        return qianyinTypePriority;
    }

    public void setQianyinTypePriority(String qianyinTypePriority) {
        this.qianyinTypePriority = qianyinTypePriority;
    }
    @Column
    public String getZixieTypePriority() {
        return zixieTypePriority;
    }

    public void setZixieTypePriority(String zixieTypePriority) {
        this.zixieTypePriority = zixieTypePriority;
    }
    @Column
    public String getSellSpecialLevel() {
        return sellSpecialLevel;
    }

    public void setSellSpecialLevel(String sellSpecialLevel) {
        this.sellSpecialLevel = sellSpecialLevel;
    }
    @Column
    public Boolean getSellAndService() {
        return sellAndService;
    }

    public void setSellAndService(Boolean sellAndService) {
        this.sellAndService = sellAndService;
    }
    @Column
    public Boolean getFinance() {
        return finance;
    }

    public void setFinance(Boolean finance) {
        this.finance = finance;
    }
    @Column
    public Boolean getInsuranceBusiness() {
        return insuranceBusiness;
    }

    public void setInsuranceBusiness(Boolean insuranceBusiness) {
        this.insuranceBusiness = insuranceBusiness;
    }
    @Column
    public Boolean getSellingReplacement() {
        return sellingReplacement;
    }

    public void setSellingReplacement(Boolean sellingReplacement) {
        this.sellingReplacement = sellingReplacement;
    }
    @Column
    public Boolean getCarTeamManage() {
        return carTeamManage;
    }

    public void setCarTeamManage(Boolean carTeamManage) {
        this.carTeamManage = carTeamManage;
    }
    @Column
    public Boolean getSecondHandBusiness() {
        return secondHandBusiness;
    }

    public void setSecondHandBusiness(Boolean secondHandBusiness) {
        this.secondHandBusiness = secondHandBusiness;
    }
    @Column
    public Boolean getSecondSustaining() {
        return secondSustaining;
    }

    public void setSecondSustaining(Boolean secondSustaining) {
        this.secondSustaining = secondSustaining;
    }
    @Column
    public Boolean getLogistics() {
        return logistics;
    }

    public void setLogistics(Boolean logistics) {
        this.logistics = logistics;
    }
    @Column
    public Boolean getIronCoal() {
        return ironCoal;
    }

    public void setIronCoal(Boolean ironCoal) {
        this.ironCoal = ironCoal;
    }
    @Column
    public Boolean getHouse() {
        return house;
    }

    public void setHouse(Boolean house) {
        this.house = house;
    }
    @Column
    public Boolean getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Boolean restaurant) {
        this.restaurant = restaurant;
    }

    public Boolean getInvestment() {
        return investment;
    }

    public void setInvestment(Boolean investment) {
        this.investment = investment;
    }
}
