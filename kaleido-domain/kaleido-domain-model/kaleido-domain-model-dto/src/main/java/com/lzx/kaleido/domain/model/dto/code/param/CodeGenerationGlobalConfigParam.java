package com.lzx.kaleido.domain.model.dto.code.param;

import lombok.Data;

import java.util.List;

/**
 * @author lwp
 * @date 2024-06-15
 **/
@Data
public class CodeGenerationGlobalConfigParam {
    
    /**
     * 作者
     */
    private String author;
    
    /**
     * 代码保存路径
     */
    private String codePath;
    
    /**
     * 代码license
     */
    private String license;
    
    /**
     * 适用的模板
     */
    private List<ApplyTemplateParam> applyTemplateList;
    
}
