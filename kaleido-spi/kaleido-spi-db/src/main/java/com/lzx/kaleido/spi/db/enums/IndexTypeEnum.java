package com.lzx.kaleido.spi.db.enums;


import com.lzx.kaleido.infra.base.enums.IBaseEnum;

/**
 * 索引类型
 *
 * @author lwp
 * @date 2023-11-14
 **/
public enum IndexTypeEnum implements IBaseEnum<String> {
    /**
     * 主键
     */
    PRIMARY_KEY("PRIMARY_KEY"),
    
    /**
     * 普通索引
     */
    NORMAL("NORMAL"),
    
    /**
     * 唯一索引
     */
    UNIQUE("UNIQUE"),
    ;
    
    
    IndexTypeEnum(String code) {
        this.initEnum(code, null);
    }
    
}
