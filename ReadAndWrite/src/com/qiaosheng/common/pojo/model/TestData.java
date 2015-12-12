package com.qiaosheng.common.pojo.model;


import com.qiaosheng.common.pojo.AbstractPersistent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Entity
@Table(name="testData")
@XmlAccessorType(XmlAccessType.NONE)
public class TestData extends AbstractPersistent<TestData> {
    private static final long serialVersionUID = 1L;


    private String organizationUuid;
    private Integer strataNetworkId;
    private String assetId = "abc";
//    private String providerId = "abc";

//    @Column
//    public String getProviderId() {
//        return providerId;
//    }
//
//    public void setProviderId(String providerId) {
//        this.providerId = providerId;
//    }

    @Column
    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    @Column
    public String getOrganizationUuid() { return organizationUuid; }
    public TestData setOrganizationUuid(String value) { this.organizationUuid = value; return this; }

    @Column
    @XmlAttribute
    public Integer getStrataNetworkId() { return strataNetworkId; }
    public TestData setStrataNetworkId(Integer strataNetworkId) { this.strataNetworkId = strataNetworkId; return this; }


}

