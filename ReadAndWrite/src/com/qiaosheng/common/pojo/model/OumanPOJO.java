package com.qiaosheng.common.pojo.model;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午10:13
 * To change this template use File | Settings | File Templates.
 */
public class OumanPOJO extends BasicPOJO {

    public String sellerName;
    public String sellerSimpleName;
    public String sellerUsedName;                    //曾用名
    public String sellerUsedJoinTime;               //曾用名加盟时间
    public String sellerJoinTime;                    //加盟时间

    public String businessType;                        //业务类别
    public String joinType;                       //                 加盟级别
    public String manageType;                       //管理级别
    public String businessMode;                       //业务模式
    public String secondFirstSeller;                   //     二级对应一级经销商/统购分销对应统购经销商


    public String oumanRoadNetwork;              //欧曼网络分类的公路车网络
    public String oumanMachineNetwork;           //欧曼网络分类的工程车网络

    public String sellerRoadNetwork;                //经销商分类的公路车网络
    public String sellerMachineNetwork;                //经销商分类的工程车车网络

    public String authedProductLine;        //授权产品线
    public String authedArea;           //授权区域

    public String onlySellSpecial;              //专营专卖

    public String oumanShopOldStandard;      //旧欧曼形象店
    public String oumanShopNewStandard;      //新欧曼形象店
    public String oumanShopAddress;            //

    public boolean isSellAndService;            //销服一体
    public String serviceStar;            //
    public boolean isFinance  ;            //
    public boolean isSellingReplacement;            //
    public boolean isSecondHandBusiness;            //
    public boolean isCarTeamManage;            //
    public boolean isInsuranceBusiness;            //
    public boolean isSecondSustaining;            //


    public String daQu;         //'大区
    public String market;              //'市场部
    public String province;               //'省份，直辖市
    public String localCity;             //'地级城市
    public String city;                     //'区县
    public String countryTown;           //县级城市
    public String cityType;               //经销商所在城市类别


}
