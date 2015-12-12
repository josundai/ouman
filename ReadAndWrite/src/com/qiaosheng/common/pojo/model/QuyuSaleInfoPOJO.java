package com.qiaosheng.common.pojo.model;

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
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午5:59
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table
@XmlAccessorType(XmlAccessType.NONE)
public class QuyuSaleInfoPOJO extends BasicPOJO {


    private String type;

    private String quyuName;

    private Integer year;

    private String priority;

    private int sellerNumber;

    private int saleNumber;

    private double averageSaleNumber;

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

    @Column
    public double getAverageSaleNumber() {
        return averageSaleNumber;
    }

    public void setAverageSaleNumber(double averageSaleNumber) {
        this.averageSaleNumber = averageSaleNumber;
    }

    public int hashCode(){
        return (quyuName+"_"+type).hashCode();
    }

    public boolean equals( Object obj){
        if( obj == null ){
            return false;
        }
        if( obj instanceof  QuyuSaleInfoPOJO ){
            QuyuSaleInfoPOJO newObj = (QuyuSaleInfoPOJO) obj;
            if( newObj.getQuyuName().equals(getQuyuName()) && newObj.getType().equals(getType())){
                return true;
            }
        }
        return false;
    }


    @Column
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Column
    public String getQuyuName() {
        return quyuName;
    }

    public void setQuyuName(String quyuName) {
        this.quyuName = quyuName;
    }

    @Column
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Column
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Column
    public int getSellerNumber() {
        return sellerNumber;
    }

    public void setSellerNumber(int sellerNumber) {
        this.sellerNumber = sellerNumber;
    }

    @Column
    public int getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(int saleNumber) {
        this.saleNumber = saleNumber;
    }

    public String toString(){
        return "Year " + year + "; Quyu: " + quyuName + "; functionType:  "+ type + "; priority: " + priority+ "; totalSaleNumberMap " + saleNumber + "; sellerNumber: " +sellerNumber;
    }

}
