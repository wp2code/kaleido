package com.lzx.kaleido.domain.model.vo.code;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lwp
 * @date 2024-01-18
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeGenerationResultVO {
    
    /**
     * 模板基本信息
     */
    private CodeGenerationTemplateVO templateInfo;
    
    /**
     * 模板代码信息
     */
    private List<CodeGenerationViewVO> codeGenerationList;
    
}
