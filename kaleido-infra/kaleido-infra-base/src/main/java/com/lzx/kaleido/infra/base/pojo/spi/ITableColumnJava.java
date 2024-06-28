package com.lzx.kaleido.infra.base.pojo.spi;

import java.io.Serializable;

/**
 * @author lwp
 * @date 2024-06-17
 **/
public interface ITableColumnJava extends Serializable {
    
    String getComment();
    
    String getColumn();
    
    String getJdbcType();
    
    String getProperty();
    
    String getJavaType();
    
    String getJavaTypeSimpleName();
    
    Boolean getPrimaryKey();
    
    Integer getDataType();
}
