package com.lzx.kaleido.domain.api.enums;

import com.lzx.kaleido.infra.base.enums.IBaseEnum;

/**
 * 默认模板状态
 *
 * @author lwp
 * @date 2023-12-17
 **/
public enum CodeTemplateDefaultEnum implements IBaseEnum<Integer> {
    DEFAULT(1, "默认"),
    NORMAL(0, "普通");
    
    CodeTemplateDefaultEnum(Integer code, String name) {
        initEnum(code, name);
    }
}
