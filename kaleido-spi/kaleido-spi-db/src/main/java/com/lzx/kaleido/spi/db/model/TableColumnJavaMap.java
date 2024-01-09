package com.lzx.kaleido.spi.db.model;

import lombok.Data;

/**
 * @author lwp
 * @date 2024-01-16
 **/
@Data
public class TableColumnJavaMap {
    
    private String property;
    
    private String javaType;
    
    private String column;
    
    private String jdbcType;
}
