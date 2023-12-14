package com.lzx.kaleido.plugins.mysql;

import cn.hutool.core.text.StrPool;
import com.lzx.kaleido.infra.base.utils.PropertyUtil;
import com.lzx.kaleido.spi.db.IDBManager;
import com.lzx.kaleido.spi.db.IDBPlugin;
import com.lzx.kaleido.spi.db.IMetaData;
import com.lzx.kaleido.spi.db.model.DBConfig;
import com.lzx.kaleido.spi.db.model.DriverProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author lwp
 * @date 2023-11-16
 **/
public class MysqlDBPlugin implements IDBPlugin {
    
    private static final String DB_TYPE = "MYSQL";
    
    /**
     * @return
     */
    @Override
    public String getDbType() {
        return DB_TYPE;
    }
    
    /**
     * @return
     */
    @Override
    public DBConfig getDBConfig() {
        return DBConfig.builder().supportSchema(false).supportDatabase(true).dbType(DB_TYPE).name(DB_TYPE)
                .driverPropertiesList(loadDriverProperties()).build();
    }
    
    /**
     * @return
     */
    @Override
    public IMetaData getMetaData() {
        return new MysqlMetaData();
    }
    
    /**
     * @return
     */
    @Override
    public IDBManager getDBManager() {
        return new MysqlDBManager();
    }
    
    
    /**
     * @return
     */
    private List<DriverProperties> loadDriverProperties() {
        final Properties properties = PropertyUtil.load("db-driver-mysql.properties");
        final String defaultVersion = properties.getProperty("jdbc.driver.default", "v8");
        final DriverProperties v5 = getDriverProperty(properties, defaultVersion, "v5");
        final DriverProperties v8 = getDriverProperty(properties, defaultVersion, "v8");
        return List.of(v5, v8);
    }
    
    /**
     * @param properties
     * @param defaultVersion
     * @param version
     * @return
     */
    private DriverProperties getDriverProperty(Properties properties, String defaultVersion, String version) {
        final String type = properties.getProperty("jdbc.driver.type", DB_TYPE);
        final String urls = properties.getProperty("jdbc.driver.%s.download.url".formatted(version), "");
        final String driverClass = properties.getProperty("jdbc.driver.%s.class".formatted(version));
        final String driverVersion = properties.getProperty("jdbc.driver.%s.version".formatted(version));
        final String jdbcUrl = properties.getProperty("jdbc.driver.%s.url".formatted(version));
        final DriverProperties driverProperty = new DriverProperties();
        driverProperty.setDefaultDriver(defaultVersion.equalsIgnoreCase(driverVersion));
        driverProperty.setJdbcUrl(jdbcUrl);
        driverProperty.setJdbcDriverClass(driverClass);
        driverProperty.setVersion(driverVersion);
        driverProperty.setType(type);
        driverProperty.setDownloadUrls(Arrays.stream(urls.split(StrPool.COMMA)).toList());
        return driverProperty;
    }
}
