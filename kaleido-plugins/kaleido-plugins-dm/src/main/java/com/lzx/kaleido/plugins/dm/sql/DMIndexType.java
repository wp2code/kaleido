package com.lzx.kaleido.plugins.dm.sql;

import com.lzx.kaleido.infra.base.enums.IBaseEnum;

/**
 * 达梦数据库索引类型
 *
 * @author lwp
 * @date 2024-03-11
 **/
public enum DMIndexType implements IBaseEnum<String> {

    PRIMARY_KEY("Primary", "PRIMARY KEY"),
    NORMAL("Normal", "INDEX"),
    UNIQUE("Unique", "UNIQUE INDEX"),
    BITMAP("Bitmap", "BITMAP INDEX");

    DMIndexType(String name, String keyword) {
        this.initEnum(keyword, name);
    }
}
