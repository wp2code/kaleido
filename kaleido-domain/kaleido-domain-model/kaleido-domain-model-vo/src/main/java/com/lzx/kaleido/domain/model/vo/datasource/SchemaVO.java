package com.lzx.kaleido.domain.model.vo.datasource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author lwp
 * @date 2023-12-13
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchemaVO implements Serializable {
    
    /**
     *
     */
    private String schemaName;
    
    /**
     *
     */
    private List<TableVO> tableList;
}
