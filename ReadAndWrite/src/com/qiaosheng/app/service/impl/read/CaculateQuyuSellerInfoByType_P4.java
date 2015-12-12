package com.qiaosheng.app.service.impl.read;

import com.qiaosheng.app.service.IBasicServiceInterface;
import com.qiaosheng.common.pojo.model.QuyuSaleInfoPOJO;
import com.qiaosheng.common.utils.BasicCommonValues;
import com.qiaosheng.common.utils.BasicConfiguredValues;
import com.qiaosheng.common.utils.BasicConstants;
import com.qiaosheng.common.utils.OutputToTxtUtil;
import com.qiaosheng.common.utils.QuyuBasicInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 */
@Component
public class CaculateQuyuSellerInfoByType_P4  implements IBasicServiceInterface {

    /**
     *分类；区域，年；优先级；数量
     */
    private static final Logger log = LoggerFactory.getLogger(CaculateQuyuSaleInfoByType_P2.class);

    public void run(){

        try {
            generateTxt();
        } catch (Exception e) {
            log.error("Failed to generate report.", e);
        }

    }

    public void generateTxt(){
        Map<Integer, StringBuffer> lineMap = new HashMap<>();

        for( String type : BasicConstants.fenleiTypeList){
            // quYu
            int lineIndex = 0;

            for( QuyuBasicInfo quyuBasicInfo : BasicCommonValues.quYuBasicInfoListWithOrder){
                String quYuName = quyuBasicInfo.getQuYuName();
                String warQuName = quyuBasicInfo.getWarQu();

                StringBuffer sb = new StringBuffer( warQuName );
                sb.append(BasicConstants.TAB_WORD_BREAK).append( quYuName).append(BasicConstants.TAB_WORD_BREAK);
                appendQuyuNumber(sb, quYuName);

                for( int year=2012; year <=2018; year++ ){
                    int totalNumPerYear = 0;
                    for( String priority : BasicConstants.priorityList){
                         //quyuSaleInfoDao.findBy( type, quYuName, year, priority   );
                        //key is quYuName + type + year + priority.
                        String key = quYuName+type+year +priority;
                        QuyuSaleInfoPOJO quyuSaleInfo = BasicCommonValues.quyuSaleInfoPerTypes.get(key);
                        int sellerNumber =  quyuSaleInfo==null?0:quyuSaleInfo.getSellerNumber();
                        sb.append(sellerNumber).append(BasicConstants.TAB_WORD_BREAK);
                        totalNumPerYear += sellerNumber;
                    }
                    sb.append(totalNumPerYear).append(BasicConstants.TAB_WORD_BREAK);
                }
//                sb.append(BasicConstants.ENTER_LINE_BREAK);
                lineMap.put( lineIndex, sb);
                lineIndex++;
            }

            String fileName = BasicConfiguredValues.PAGE4And5_OUTPUT_DIRECTORY + type + "_渠道数量.txt";
            OutputToTxtUtil.writeTo( lineMap, fileName);
        }
    }

    private void appendQuyuNumber(StringBuffer oneLine, String quyuName){
        Map<String, Integer> cityNumberMap =  BasicCommonValues.quyuCityNumberPerPriority.get(quyuName).get(BasicConstants.NETWORKTYPE_ROAD);


        int pNumberA = cityNumberMap.get(BasicConstants.SPECIAL_SELL_TYPE_A)==null?0:cityNumberMap.get(BasicConstants.SPECIAL_SELL_TYPE_A);
        oneLine.append(pNumberA).append(BasicConstants.TAB_WORD_BREAK);
        int pNumberB = cityNumberMap.get(BasicConstants.SPECIAL_SELL_TYPE_B)==null?0:cityNumberMap.get(BasicConstants.SPECIAL_SELL_TYPE_B);
        oneLine.append(pNumberB).append(BasicConstants.TAB_WORD_BREAK);
        int pNumberC = cityNumberMap.get(BasicConstants.SPECIAL_SELL_TYPE_C)==null?0:cityNumberMap.get(BasicConstants.SPECIAL_SELL_TYPE_C);
        oneLine.append(pNumberC).append(BasicConstants.TAB_WORD_BREAK);
        int pNumberD = cityNumberMap.get(BasicConstants.SPECIAL_SELL_TYPE_D)==null?0:cityNumberMap.get(BasicConstants.SPECIAL_SELL_TYPE_D);
        oneLine.append(pNumberD).append(BasicConstants.TAB_WORD_BREAK);
        int pNumberTotal = pNumberA + pNumberB + pNumberC + pNumberD;
        oneLine.append(pNumberTotal).append(BasicConstants.TAB_WORD_BREAK);

    }

}
