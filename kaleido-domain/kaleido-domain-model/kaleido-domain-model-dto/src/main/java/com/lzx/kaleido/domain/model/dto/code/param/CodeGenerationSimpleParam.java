package com.lzx.kaleido.domain.model.dto.code.param;

import lombok.Data;

import java.util.List;

/**
 * @author lwp
 * @date 2024-06-13
 **/
@Data
public class CodeGenerationSimpleParam {
    
    /**
     * 模板ID
     */
    private Long templateId;
    
    /**
     * 模板名称
     */
    private String templateName;
    
    /**
     * 模板类型名称
     */
    private List<String> nameList;
    
}
