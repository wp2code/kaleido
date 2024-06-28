package com.lzx.kaleido.domain.model.dto.code.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lwp
 * @date 2023-12-10
 **/
@Data
public class CodeGenerationTemplateQueryParam implements Serializable {
    
    /**
     * 模板名称
     */
    private String templateName;
    
    /**
     * 模板语言
     */
    private String language = "java";
    
    /**
     * 隐藏状态：0-显示；1-隐藏
     */
    private Integer hideStatus;
    
}
