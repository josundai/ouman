package com.qiaosheng.app;

import com.qiaosheng.app.service.impl.read.CalculateP2_CityCoverage.CaculateQuyuSaleInfoByType_P2_ForGlobal;
import com.qiaosheng.app.service.impl.read.ReadAllQuyuInfoFromSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiaosheng.app.service.impl.read.CaculateBrandCoverage_P19;
import com.qiaosheng.app.service.impl.read.CaculateQuyuSaleInfoByType_P2;
import com.qiaosheng.app.service.impl.read.CaculateQuyuSellerInfoByType_P4;
import com.qiaosheng.app.service.impl.read.CaculateSaleInfoByLeibie_P1;
import com.qiaosheng.app.service.impl.read.CaculateSellerByBrand_P15;
import com.qiaosheng.app.service.impl.read.CaculateSellerInfoByType_P11;
import com.qiaosheng.app.service.impl.read.CaculateSellerInfoByType_P13;
import com.qiaosheng.app.service.impl.read.CaculateSellerInfoByType_P14;
import com.qiaosheng.app.service.impl.read.CaculateSellerInfoByType_P6;
import com.qiaosheng.app.service.impl.read.CaculateSellerInfoByType_P8;
import com.qiaosheng.app.service.impl.read.CaculateSubBrand_P18;
import com.qiaosheng.app.service.impl.read.CaculateTopSale_P17;
import com.qiaosheng.app.service.impl.read.CaculateValueChain_P10;
import com.qiaosheng.app.service.impl.read.NetworkPlanTable_EXCEL;
import com.qiaosheng.app.service.impl.read.ReadJinpinOneLineFromSource;
import com.qiaosheng.app.service.impl.read.ReadOumanFromSource;
import com.qiaosheng.common.utils.InitDirectoriesUtil;
import com.qiaosheng.common.utils.LoadConfiguration;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai

 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);


    public static  void main(String [] args){


//        ApplicationContext applicationContext = newbaidu GenericXmlApplicationContext("webAppContext.xml");

        if(!LoadConfiguration.loadConfigurations()){
        	log.error("Cannot read the configuration file!");
        	return;
        }
        InitDirectoriesUtil.initDirectories();


        Main m = new Main();
        m.simpleRun();
//        m.test(applicationContext);
    }

    private void simpleRun(){

        ReadAllQuyuInfoFromSource readAllQuyuInfoFromSource = new ReadAllQuyuInfoFromSource();
        readAllQuyuInfoFromSource.readQuyuPriority();

        ReadJinpinOneLineFromSource readJinpinSource = new ReadJinpinOneLineFromSource();
        readJinpinSource.readAllJinpin();

        ReadOumanFromSource readOumanFromSource = new ReadOumanFromSource();
        readOumanFromSource.readOuman();


        log.info("Have red and saved the Jinpin Info.");

        CaculateSaleInfoByLeibie_P1 caculateSaleInfoByLeibie_p1 = new CaculateSaleInfoByLeibie_P1 ();
        caculateSaleInfoByLeibie_p1.run();

        log.info("Have read and output page1");

        CaculateSellerInfoByType_P6 caculateSellerInfoByType_p6 = new CaculateSellerInfoByType_P6();
        caculateSellerInfoByType_p6.run( );

        log.info("Have read and output page 6");

        CaculateQuyuSellerInfoByType_P4 caculateSellerInfoByType_P4 =  new CaculateQuyuSellerInfoByType_P4();
        caculateSellerInfoByType_P4.run();

        log.info("Have read and output page 4");

        CaculateQuyuSaleInfoByType_P2 caculateQuyuSaleInfoByType_P2 =   new CaculateQuyuSaleInfoByType_P2();
        caculateQuyuSaleInfoByType_P2.run();


        log.info("Have read and output page2.");

//        int a=19;
//        if( a>0 ){
//            log.info("程序结束.");
//            return;
//        }



        CaculateSellerInfoByType_P8 caculateSellerInfoByType_P8 =   new CaculateSellerInfoByType_P8();
        caculateSellerInfoByType_P8.run();

        log.info("Have read and output page8");


        CaculateValueChain_P10 caculateValueChain_P10 =   new CaculateValueChain_P10();
        caculateValueChain_P10.run();

        log.info("Have read and output page10");

        CaculateSellerInfoByType_P11 caculateSellerInfoByType_P11 =  new CaculateSellerInfoByType_P11();
        caculateSellerInfoByType_P11.run();

        log.info("Have read and output page11" );

        CaculateSellerInfoByType_P13 caculateSellerInfoByType_P13 = new CaculateSellerInfoByType_P13();
        caculateSellerInfoByType_P13.run();

        log.info("Have read and output page13 " );

        /*
        comment this to improve speed.
        CaculateSellerInfoByType_P14 caculateSellerInfoByType_P14 =  new CaculateSellerInfoByType_P14();
        caculateSellerInfoByType_P14.run();

        log.info("Have read and output page14" );
        */

        CaculateSellerByBrand_P15 caculateSellerByBrand_p15 =  new CaculateSellerByBrand_P15();
        caculateSellerByBrand_p15.run();

        log.info("Have read and output  page15" );

                CaculateTopSale_P17 caculateTopSale_p17 = new CaculateTopSale_P17();
        caculateTopSale_p17.run();

        log.info("Have read and output  page17" );


        CaculateSubBrand_P18 caculateSubBrand_p18 = new CaculateSubBrand_P18();
        caculateSubBrand_p18.run();

        log.info("Have read and output  page18" );

        CaculateBrandCoverage_P19 caculateBrandCoverage_p19 = new CaculateBrandCoverage_P19();
        caculateBrandCoverage_p19.run();

        log.info("Have read and output  page19" );


        NetworkPlanTable_EXCEL networkPlanTable_excel = new NetworkPlanTable_EXCEL();
        networkPlanTable_excel.run();

        log.info("Have read and output  统计结果" );

    }

    /*
    private void test(ApplicationContext applicationContext ){

        IPageWriting writing = applicationContext.getBean("networkFeedService", IPageWriting.class);

        if( writing instanceof NetworkFeedService){
            NetworkFeedService s = ((NetworkFeedService) writing);
            s.hello();
        }
        log.info("Begin to run Java App.");


        ReadJinpinOneLineFromSource readJinpinSource =  (ReadJinpinOneLineFromSource) applicationContext.getBean("readJinpinOneLineFromSource", IBasicServiceInterface.class);
        readJinpinSource.readAllJinpin();
        IJinpinOnelineDao dao = readJinpinSource.getJinpinOnelineDao();

        log.info("Have red and saved the Jinpin Info.");

        CaculateSaleInfoByLeibie_P1 caculateSaleInfoByLeibie_p1 =   (CaculateSaleInfoByLeibie_P1)   applicationContext.getBean("caculateSaleInfoByLeibie_P1", IBasicServiceInterface.class);
        caculateSaleInfoByLeibie_p1.run();

        log.info("Have read and output 鍚勫姛鑳芥巿鏉冩笭閬撹瘎浼�");

        CaculateSellerInfoByType_P6 caculateSellerInfoByType_p6 = (CaculateSellerInfoByType_P6) applicationContext.getBean("caculateSellerInfoByType_P6", IBasicServiceInterface.class);
        caculateSellerInfoByType_p6.run( );

        log.info("Have read and output 鍚勫尯鍩熸笭閬撴暟閲�");

//        CaculateQuyuSellerInfoByType_P4 caculateSellerInfoByType_P4 = (CaculateQuyuSellerInfoByType_P4) applicationContext.getBean("caculateQuyuSellerInfoByType_P4", ICaculateQuyuSellerInfoByType_P4.class);
//        caculateSellerInfoByType_P4.run();

        log.info("Have read and output 鍚勫尯鍩熸笭閬撶偣鍧囬攢閲�");

        CaculateQuyuSaleInfoByType_P2 caculateQuyuSaleInfoByType_P2 =   (CaculateQuyuSaleInfoByType_P2)   applicationContext.getBean("caculateQuyuSaleInfoByType_P2", ICaculateQuyuSaleInfoByType_P2.class);
        caculateQuyuSaleInfoByType_P2.run();


        log.info("Have read and output 鍚勫尯鍩熸笭閬撹鐩栧害.");


        CaculateSellerInfoByType_P8 caculateSellerInfoByType_P8 =   (CaculateSellerInfoByType_P8)   applicationContext.getBean("caculateSellerInfoByType_P8", IBasicServiceInterface.class);
        caculateSellerInfoByType_P8.run();

        log.info("Have read and output 鍚勫尯鍩熸笭閬撻攢閲忓彴闃�);


        CaculateValueChain_P10 caculateValueChain_P10 =   (CaculateValueChain_P10)   applicationContext.getBean("caculateValueChain_P10", IBasicServiceInterface.class);
        caculateValueChain_P10.run();

        log.info("Have read and output 鍚勪环鍊奸摼缁勫悎璇勪环");

        CaculateSellerInfoByType_P11 caculateSellerInfoByType_P11 =   (CaculateSellerInfoByType_P11)   applicationContext.getBean("caculateSellerInfoByType_P11", IBasicServiceInterface.class);
        caculateValueChain_P10.run();

        log.info("Have read and output 鍚勫尯鍩熼攢閲�);

        CaculateSellerInfoByType_P13 caculateSellerInfoByType_P13 =   (CaculateSellerInfoByType_P13)   applicationContext.getBean("caculateSellerInfoByType_P13", IBasicServiceInterface.class);
        caculateSellerInfoByType_P13.run();

        log.info("Have read and output 褰㈣薄搴楀崰姣旀儏鍐�);

        CaculateSellerInfoByType_P14 caculateSellerInfoByType_P14 =   (CaculateSellerInfoByType_P14)   applicationContext.getBean("caculateSellerInfoByType_P14", IBasicServiceInterface.class);
        caculateSellerInfoByType_P14.run();

        log.info("Have read and output 褰㈣薄搴楀垎甯冩儏鍐�);

        CaculateSellerByBrand_P15 caculateSellerByBrand_p15 =  new CaculateSellerByBrand_P15();
        caculateSellerByBrand_p15.run();

        log.info("Have read and output 瑕嗙洊搴﹀姣斿垎鏋�);

        CaculateTopSale_P17 caculateTopSale_p17 = new CaculateTopSale_P17();
        caculateTopSale_p17.run();

        log.info("Have read and output 閿�噺闆嗕腑搴﹀強浜屽叓瀹氬緥瀵规瘮鍒嗘瀽");


        CaculateSubBrand_P18 caculateSubBrand_p18 = new CaculateSubBrand_P18();
        caculateSubBrand_p18.run();

        log.info("Have read and output 瀛愬搧鐗岄攢閲忓姣斿垎鏋�);

        CaculateBrandCoverage_P19 caculateBrandCoverage_p19 = new CaculateBrandCoverage_P19();
        caculateBrandCoverage_p19.run();

        log.info("Have read and output 鍚勫搧鐗屽垎鍩庡競绫诲埆瑕嗙洊搴︺�閿�噺璐＄尞搴﹀姣斿垎鏋�);
    }
    */

}
