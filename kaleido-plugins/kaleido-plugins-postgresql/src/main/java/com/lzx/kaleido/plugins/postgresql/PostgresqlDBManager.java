package com.lzx.kaleido.plugins.postgresql;

import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.infra.base.excption.CommonException;
import com.lzx.kaleido.spi.db.jdbc.BaseDBManager;
import com.lzx.kaleido.spi.db.model.ConnectionInfo;
import com.lzx.kaleido.spi.db.sql.SQLExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @author lwp
 * @date 2023-11-17
 **/
public class PostgresqlDBManager extends BaseDBManager {
    
    /**
     * @param connection
     * @param schemaName
     * @param database
     * @return
     */
    @Override
    public boolean connectDatabase(Connection connection, final String schemaName, final String database) throws CommonException {
        if (StrUtil.isNotBlank(schemaName)) {
            try {
                SQLExecutor.getInstance().execute(connection, "SET search_path TO \"" + schemaName + "\"");
                return true;
            } catch (SQLException e) {
                throw new CommonException("connectDatabase 异常", e);
            }
        }
        return super.connectDatabase(connection, schemaName, database);
    }
    
    /**
     * @param fmtUrl
     * @param connectionInfo
     * @return
     */
    @Override
    public String getJdbcUrl(final String fmtUrl, final ConnectionInfo connectionInfo) {
        String url = connectionInfo.getUrl();
        if (StrUtil.isNotBlank(connectionInfo.getSchemaName())) {
            url = url + "&currentSchema=" + connectionInfo.getSchemaName();
        }
        return fmtUrl.formatted(url, connectionInfo.getPort(), Optional.ofNullable(connectionInfo.getDatabaseName()).orElse("postgres"));
    }
}
