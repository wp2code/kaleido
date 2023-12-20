package com.lzx.kaleido.spi.db.jdbc;

import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.spi.db.IMetaData;
import com.lzx.kaleido.spi.db.model.metaData.Database;
import com.lzx.kaleido.spi.db.model.metaData.Schema;
import com.lzx.kaleido.spi.db.model.metaData.Table;
import com.lzx.kaleido.spi.db.model.metaData.TableColumn;
import com.lzx.kaleido.spi.db.model.metaData.TableIndex;
import com.lzx.kaleido.spi.db.model.metaData.Type;
import com.lzx.kaleido.spi.db.sql.SQLExecutor;

import java.sql.Connection;
import java.util.List;

/**
 * @author lwp
 * @date 2023-11-15
 **/
public class BaseMetaData implements IMetaData {
    
    /**
     * 获取数据库支持的所有的类型
     *
     * @param connection
     * @return
     */
    @Override
    public List<Type> types(final Connection connection) {
        return SQLExecutor.getInstance().types(connection);
    }
    
    /**
     * 获取数据库
     *
     * @param connection
     * @return
     */
    @Override
    public List<Database> databases(final Connection connection) {
        return SQLExecutor.getInstance().databases(connection, null);
    }
    
    /**
     * 获取schema
     *
     * @param connection
     * @param databaseName
     * @return
     */
    @Override
    public List<Schema> schemas(final Connection connection, final String databaseName,final String currConnectionDatabase) {
        return SQLExecutor.getInstance().schemas(connection, null, databaseName);
    }
    
    /**
     * 获取数据库表
     *
     * @param connection
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @return
     */
    @Override
    public List<Table> tables(final Connection connection, final String databaseName, final String schemaName, final String tableName,
            final boolean queryTableDetailMore) {
        return SQLExecutor.getInstance()
                .tables(connection, StrUtil.isBlank(databaseName) ? null : databaseName, StrUtil.isBlank(schemaName) ? null : schemaName,
                        tableName, new String[] {"TABLE", "SYSTEM TABLE"}, null);
        
    }
    
    
    /**
     * 获取表列信息
     *
     * @param connection
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @return
     */
    @Override
    public List<TableColumn> columns(final Connection connection, final String databaseName, final String schemaName,
            final String tableName) {
        return SQLExecutor.getInstance()
                .columns(connection, StrUtil.isBlank(databaseName) ? null : databaseName, StrUtil.isBlank(schemaName) ? null : schemaName,
                        tableName, null);
    }
    
    /**
     * 获取表索引
     *
     * @param connection
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @return
     */
    @Override
    public List<TableIndex> indexes(final Connection connection, final String databaseName, final String schemaName,
            final String tableName) {
        return SQLExecutor.getInstance()
                .indexes(connection, StrUtil.isBlank(databaseName) ? null : databaseName, StrUtil.isBlank(schemaName) ? null : schemaName,
                        tableName);
        
    }
}
