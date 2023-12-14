package com.lzx.kaleido.spi.db;

import com.lzx.kaleido.infra.base.excption.CommonException;
import com.lzx.kaleido.spi.db.model.ConnectionInfo;
import org.springframework.lang.NonNull;

import java.sql.Connection;

/**
 * @author lwp
 * @date 2023-11-16
 **/
public interface IDBManager {
    
    /**
     * 获取jdbc连接
     *
     * @param fmtUrl
     * @param connectionInfo
     * @return
     */
    default String getJdbcUrl(String fmtUrl, final ConnectionInfo connectionInfo) {
        return fmtUrl.formatted(connectionInfo.getUrl(), connectionInfo.getPort());
    }
    
    /**
     * 获取数据库连接
     *
     * @param connectionInfo
     * @return
     * @throws CommonException
     */
    Connection getConnection(@NonNull final ConnectionInfo connectionInfo) throws CommonException;
    
    /**
     * 连接数据库
     *
     * @param connection
     * @param schemaName
     * @param database
     * @return
     * @throws CommonException
     */
    boolean connectDatabase(final Connection connection, final String schemaName, final String database) throws CommonException;
}
