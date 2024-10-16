package com.lzx.kaleido.plugins.postgresql;

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
 * @author lwp
 * @date 2023-11-16
 **/
public class PostgresqlDBPlugin implements IDBPlugin {
    
//    private static final String DB_TYPE = "PostgreSQL";
    
    
    /**
     * @return
     */
    @Override
    public String getDbType() {
        return DBConstant.PG_DB_TYPE;
    }
    
    /**
     * @return
     */
    @Override
    public DBConfig getDBConfig() {
        return DBConfig.builder().supportSchema(true).supportDatabase(true).dbType(DBConstant.PG_DB_TYPE).name(DBConstant.PG_DB_TYPE)
                .driverPropertiesList(loadDriverProperties()).build();
    }
    
    /**
     * @return
     */
    @Override
    public IMetaData getMetaData() {
        return new PostgresqlMetaData();
    }
    
    /**
     * @return
     */
    @Override
    public IDBManager getDBManager() {
        return new PostgresqlDBManager();
    }
    
    
    /**
     * @return
     */
    private List<DriverProperties> loadDriverProperties() {
        final Properties properties = PropertyUtil.load("db-driver-pgsql.properties");
        final String type = properties.getProperty("jdbc.driver.type", DBConstant.PG_DB_TYPE);
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
