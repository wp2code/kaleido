package com.lzx.kaleido.domain.api.enums;

import com.lzx.kaleido.infra.base.enums.IBaseEnum;

/**
 * @author lwp
 * @date 2023-12-17
 **/
public enum CodeTemplateHideStatus implements IBaseEnum<Integer> {
    SHOW(0, "显示"),
    HIDE(1, "隐藏");
    
    CodeTemplateHideStatus(Integer code, String name) {
        initEnum(code, name);
    }
}
