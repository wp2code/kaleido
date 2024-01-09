package com.lzx.kaleido.infra.base.enums;

import com.lzx.kaleido.infra.base.model.EnumBean;
import com.lzx.kaleido.infra.base.utils.EasyEnumUtil;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author lwp
 * @date 2023-11-16
 **/
public interface IBaseEnum<T> extends Serializable {
    
    /**
     * @return
     */
    default T getCode() {
        return Optional.ofNullable(EasyEnumUtil.getEnum(this)).map(EnumBean::getCode).orElse(null);
    }
    
    /**
     * @return
     */
    default String getName() {
        return Optional.ofNullable(EasyEnumUtil.getEnum(this)).map(EnumBean::getName).orElse(null);
    }
    
    
    /**
     * @param code
     * @param name
     */
    default void initEnum(T code, String name) {
        EasyEnumUtil.putEnum(this, code, name);
    }
}
