package com.lzx.kaleido.spi.db.sql;

import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.infra.base.constant.Constants;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.pojo.R;
import com.lzx.kaleido.spi.db.IResultSetValueHandler;
import com.lzx.kaleido.spi.db.enums.DataType;
import com.lzx.kaleido.spi.db.jdbc.DefaultResultSetValueHandler;
import com.lzx.kaleido.spi.db.model.JdbcResultInfo;
import com.lzx.kaleido.spi.db.model.metaData.Database;
import com.lzx.kaleido.spi.db.model.metaData.Row;
import com.lzx.kaleido.spi.db.model.metaData.Schema;
import com.lzx.kaleido.spi.db.model.metaData.Table;
import com.lzx.kaleido.spi.db.model.metaData.TableColumn;
import com.lzx.kaleido.spi.db.model.metaData.TableIndex;
import com.lzx.kaleido.spi.db.model.metaData.TableIndexColumn;
import com.lzx.kaleido.spi.db.model.metaData.Type;
import com.lzx.kaleido.spi.db.utils.JdbcUtil;
import com.lzx.kaleido.spi.db.utils.ResultSetUtil;
import io.micrometer.common.util.StringUtils;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lwp
 * @date 2023-11-09
 **/
public class SQLExecutor {
    
    /**
     *
     */
    private static final SQLExecutor INSTANCE = new SQLExecutor();
    
    /**
     * @return
     */
    public static SQLExecutor getInstance() {
        return INSTANCE;
    }
    
    private SQLExecutor() {
    }
    
    
    /**
     * @param connection
     * @param sql
     * @throws SQLException
     */
    public void execute(Connection connection, String sql) throws SQLException {
        execute(sql, connection, true, new DefaultResultSetValueHandler());
    }
    
    /**
     * @param connection
     * @param sql
     * @param function
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T execute(Connection connection, String sql, ResultSetFunction<T> function) {
        if (StringUtils.isBlank(sql)) {
            return null;
        }
        try (Statement stmt = connection.createStatement();) {
            boolean query = stmt.execute(sql);
            if (query) {
                try (ResultSet rs = stmt.getResultSet()) {
                    return function.apply(rs);
                }
            }
        } catch (SQLException e) {
            throw new CommonRuntimeException(e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * @param sql
     * @param connection
     * @param limitRowSize
     * @param resultSetValueHandler
     * @return
     * @throws SQLException
     */
    public R<JdbcResultInfo> execute(final String sql, final Connection connection, final boolean limitRowSize,
            final IResultSetValueHandler resultSetValueHandler) throws SQLException {
        final JdbcResultInfo jdbcResultInfo = new JdbcResultInfo();
        ResultSet resultSet = null;
        try (Statement stmt = connection.createStatement()) {
            stmt.setFetchSize(Constants.MAX_PAGE_SIZE);
            boolean isQuery = stmt.execute(sql);
            jdbcResultInfo.setQuery(isQuery);
            if (isQuery) {
                resultSet = stmt.getResultSet();
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int col = resultSetMetaData.getColumnCount();
                final List<TableColumn> tableColumnList = new ArrayList<>(col);
                for (int i = 1; i <= col; i++) {
                    final String columnName = ResultSetUtil.getColumnName(resultSetMetaData, i);
                    final DataType dataType = JdbcUtil.resolveDataType(resultSetMetaData.getColumnTypeName(i),
                            resultSetMetaData.getColumnType(i));
                    final TableColumn tableColumn = TableColumn.builder().dataType(dataType.getCode()).dataTypeEnum(dataType)
                            .name(columnName).columnIndex(i).build();
                    tableColumnList.add(tableColumn);
                }
                final List<List<Row>> dataList = new ArrayList<>();
                while (resultSet.next()) {
                    final List<Row> rowList = new ArrayList<>(tableColumnList.size());
                    for (final TableColumn tableColumn : tableColumnList) {
                        rowList.add(Row.builder().value(resultSetValueHandler.getString(resultSet, tableColumn, limitRowSize))
                                .name(tableColumn.getName()).tableColumn(tableColumn).build());
                    }
                    dataList.add(rowList);
                }
                jdbcResultInfo.setDataList(dataList);
            } else {
                final int updateCount = stmt.getUpdateCount();
                jdbcResultInfo.setUpdateCount(updateCount);
            }
        } finally {
            JdbcUtil.closeResultSet(resultSet);
        }
        return R.success(jdbcResultInfo);
    }
    
    /**
     * @param connection
     * @param predicate
     * @return
     */
    public List<Database> databases(Connection connection, Predicate<Database> predicate) {
        try (ResultSet resultSet = connection.getMetaData().getCatalogs();) {
            List<Database> databases = ResultSetUtil.toObjectList(resultSet, Database.class);
            if (CollectionUtils.isEmpty(databases)) {
                return databases;
            }
            final Stream<Database> databaseStream = databases.stream().filter(database -> database.getName() != null);
            if (predicate != null) {
                return databaseStream.filter(predicate).collect(Collectors.toList());
            }
            return databaseStream.collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * @param connection
     * @param databaseName
     * @param schemaName
     * @param tableNamePattern
     * @param types
     * @param function
     * @return
     */
    public List<Table> tables(Connection connection, String databaseName, String schemaName, String tableNamePattern, String[] types,
            Function<ResultSet, List<Table>> function) {
        try {
            final DatabaseMetaData metadata = connection.getMetaData();
            ResultSet resultSet = metadata.getTables(databaseName, schemaName, tableNamePattern, types);
            if (function != null) {
                return function.apply(resultSet);
            }
            return ResultSetUtil.toObjectList(resultSet, Table.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * @param connection
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @param columnName
     * @return
     */
    public List<TableColumn> columns(Connection connection, String databaseName, String schemaName, String tableName, String columnName) {
        try (ResultSet resultSet = connection.getMetaData().getColumns(databaseName, schemaName, tableName, columnName)) {
            return ResultSetUtil.toObjectList(resultSet, TableColumn.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * @param connection
     * @param databaseName
     * @param schemaName
     * @return
     */
    public List<Schema> schemas(Connection connection, String databaseName, String schemaName) {
        if (StrUtil.isBlank(databaseName) && StrUtil.isBlank(schemaName)) {
            try (ResultSet resultSet = connection.getMetaData().getSchemas()) {
                return ResultSetUtil.toObjectList(resultSet, Schema.class);
            } catch (SQLException e) {
                throw new RuntimeException("Get schemas error", e);
            }
        }
        
        try (ResultSet resultSet = connection.getMetaData().getSchemas(databaseName, schemaName)) {
            return ResultSetUtil.toObjectList(resultSet, Schema.class);
        } catch (SQLException e) {
            throw new RuntimeException("Get schemas error", e);
        }
    }
    
    /**
     * @param connection
     * @param databaseName
     * @param schemaName
     * @param tableName
     * @return
     */
    public List<TableIndex> indexes(Connection connection, String databaseName, String schemaName, String tableName) {
        final List<TableIndex> tableIndices = new ArrayList<>();
        try (ResultSet resultSet = connection.getMetaData().getIndexInfo(databaseName, schemaName, tableName, false, false)) {
            List<TableIndexColumn> tableIndexColumns = ResultSetUtil.toObjectList(resultSet, TableIndexColumn.class);
            tableIndexColumns.stream().filter(c -> c.getIndexName() != null).collect(Collectors.groupingBy(TableIndexColumn::getIndexName))
                    .entrySet().forEach(entry -> {
                        TableIndex tableIndex = new TableIndex();
                        TableIndexColumn column = entry.getValue().get(0);
                        tableIndex.setName(entry.getKey());
                        tableIndex.setTableName(column.getTableName());
                        tableIndex.setSchemaName(column.getSchemaName());
                        tableIndex.setDatabaseName(column.getDatabaseName());
                        tableIndex.setUnique(!column.getNonUnique());
                        tableIndex.setColumnList(entry.getValue());
                        tableIndices.add(tableIndex);
                    });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tableIndices;
    }
    
    /**
     * @param connection
     * @return
     */
    public List<Type> types(Connection connection) {
        try (ResultSet resultSet = connection.getMetaData().getTypeInfo();) {
            return ResultSetUtil.toObjectList(resultSet, Type.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
