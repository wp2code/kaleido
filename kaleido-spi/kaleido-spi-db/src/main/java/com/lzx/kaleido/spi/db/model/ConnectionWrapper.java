package com.lzx.kaleido.spi.db.model;

import lombok.Data;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lwp
 * @date 2024-03-15
 **/
@Data
public class ConnectionWrapper {
    
    private String id;
    
    private Connection connection;
    
    private String dbType;
    
    private String databaseName;
    
    /**
     *
     */
    
    private DBConfig propertiesConfig;
    
    /**
     * @param id
     * @param connection
     * @param dbType
     * @param databaseName
     * @param propertiesConfig
     */
    public ConnectionWrapper(String id, Connection connection, String dbType, String databaseName,DBConfig propertiesConfig) {
        this.id = id;
        this.connection = connection;
        this.dbType = dbType;
        this.databaseName = databaseName;
        this.propertiesConfig = propertiesConfig;
    }
    
    /**
     * @return
     */
    public Map<String, ConnectionWrapper> toMap() {
        final Map<String, ConnectionWrapper> map = new HashMap<>(1);
        map.put(this.getDatabaseName(), this);
        return map;
    }
}
