package com.lzx.kaleido.spi.db.jdbc;

import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.infra.base.enums.IBaseEnum;
import com.lzx.kaleido.infra.base.enums.JavaTypeEnum;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.utils.EasyEnumUtil;
import com.lzx.kaleido.spi.db.IMetaData;
import com.lzx.kaleido.spi.db.enums.DataType;
import com.lzx.kaleido.spi.db.model.TableColumnJavaMap;
import com.lzx.kaleido.spi.db.model.metaData.Database;
import com.lzx.kaleido.spi.db.model.metaData.Schema;
import com.lzx.kaleido.spi.db.model.metaData.Table;
import com.lzx.kaleido.spi.db.model.metaData.TableColumn;
import com.lzx.kaleido.spi.db.model.metaData.TableIndex;
import com.lzx.kaleido.spi.db.model.metaData.Type;
import com.lzx.kaleido.spi.db.sql.SQLExecutor;
import com.lzx.kaleido.spi.db.utils.JdbcUtil;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lwp
 * @date 2023-11-15
 **/
public abstract class BaseMetaData implements IMetaData {
    
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
    public List<Database> databases(final Connection connection, final String databaseName) {
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
    public List<Schema> schemas(final Connection connection, final String databaseName, final String currConnectionDatabase) {
        return SQLExecutor.getInstance().schemas(connection, null, databaseName);
    }
    
    /**
     * 表的ddl
     *
     * @param connection
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @return
     */
    @Override
    public String tableDDL(final Connection connection, final String databaseName, final String schemaName, final String tableName) {
        return SQLExecutor.getInstance().ddl(connection, databaseName, schemaName, tableName);
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
     * @param columnList
     * @return
     */
    @Override
    public List<TableColumnJavaMap> transformJavaProperty(final List<TableColumn> columnList) {
        return columnList.stream().map(v -> {
            final TableColumnJavaMap tableColumnJavaMap = new TableColumnJavaMap();
            final JavaTypeEnum javaType = JdbcUtil.toJavaTypeEnum(v.getColumnType());
            //重新获取数据类型
            if (v.getDataType() == null) {
                final IBaseEnum<Integer> dataTypeEnum = EasyEnumUtil.getEnumByName(DataType.class, javaType.getSimpleType().toUpperCase());
                if (dataTypeEnum != null) {
                    v.setDataType(dataTypeEnum.getCode());
                }
            }
            tableColumnJavaMap.setColumn(v.getName());
            tableColumnJavaMap.setJdbcType(v.getColumnType());
            tableColumnJavaMap.setProperty(StrUtil.toCamelCase(v.getName()));
            tableColumnJavaMap.setJavaType(javaType.getType());
            tableColumnJavaMap.setJavaTypeSimpleName(javaType.getSimpleType());
            tableColumnJavaMap.setPrimaryKey(v.getPrimaryKey());
            tableColumnJavaMap.setComment(v.getComment());
            tableColumnJavaMap.setDataType(v.getDataType());
            return tableColumnJavaMap;
        }).collect(Collectors.toList());
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
    
    @Override
    public List<String> primaryKeys(final Connection connection, final String databaseName, final String schemaName,
            final String tableName) {
        return SQLExecutor.getInstance().execute(connection, primaryKeySql(databaseName, schemaName, tableName), (resultSet) -> {
            final List<String> pkList = new ArrayList<>();
            while (resultSet.next()) {
                final String columnName = resultSet.getString("COLUMN_NAME");
                if (StrUtil.isNotBlank(columnName)) {
                    pkList.add(columnName);
                }
            }
            return pkList;
        });
    }
    
    /**
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @return
     */
    public String primaryKeySql(final String databaseName, final String schemaName, final String tableName) {
        throw new CommonRuntimeException("run primary key sql is null", null);
    }
}
