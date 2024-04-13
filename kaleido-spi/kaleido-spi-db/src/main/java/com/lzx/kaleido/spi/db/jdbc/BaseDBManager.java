package com.lzx.kaleido.spi.db.jdbc;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonException;
import com.lzx.kaleido.spi.db.IDBManager;
import com.lzx.kaleido.spi.db.model.ConnectionInfo;
import com.lzx.kaleido.spi.db.model.ConnectionWrapper;
import com.lzx.kaleido.spi.db.model.DriverProperties;
import com.lzx.kaleido.spi.db.sql.DriverManager;
import com.lzx.kaleido.spi.db.utils.JdbcUtil;

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
    public ConnectionWrapper getConnection(final ConnectionInfo connectionInfo) throws CommonException {
        Connection connection = null;
        ConnectionWrapper connectionWrapper = null;
        try {
            final DriverProperties driverProperties = Optional.ofNullable(connectionInfo.getPropertiesConfig())
                    .map(v -> v.getDefaultDriverProperties(connectionInfo.getSpecifyVersion()))
                    .orElseThrow(() -> new CommonException(ErrorCode.CONNECTION_CONFIG_ABNORMAL));
            final String jdbcUrl = this.getJdbcUrl(driverProperties.getJdbcUrl(), connectionInfo);
            connection = DriverManager.getConnection(jdbcUrl, connectionInfo.getUserName(), connectionInfo.getPassword(), driverProperties);
            if (connection == null) {
                return null;
            }
            if (StrUtil.isNotBlank(connectionInfo.getDatabaseName()) || StrUtil.isNotBlank(connectionInfo.getSchemaName())) {
                connectDatabase(connection, connectionInfo.getSchemaName(), connectionInfo.getDatabaseName());
            }
            if (StrUtil.isBlank(connectionInfo.getDatabaseName())) {
                connectionInfo.setDatabaseName(connection.getCatalog());
            }
            connectionInfo.setConnection(
                    connectionWrapper = new ConnectionWrapper(Optional.ofNullable(connectionInfo.getId()).orElse(IdUtil.fastSimpleUUID()),
                            connection, connectionInfo.getDbType(), connectionInfo.getDatabaseName(),
                            connectionInfo.getPropertiesConfig()));
        } catch (SQLException | CommonException e) {
            JdbcUtil.closeConnection(connection);
            throw new CommonException(ErrorCode.CONNECTION_FAILED, e);
        }
        return connectionWrapper;
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
