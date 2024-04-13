package com.lzx.kaleido.domain.core.datasource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonException;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.utils.ServiceLoaderUtil;
import com.lzx.kaleido.spi.db.ConnectionManager;
import com.lzx.kaleido.spi.db.IDBManager;
import com.lzx.kaleido.spi.db.IDBPlugin;
import com.lzx.kaleido.spi.db.IMetaData;
import com.lzx.kaleido.spi.db.model.ConnectionInfo;
import com.lzx.kaleido.spi.db.model.ConnectionWrapper;
import com.lzx.kaleido.spi.db.model.TableColumnJavaMap;
import com.lzx.kaleido.spi.db.model.metaData.Database;
import com.lzx.kaleido.spi.db.model.metaData.Schema;
import com.lzx.kaleido.spi.db.model.metaData.Table;
import com.lzx.kaleido.spi.db.model.metaData.TableColumn;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

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
     * @param isCacheConnection
     * @return
     * @throws CommonException
     */
    public ConnectionWrapper getConnection(final ConnectionInfo connectionInfo, boolean isCacheConnection) throws CommonException {
        Assert.notNull(connectionInfo);
        ConnectionWrapper connectionWrapper = connectionInfo.getConnection();
        if (connectionWrapper != null && isCacheConnection) {
            return connectionWrapper;
        }
        final IDBPlugin dataSource = this.getDataSource(connectionInfo.getDbType());
        connectionInfo.setPropertiesConfig(dataSource.getDBConfig());
        final IDBManager dbManager = dataSource.getDBManager();
        if (dbManager != null) {
            connectionWrapper = dbManager.getConnection(connectionInfo);
            if (connectionWrapper != null) {
                if (isCacheConnection) {
                    ConnectionManager.getInstance().register(connectionWrapper);
                }
                return connectionWrapper;
            }
        }
        throw new CommonException(ErrorCode.CONNECTION_FAILED);
    }
    
    /**
     *
     */
    public void clear() {
        ConnectionManager.getInstance().removeAll();
    }
    
    /**
     * @param connectionInfo
     * @return
     */
    public List<Database> getDateBaseList(final ConnectionInfo connectionInfo) {
        final IDBPlugin dataSource = getDataSource(connectionInfo.getDbType());
        final IMetaData metaData = dataSource.getMetaData();
        if (metaData != null) {
            return metaData.databases(connectionInfo.getConnection().getConnection(), connectionInfo.getDatabaseName());
        }
        return null;
    }
    
    /**
     * @param connectionInfo
     * @param convert
     * @param <T>
     * @return
     */
    public <T> List<T> getDateBaseAndConvert(final ConnectionInfo connectionInfo, BiFunction<IDBPlugin, Database, T> convert) {
        final IDBPlugin dataSource = getDataSource(connectionInfo.getDbType());
        final IMetaData metaData = dataSource.getMetaData();
        if (metaData != null) {
            final List<Database> databases = metaData.databases(connectionInfo.getConnection().getConnection(),
                    connectionInfo.getDatabaseName());
            if (CollUtil.isNotEmpty(databases)) {
                return databases.stream().map(v -> convert.apply(dataSource, v)).collect(Collectors.toList());
            }
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
            //            if (StrUtil.isNotBlank(databaseName) && !databaseName.equals(connectionInfo.getDatabaseName())) {
            //                connectionInfo.setResetConnection(true);
            //                connectionInfo.setDatabaseName(databaseName);
            //                getConnection(connectionInfo);
            //            }
            return metaData.schemas(connectionInfo.getConnection().getConnection(), databaseName, connectionInfo.getDatabaseName());
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
            return metaData.tables(connectionInfo.getConnection().getConnection(), databaseName, schemaName, tableNamePattern,
                    queryTableDetailMore);
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
     * @param connectionId
     * @param dataBaseName
     * @param schemaName
     * @param tableName
     * @return
     */
    public List<TableColumnJavaMap> getTableColumnJavaMapList(final String connectionId, String dataBaseName, final String schemaName,
            final String tableName) {
        final ConnectionWrapper connection = ConnectionManager.getInstance().getConnectionThrowException(connectionId, dataBaseName);
        final IDBPlugin dataSource = getDataSource(connection.getDbType());
        final IMetaData metaData = dataSource.getMetaData();
        if (metaData != null) {
            final List<TableColumn> columns = metaData.columns(connection.getConnection(), dataBaseName, schemaName, tableName);
            if (CollUtil.isNotEmpty(columns)) {
                return metaData.transformJavaProperty(columns);
            }
        }
        return null;
    }
    
    /**
     * @param connection
     */
    public void closeConnection(final ConnectionWrapper connection) {
        try {
            if (connection != null) {
                ConnectionManager.getInstance().remove(connection.getId(), connection.getDatabaseName());
            }
        } catch (Exception ex) {
            //IG
        }
    }
    
    /**
     * @return
     */
    public Collection<IDBPlugin> getInstances() {
        return ServiceLoaderUtil.getServiceInstances(IDBPlugin.class);
    }
}
