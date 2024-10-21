package com.lzx.kaleido.plugins.mysql;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.infra.base.enums.IBaseEnum;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.utils.EasyEnumUtil;
import com.lzx.kaleido.plugins.mysql.sql.ISQL;
import com.lzx.kaleido.plugins.mysql.sql.MysqlIndexType;
import com.lzx.kaleido.spi.db.enums.DataType;
import com.lzx.kaleido.spi.db.enums.DataTypeAdapter;
import com.lzx.kaleido.spi.db.jdbc.BaseMetaData;
import com.lzx.kaleido.spi.db.model.metaData.Database;
import com.lzx.kaleido.spi.db.model.metaData.Table;
import com.lzx.kaleido.spi.db.model.metaData.TableColumn;
import com.lzx.kaleido.spi.db.model.metaData.TableIndex;
import com.lzx.kaleido.spi.db.model.metaData.TableIndexColumn;
import com.lzx.kaleido.spi.db.sql.SQLExecutor;
import com.lzx.kaleido.spi.db.utils.ResultSetUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotEmpty;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lwp
 * @date 2023-11-15
 **/
@Slf4j
public class MysqlMetaData extends BaseMetaData {
    
    private final List<String> systemDatabases = Arrays.asList("information_schema", "performance_schema", "mysql", "sys");
    
    /**
     * @param connection
     * @param databaseName
     * @return
     */
    @Override
    public List<Database> databases(Connection connection, String databaseName) {
        return SQLExecutor.getInstance().databases(connection,
                v -> !systemDatabases.contains(v.getName()) && (StrUtil.isBlank(databaseName) || StrUtil.equals(databaseName,
                        v.getName())));
    }
    
    /**
     * @param connection
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @return
     */
    @Override
    public String tableDDL(Connection connection, @NotEmpty String databaseName, String schemaName, @NotEmpty String tableName) {
        String sql = "SHOW CREATE TABLE " + format(databaseName) + "." + format(tableName);
        return SQLExecutor.getInstance().execute(connection, sql, resultSet -> {
            if (resultSet.next()) {
                return resultSet.getString("Create Table");
            }
            return null;
        });
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
                        tableName, new String[] {"TABLE", "SYSTEM TABLE"}, (resultSet) -> {
                            final List<Table> tables = ResultSetUtil.toObjectList(resultSet, Table.class);
                            if (queryTableDetailMore && CollUtil.isNotEmpty(tables)) {
                                try (Statement stmt = connection.createStatement()) {
                                    if (StrUtil.isNotBlank(databaseName)) {
                                        stmt.execute("use `%s`;".formatted(databaseName));
                                    }
                                    for (Table table : tables) {
                                        final String sql = String.format("show table status where name='%s';", table.getName());
                                        boolean query = stmt.execute(sql);
                                        if (query) {
                                            try (ResultSet rs = stmt.getResultSet();) {
                                                if (rs.next()) {
                                                    table.setComment(rs.getString("Comment"));
                                                }
                                            }
                                        }
                                    }
                                } catch (SQLException e) {
                                    throw new CommonRuntimeException(e);
                                }
                            }
                            return tables;
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
    public List<TableIndex> indexes(Connection connection, String databaseName, String schemaName, String tableName) {
        final StringBuilder queryBuf = new StringBuilder("SHOW INDEX FROM ");
        queryBuf.append("`").append(tableName).append("`");
        queryBuf.append(" FROM ");
        queryBuf.append("`").append(databaseName).append("`");
        return SQLExecutor.getInstance().execute(connection, queryBuf.toString(), resultSet -> {
            LinkedHashMap<String, TableIndex> map = new LinkedHashMap();
            while (resultSet.next()) {
                String keyName = resultSet.getString("Key_name");
                TableIndex tableIndex = map.get(keyName);
                if (tableIndex != null) {
                    List<TableIndexColumn> columnList = tableIndex.getColumnList();
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
                    index.setUnique(!resultSet.getBoolean("Non_unique"));
                    index.setType(resultSet.getString("Index_type"));
                    index.setComment(resultSet.getString("Index_comment"));
                    List<TableIndexColumn> tableIndexColumns = new ArrayList<>();
                    tableIndexColumns.add(getTableIndexColumn(resultSet));
                    index.setColumnList(tableIndexColumns);
                    if ("PRIMARY".equalsIgnoreCase(keyName)) {
                        index.setType(MysqlIndexType.PRIMARY_KEY.getName());
                    } else if (index.getUnique()) {
                        index.setType(MysqlIndexType.UNIQUE.getName());
                    } else if ("SPATIAL".equalsIgnoreCase(index.getType())) {
                        index.setType(MysqlIndexType.SPATIAL.getName());
                    } else if ("FULLTEXT".equalsIgnoreCase(index.getType())) {
                        index.setType(MysqlIndexType.FULLTEXT.getName());
                    } else {
                        index.setType(MysqlIndexType.NORMAL.getName());
                    }
                    map.put(keyName, index);
                }
            }
            return map.values().stream().collect(Collectors.toList());
        });
        
    }
    
    @Override
    public String primaryKeySql(final String databaseName, final String schemaName, final String tableName) {
        return "SELECT column_name FROM INFORMATION_SCHEMA.`KEY_COLUMN_USAGE` WHERE TABLE_SCHEMA = '%s'"
                + "AND table_name = '%s' AND constraint_name = 'PRIMARY'".formatted(schemaName, tableName);
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
        final String sql = ISQL.SELECT_TABLE_COLUMNS.formatted(Optional.ofNullable(schemaName).orElse(databaseName), tableName);
        final List<TableColumn> tableColumns = new ArrayList<>();
        return SQLExecutor.getInstance().execute(connection, sql, resultSet -> {
            while (resultSet.next()) {
                final TableColumn column = new TableColumn();
                column.setDatabaseName(databaseName);
                column.setTableName(tableName);
                final String dataType = resultSet.getString("DATA_TYPE").toUpperCase();
                IBaseEnum<Integer> dataTypeEnum = EasyEnumUtil.getEnumByName(DataType.class, dataType);
                if (dataTypeEnum == null) {
                    dataTypeEnum = EasyEnumUtil.getEnumByName(DataTypeAdapter.class, dataType);
                }
                if (dataTypeEnum != null) {
                    column.setDataType(dataTypeEnum.getCode());
                } else {
                    log.error("数据库字段类型{}不适配", dataType);
                }
                column.setName(resultSet.getString("COLUMN_NAME"));
                column.setColumnType(dataType);
                column.setDefaultValue(resultSet.getString("COLUMN_DEFAULT"));
                column.setAutoIncrement(resultSet.getString("EXTRA").contains("auto_increment"));
                column.setComment(resultSet.getString("COLUMN_COMMENT"));
                column.setPrimaryKey("PRI".equalsIgnoreCase(resultSet.getString("COLUMN_KEY")));
                column.setNullable("YES".equalsIgnoreCase(resultSet.getString("IS_NULLABLE")) ? 1 : 0);
                column.setOrdinalPosition(resultSet.getInt("ORDINAL_POSITION"));
                column.setDecimalDigits(resultSet.getInt("NUMERIC_SCALE"));
                column.setCharSetName(resultSet.getString("CHARACTER_SET_NAME"));
                column.setCollationName(resultSet.getString("COLLATION_NAME"));
                setColumnSize(column, resultSet.getString("COLUMN_TYPE"));
                tableColumns.add(column);
            }
            return tableColumns;
        });
    }
    
    /**
     * @param tableName
     * @return
     */
    private static String format(String tableName) {
        return "`" + tableName + "`";
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
        tableIndexColumn.setCardinality(resultSet.getLong("Cardinality"));
        tableIndexColumn.setSubPart(resultSet.getLong("Sub_part"));
        String collation = resultSet.getString("Collation");
        if ("a".equalsIgnoreCase(collation)) {
            tableIndexColumn.setAscOrDesc("ASC");
        } else if ("d".equalsIgnoreCase(collation)) {
            tableIndexColumn.setAscOrDesc("DESC");
        }
        return tableIndexColumn;
    }
    
    /**
     * @param column
     * @param columnType
     */
    private void setColumnSize(TableColumn column, String columnType) {
        try {
            if (columnType.contains("(")) {
                String size = columnType.substring(columnType.indexOf("(") + 1, columnType.indexOf(")"));
                if ("SET".equalsIgnoreCase(column.getColumnType()) || "ENUM".equalsIgnoreCase(column.getColumnType())) {
                    column.setValue(size);
                } else {
                    if (size.contains(",")) {
                        String[] sizes = size.split(",");
                        if (StringUtils.isNotBlank(sizes[0])) {
                            column.setColumnSize(Integer.parseInt(sizes[0]));
                        }
                        if (StringUtils.isNotBlank(sizes[1])) {
                            column.setDecimalDigits(Integer.parseInt(sizes[1]));
                        }
                    } else {
                        column.setColumnSize(Integer.parseInt(size));
                    }
                }
            }
        } catch (Exception e) {
            //IG
        }
    }
}
