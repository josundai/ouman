package com.qiaosheng.app.service.impl.read;

import com.qiaosheng.app.service.IBasicServiceInterface;
import com.qiaosheng.common.pojo.model.JinpinOneLinePOJO;
import com.qiaosheng.common.utils.BasicConfiguredValues;
import com.qiaosheng.common.utils.BasicConstants;
import com.qiaosheng.common.utils.OutputToTxtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1, get all SellerInfo at first.
 *
 *
 */
@Component
public class CaculateValueChain_P10 implements IBasicServiceInterface {
    private static final Logger log = LoggerFactory.getLogger(CaculateValueChain_P10.class);

    //Key is valueChain.  value is numbers Per Year.
    private Map<String, Map<Integer, SaleAndSellerNumber>> outputMap = new HashMap<>();


    public void run(){
        try {
            generateText(ReadJinpinOneLineFromSource.readJinpinInputData);
        } catch (Exception e) {
            log.error("Failed to generate report.", e);
        }
    }

    public void generateText(List<JinpinOneLinePOJO> jinpinOneList) {
        Map<String, SellerValueChain> sellerValueChainMap = getSellerValueChainMap( jinpinOneList);
        convertToValueChainMap( sellerValueChainMap );

        OutputToTxtUtil.writeTo( toTxtLines( outputMap), BasicConfiguredValues.PAGE10_OUTPUT_DIRECTORY);
    }

    private Map<Integer, StringBuffer> toTxtLines(Map<String, Map<Integer, SaleAndSellerNumber>> saleAndSellerPerValueChainMap) {
        Map<Integer, StringBuffer> outputLines = new HashMap<>();

        int lineIndex = 0;

        for(Map.Entry<String, Map<Integer, SaleAndSellerNumber>> item:  saleAndSellerPerValueChainMap.entrySet() ){

            StringBuffer sb = new StringBuffer(item.getKey()).append(BasicConstants.TAB_WORD_BREAK);

            Map<Integer, SaleAndSellerNumber> yearValues = item.getValue();

            for( int i=2012; i<=2018 ; i++){
                SaleAndSellerNumber saleAndSellerNumber = yearValues.get(i);
                if( saleAndSellerNumber == null){
                    saleAndSellerNumber = new SaleAndSellerNumber();
                }
                sb.append( saleAndSellerNumber.sellerNumber).append(BasicConstants.TAB_WORD_BREAK);
                sb.append( saleAndSellerNumber.sellerPercentage).append(BasicConstants.TAB_WORD_BREAK);
                sb.append( saleAndSellerNumber.saleNumber).append(BasicConstants.TAB_WORD_BREAK);
                sb.append( saleAndSellerNumber.salePercentage).append(BasicConstants.TAB_WORD_BREAK);
            }

            outputLines.put(lineIndex, sb );
            lineIndex++;
        }
        return outputLines;
    }

    //    private Map<Integer, StringBuffer> generateTxt(SaleStagePerType saleStageResult) {
//
//    }
//
      private Map<String, Map<Integer, SaleAndSellerNumber>> convertToValueChainMap( Map<String, SellerValueChain> sellerValueChainMap ){
          //key is ValueChain. Value is: saleNumberPer year
          Map<String, Map<Integer, Integer>> saleNumberPerYearValueChain = new HashMap<>();
          //key is ValueChain. Value is: sellerNumberPer year
          Map<String, Map<Integer, Integer>> sellerNumberPerYearValueChain = new HashMap<>();
          //key is year; value is totalSellerNumber
          Map<Integer, Integer> totalSellerNumberPerYear = new HashMap<>();
          //key is year; value is totalSaleNumberMap
          Map<Integer, Integer> totalSaleNumberPerYear = new HashMap<>();


          for( SellerValueChain sellerValueChain : sellerValueChainMap.values() ){



              for( int i=2012; i<=2018;i++){
                  String valueChainEnum = sellerValueChain.valueChainPerYear.get(i);
                  if( valueChainEnum == null){
                      continue;
                  }

                  Map<Integer, Integer> yearSaleNumberMap = saleNumberPerYearValueChain.get(valueChainEnum);
                  if( yearSaleNumberMap==null){
                      yearSaleNumberMap = new HashMap<>();
                      saleNumberPerYearValueChain.put(valueChainEnum, yearSaleNumberMap);
                  }

                  Map<Integer, Integer> yearSellerNumberMap = sellerNumberPerYearValueChain.get(valueChainEnum);
                  if( yearSellerNumberMap==null){
                      yearSellerNumberMap = new HashMap<>();
                      sellerNumberPerYearValueChain.put(valueChainEnum, yearSellerNumberMap);
                  }

                  //input
                  int sellerSaleNumber = sellerValueChain.saleNumberPerYearValue.get(i)==null?0:sellerValueChain.saleNumberPerYearValue.get(i);
                  //set value
                  int existSaleValue = yearSaleNumberMap.get(i)==null?0:yearSaleNumberMap.get(i);
                  yearSaleNumberMap.put(i, existSaleValue+sellerSaleNumber);

                  int existSellerNumber = yearSellerNumberMap.get(i)==null?0:yearSellerNumberMap.get(i);
                  yearSellerNumberMap.put(i, existSellerNumber+1);

                  int existTotalSaleNumber = totalSaleNumberPerYear.get(i)==null?0:totalSaleNumberPerYear.get(i);
                  totalSaleNumberPerYear.put(i, existTotalSaleNumber + sellerSaleNumber );

                  int existTotalSellerNumber = totalSellerNumberPerYear.get(i)==null?0:totalSellerNumberPerYear.get(i);
                  totalSellerNumberPerYear.put(i, existTotalSellerNumber + 1);
              }
          }

          ////////////////////////////// 	开始计算最终结果
          for( String valueChainValue : ValueChainEnum.chainValueList ){

              //For Output
              Map<Integer, SaleAndSellerNumber> yearSaleAndSellerStatistic = outputMap.get(valueChainValue);
              if( yearSaleAndSellerStatistic == null ){
                  yearSaleAndSellerStatistic = new HashMap<>();
                  outputMap.put(valueChainValue, yearSaleAndSellerStatistic);
              }

              if( valueChainValue.equals(ValueChainEnum.OTHER)){
                  continue;
              }


              //input is ready.
              Map<Integer, Integer> saleNumberPerYear = saleNumberPerYearValueChain.get( valueChainValue );
              Map<Integer, Integer> sellerNumberPerYear = sellerNumberPerYearValueChain.get( valueChainValue );
              if(saleNumberPerYear==null){
                  saleNumberPerYear = new HashMap<>();
              }
              if(sellerNumberPerYear==null){
                  sellerNumberPerYear = new HashMap<>();
              }

              for( int i=2012; i<=2018; i++ ){
                  SaleAndSellerNumber saleAndSellerNumber = yearSaleAndSellerStatistic.get(i);
                  if( saleAndSellerNumber == null){
                      saleAndSellerNumber = new SaleAndSellerNumber();
                      yearSaleAndSellerStatistic.put(i, saleAndSellerNumber);
                  }

                  saleAndSellerNumber.saleNumber = saleNumberPerYear.get(i)==null?0:saleNumberPerYear.get(i);
                  saleAndSellerNumber.sellerNumber = sellerNumberPerYear.get(i)==null?0:sellerNumberPerYear.get(i);

                  double totalSaleNumber = (double)(totalSaleNumberPerYear.get(i)==null?0:totalSaleNumberPerYear.get(i));
                  double salePercentage = (!(totalSaleNumber>0))?0:((double)saleAndSellerNumber.saleNumber)/ totalSaleNumber;
                  saleAndSellerNumber.salePercentage = Double.parseDouble(BasicConstants.DecimalFormat.format(salePercentage));

                  double totalSellerNumber = (double)(totalSellerNumberPerYear.get(i)==null?0:totalSellerNumberPerYear.get(i));
                  double sellerPercentage = (!(totalSellerNumber>0))?0:((double)saleAndSellerNumber.sellerNumber)/totalSellerNumber;
                  saleAndSellerNumber.sellerPercentage = Double.parseDouble(BasicConstants.DecimalFormat.format(sellerPercentage));
              }
          }
          return outputMap;
      }





    private Map<String, SellerValueChain> getSellerValueChainMap(List<JinpinOneLinePOJO> jinpinOneList) {
        Map<String, SellerValueChain> sellerValueChainMap = new HashMap<>();
//        第一遍是为了读取： sellerName
        for(JinpinOneLinePOJO oneLine : jinpinOneList ){

            String sellerName = oneLine.sellerName;
            SellerValueChain sellerValueChain =  sellerValueChainMap.get(sellerName);
            if( sellerValueChain == null ){
                sellerValueChain = new SellerValueChain(sellerName);
                sellerValueChainMap.put( sellerName, sellerValueChain);
            }
            String valueChain = getValueChain(oneLine);
            sellerValueChain.valueChainPerYear.put( oneLine.year, valueChain );
            int saleNumberOftheYear = sellerValueChain.saleNumberPerYearValue.get( oneLine.year) == null?0:sellerValueChain.saleNumberPerYearValue.get( oneLine.year);
            sellerValueChain.saleNumberPerYearValue.put( oneLine.year, saleNumberOftheYear + oneLine.saleNumber );
        }
//        Second: calculate the SellerNumber, and average SellerNumber.
        return sellerValueChainMap;
    }

    class SaleAndSellerNumber{
        public int sellerNumber;
        public double sellerPercentage;
        public int saleNumber;
        public double salePercentage;

    }


    class SellerValueChain{


        public String sellerName;
//        public String valueChain = ValueChainEnum.ZERO;
        //Key is year
        public Map<Integer, Integer> saleNumberPerYearValue = new HashMap<>();
        public Map<Integer, String> valueChainPerYear = new HashMap<>();

        public SellerValueChain(String sellerName ){
            this.sellerName = sellerName;
        }

    }



    private String getValueChain(JinpinOneLinePOJO oneLine){
        //  SERVICE   FINANCE    CARTEAM    SECOND  peijian
        boolean isService = oneLine.sellAndService;
        boolean isFinance = oneLine.finance;
        boolean isCarTeam = oneLine.carTeamManage;
        boolean isSecond = oneLine.secondHandBusiness;
        boolean isPeijian = oneLine.sellingReplacement;

        if( isService && isFinance && isCarTeam && isSecond && isPeijian){
            return ValueChainEnum.ALL_FIVE;
        }

        if( isService && isFinance && isCarTeam && isSecond && isPeijian){
            return ValueChainEnum.FOUR_SERVICE_FINANCE_CARTEAM_SECOND;
        }
        if( isService && (!isFinance) && isCarTeam && isSecond && isPeijian){
            return ValueChainEnum.FOUR_SERVICE_PEIJIAN_CARTEAM_SECOND;
        }
        if( (!isService) && isFinance && isCarTeam && isSecond && isPeijian){
            return ValueChainEnum.FOUR_FINANCE_PEIJIAN_CARTEAM_SECOND;
        }
        if( isService && isFinance && isCarTeam && (!isSecond) && isPeijian){
            return ValueChainEnum.FOUR_SERVICE_FINANCE_PEIJIAN_CARTEAM;
        }
        if( isService && isFinance && (!isCarTeam) && isSecond && isPeijian){
            return ValueChainEnum.FOUR_SERVICE_FINANCE_PEIJIAN_SECOND;
        }

        if( (!isService) && isFinance && isCarTeam && isSecond && (!isPeijian)){
            return ValueChainEnum.THREE_FINANCE_CARTEAM_SECOND;
        }
        if( (!isService) && (!isFinance) && isCarTeam && isSecond && isPeijian){
            return ValueChainEnum.THREE_PEIJIAN_CARTEAM_SECOND;
        }
        if( isService && isFinance && isCarTeam && (!isSecond) && !isPeijian){
            return ValueChainEnum.THREE_SERVICE_FINANCE_CARTEAM;
        }
        if( (!isService) && isFinance && isCarTeam && (!isSecond) && isPeijian){
            return ValueChainEnum.THREE_FINANCE_PEIJIAN_CARTEAM;
        }

        if( isService && (!isFinance) && (!isCarTeam) && isSecond && isPeijian){
            return ValueChainEnum.THREE_SERVICE_PEIJIAN_SECOND;
        }
        if( (!isService) && isFinance && (!isCarTeam) && isSecond && isPeijian){
            return ValueChainEnum.THREE_FINANCE_PEIJIAN_SECOND;
        }
        if( isService && isFinance && (!isCarTeam) && (!isSecond) && isPeijian){
            return ValueChainEnum.THREE_SERVICE_FINANCE_PEIJIAN;
        }

        if( isService && (!isFinance) && isCarTeam && (!isSecond) && !isPeijian){
            return ValueChainEnum.TWOE_SERVICE_CARTEAM;
        }
        if( (!isService) && isFinance && isCarTeam && (!isSecond) && !isPeijian){
            return ValueChainEnum.TWO_FINANCE_CARTEAM;
        }
        if( (!isService) && (!isFinance) && (isCarTeam) && (!isSecond) && (isPeijian)){
            return ValueChainEnum.TWO_PEIJIAN_CARTEAM;
        }
        if( (isService) && (!isFinance) && (!isCarTeam) && (isSecond) && (!isPeijian)){
            return ValueChainEnum.TWO_SERVICE_SECOND;
        }
        if( (!isService) && (isFinance) && (!isCarTeam) && (isSecond) && (!isPeijian)){
            return ValueChainEnum.TWO_FINANCE_SECOND;
        }
        if( (isService) && (isFinance) && (!isCarTeam) && (!isSecond) && (!isPeijian)){
            return ValueChainEnum.TWO_SERVICE_FINANCE;
        }

        if( (isService) && (!isFinance) && (!isCarTeam) && (!isSecond) && (isPeijian)){
            return ValueChainEnum.TWO_SERVICE_PEIJIAN;
        }
        if( (!isService) && (isFinance) && (!isCarTeam) && (!isSecond) && (isPeijian)){
            return ValueChainEnum.TWO_FINANCE_PEIJIAN;
        }
        if( (!isService) && (!isFinance) && (!isCarTeam) && (!isSecond) && (isPeijian)){
            return ValueChainEnum.ONE_PEIJIAN;
        }

        if( (isService) && (!isFinance) && (!isCarTeam) && (!isSecond) && (!isPeijian)){
            return ValueChainEnum.ONE_SERVICE;
        }
        if( (!isService) && (isFinance) && (!isCarTeam) && (!isSecond) && (!isPeijian)){
            return ValueChainEnum.ONE_FINANCE;
        }
        if( (!isService) && (!isFinance) && (!isCarTeam) && (!isSecond) && (!isPeijian)){
            return ValueChainEnum.ZERO;
        }
        return ValueChainEnum.OTHER;
    }





    static class ValueChainEnum{

        public static final String ALL_FIVE = "服务+金融+配件+车队挂靠+二手车";

        public static final String FOUR_SERVICE_FINANCE_CARTEAM_SECOND = "服务+金融+车队挂靠+二手车";
        public static final String FOUR_SERVICE_PEIJIAN_CARTEAM_SECOND = "服务+配件+车队挂靠+二手车";
        public static final String FOUR_FINANCE_PEIJIAN_CARTEAM_SECOND = "金融+配件+车队挂靠+二手车";
        public static final String FOUR_SERVICE_FINANCE_PEIJIAN_CARTEAM = "服务+金融+配件+车队挂靠";
        public static final String FOUR_SERVICE_FINANCE_PEIJIAN_SECOND = "服务+金融+配件+二手车";


        public static final String THREE_FINANCE_CARTEAM_SECOND = "金融+车队挂靠+二手车";
        public static final String THREE_PEIJIAN_CARTEAM_SECOND = "配件+车队挂靠+二手车";
        public static final String THREE_SERVICE_FINANCE_CARTEAM = "服务+金融+车队挂靠";
        public static final String THREE_FINANCE_PEIJIAN_CARTEAM = "金融+配件+车队挂靠";
        public static final String THREE_SERVICE_PEIJIAN_SECOND = "服务+配件+车队挂靠";
        public static final String THREE_FINANCE_PEIJIAN_SECOND = "金融+配件+二手车";
        public static final String THREE_SERVICE_FINANCE_PEIJIAN = "服务+金融+配件";

        public static final String TWOE_SERVICE_CARTEAM = "服务+车队挂靠";
        public static final String TWO_FINANCE_CARTEAM = "金融+车队挂靠";
        public static final String TWO_PEIJIAN_CARTEAM = "配件+车队挂靠";
        public static final String TWO_SERVICE_SECOND = "服务+二手车";
        public static final String TWO_FINANCE_SECOND = "金融+二手车";
        public static final String TWO_SERVICE_FINANCE = "服务+金融";
        public static final String TWO_SERVICE_PEIJIAN = "服务+配件";
        public static final String TWO_FINANCE_PEIJIAN = "金融+配件";


        public static final String ONE_SERVICE = "服务";
        public static final String ONE_FINANCE = "金融";
        public static final String ONE_PEIJIAN = "配件";

        public static final String ZERO = "单一销售";

        public static final String OTHER = "其他组合";

        public static List<String> chainValueList = new ArrayList<>();

        static{
            chainValueList.add( ALL_FIVE);

            chainValueList.add(FOUR_SERVICE_FINANCE_CARTEAM_SECOND );
            chainValueList.add(FOUR_SERVICE_PEIJIAN_CARTEAM_SECOND );
            chainValueList.add(FOUR_FINANCE_PEIJIAN_CARTEAM_SECOND );
            chainValueList.add(FOUR_SERVICE_FINANCE_PEIJIAN_CARTEAM );
            chainValueList.add(FOUR_SERVICE_FINANCE_PEIJIAN_SECOND );


            chainValueList.add(THREE_FINANCE_CARTEAM_SECOND );
            chainValueList.add(THREE_PEIJIAN_CARTEAM_SECOND );
            chainValueList.add(THREE_SERVICE_FINANCE_CARTEAM );
            chainValueList.add(THREE_FINANCE_PEIJIAN_CARTEAM );
            chainValueList.add(THREE_SERVICE_PEIJIAN_SECOND );
            chainValueList.add(THREE_FINANCE_PEIJIAN_SECOND );
            chainValueList.add(THREE_SERVICE_FINANCE_PEIJIAN );

            chainValueList.add(TWOE_SERVICE_CARTEAM );
            chainValueList.add(TWO_FINANCE_CARTEAM );
            chainValueList.add(TWO_PEIJIAN_CARTEAM );
            chainValueList.add(TWO_SERVICE_SECOND );
            chainValueList.add(TWO_FINANCE_SECOND );
            chainValueList.add(TWO_SERVICE_FINANCE );
            chainValueList.add(TWO_SERVICE_PEIJIAN );
            chainValueList.add(TWO_FINANCE_PEIJIAN );

            chainValueList.add(ONE_SERVICE );
            chainValueList.add(ONE_FINANCE );
            chainValueList.add(ONE_PEIJIAN );

            chainValueList.add(ZERO );

            chainValueList.add(OTHER);
        }
    }

}
