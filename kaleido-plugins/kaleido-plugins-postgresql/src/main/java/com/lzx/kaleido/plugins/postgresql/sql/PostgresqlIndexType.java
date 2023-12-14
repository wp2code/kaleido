package com.lzx.kaleido.plugins.postgresql.sql;

import com.lzx.kaleido.infra.base.enums.IBaseEnum;

public enum PostgresqlIndexType implements IBaseEnum<String> {
    
    PRIMARY("Primary", "PRIMARY KEY"),
    
    FOREIGN("Foreign", "FOREIGN KEY"),
    
    NORMAL("Normal", "INDEX"),
    
    UNIQUE("Unique", "UNIQUE"),
    ;
    
    
    PostgresqlIndexType(String name, String keyword) {
        this.initEnum(keyword, name);
    }
    
    
}
