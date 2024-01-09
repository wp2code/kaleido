package com.lzx.kaleido.plugins.template.code;

import com.lzx.kaleido.plugins.template.model.TemplateContext;

import java.io.OutputStream;

/**
 * @author lwp
 * @date 2023-12-14
 **/
public interface ICodeTemplateProcessor {
    
    /**
     * 代码模板处理
     *
     * @param templateContext
     * @return 代码路径
     */
    String process(final TemplateContext templateContext);
    
    /**
     * 代码模板处理
     *
     * @param templateContext
     * @return 代码字符串
     */
    String processToStr(final TemplateContext templateContext);
    
    /**
     * 代码模板处理
     *
     * @param templateContext
     * @param outputStream
     */
    void process(final TemplateContext templateContext, final OutputStream outputStream);
}
