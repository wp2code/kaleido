package com.lzx.kaleido.spi.db.model.metaData;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.lzx.kaleido.spi.db.enums.DataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author lwp
 **/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TableColumn implements Serializable {
    
    
    @JsonAlias({"DATA_TYPE", "data_type"})
    private Integer dataType;
    
    /**
     * 列名
     */
    @JsonAlias({"COLUMN_NAME", "column_name"})
    private String name;
    
    /**
     * 空间名
     */
    @JsonAlias({"TABLE_SCHEM", "table_schem"})
    private String schemaName;
    
    /**
     * 数据库名
     */
    @JsonAlias({"TABLE_CAT", "table_cat"})
    private String databaseName;
    
    /**
     * 表名
     */
    @JsonAlias({"TABLE_NAME", "table_name"})
    private String tableName;
    
    @JsonAlias({"COLUMN_DEF", "column_def"})
    private String defaultValue;
    
    @JsonAlias({"REMARKS", "remarks"})
    private String comment;
    
    
    @JsonAlias({"TYPE_NAME", "type_name"})
    private String columnType;
    
    @JsonAlias({"ORDINAL_POSITION", "ordinal_position"})
    private Integer columnIndex;
    
    @JsonAlias({"NULLABLE", "nullable"})
    private Integer nullable;
    
    @JsonAlias({"DECIMAL_DIGITS", "decimal_digits"})
    private Integer decimalDigits;
    
    /**
     * Radix (typically either 10 or 2)
     */
    
    @JsonAlias({"NUM_PREC_RADIX", "num_prec_radix"})
    private Integer numPrecRadix;
    
    @JsonAlias({"COLUMN_SIZE", "column_size"})
    private Integer columnSize;
    
    @JsonAlias({"ORDINAL_POSITION", "ordinal_position"})
    private Integer ordinalPosition;
    
    private Boolean primaryKey;
    
    private Boolean autoIncrement;
    
    
    /**
     *
     */
    private DataType dataTypeEnum;
    
    private String charSetName;
    
    private String collationName;
    
    //Mysql
    private String value;
    
}
