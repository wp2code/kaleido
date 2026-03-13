package com.lzx.kaleido.plugins.dm;

import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.infra.base.enums.IBaseEnum;
import com.lzx.kaleido.infra.base.utils.EasyEnumUtil;
import com.lzx.kaleido.plugins.dm.sql.DMIndexType;
import com.lzx.kaleido.plugins.dm.sql.ISQL;
import com.lzx.kaleido.spi.db.enums.DataType;
import com.lzx.kaleido.spi.db.jdbc.BaseMetaData;
import com.lzx.kaleido.spi.db.model.metaData.Database;
import com.lzx.kaleido.spi.db.model.metaData.Schema;
import com.lzx.kaleido.spi.db.model.metaData.Table;
import com.lzx.kaleido.spi.db.model.metaData.TableColumn;
import com.lzx.kaleido.spi.db.model.metaData.TableIndex;
import com.lzx.kaleido.spi.db.model.metaData.TableIndexColumn;
import com.lzx.kaleido.spi.db.sql.SQLExecutor;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * 达梦数据库元数据实现
 *
 * @author lwp
 * @date 2024-03-11
 **/
@Slf4j
public class DMMetaData extends BaseMetaData {
    
    /**
     * 获取达梦实例数据库名
     */
    @Override
    public List<Database> databases(Connection connection, String databaseName) {
        String SQL = """
                SELECT
                    U.NAME AS USER_NAME,
                    SCH.NAME AS SCHEMA_NAME
                FROM
                    SYSOBJECTS U,
                    SYSOBJECTS SCH
                WHERE
                    U.TYPE$ = 'UR'
                    AND SCH.TYPE$ = 'SCH'
                    AND U.ID = SCH.PID  AND U.NAME NOT IN ('SYS', 'SYSTEM', 'SYSAUDITOR', 'SYSSSO', 'CTISYS') AND SCH.NAME !='SYSDBA'
                """;
        if (StrUtil.isNotBlank(databaseName)) {
            SQL = SQL + " AND SCH.NAME='" + databaseName + "'";
        }
        return SQLExecutor.getInstance().execute(connection, SQL, resultSet -> {
            final List<Database> databases = new ArrayList<>();
            while (resultSet.next()) {
                final Database database = new Database();
                database.setName(resultSet.getString("SCHEMA_NAME"));
                database.setOwner(resultSet.getString("USER_NAME"));
                databases.add(database);
            }
            return databases;
        });
    }
    
    /**
     * 获取非系统 Schema 列表
     */
    @Override
    public List<Schema> schemas(Connection connection, String databaseName, String currConnectionDatabase) {
        return SQLExecutor.getInstance().execute(connection, "SELECT USERNAME AS SCHEMA_NAME FROM DBA_USERS WHERE USERNAME NOT IN "
                + "('SYS','SYSDBA','SYSSSO','SYSAUDITOR','CTISYS') ORDER BY USERNAME", resultSet -> {
            final List<Schema> schemas = new ArrayList<>();
            while (resultSet.next()) {
                final Schema schema = new Schema();
                schema.setName(resultSet.getString("SCHEMA_NAME"));
                schema.setDatabaseName(databaseName);
                schemas.add(schema);
            }
            return schemas;
        });
    }
    
    /**
     * 获取表列表
     */
    @Override
    public List<Table> tables(Connection connection, String databaseName, String schemaName, String tableName, boolean queryTableDetailMore) {
        final String sql = String.format(ISQL.SELECT_TABLES, databaseName);
        return SQLExecutor.getInstance().execute(connection, sql, resultSet -> {
            final List<Table> tables = new ArrayList<>();
            while (resultSet.next()) {
                final String name = resultSet.getString("TABLE_NAME");
                if (StrUtil.isNotBlank(tableName) && !StrUtil.equalsIgnoreCase(tableName, name)) {
                    continue;
                }
                final Table table = new Table();
                table.setName(name);
                table.setComment(resultSet.getString("COMMENTS"));
                table.setSchemaName(schemaName);
                table.setDatabaseName(databaseName);
                tables.add(table);
            }
            return tables;
        });
    }
    
    /**
     * 获取表列信息，并映射 DM 特有类型
     */
    @Override
    public List<TableColumn> columns(Connection connection, String databaseName, String schemaName, String tableName) {
        try {
            final List<TableColumn> columnList = super.columns(connection, databaseName, schemaName, tableName);
            final List<String> primaryKeys = super.primaryKeys(connection, Optional.ofNullable(databaseName).orElse(connection.getCatalog()),
                    Optional.ofNullable(schemaName).orElse(connection.getSchema()), tableName);
            columnList.forEach(v -> {
                // 映射 DM 特有类型到标准类型
                String mappedType = mapDmType(v.getColumnType());
                v.setColumnType(mappedType);
                final IBaseEnum<Integer> dataTypeEnum = EasyEnumUtil.getEnumByName(DataType.class, mappedType);
                if (dataTypeEnum != null) {
                    v.setDataType(dataTypeEnum.getCode());
                }
                v.setPrimaryKey(primaryKeys != null && primaryKeys.contains(v.getName()));
            });
            return columnList;
        } catch (Exception ex) {
            log.error("Get DM columns error", ex);
        }
        return null;
    }
    
    /**
     * 主键查询 SQL
     */
    @Override
    public String primaryKeySql(final String databaseName, final String schemaName, final String tableName) {
        return String.format(ISQL.SELECT_PRIMARY_KEY, schemaName, tableName);
    }
    
    /**
     * 获取索引列表
     */
    @Override
    public List<TableIndex> indexes(Connection connection, String databaseName, String schemaName, String tableName) {
        final String sql = String.format(ISQL.SELECT_TABLE_INDEXES, schemaName, tableName);
        return SQLExecutor.getInstance().execute(connection, sql, resultSet -> {
            final LinkedHashMap<String, TableIndex> map = new LinkedHashMap<>();
            while (resultSet.next()) {
                final String indexName = resultSet.getString("INDEX_NAME");
                final String constraintType = resultSet.getString("CONSTRAINT_TYPE");
                TableIndex tableIndex = map.get(indexName);
                if (tableIndex == null) {
                    tableIndex = new TableIndex();
                    tableIndex.setName(indexName);
                    tableIndex.setTableName(tableName);
                    tableIndex.setSchemaName(schemaName);
                    tableIndex.setDatabaseName(databaseName);
                    final boolean isUnique = "UNIQUE".equalsIgnoreCase(resultSet.getString("UNIQUENESS"));
                    tableIndex.setUnique(isUnique);
                    if ("P".equals(constraintType)) {
                        tableIndex.setType(DMIndexType.PRIMARY_KEY.getName());
                    } else if (isUnique || "U".equals(constraintType)) {
                        tableIndex.setType(DMIndexType.UNIQUE.getName());
                    } else {
                        tableIndex.setType(DMIndexType.NORMAL.getName());
                    }
                    tableIndex.setColumnList(new ArrayList<>());
                    map.put(indexName, tableIndex);
                }
                final TableIndexColumn indexColumn = new TableIndexColumn();
                indexColumn.setColumnName(resultSet.getString("COLUMN_NAME"));
                indexColumn.setOrdinalPosition(resultSet.getShort("COLUMN_POSITION"));
                final String descend = resultSet.getString("DESCEND");
                indexColumn.setAscOrDesc("DESC".equalsIgnoreCase(descend) ? "D" : "A");
                tableIndex.getColumnList().add(indexColumn);
            }
            // 按 ordinalPosition 排序列
            map.values().forEach(idx -> {
                if (idx.getColumnList() != null) {
                    idx.setColumnList(idx.getColumnList().stream().sorted(Comparator.comparing(TableIndexColumn::getOrdinalPosition))
                            .collect(Collectors.toList()));
                }
            });
            return new ArrayList<>(map.values());
        });
    }
    
    /**
     * 获取表 DDL（使用 DBMS_METADATA 包）
     */
    @Override
    public String tableDDL(Connection connection, String databaseName, String schemaName, String tableName) {
        try {
            final String sql = String.format("SELECT DBMS_METADATA.GET_DDL('TABLE', '%s', '%s') AS DDL FROM DUAL", tableName, schemaName);
            return SQLExecutor.getInstance().execute(connection, sql, resultSet -> {
                if (resultSet.next()) {
                    return resultSet.getString("DDL");
                }
                return null;
            });
        } catch (Exception ex) {
            log.error("Get DM table DDL error", ex);
            return null;
        }
    }
    
    /**
     * 将 DM 特有类型映射为标准 DataType 枚举名称
     */
    private String mapDmType(String dmType) {
        if (StrUtil.isBlank(dmType)) {
            return dmType;
        }
        final String upper = dmType.toUpperCase();
        switch (upper) {
            case "VARCHAR2":
                return "VARCHAR";
            case "NUMBER":
                return "DECIMAL";
            case "CLOB":
            case "TEXT":
                return "CLOB";
            case "BLOB":
                return "BLOB";
            case "INT":
            case "PLS_INTEGER":
                return "INTEGER";
            case "LONG":
                return "LONGVARCHAR";
            default:
                return upper;
        }
    }
}
