package com.lzx.kaleido.infra.base.model;


import lombok.Data;

import java.io.Serializable;

/**
 * @author lwp
 * @date 2023-11-17
 **/
@Data
public class EnumBean<T> implements Serializable {
    
    /**
     *
     */
    private final T code;
    
    /**
     *
     */
    private final String name;
    
    public EnumBean(T code, String name) {
        this.code = code;
        this.name = name;
    }
}
