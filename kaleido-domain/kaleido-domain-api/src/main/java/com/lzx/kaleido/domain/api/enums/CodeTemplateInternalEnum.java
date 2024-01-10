package com.lzx.kaleido.domain.api.enums;

import com.lzx.kaleido.infra.base.enums.IBaseEnum;

/**
 * 内部模板状态
 *
 * @author lwp
 * @date 2023-12-17
 **/
public enum CodeTemplateInternalEnum implements IBaseEnum<Integer> {
    N(0, "不是"),
    Y(1, "是");
    
    CodeTemplateInternalEnum(Integer code, String name) {
        initEnum(code, name);
    }
}
