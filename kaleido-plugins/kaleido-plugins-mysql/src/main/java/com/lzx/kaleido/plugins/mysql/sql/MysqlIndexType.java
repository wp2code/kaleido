package com.lzx.kaleido.plugins.mysql.sql;

import com.lzx.kaleido.infra.base.enums.IBaseEnum;

public enum MysqlIndexType implements IBaseEnum<String> {
    
    PRIMARY_KEY("Primary", "PRIMARY KEY"),
    
    NORMAL("Normal", "INDEX"),
    
    UNIQUE("Unique", "UNIQUE INDEX"),
    
    FULLTEXT("Fulltext", "FULLTEXT INDEX"),
    
    SPATIAL("Spatial", "SPATIAL INDEX");
    
    
    /**
     * @param name
     * @param keyword
     */
    MysqlIndexType(String name, String keyword) {
        this.initEnum(keyword, name);
    }
    
    
}
