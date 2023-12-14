package com.lzx.kaleido.spi.db.model.metaData;

import lombok.Builder;
import lombok.Data;

/**
 * @author lwp
 * @date 2023-11-15
 **/
@Data
@Builder
public class Row {
    
    private String name;
    
    private String value;
    
    private TableColumn tableColumn;
    
}
