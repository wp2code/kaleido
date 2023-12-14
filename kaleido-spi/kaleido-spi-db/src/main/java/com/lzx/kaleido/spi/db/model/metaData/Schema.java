package com.lzx.kaleido.spi.db.model.metaData;

import com.fasterxml.jackson.annotation.JsonAlias;
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
public class Schema implements Serializable {
    
    /**
     * databaseName
     */
    @JsonAlias({"TABLE_CATALOG","table_catalog"})
    private String databaseName;
    /**
     * 数据名字
     */
    @JsonAlias({"TABLE_SCHEM","table_schem"})
    private String name;
    
    
    private String comment;
    
    
    private String owner;
}
