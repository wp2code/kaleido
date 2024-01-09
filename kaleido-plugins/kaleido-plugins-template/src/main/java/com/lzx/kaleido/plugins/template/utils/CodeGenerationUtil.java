package com.lzx.kaleido.plugins.template.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.TemplateException;
import com.lzx.kaleido.plugins.template.code.ICodeTemplateProcessor;
import com.lzx.kaleido.plugins.template.enums.ResourceMode;
import com.lzx.kaleido.plugins.template.model.TemplateConfigInfo;
import com.lzx.kaleido.plugins.template.model.TemplateContext;
import lombok.experimental.UtilityClass;

import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;

/**
 * @author lwp
 * @date 2023-12-17
 **/
@UtilityClass
public class CodeGenerationUtil {
    
    private static final String TEMPLATE_PATH = "/templates/";
    
    private static ICodeTemplateProcessor codeTemplateProcessor;
    
    /**
     * @param codeTemplateProcessor
     */
    public void setCodeTemplateProcessor(ICodeTemplateProcessor codeTemplateProcessor) {
        CodeGenerationUtil.codeTemplateProcessor = Optional.ofNullable(codeTemplateProcessor)
                .orElseThrow(() -> new TemplateException("代码模板器不能为空"));
    }
    
    /**
     * @param templateContext
     * @return
     */
    public String process(final TemplateContext templateContext) {
        return codeTemplateProcessor.process(templateContext);
    }
    
    /**
     * @param templateContext
     * @return
     */
    public String processToStr(final TemplateContext templateContext) {
        return codeTemplateProcessor.processToStr(templateContext);
    }
    
    /**
     * @param templateContext
     * @param outputStream
     */
    public void process(final TemplateContext templateContext, final OutputStream outputStream) {
        codeTemplateProcessor.process(templateContext, outputStream);
    }
    
    
    /**
     * 动态模板解析处理
     *
     * @param templateContent
     * @param outDirPath
     * @param outFileName
     * @param templateParams
     * @param engineName
     * @param templateName
     * @param templatePath
     * @param resourceMode
     * @return
     */
    public String processTemplate(final String templateContent, final String outDirPath, final String outFileName,
            final Map<?, ?> templateParams, final String engineName, final String templateName, final String templatePath,
            final ResourceMode resourceMode) {
        final TemplateContext templateContext = TemplateContext.builder().templateName(templateName).templateParams(templateParams)
                .outDirPath(outDirPath).engineName(engineName).outFileName(outFileName).templateConfig(
                        TemplateConfigInfo.builder().templateContent(templateContent).resourceMode(resourceMode)
                                .templatePath(StrUtil.isNotBlank(templatePath) ? templatePath : TEMPLATE_PATH).build()).build();
        return process(templateContext);
    }
    
    /**
     * 动态模板解析处理
     *
     * @param templateContent
     * @param templateParams
     * @param engineName
     * @param templateName
     * @param templatePath
     * @param resourceMode
     * @return
     */
    public String processTemplateToStr(final String templateContent, final Map<?, ?> templateParams, final String engineName,
            final String templateName, final String templatePath, final ResourceMode resourceMode) {
        final TemplateContext templateContext = TemplateContext.builder().templateName(templateName).templateParams(templateParams)
                .engineName(engineName).templateConfig(
                        TemplateConfigInfo.builder().templateContent(templateContent).resourceMode(resourceMode)
                                .templatePath(StrUtil.isNotBlank(templatePath) ? templatePath : TEMPLATE_PATH).build()).build();
        return processToStr(templateContext);
    }
    
    /**
     * 动态模板解析处理-响应文件流
     *
     * @param templateContent
     * @param outputStream
     * @param templateParams
     * @param engineName
     * @param templateName
     */
    public void processTemplateOutStream(final String templateContent, final OutputStream outputStream, final Map<?, ?> templateParams,
            final String engineName, final String templateName) {
        final TemplateContext templateContext = TemplateContext.builder().templateName(templateName).templateParams(templateParams)
                .engineName(engineName).templateConfig(
                        TemplateConfigInfo.builder().templateContent(templateContent).resourceMode(ResourceMode.STRING).templatePath(null)
                                .build()).build();
        process(templateContext, outputStream);
    }
}
