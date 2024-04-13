package com.lzx.kaleido.spi.db;

import com.lzx.kaleido.spi.db.model.TableColumnJavaMap;
import com.lzx.kaleido.spi.db.model.metaData.Database;
import com.lzx.kaleido.spi.db.model.metaData.Schema;
import com.lzx.kaleido.spi.db.model.metaData.Table;
import com.lzx.kaleido.spi.db.model.metaData.TableColumn;
import com.lzx.kaleido.spi.db.model.metaData.TableIndex;
import com.lzx.kaleido.spi.db.model.metaData.Type;
import jakarta.validation.constraints.NotEmpty;

import java.sql.Connection;
import java.util.List;

/**
 * @author lwp
 * @date 2023-11-16
 **/
public interface IMetaData {
    
    /**
     * 获取数据库支持的所有的类型
     *
     * @param connection
     * @return
     */
    List<Type> types(Connection connection);
    
    /**
     * 获取数据库
     *
     * @param connection
     * @param databaseName
     * @return
     */
    List<Database> databases(Connection connection,String databaseName);
    
    /**
     * 获取schema
     *
     * @param connection
     * @param databaseName
     * @param currConnectionDatabase
     * @return
     */
    List<Schema> schemas(Connection connection, String databaseName, String currConnectionDatabase);
    
    /**
     * 获取数据库表
     *
     * @param connection
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @return
     */
    List<Table> tables(Connection connection, @NotEmpty String databaseName, String schemaName, String tableName,
            final boolean queryTableDetailMore);
    
    /**
     * 表的ddl
     *
     * @param connection
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @return
     */
    default String tableDDL(Connection connection, @NotEmpty String databaseName, String schemaName, @NotEmpty String tableName) {
        return null;
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
    List<TableColumn> columns(Connection connection, @NotEmpty String databaseName, String schemaName, @NotEmpty String tableName);
    
    /**
     * 转换java属性
     *
     * @param columnList
     * @return
     */
    List<TableColumnJavaMap> transformJavaProperty(List<TableColumn> columnList);
    
    /**
     * 获取表索引
     *
     * @param connection
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @return
     */
    List<TableIndex> indexes(Connection connection, @NotEmpty String databaseName, String schemaName, @NotEmpty String tableName);
    
    /**
     * @param connection
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @return
     */
    List<String> primaryKeys(Connection connection, @NotEmpty String databaseName, String schemaName, @NotEmpty String tableName);
}
