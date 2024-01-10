package com.lzx.kaleido.domain.api.enums;

import com.lzx.kaleido.infra.base.enums.IBaseEnum;

/**
 * 模板显示\隐藏状态
 *
 * @author lwp
 **/
public enum CodeTemplateHideEnum implements IBaseEnum<Integer> {
    SHOW(0, "显示"),
    HIDE(1, "隐藏");
    
    CodeTemplateHideEnum(Integer code, String name) {
        initEnum(code, name);
    }
}
