package com.lzx.kaleido.domain.model.vo.code;

import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.infra.base.annotations.validation.AddGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lwp
 * @date 2024-01-10
 **/
@Data
public class CodeGenerationTemplateViewVO implements Serializable {
    
    /**
     * 模板名称
     */
    @NotEmpty(groups = {AddGroup.class})
    private String templateName;
    
    /**
     * 模板语言
     */
    @NotEmpty(groups = {AddGroup.class})
    private String language;
    
    /**
     * 模板基本配置信息
     */
    @Valid
    @NotNull(groups = {AddGroup.class})
    private BasicConfigVO basicConfig;
    
    /**
     * 模板代码配置信息
     */
    @Size(min = 1)
    @NotNull(groups = {AddGroup.class})
    private List<CodeGenerationTemplateViewConfigVO> codeConfigList;
}
