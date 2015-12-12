package com.qiaosheng.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 版权申明：本程序所有代码以及文档均为公司版权所有，任何公司或个人未经书面许可不得拷贝复制或者修改。
 * User: dai
 * Time: 下午12:40
 * To change this template use File | Settings | File Templates.
 */
public class LoadConfiguration {
    private static final Logger log = LoggerFactory.getLogger(LoadConfiguration.class);

    public static boolean loadConfigurations() {
        try {
            ClassPathResource cr = new ClassPathResource("configure.properties");//会重新加载spring框架
            BufferedReader dr = new BufferedReader(new InputStreamReader(cr.getInputStream(), "utf-8"));
            String line = null;
            int i = 0;
            Map<String, String> inputConfiguraitons = new HashMap<>();
            while ((line = dr.readLine()) != null) {
                if( StringUtils.isEmpty(line.trim())){
                    continue;
                }
                try {
                    String[] vv= line.split("=");
                    inputConfiguraitons.put(vv[0].trim(), vv[1].trim());
                } catch (Exception e) {
                    log.error("exception!", e);
                }
            }
            BasicConfiguredValues.JINPIN_DIRECTORY = inputConfiguraitons.get("INPUT_DIRECTORY");
            BasicConfiguredValues.basicDir = inputConfiguraitons.get("OUTPUT_DIRECTORY");
            BasicConfiguredValues.reloadDirctories();

            log.info("Has Read configuration properties! Set INPUT DIRECTORY as {}, and OUTPUT DIRECTORY as {}", BasicConfiguredValues.JINPIN_DIRECTORY, BasicConfiguredValues.basicDir);

        } catch (Exception e) {
            log.error("Cannot read configurations!", e);
            return false;
        }
        return true;


    }
}
