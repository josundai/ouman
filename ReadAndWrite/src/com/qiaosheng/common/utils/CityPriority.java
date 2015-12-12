package com.qiaosheng.common.utils;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午5:17
 * To change this template use File | Settings | File Templates.
 */
public class CityPriority {

    private String cityPriorityOfGlobalNetwork;                 //地级城市分类： 总体网络
    private String cityPriorityOfRoadNetwork ;                  //地级城市分类： 公路车网
    private String cityPriorityOfMachineNetwork;                //地级城市分类： 工程车网
    private String cityPriorityOfPinbanNetwork ;                //地级城市分类： 平板车网
    private String cityPriorityOfQianyinNetwork;                //地级城市分类： 牵引车网
    private String cityPriorityOfZixieNetwork  ;                //地级城市分类： 自卸车网

    public CityPriority(){

    }

    public CityPriority( String cityPriorityOfGlobalNetwork , String cityPriorityOfRoadNetwork , String cityPriorityOfMachineNetwork, String cityPriorityOfPinbanNetwork, String cityPriorityOfQianyinNetwork, String cityPriorityOfZixieNetwork){
        this.cityPriorityOfGlobalNetwork = cityPriorityOfGlobalNetwork;
        this.cityPriorityOfMachineNetwork = cityPriorityOfMachineNetwork;
        this.cityPriorityOfPinbanNetwork = cityPriorityOfPinbanNetwork;
        this.cityPriorityOfQianyinNetwork = cityPriorityOfQianyinNetwork;
        this.cityPriorityOfRoadNetwork = cityPriorityOfRoadNetwork;
        this.cityPriorityOfZixieNetwork = cityPriorityOfZixieNetwork;
    }



    public String getCityPriorityOfGlobalNetwork() {
        return cityPriorityOfGlobalNetwork;
    }

    public void setCityPriorityOfGlobalNetwork(String cityPriorityOfGlobalNetwork) {
        this.cityPriorityOfGlobalNetwork = cityPriorityOfGlobalNetwork;
    }

    public String getCityPriorityOfRoadNetwork() {
        return cityPriorityOfRoadNetwork;
    }

    public void setCityPriorityOfRoadNetwork(String cityPriorityOfRoadNetwork) {
        this.cityPriorityOfRoadNetwork = cityPriorityOfRoadNetwork;
    }

    public String getCityPriorityOfMachineNetwork() {
        return cityPriorityOfMachineNetwork;
    }

    public void setCityPriorityOfMachineNetwork(String cityPriorityOfMachineNetwork) {
        this.cityPriorityOfMachineNetwork = cityPriorityOfMachineNetwork;
    }

    public String getCityPriorityOfPinbanNetwork() {
        return cityPriorityOfPinbanNetwork;
    }

    public void setCityPriorityOfPinbanNetwork(String cityPriorityOfPinbanNetwork) {
        this.cityPriorityOfPinbanNetwork = cityPriorityOfPinbanNetwork;
    }

    public String getCityPriorityOfQianyinNetwork() {
        return cityPriorityOfQianyinNetwork;
    }

    public void setCityPriorityOfQianyinNetwork(String cityPriorityOfQianyinNetwork) {
        this.cityPriorityOfQianyinNetwork = cityPriorityOfQianyinNetwork;
    }

    public String getCityPriorityOfZixieNetwork() {
        return cityPriorityOfZixieNetwork;
    }

    public void setCityPriorityOfZixieNetwork(String cityPriorityOfZixieNetwork) {
        this.cityPriorityOfZixieNetwork = cityPriorityOfZixieNetwork;
    }
}
