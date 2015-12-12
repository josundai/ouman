package com.qiaosheng.app.service.impl.read;

import com.qiaosheng.app.service.IBasicServiceInterface;
import com.qiaosheng.common.pojo.model.JinpinOneLinePOJO;
import com.qiaosheng.common.utils.BasicCommonValues;
import com.qiaosheng.common.utils.BasicConfiguredValues;
import com.qiaosheng.common.utils.BasicConstants;
import com.qiaosheng.common.utils.OutputToTxtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Date: 15-8-17
 * Time: 下午5:03
 * For page 13 and 14.
 */
@Component
public class CaculateTopSale_P17 implements IBasicServiceInterface {
    private static final Logger log = LoggerFactory.getLogger(CaculateTopSale_P17.class);

    private static List<String> outputTitleSequence = new ArrayList<>();

    /*
      */
    private static final String BRAND_OUMAN = "欧曼";
    private static final String BRAND_DONGFENG = "东风";
    private static final String BRAND_JIEFANG = "解放";
    private static final String BRAND_OUMANVT = "欧曼VT网";
    private static final String BRAND_QINDAO_JIEFANG = "青岛直销";
    private static final String BRAND_LIUQI = "柳汽";
    private static final String BRAND_OTHERS = "其他品牌";

    static {
        outputTitleSequence.add( BRAND_OUMAN );
        outputTitleSequence.add( BRAND_DONGFENG );
        outputTitleSequence.add( BRAND_JIEFANG );
        outputTitleSequence.add( BRAND_OUMANVT );
        outputTitleSequence.add( BRAND_QINDAO_JIEFANG );
        outputTitleSequence.add( BRAND_LIUQI );
        outputTitleSequence.add( BRAND_OTHERS );
    }

    public void run(){
        try {
            generateText(ReadJinpinOneLineFromSource.readJinpinInputData);
        } catch (Exception e) {
            log.error("Failed to generate report.", e);
        }
    }


    //String is Type as above. Value is           销售情况per每个地区每年
    private void generateText( List<JinpinOneLinePOJO> jinpinOneList){
        Map<Integer, StringBuffer> outputMap1 = new HashMap<>();
        Map<Integer, StringBuffer> outputMap2 = new HashMap<>();

        int index = 0;
//        for( String brand : outputTitleSequence ){
        for(String brand : BasicCommonValues.brandList){
            StringBuffer sbFor80 = new StringBuffer();
            StringBuffer sbForTop50 = new StringBuffer();
            readWithFilter( jinpinOneList, brand, sbFor80, sbForTop50);
            outputMap1.put( index, sbFor80 );
            outputMap2.put(index, sbForTop50);
            index++;
        }

        OutputToTxtUtil.writeTo( outputMap1, BasicConfiguredValues.PAGE17_OUTPUT_DIRECTORY+"EightyVSTwenty.txt");
        OutputToTxtUtil.writeTo( outputMap2, BasicConfiguredValues.PAGE17_OUTPUT_DIRECTORY+"Top50.txt");
    }


    private void readWithFilter(  List<JinpinOneLinePOJO> jinpinOneList, String filter, StringBuffer sbFor80 ,StringBuffer sbForTop50  ){


        sbFor80.append(filter).append(BasicConstants.TAB_WORD_BREAK);
        sbForTop50.append(filter).append(BasicConstants.TAB_WORD_BREAK);


        for( int year=2012; year<=2018; year++ ){
            if( year!=2015){
                continue;
            }

            int totalSaleNumber = 0;
            int eightyPercentTotalNumber = 0;

            int totalSellerNumber_mid = 0;
            //key is sellerName. subkey is year.
            Map<String,Integer> sellerSaleInfo = new HashMap<>();

            List<SellerSaleNumber> sortedList = new ArrayList<>();

            boolean noMatch = true;
            for( JinpinOneLinePOJO jinpinOneLinePOJO : jinpinOneList){
                if( !jinpinOneLinePOJO.brand.equals(filter)){
                    continue;
                }
                if( jinpinOneLinePOJO.year != year ){
                    continue;
                }
                noMatch = false;
                String sellerName = jinpinOneLinePOJO.sellerName;

                int saleNumberOfSeller = sellerSaleInfo.get(sellerName)==null?0: sellerSaleInfo.get(sellerName);
                sellerSaleInfo.put(sellerName, saleNumberOfSeller+ jinpinOneLinePOJO.saleNumber );
                totalSaleNumber += jinpinOneLinePOJO.saleNumber;

            }
            if( noMatch ){
                sbFor80.append(0).append(BasicConstants.TAB_WORD_BREAK)
                        .append(0).append(BasicConstants.TAB_WORD_BREAK)
                        .append(0).append(BasicConstants.TAB_WORD_BREAK)
                        .append(0).append(BasicConstants.TAB_WORD_BREAK);
                continue;
            }

            totalSellerNumber_mid = sellerSaleInfo.size();
            eightyPercentTotalNumber = (int) Math.round(0.8 * totalSaleNumber);

            for( Map.Entry<String,Integer> item : sellerSaleInfo.entrySet() ){
                SellerSaleNumber sellerSaleNumber = new SellerSaleNumber( item.getKey(), item.getValue());
                sortedList.add( sellerSaleNumber);

            }

            Collections.sort(sortedList);

            int sellerIndex = 1;
            int topSaleSumNumber = 0;
            for( SellerSaleNumber sellerSaleNumber : sortedList ){
                topSaleSumNumber += sellerSaleNumber.saleNumber;
                if( topSaleSumNumber >= eightyPercentTotalNumber ){
//                    log.debug("Match 80%!");
                    break;
                }
                sellerIndex++;
            }

            String sellerPercentage  = BasicConstants.DecimalFormat.format( (double)sellerIndex/(double) totalSellerNumber_mid );

            sbFor80.append(totalSaleNumber).append(BasicConstants.TAB_WORD_BREAK)
                    .append(eightyPercentTotalNumber).append(BasicConstants.TAB_WORD_BREAK)
                    .append(sellerIndex).append(BasicConstants.TAB_WORD_BREAK)
                    .append(sellerPercentage).append(BasicConstants.TAB_WORD_BREAK);

            if( year!=2015){
                continue;
            }

            int topIndex = 1;
            int totalSaleNumberForTop50 = 0;
            for( SellerSaleNumber sellerSaleNumber : sortedList ){
                totalSaleNumberForTop50 += sellerSaleNumber.saleNumber;
                if( topIndex == 3 || topIndex == 5 || topIndex == 10 || topIndex == 20 || topIndex == 50 ){
                    String topSellerPercentage  = BasicConstants.DecimalFormat.format( (double)totalSaleNumberForTop50/(double) totalSaleNumber );
                    sbForTop50.append( totalSaleNumberForTop50).append(BasicConstants.TAB_WORD_BREAK).append( topSellerPercentage).append(BasicConstants.TAB_WORD_BREAK);
                }
                topIndex ++;
            }

            if( sellerSaleInfo.size()<3 ){
                circleAdd(5, sbForTop50,totalSaleNumberForTop50  );
            }else if( sellerSaleInfo.size()<5 ){
                circleAdd(4, sbForTop50,totalSaleNumberForTop50  );
            }else if( sellerSaleInfo.size()<10 ){
                circleAdd(3, sbForTop50,totalSaleNumberForTop50  );
            }else if( sellerSaleInfo.size()<20 ){
                circleAdd(2, sbForTop50,totalSaleNumberForTop50  );
            }else if( sellerSaleInfo.size()<50 ){
                circleAdd(1, sbForTop50,totalSaleNumberForTop50  );
            }
        }

        //get the sale Number
    }

    private void circleAdd( int circleTimes, StringBuffer sb,int number ){
        for( int i=0;i<circleTimes; i++){
            sb.append( number).append(BasicConstants.TAB_WORD_BREAK).append( 1).append(BasicConstants.TAB_WORD_BREAK);
        }
    }

    class SellerSaleNumber implements Comparable {
        String sellerName;
        int saleNumber;
        public SellerSaleNumber(String sellerName, int saleNumber ){
            this.sellerName = sellerName;
            this.saleNumber = saleNumber;
        }
        @Override
        public int compareTo(Object o) {
            if( o instanceof  SellerSaleNumber ){
                return  ((SellerSaleNumber) o).saleNumber - saleNumber;
            }
            return 0;
        }
    }




}
