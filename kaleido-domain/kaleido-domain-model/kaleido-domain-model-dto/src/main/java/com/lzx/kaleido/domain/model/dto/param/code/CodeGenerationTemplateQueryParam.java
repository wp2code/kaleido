package com.lzx.kaleido.domain.model.dto.param.code;

import lombok.Data;

/**
 * @author lwp
 * @date 2023-12-10
 **/
@Data
public class CodeGenerationTemplateQueryParam {
    
    /**
     * 模板名称
     */
    private String templateName;
    
    /**
     * 模板语言
     */
    private String language;
    
    /**
     * 隐藏状态：0-显示；1-隐藏
     */
    private Integer hideStatus;
    
}
