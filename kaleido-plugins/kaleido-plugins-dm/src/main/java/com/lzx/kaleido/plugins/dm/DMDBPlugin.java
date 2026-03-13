package com.lzx.kaleido.plugins.dm;

import cn.hutool.core.text.StrPool;
import com.lzx.kaleido.infra.base.utils.PropertyUtil;
import com.lzx.kaleido.spi.db.IDBManager;
import com.lzx.kaleido.spi.db.IDBPlugin;
import com.lzx.kaleido.spi.db.IMetaData;
import com.lzx.kaleido.spi.db.constants.DBConstant;
import com.lzx.kaleido.spi.db.model.DBConfig;
import com.lzx.kaleido.spi.db.model.DriverProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 达梦数据库插件
 *
 * @author lwp
 * @date 2024-03-11
 **/
public class DMDBPlugin implements IDBPlugin {

    @Override
    public String getDbType() {
        return DBConstant.DM_DB_TYPE;
    }

    @Override
    public DBConfig getDBConfig() {
        return DBConfig.builder()
                .supportSchema(false)
                .supportDatabase(true)
                .dbType(DBConstant.DM_DB_TYPE)
                .name(DBConstant.DM_DB_TYPE)
                .driverPropertiesList(loadDriverProperties())
                .build();
    }

    @Override
    public IMetaData getMetaData() {
        return new DMMetaData();
    }

    @Override
    public IDBManager getDBManager() {
        return new DMDBManager();
    }

    private List<DriverProperties> loadDriverProperties() {
        final Properties properties = PropertyUtil.load("db-driver-dm.properties");
        final String type = properties.getProperty("jdbc.driver.type", DBConstant.DM_DB_TYPE);
        final String urls = properties.getProperty("jdbc.driver.download.url");
        final String driverClass = properties.getProperty("jdbc.driver.class");
        final String driverVersion = properties.getProperty("jdbc.driver.version");
        final String jdbcUrl = properties.getProperty("jdbc.driver.url");
        final DriverProperties driverProperty = new DriverProperties();
        driverProperty.setDefaultDriver(true);
        driverProperty.setJdbcUrl(jdbcUrl);
        driverProperty.setJdbcDriverClass(driverClass);
        driverProperty.setVersion(driverVersion);
        driverProperty.setType(type);
        driverProperty.setDownloadUrls(Arrays.stream(urls.split(StrPool.COMMA)).toList());
        return List.of(driverProperty);
    }
}
