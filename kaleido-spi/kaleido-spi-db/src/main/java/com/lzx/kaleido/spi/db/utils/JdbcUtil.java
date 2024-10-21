package com.lzx.kaleido.spi.db.utils;

import com.lzx.kaleido.infra.base.enums.JavaTypeEnum;
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
        return toJavaTypeEnum(columnType).getType();
    }
    
    /**
     * @param columnType
     * @return
     */
    public static JavaTypeEnum toJavaTypeEnum(String columnType) {
        columnType = columnType.toUpperCase();
        if ("TEXT".equals(columnType) || "VARCHAR".equals(columnType) || "TINYTEXT".equals(columnType) || "LONGTEXT".equals(columnType)
                || "JSON".equals(columnType) || "XML".equals(columnType) || "NCHAR".equals(columnType)) {
            return JavaTypeEnum.String;
        }
        if ("BIGINT".equals(columnType) || "INT8".equals(columnType) || "BIGSERIAL".equals(columnType)) {
            return JavaTypeEnum.Long;
        }
        if ("INT".equals(columnType) || "INTEGER".equals(columnType) || "MEDIUMINT".equals(columnType) || "SMALLINT".equals(columnType)
                || "INT2".equals(columnType) || "INT4".equals(columnType) || "INT UNSIGNED".equals(columnType)) {
            return JavaTypeEnum.Integer;
        }
        if ("FLOAT".equals(columnType) || "FLOAT4".equals(columnType)) {
            return JavaTypeEnum.Float;
        }
        if ("DOUBLE".equals(columnType) || "FLOAT8".equals(columnType) || "MONEY".equals(columnType)) {
            return JavaTypeEnum.Double;
        }
        if ("NUMERIC".equals(columnType) || "DECIMAL".equals(columnType) || "numeric".equals(columnType)) {
            return JavaTypeEnum.BigDecimal;
        }
        if ("DATE".equals(columnType) || "YEAR".equals(columnType)) {
            return JavaTypeEnum.Date;
        }
        if ("TIMESTAMP".equals(columnType) || "DATETIME".equals(columnType)) {
            return JavaTypeEnum.LocalDateTime;
        }
        if ("TINYINT".equals(columnType)) {
            return JavaTypeEnum.Byte;
        }
        if ("BIT".equals(columnType) || "BOOL".equals(columnType)) {
            return JavaTypeEnum.Boolean;
        }
        if ("BLOB".equals(columnType)) {
            return JavaTypeEnum.Bytes;
        }
        if ("CLOB".equals(columnType)) {
            return JavaTypeEnum.Clob;
        }
        if ("TIME".equals(columnType)) {
            return JavaTypeEnum.Time;
        }
        if ("CHAR".equals(columnType)) {
            return JavaTypeEnum.CHAR;
        }
        
        return JavaTypeEnum.Object;
    }
    
    /**
     * @param typeName
     * @param type
     * @return
     */
    public DataType resolveDataType(final String typeName, final Integer type) {
        final Integer typeCode = getTypeByTypeName(typeName, type);
        return EasyEnumUtil.getEnumByCode(DataType.class, typeCode);
    }
    
    /**
     * @param typeName
     * @param type
     * @return
     */
    private int getTypeByTypeName(String typeName, Integer type) {
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
