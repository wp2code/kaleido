package com.lzx.kaleido.domain.model.vo.datasource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lwp
 * @date 2023-11-18
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableVO implements Serializable {

    private String tableName;
    
    private String dataBaseName;
    
    private String schemaName;
    
    private String comment;
}
