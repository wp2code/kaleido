package com.lzx.kaleido.spi.db.model.metaData;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author lwp
 * @date 2023-11-16
 **/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Database implements Serializable {
    
    @JsonAlias({"TABLE_CAT"})
    private String name;
    
    /**
     * schema name
     */
    private List<Schema> schemas;
    
    
    private String comment;
    
    private String charset;
    
    private String collation;
    
    private String owner;
}
