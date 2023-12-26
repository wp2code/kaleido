package com.lzx.kaleido.domain.model.dto.param.code;

import lombok.Data;

import java.util.Optional;

/**
 * @author lwp
 * @date 2023-12-18
 **/
@Data
public class CodeGenerationParam {
    
    /**
     * 模板ID
     */
    private Long templateConfigId;
    
    /**
     * 模板内容
     */
    private String templateContent;
    
    /**
     * 模板名称
     */
    private String templateName;
    
    /**
     * 模板引擎名称
     */
    private String templateEngineName;
    
    /**
     * 输出到的文件路径
     */
    private String outDirPath;
    
    /**
     * 输出到的文件名称（例如：Demo.java)
     */
    private String outFileName;
    
    /**
     * 模板变量参数（json）
     */
    private String templateParams;
    
    /**
     * @param defaultEngineName
     * @return
     */
    public String getEngineNameIfAbsent(String defaultEngineName) {
        return Optional.ofNullable(templateEngineName).orElse(defaultEngineName);
    }

}
