package com.lzx.kaleido.spi.db.utils;

import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.utils.EasyEnumUtil;
import com.lzx.kaleido.spi.db.enums.DataType;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author lwp
 * @date 2023-11-14
 **/
@Slf4j
@UtilityClass
public class JdbcUtil {
    
    
    /**
     * @param rs
     */
    public void closeResultSet(@Nullable final ResultSet rs) {
        if (rs != null) {
            try {
                if (!rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException var2) {
                log.error("Could not close JDBC ResultSet", var2);
            } catch (Throwable var3) {
                log.error("Unexpected exception on closing JDBC ResultSet", var3);
            }
        }
        
    }
    
    /**
     * @param connection
     */
    public void closeConnection(final Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new CommonRuntimeException(e);
            }
        }
    }
    
    /**
     * @param columnType
     * @return
     */
    public static String toJavaType(String columnType) {
        columnType = columnType.toUpperCase();
        if ("TINYINT".equals(columnType)) {
            columnType = "java.lang.Byte";
        } else {
            if ("CHAR".equals(columnType) || "TEXT".equals(columnType) || "VARCHAR".equals(columnType) || "TINYTEXT".equals(columnType)
                    || "LONGTEXT".equals(columnType) || "JSON".equals(columnType) || "XML".equals(columnType)) {
                columnType = "java.lang.String";
            } else if ("BIGINT".equals(columnType) || "INT8".equals(columnType) || "BIGSERIAL".equals(columnType)) {
                columnType = "java.lang.Long";
            } else if ("INT".equals(columnType) || "INTEGER".equals(columnType) || "MEDIUMINT".equals(columnType) || "SMALLINT".equals(
                    columnType) || "INT2".equals(columnType) || "INT4".equals(columnType)) {
                columnType = "java.lang.Integer";
            } else if ("FLOAT".equals(columnType) || "FLOAT4".equals(columnType)) {
                columnType = "java.lang.Float";
            } else if ("DOUBLE".equals(columnType) || "FLOAT8".equals(columnType) || "MONEY".equals(columnType)) {
                columnType = "java.lang.Double";
            } else if ("NUMERIC".equals(columnType) || "DECIMAL".equals(columnType) || "numeric".equals(columnType)) {
                columnType = "java.math.BigDecimal";
            } else if ("DATE".equals(columnType) || "YEAR".equals(columnType)) {
                return "java.util.Date";
            } else if ("TIMESTAMP".equals(columnType) || "DATETIME".equals(columnType)) {
                return "java.time.LocalDateTime";
            } else if ("BIT".equals(columnType) || "BOOL".equals(columnType)) {
                return "java.lang.Boolean";
            } else if ("BLOB".equals(columnType)) {
                return "java.lang.byte[]";
            } else if ("CLOB".equals(columnType)) {
                columnType = "java.sql.Clob";
            } else if ("TIME".equals(columnType)) {
                columnType = "java.sql.Time";
            } else if ("NCHAR".equals(columnType)) {
                columnType = "java.lang.String";
            } else if ("INT UNSIGNED".equals(columnType)) {
                columnType = "java.lang.Integer";
            } else {
                columnType = "java.lang.Object";
            }
        }
        return columnType;
    }
    
    /**
     * @param typeName
     * @param type
     * @return
     */
    public DataType resolveDataType(final String typeName, final int type) {
        final int typeCode = getTypeByTypeName(typeName, type);
        return EasyEnumUtil.getEnumByCode(DataType.class, typeCode);
    }
    
    /**
     * @param typeName
     * @param type
     * @return
     */
    private int getTypeByTypeName(String typeName, int type) {
        // [JDBC: SQLite driver uses VARCHAR value type for all LOBs]
        if (type == Types.OTHER || type == Types.VARCHAR) {
            if ("BLOB".equalsIgnoreCase(typeName)) {
                return Types.BLOB;
            } else if ("CLOB".equalsIgnoreCase(typeName)) {
                return Types.CLOB;
            } else if ("NCLOB".equalsIgnoreCase(typeName)) {
                return Types.NCLOB;
            }
        } else if (type == Types.BIT) {
            // Workaround for MySQL (and maybe others) when TINYINT(1) == BOOLEAN
            if ("TINYINT".equalsIgnoreCase(typeName)) {
                return Types.TINYINT;
            }
        }
        return type;
    }
}
