package com.lzx.kaleido.domain.model.dto.code.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author lwp
 * @date 2024-06-16
 **/
@Data
public class CodeGenerationTemplateUpdateParam {
    
    @NotNull(message = "模板ID为空")
    private Long templateId;
    
    @NotNull(message = "更新模板类型为空")
    private String name;
    private String nameSuffix;
    
    @NotNull(message = "包路径不能为空")
    private String sourceFolder;
    
    @NotNull(message = "包路名称不能为空")
    private String packageName;
    
    private String codePath;
    
    private String superclassName;
    
    private Boolean useLombok;
    
    private Boolean useMybatisPlus;
    
    private Boolean useSwagger;
    
    private String responseGenericClass;
    
    /**
     * 默认忽略生成的字段
     */
    private List<String> defaultIgFields;
}
