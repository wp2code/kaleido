package com.lzx.kaleido.test;


import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.infra.base.utils.PropertyUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

/**
 * @author lwp
 * @date 2023-12-19
 **/
class PropertyUtilTest {
    
    @Test
    void loadProperties() {
        final Properties pgsql = PropertyUtil.load("db-driver-pgsql.properties");
        final Properties mysql = PropertyUtil.load("db-driver-mysql.properties");
        Assertions.assertFalse(pgsql == null || mysql == null);
    }
    
    @Test
    void loadToPgsql() {
        final Properties properties = PropertyUtil.load("db-driver-pgsql.properties");
        final String url = properties.getProperty("jdbc.driver.download.url");
        final String driverClass = properties.getProperty("jdbc.driver.class");
        final String version = properties.getProperty("jdbc.driver.version");
        final String jdbcUrl = properties.getProperty("jdbc.driver.url");
        final String defaultVersion = properties.getProperty("jdbc.driver.default");
        Assertions.assertFalse(
                StrUtil.isBlank(url) || StrUtil.isBlank(driverClass) || StrUtil.isBlank(defaultVersion) || StrUtil.isBlank(version)
                        || StrUtil.isBlank(jdbcUrl));
    }
    
    @Test
    void loadToMysqlV5() {
        final Properties properties = PropertyUtil.load("db-driver-mysql.properties");
        final String url = properties.getProperty("jdbc.driver.v5.download.url");
        final String driverClass = properties.getProperty("jdbc.driver.v5.class");
        final String version = properties.getProperty("jdbc.driver.v5.version");
        final String jdbcUrl = properties.getProperty("jdbc.driver.v5.url");
        final String defaultVersion = properties.getProperty("jdbc.driver.default");
        Assertions.assertFalse(
                StrUtil.isBlank(url) || StrUtil.isBlank(driverClass) || StrUtil.isBlank(defaultVersion) || StrUtil.isBlank(version)
                        || StrUtil.isBlank(jdbcUrl));
    }
    
    @Test
    void loadToMysqlV8() {
        final Properties properties = PropertyUtil.load("db-driver-mysql.properties");
        final String url = properties.getProperty("jdbc.driver.v5.download.url");
        final String driverClass = properties.getProperty("jdbc.driver.v8.class");
        final String version = properties.getProperty("jdbc.driver.v8.version");
        final String jdbcUrl = properties.getProperty("jdbc.driver.v8.url");
        final String defaultVersion = properties.getProperty("jdbc.driver.default");
        Assertions.assertFalse(
                StrUtil.isBlank(url) || StrUtil.isBlank(driverClass) || StrUtil.isBlank(defaultVersion) || StrUtil.isBlank(version)
                        || StrUtil.isBlank(jdbcUrl));
    }
}
