package com.lzx.kaleido.domain.model.dto.datasource.param;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lwp
 * @date 2024-02-18
 **/
@Data
@Builder
public class TableFieldColumnParam implements Serializable {
    
    private String dataBaseName;
    
    private String schemaName;
    
    private String tableName;
    
    private String connectionId;
}
