package com.lzx.kaleido.plugins.template.model;

import com.lzx.kaleido.plugins.template.enums.ResourceMode;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * @author lwp
 * @date 2023-12-14
 **/
@Getter
@SuperBuilder
public class TemplateConfigInfo {
    
    public final static TemplateConfigInfo DEFAULT = TemplateConfigInfo.builder().build();
    
    /**
     * 模板编码
     */
    @Builder.Default
    private String encoding = "utf-8";
    
    /**
     * 模板加载地址
     */
    private String templatePath;
    
    /**
     * 模板资源加载模式
     */
    @Builder.Default
    private ResourceMode resourceMode = ResourceMode.STRING;
    
    /**
     * 模板引擎名称
     */
    @Builder.Default
    private String engineName = "Freemarker";
    
    /**
     * 使用模板缓存
     */
    @Builder.Default
    private boolean useCache = true;
    
    
    /**
     * 模板内容(适用动态的字符串模板)
     * <p>
     * #ResourceMode.STRING
     */
    private String templateContent;
    
    
}
