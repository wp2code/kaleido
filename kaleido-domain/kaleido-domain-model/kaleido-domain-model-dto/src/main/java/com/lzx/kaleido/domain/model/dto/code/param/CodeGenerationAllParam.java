package com.lzx.kaleido.domain.model.dto.code.param;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lwp
 * @date 2024-01-18
 **/
@Data
public class CodeGenerationAllParam implements Serializable {
    
    /**
     * 所属模板ID
     */
    @NotNull(message = "代码模板ID不能为空")
    private Long templateId;
    
    /**
     * 连接ID
     */
    private String connectionId;
    
    /**
     *
     */
    @Valid
    @NotNull(message = "代码生成参数配置不能为null")
    @Size(min = 1, message = "代码生成参数配置不能为空")
    private List<CodeGenerationTableParam> codeGenerationList;
    
    /**
     * 响应的模板代码
     */
    private List<String> responseTemplateCodeList;
}
