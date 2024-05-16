package com.lzx.kaleido.domain.model.vo.code;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lwp
 * @date 2024-04-16
 **/
@Data
public class JavaTypeVO implements Serializable {
    
    private String type;
    
    private String simpleType;
    private String classification;
}
