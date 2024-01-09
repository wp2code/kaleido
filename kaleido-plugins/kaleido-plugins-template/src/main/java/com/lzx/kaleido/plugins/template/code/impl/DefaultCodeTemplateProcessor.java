package com.lzx.kaleido.plugins.template.code.impl;

import com.lzx.kaleido.plugins.template.code.ICodeTemplateProcessor;
import com.lzx.kaleido.plugins.template.engine.ITemplateEngine;
import com.lzx.kaleido.plugins.template.exception.TemplateParseException;
import com.lzx.kaleido.plugins.template.model.TemplateContext;
import com.lzx.kaleido.plugins.template.template.ITemplate;
import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;
import java.util.List;

/**
 * @author lwp
 * @date 2023-12-14
 **/
@Slf4j
public record DefaultCodeTemplateProcessor(List<ITemplateEngine> templateEngines) implements ICodeTemplateProcessor {
    
    /**
     * 代码模板处理
     *
     * @param templateContext
     */
    @Override
    public String process(final TemplateContext templateContext) {
        try {
            this.getTemplate(templateContext).render(templateContext.getTemplateParams(), templateContext.getFileFullPath());
            return templateContext.getFileFullPath();
        } catch (Exception e) {
            log.error("模板处理失败！", e);
            throw new TemplateParseException("代码模板处理异常", e);
        }
    }
    
    /**
     * 代码模板处理
     *
     * @param templateContext
     * @return 代码字符串
     */
    @Override
    public String processToStr(final TemplateContext templateContext) {
        try {
            return this.getTemplate(templateContext).render(templateContext.getTemplateParams());
        } catch (Exception e) {
            log.error("模板处理失败！", e);
            throw new TemplateParseException("代码模板处理异常", e);
        }
    }
    
    /**
     * @param templateContext
     * @param outputStream
     */
    @Override
    public void process(final TemplateContext templateContext, final OutputStream outputStream) {
        try {
            this.getTemplate(templateContext).render(templateContext.getTemplateParams(), outputStream);
        } catch (Exception e) {
            log.error("模板处理失败！", e);
            throw new TemplateParseException("代码模板处理异常-outputStream", e);
        }
    }
    
    /**
     * @param templateContext
     * @return
     */
    private ITemplate getTemplate(final TemplateContext templateContext) {
        final ITemplateEngine templateEngine = this.getTemplateEngine(templateContext.getEngineName());
        return templateEngine.getTemplate(templateContext.getTemplateName(), templateContext.getTemplateConfig());
    }
    
    /**
     * @param engineName
     * @return
     */
    private ITemplateEngine getTemplateEngine(String engineName) {
        return templateEngines.stream().filter(v -> v.getEngineName().equals(engineName)).findFirst()
                .orElseThrow(() -> new TemplateParseException("模板引擎加载失败", null));
    }
}
