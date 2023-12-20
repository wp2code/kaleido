package com.lzx.kaleido.spi.db.model;

import com.lzx.kaleido.spi.db.utils.JdbcUtil;
import lombok.Data;

import java.sql.Connection;

/**
 * @author lwp
 **/
@Data
public class ConnectionInfo {
    
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
    
    private transient Connection connection;
    
    /**
     * 是否重置连接
     */
    private boolean resetConnection;
    
    /**
     * 数据库属性配置
     */
    private DBConfig propertiesConfig;
    
    
    public static ConnectionInfo of(final String dbName, final String type, final String name, final Integer port, final String url,
            final String userName, final String password, final String extend) {
        final ConnectionInfo connectionInfo = new ConnectionInfo();
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
    
    /**
     * @return
     */
    public Connection getConnectionNotReset() {
        if (resetConnection) {
            if (connection != null) {
                JdbcUtil.closeConnection(connection);
            }
            return null;
        }
        return connection;
    }
}
