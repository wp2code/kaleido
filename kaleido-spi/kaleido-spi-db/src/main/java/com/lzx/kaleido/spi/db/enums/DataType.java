package com.lzx.kaleido.spi.db.enums;

import com.lzx.kaleido.infra.base.enums.IBaseEnum;

/**
 * @author lwp
 * @date 2023-11-14
 **/
public enum DataType implements IBaseEnum<Integer> {
    ARRAY(java.sql.Types.ARRAY), //
    BIT(java.sql.Types.BIT), //
    TINYINT(java.sql.Types.TINYINT), //
    SMALLINT(java.sql.Types.SMALLINT), //
    INTEGER(java.sql.Types.INTEGER), //
    BIGINT(java.sql.Types.BIGINT), //
    FLOAT(java.sql.Types.FLOAT), //
    REAL(java.sql.Types.REAL), //
    DOUBLE(java.sql.Types.DOUBLE), //
    NUMERIC(java.sql.Types.NUMERIC), //
    DECIMAL(java.sql.Types.DECIMAL), //
    CHAR(java.sql.Types.CHAR), //
    VARCHAR(java.sql.Types.VARCHAR), //
    LONGVARCHAR(java.sql.Types.LONGVARCHAR), //
    DATE(java.sql.Types.DATE), //
    TIME(java.sql.Types.TIME), //
    TIMESTAMP(java.sql.Types.TIMESTAMP), //
    BINARY(java.sql.Types.BINARY), //
    VARBINARY(java.sql.Types.VARBINARY), //
    LONGVARBINARY(java.sql.Types.LONGVARBINARY), //
    NULL(java.sql.Types.NULL), //
    OTHER(java.sql.Types.OTHER), //
    BLOB(java.sql.Types.BLOB), //
    CLOB(java.sql.Types.CLOB), //
    BOOLEAN(java.sql.Types.BOOLEAN), //
    CURSOR(-10), // Oracle
    UNDEFINED(Integer.MIN_VALUE + 1000), //
    NVARCHAR(java.sql.Types.NVARCHAR), // JDK6
    NCHAR(java.sql.Types.NCHAR), // JDK6
    NCLOB(java.sql.Types.NCLOB), // JDK6
    STRUCT(java.sql.Types.STRUCT), //
    JAVA_OBJECT(java.sql.Types.JAVA_OBJECT), //
    DISTINCT(java.sql.Types.DISTINCT), //
    REF(java.sql.Types.REF), //
    DATALINK(java.sql.Types.DATALINK), //
    ROWID(java.sql.Types.ROWID), // JDK6
    LONGNVARCHAR(java.sql.Types.LONGNVARCHAR), // JDK6
    SQLXML(java.sql.Types.SQLXML), // JDK6
    DATETIMEOFFSET(-155), // SQL Server 2008
    TIME_WITH_TIMEZONE(2013), // JDBC 4.2 JDK8
    TIMESTAMP_WITH_TIMEZONE(2014); // JDBC 4.2 JDK8
    
    
    DataType(Integer type) {
        this.initEnum(type, null);
    }
    
}
