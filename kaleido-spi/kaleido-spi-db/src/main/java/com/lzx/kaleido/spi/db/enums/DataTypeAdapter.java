package com.lzx.kaleido.spi.db.enums;

import com.lzx.kaleido.infra.base.enums.IBaseEnum;

/**
 * @author lwp
 * @date 2024-10-19
 **/
public enum DataTypeAdapter implements IBaseEnum<Integer> {
    DATETIME(java.sql.Types.TIMESTAMP),
    TEXT(java.sql.Types.LONGVARCHAR),
    ;
    
    DataTypeAdapter(Integer type) {
        this.initEnum(type, this.name());
    }
}
