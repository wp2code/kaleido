package com.lzx.kaleido.spi.db.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lwp
 **/
@Data
public class ConnectionInfo implements Serializable {
    
    private String id;
    
    private String dbType;
    
    private String name;
    
    private String url;
    
    private Integer port;
    
    private String userName;
    
    private String password;
    
    private String databaseName;
    
    private String schemaName;
    
    private String extend;
    
    /**
     * 指定版本
     */
    private String specifyVersion;
    
    private ConnectionWrapper connection;
    
    /**
     * 数据库属性配置
     */
    private DBConfig propertiesConfig;
    
    public static ConnectionInfo of(final String id, final String dbName, final String type, final String name, final Integer port,
            final String url, final String userName, final String password, final String extend) {
        final ConnectionInfo connectionInfo = new ConnectionInfo();
        connectionInfo.setId(id);
        connectionInfo.setDatabaseName(dbName);
        connectionInfo.setDbType(type);
        connectionInfo.setName(name);
        connectionInfo.setPort(port);
        connectionInfo.setUrl(url);
        connectionInfo.setUserName(userName);
        connectionInfo.setPassword(password);
        connectionInfo.setExtend(extend);
        return connectionInfo;
    }
}
