package com.lzx.kaleido.domain.model.vo.code;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lwp
 * @date 2024-01-14
 **/
@Data
public class CodeGenerationTemplateViewConfigVO implements Serializable {
    
    private String name;
    
    private String config;
}
