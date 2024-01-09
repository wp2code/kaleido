package com.lzx.kaleido.domain.api.enums;

import com.lzx.kaleido.infra.base.enums.IBaseEnum;

/**
 * @author lwp
 * @date 2023-12-17
 **/
public enum CodeTemplateDefaultStatus implements IBaseEnum<Integer> {
    DEFAULT(1, "默认"),
    NORMAL(0, "普通");
    
    CodeTemplateDefaultStatus(Integer code, String name) {
        initEnum(code, name);
    }
}
