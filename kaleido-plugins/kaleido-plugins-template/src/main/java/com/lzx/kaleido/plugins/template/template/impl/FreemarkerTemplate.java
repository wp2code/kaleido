package com.lzx.kaleido.plugins.template.template.impl;

import com.lzx.kaleido.plugins.template.exception.TemplateParseException;
import com.lzx.kaleido.plugins.template.template.AbsTemplate;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

/**
 * @author lwp
 * @date 2023-12-13
 **/
@RequiredArgsConstructor
public class FreemarkerTemplate extends AbsTemplate {
    
    private final Template template;
    
    /**
     * @param template
     * @return
     */
    public static FreemarkerTemplate wrap(Template template) {
        return (null == template) ? null : new FreemarkerTemplate(template);
    }
    
    /**
     * 将模板与绑定参数融合后输出到Writer
     *
     * @param bindingMap 绑定的参数，此Map中的参数会替换模板中的变量
     * @param writer     输出
     */
    @Override
    public void render(final Map<?, ?> bindingMap, final Writer writer) {
        try {
            template.process(bindingMap, writer);
        } catch (TemplateException | IOException e) {
            throw new TemplateParseException("模板处理异常！", e);
        }
    }
    
    /**
     * 写出到输出流
     *
     * @param bindingMap 绑定的参数
     * @param out        输出流
     */
    @Override
    public void render(final Map<?, ?> bindingMap, final OutputStream out) {
        try (final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out, this.template.getEncoding());) {
            template.process(bindingMap, outputStreamWriter);
        } catch (TemplateException | IOException e) {
            throw new TemplateParseException("模板处理异常！", e);
        }
    }
    
}
