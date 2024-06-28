package com.lzx.kaleido.domain.model.dto.datasource.param;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lwp
 * @date 2024-02-18
 **/
@Data
@Accessors(chain = true)
public class TableFieldColumnParam implements Serializable {
    
    private String dataBaseName;
    
    private String schemaName;
    
    private String tableName;
    
    private String connectionId;
    
}
