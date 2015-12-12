package com.qiaosheng.common.utils;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.hibernate.SingletonEhCacheRegionFactory;

import java.util.Properties;

public class DBCacheRegionFactory extends SingletonEhCacheRegionFactory {

    public DBCacheRegionFactory(Properties prop) {
        super(prop);
    }

    public CacheManager getCacheManager() {
        return super.manager;
    }
}
