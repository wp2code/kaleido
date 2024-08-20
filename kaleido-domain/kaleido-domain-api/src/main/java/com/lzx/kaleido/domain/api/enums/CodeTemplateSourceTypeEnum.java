package com.lzx.kaleido.domain.api.enums;

import com.lzx.kaleido.infra.base.enums.IBaseEnum;

/**
 * @author lwp
 * @date 2024-06-14
 **/
public enum CodeTemplateSourceTypeEnum implements IBaseEnum<Integer> {
    INIT_ADD(0, "初始化自动创建"),
    MANUAL_ADD(1, "手动创建"),
    COPY_ADD(2, "复制创建"),
    IMPORT_ADD(3, "导入创建"),
    ;
    
    CodeTemplateSourceTypeEnum(Integer code, String name) {
        initEnum(code, name);
    }
}
