package com.lzx.kaleido.plugins.template.engine;

import com.lzx.kaleido.plugins.template.model.TemplateConfigInfo;
import com.lzx.kaleido.plugins.template.template.ITemplate;

import java.util.List;

/**
 * @author lwp
 * @date 2023-12-14
 **/
public interface ITemplateEngine {
    
    /**
     * @return
     */
    String getEngineName();
    
    /**
     * @param templateName
     * @param templateConfig
     * @return
     */
    ITemplate getTemplate(final String templateName, final TemplateConfigInfo templateConfig);
    
    
    /**
     * 支持的模板后缀
     *
     * @return
     */
    default List<String> supportTemplateSuffix() {
        return null;
    }
}
