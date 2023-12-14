package com.lzx.kaleido.spi.db.jdbc;

import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonException;
import com.lzx.kaleido.spi.db.IDBManager;
import com.lzx.kaleido.spi.db.model.ConnectionInfo;
import com.lzx.kaleido.spi.db.model.DriverProperties;
import com.lzx.kaleido.spi.db.sql.DriverManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author lwp
 * @date 2023-11-09
 **/
public class BaseDBManager implements IDBManager {
    
    /**
     * 获取数据库连接
     *
     * @param connectionInfo
     * @return
     * @throws CommonException
     */
    @Override
    public Connection getConnection(final ConnectionInfo connectionInfo) throws CommonException {
        Connection connection = connectionInfo.getConnection();
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            final DriverProperties driverProperties = Optional.ofNullable(connectionInfo.getPropertiesConfig())
                    .map(v -> v.getDefaultDriverProperties(connectionInfo.getSpecifyVersion()))
                    .orElseThrow(() -> new CommonException(ErrorCode.CONNECTION_CONFIG_ABNORMAL));
            final String jdbcUrl = this.getJdbcUrl(driverProperties.getJdbcUrl(), connectionInfo);
            connection = DriverManager.getConnection(jdbcUrl, connectionInfo.getUserName(), connectionInfo.getPassword(), driverProperties);
            if (StrUtil.isNotBlank(connectionInfo.getDatabaseName()) || StrUtil.isNotBlank(connectionInfo.getSchemaName())) {
                connectDatabase(connection, connectionInfo.getSchemaName(), connectionInfo.getDatabaseName());
            }
            connectionInfo.setConnection(connection);
        } catch (SQLException | CommonException e) {
            DriverManager.close(connection);
            throw new CommonException(ErrorCode.CONNECTION_FAILED, e);
        }
        return connection;
    }
    
    /**
     * 连接数据库
     *
     * @param connection
     * @param schemaName
     * @param database
     * @return
     * @throws CommonException
     */
    @Override
    public boolean connectDatabase(final Connection connection, final String schemaName, final String database) throws CommonException {
        return true;
    }
}
