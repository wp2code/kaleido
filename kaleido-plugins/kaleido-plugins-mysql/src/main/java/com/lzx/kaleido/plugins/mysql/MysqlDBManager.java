package com.lzx.kaleido.plugins.mysql;

import com.lzx.kaleido.spi.db.jdbc.BaseDBManager;
import com.lzx.kaleido.spi.db.sql.SQLExecutor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lwp
 * @date 2023-11-11
 **/
@Slf4j
public class MysqlDBManager extends BaseDBManager {
    
    /**
     * 连接数据库
     *
     * @param connection
     * @param database
     * @return
     */
    @Override
    public boolean connectDatabase(final Connection connection,  final String schemaName,final String database) {
        if (database != null && database.length() > 0) {
            try {
                SQLExecutor.getInstance().execute(connection, "use `" + database + "`;");
                return true;
            } catch (SQLException e) {
                log.error("connect database error !{}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
