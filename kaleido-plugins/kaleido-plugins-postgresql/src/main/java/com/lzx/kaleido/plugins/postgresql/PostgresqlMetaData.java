package com.lzx.kaleido.plugins.postgresql;

import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.plugins.postgresql.sql.ISQL;
import com.lzx.kaleido.plugins.postgresql.sql.PostgresqlIndexType;
import com.lzx.kaleido.spi.db.jdbc.BaseMetaData;
import com.lzx.kaleido.spi.db.model.metaData.Database;
import com.lzx.kaleido.spi.db.model.metaData.Schema;
import com.lzx.kaleido.spi.db.model.metaData.TableColumn;
import com.lzx.kaleido.spi.db.model.metaData.TableIndex;
import com.lzx.kaleido.spi.db.model.metaData.TableIndexColumn;
import com.lzx.kaleido.spi.db.sql.SQLExecutor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lwp
 * @date 2023-11-17
 **/
public class PostgresqlMetaData extends BaseMetaData {
    
    private List<String> systemSchemas = Arrays.asList("pg_toast", "pg_temp_1", "pg_toast_temp_1", "pg_catalog", "information_schema");
    
    
    /**
     * @param connection
     * @return
     */
    @Override
    public List<Database> databases(Connection connection) {
        List<Database> databaseList = SQLExecutor.getInstance().execute(connection, "SELECT datname FROM pg_database;", resultSet -> {
            final List<Database> databases = new ArrayList<>();
            while (resultSet.next()) {
                String dbName = resultSet.getString("datname");
                if ("template0".equals(dbName) || "template1".equals(dbName)) {
                    continue;
                }
                final Database database = new Database();
                database.setName(dbName);
                databases.add(database);
            }
            return databases;
        });
        return databaseList;
    }
    
    /**
     * @param connection
     * @param databaseName
     * @param currConnectionDatabase
     * @return
     */
    @Override
    public List<Schema> schemas(Connection connection, String databaseName, String currConnectionDatabase) {
        final List<Schema> schemas = SQLExecutor.getInstance()
                .execute(connection, "SELECT catalog_name, schema_name FROM information_schema.schemata;", (resultSet) -> {
                    final List<Schema> databases = new ArrayList<>();
                    while (resultSet.next()) {
                        final String name = resultSet.getString("schema_name");
                        if (systemSchemas.contains(name)) {
                            continue;
                        }
                        final String catalogName = resultSet.getString("catalog_name");
                        final Schema schema = new Schema();
                        schema.setName(name);
                        schema.setDatabaseName(catalogName);
                        databases.add(schema);
                    }
                    return databases;
                    
                });
        return schemas;
    }
    
    /**
     * @param connection
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @return
     */
    @Override
    public List<TableColumn> columns(Connection connection, String databaseName, String schemaName, String tableName) {
        List<TableColumn> columnList = super.columns(connection, databaseName, schemaName, tableName);
        columnList.forEach(v -> {
            if (StrUtil.equalsIgnoreCase(v.getColumnType(), "bpchar")) {
                v.setColumnType("CHAR");
            } else {
                v.setColumnType(v.getColumnType().toUpperCase());
            }
        });
        return columnList;
    }
    
    /**
     * @param connection
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @return
     */
    @Override
    public List<TableIndex> indexes(Connection connection, String databaseName, String schemaName, String tableName) {
        String constraintSql = String.format(ISQL.SELECT_KEY_INDEX, schemaName, tableName);
        Map<String, String> constraintMap = new HashMap();
        LinkedHashMap<String, TableIndex> foreignMap = new LinkedHashMap();
        SQLExecutor.getInstance().execute(connection, constraintSql, resultSet -> {
            while (resultSet.next()) {
                String keyName = resultSet.getString("Key_name");
                String constraintType = resultSet.getString("Constraint_type");
                constraintMap.put(keyName, constraintType);
                if (StrUtil.equalsIgnoreCase(constraintType, PostgresqlIndexType.FOREIGN.getCode())) {
                    TableIndex tableIndex = foreignMap.get(keyName);
                    String columnName = resultSet.getString("Column_name");
                    if (tableIndex == null) {
                        tableIndex = new TableIndex();
                        tableIndex.setDatabaseName(databaseName);
                        tableIndex.setSchemaName(schemaName);
                        tableIndex.setTableName(tableName);
                        tableIndex.setName(keyName);
                        tableIndex.setForeignSchemaName(resultSet.getString("Foreign_schema_name"));
                        tableIndex.setForeignTableName(resultSet.getString("Foreign_table_name"));
                        tableIndex.setForeignColumnNamelist(Collections.singletonList(columnName));
                        tableIndex.setType(PostgresqlIndexType.FOREIGN.getName());
                        foreignMap.put(keyName, tableIndex);
                    } else {
                        tableIndex.getForeignColumnNamelist().add(columnName);
                    }
                }
            }
            return null;
        });
        
        String sql = String.format(ISQL.SELECT_TABLE_INDEX, schemaName, tableName);
        return SQLExecutor.getInstance().execute(connection, sql, resultSet -> {
            LinkedHashMap<String, TableIndex> map = new LinkedHashMap(foreignMap);
            while (resultSet.next()) {
                String keyName = resultSet.getString("Key_name");
                TableIndex tableIndex = map.get(keyName);
                if (tableIndex != null) {
                    List<TableIndexColumn> columnList = tableIndex.getColumnList();
                    if (columnList == null) {
                        columnList = new ArrayList<>();
                        tableIndex.setColumnList(columnList);
                    }
                    columnList.add(getTableIndexColumn(resultSet));
                    columnList = columnList.stream().sorted(Comparator.comparing(TableIndexColumn::getOrdinalPosition))
                            .collect(Collectors.toList());
                    tableIndex.setColumnList(columnList);
                } else {
                    TableIndex index = new TableIndex();
                    index.setDatabaseName(databaseName);
                    index.setSchemaName(schemaName);
                    index.setTableName(tableName);
                    index.setName(keyName);
                    index.setUnique(!StrUtil.equals("t", resultSet.getString("NON_UNIQUE")));
                    index.setMethod(resultSet.getString("Index_method"));
                    index.setComment(resultSet.getString("Index_comment"));
                    List<TableIndexColumn> tableIndexColumns = new ArrayList<>();
                    tableIndexColumns.add(getTableIndexColumn(resultSet));
                    index.setColumnList(tableIndexColumns);
                    String constraintType = constraintMap.get(keyName);
                    if (StrUtil.equals("t", resultSet.getString("Index_primary"))) {
                        index.setType(PostgresqlIndexType.PRIMARY.getName());
                    } else if (StrUtil.equalsIgnoreCase(constraintType, PostgresqlIndexType.UNIQUE.getName())) {
                        index.setType(PostgresqlIndexType.UNIQUE.getName());
                    } else {
                        index.setType(PostgresqlIndexType.NORMAL.getName());
                    }
                    map.put(keyName, index);
                }
            }
            return new ArrayList<>(map.values());
        });
        
    }
    
    /**
     * @param connection
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @return
     */
    @Override
    public String tableDDL(Connection connection, String databaseName, String schemaName, String tableName) {
        SQLExecutor.getInstance().execute(connection, ISQL.FUNCTION_SQL.replaceFirst("tableSchema", schemaName), resultSet -> null);
        String ddlSql = "select showcreatetable('" + schemaName + "','" + tableName + "') as sql";
        return SQLExecutor.getInstance().execute(connection, ddlSql, resultSet -> {
            if (resultSet.next()) {
                return resultSet.getString("sql");
            }
            return null;
        });
    }
    
    
    /**
     * @param resultSet
     * @return
     * @throws SQLException
     */
    private TableIndexColumn getTableIndexColumn(ResultSet resultSet) throws SQLException {
        TableIndexColumn tableIndexColumn = new TableIndexColumn();
        tableIndexColumn.setColumnName(resultSet.getString("Column_name"));
        tableIndexColumn.setOrdinalPosition(resultSet.getShort("Seq_in_index"));
        tableIndexColumn.setCollation(resultSet.getString("Collation"));
        tableIndexColumn.setAscOrDesc(resultSet.getString("Collation"));
        return tableIndexColumn;
    }
}
