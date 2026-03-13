package com.lzx.kaleido.plugins.dm;

import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.infra.base.excption.CommonException;
import com.lzx.kaleido.spi.db.jdbc.BaseDBManager;
import com.lzx.kaleido.spi.db.model.ConnectionInfo;
import com.lzx.kaleido.spi.db.sql.SQLExecutor;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 达梦数据库连接管理
 *
 * @author lwp
 * @date 2024-03-11
 **/
public class DMDBManager extends BaseDBManager {

    @Override
    public boolean connectDatabase(Connection connection, final String schemaName, final String database) throws CommonException {
        if (StrUtil.isNotBlank(schemaName)) {
            try {
                SQLExecutor.getInstance().execute(connection, "SET SCHEMA \"" + schemaName + "\"");
                return true;
            } catch (SQLException e) {
                throw new CommonException("connectDatabase 异常", e);
            }
        }
        return super.connectDatabase(connection, schemaName, database);
    }

    @Override
    public String getJdbcUrl(final String fmtUrl, final ConnectionInfo connectionInfo) {
        return fmtUrl.formatted(connectionInfo.getUrl(), connectionInfo.getPort());
    }
}
