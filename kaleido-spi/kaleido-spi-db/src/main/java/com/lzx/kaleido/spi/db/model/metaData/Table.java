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
 * @date 2023-11-15
 **/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Table implements Serializable {
    
    @JsonAlias({"TABLE_NAME"})
    private String name;
    
    /**
     * 描述
     */
    @JsonAlias({"REMARKS"})
    private String comment;
    
    /**
     * DB 名
     */
    @JsonAlias({"TABLE_SCHEM"})
    private String schemaName;
    
    /**
     * 列表
     */
    private List<TableColumn> columnList;
    
    /**
     * 索引列表
     */
    private List<TableIndex> indexList;
    
    /**
     * DB类型
     */
    private String dbType;
    
    /**
     * 数据库名
     */
    @JsonAlias("TABLE_CAT")
    private String databaseName;
    
    /**
     * 表类型
     */
    @JsonAlias("TABLE_TYPE")
    private String type;
    
    /**
     * ddl
     */
    private String ddl;
    
    
    private String engine;
    
    
    private String charset;
    
    
    private String collate;
    
    
    private Long incrementValue;
    
    
    private String partition;
    
    
    private String tablespace;
}
