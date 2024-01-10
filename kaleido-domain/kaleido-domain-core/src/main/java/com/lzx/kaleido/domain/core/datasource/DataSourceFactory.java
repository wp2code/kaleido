package com.lzx.kaleido.domain.core.datasource;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonException;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.utils.ServiceLoaderUtil;
import com.lzx.kaleido.spi.db.IDBManager;
import com.lzx.kaleido.spi.db.IDBPlugin;
import com.lzx.kaleido.spi.db.IMetaData;
import com.lzx.kaleido.spi.db.model.ConnectionInfo;
import com.lzx.kaleido.spi.db.model.metaData.Database;
import com.lzx.kaleido.spi.db.model.metaData.Schema;
import com.lzx.kaleido.spi.db.model.metaData.Table;
import com.lzx.kaleido.spi.db.model.metaData.TableColumn;
import com.lzx.kaleido.spi.db.utils.JdbcUtil;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

/**
 * @author lwp
 * @date 2023-11-18
 **/
public class DataSourceFactory {
    
    private final static DataSourceFactory instance = new DataSourceFactory();
    
    static {
        ServiceLoaderUtil.register(IDBPlugin.class);
    }
    
    private DataSourceFactory() {
    }
    
    public static DataSourceFactory getInstance() {
        return instance;
    }
    
    /**
     * @param dbType
     * @return
     */
    public IDBPlugin getDataSource(final String dbType) {
        final Collection<IDBPlugin> instances = getInstances();
        return instances.stream().filter(v -> dbType.equalsIgnoreCase(v.getDbType())).findFirst()
                .orElseThrow(() -> new CommonRuntimeException(ErrorCode.FAILED));
    }
    
    /**
     * @param connectionInfo
     * @return
     * @throws CommonException
     */
    public Connection getConnection(final ConnectionInfo connectionInfo) throws CommonException {
        Assert.notNull(connectionInfo);
        Connection connection = connectionInfo.getConnectionNotReset();
        if (connection != null) {
            return connection;
        }
        final IDBPlugin dataSource = this.getDataSource(connectionInfo.getDbType());
        connectionInfo.setPropertiesConfig(dataSource.getDBConfig());
        final IDBManager dbManager = dataSource.getDBManager();
        if (dbManager != null) {
            connection = dbManager.getConnection(connectionInfo);
            if (connection != null) {
                return connection;
            }
        }
        throw new CommonException(ErrorCode.CONNECTION_FAILED);
    }
    
    /**
     * @param connectionInfo
     * @return
     */
    public List<Database> getDateBaseList(final ConnectionInfo connectionInfo) {
        final IDBPlugin dataSource = getDataSource(connectionInfo.getDbType());
        final IMetaData metaData = dataSource.getMetaData();
        if (metaData != null) {
            return metaData.databases(connectionInfo.getConnection());
        }
        return null;
    }
    
    /**
     * @param connectionInfo
     * @param databaseName
     * @return
     */
    public List<Schema> getSchemasList(final ConnectionInfo connectionInfo, final String databaseName) throws CommonException {
        final IDBPlugin dataSource = getDataSource(connectionInfo.getDbType());
        final IMetaData metaData = dataSource.getMetaData();
        if (metaData != null) {
            // 数据库不一致，切换数据库连接
            if (StrUtil.isNotBlank(databaseName) && !databaseName.equals(connectionInfo.getDatabaseName())) {
                connectionInfo.setResetConnection(true);
                connectionInfo.setDatabaseName(databaseName);
                getConnection(connectionInfo);
            }
            return metaData.schemas(connectionInfo.getConnection(), databaseName, connectionInfo.getDatabaseName());
        }
        return null;
    }
    
    /**
     * @param connectionInfo
     * @param databaseName
     * @param schemaName
     * @param tableNamePattern
     * @return
     */
    public List<Table> getTableList(final ConnectionInfo connectionInfo, final String databaseName, final String schemaName,
            final String tableNamePattern, final boolean queryTableDetailMore) {
        final IDBPlugin dataSource = getDataSource(connectionInfo.getDbType());
        final IMetaData metaData = dataSource.getMetaData();
        if (metaData != null) {
            return metaData.tables(connectionInfo.getConnection(), databaseName, schemaName, tableNamePattern, queryTableDetailMore);
        }
        return null;
    }
    
    /**
     * @param connectionInfo
     * @param databaseName
     * @return
     */
    public List<Table> getTableList(final ConnectionInfo connectionInfo, final String databaseName, final boolean queryTableDetailMore) {
        return getTableList(connectionInfo, databaseName, connectionInfo.getSchemaName(), null, queryTableDetailMore);
    }
    
    /**
     * @param connectionInfo
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @return
     */
    public List<TableColumn> getColumnList(final ConnectionInfo connectionInfo, final String databaseName, final String schemaName,
            final String tableName) {
        final IDBPlugin dataSource = getDataSource(connectionInfo.getDbType());
        final IMetaData metaData = dataSource.getMetaData();
        if (metaData != null) {
            return metaData.columns(connectionInfo.getConnection(), databaseName, schemaName, tableName);
        }
        return null;
    }
    
    /**
     * @param connection
     */
    public void closeConnection(final Connection connection) {
        JdbcUtil.closeConnection(connection);
    }
    
    /**
     * @return
     */
    public Collection<IDBPlugin> getInstances() {
        return ServiceLoaderUtil.getServiceInstances(IDBPlugin.class);
    }
    
}
