package com.lzx.kaleido.domain.model.vo.datasource;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lwp
 * @date 2023-11-19
 **/
@Data
public class DatabaseVO implements Serializable {
    
    
    private String name;
    
    /**
     *
     */
    private List<TableVO> tableList;
    
    /**
     *
     */
    private List<SchemaVO> schemaList;
    
    /**
     *
     */
    private boolean supportSchema;
}
