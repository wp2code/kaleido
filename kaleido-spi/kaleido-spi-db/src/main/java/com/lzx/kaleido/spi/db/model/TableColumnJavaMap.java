package com.lzx.kaleido.spi.db.model;

import com.lzx.kaleido.infra.base.pojo.spi.ITableColumnJava;
import lombok.Data;

/**
 * @author lwp
 * @date 2024-01-16
 **/
@Data
public class TableColumnJavaMap implements ITableColumnJava {
    private String comment;
    private String column;
    private String jdbcType;
    private String property;
    private String javaType;
    private String javaTypeSimpleName;
    private Boolean primaryKey;
    private Integer dataType;
    

}
