package com.lzx.kaleido.plugins.template.engine.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.SimpleCache;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.ClassUtil;
import com.lzx.kaleido.plugins.template.engine.ITemplateEngine;
import com.lzx.kaleido.plugins.template.exception.TemplateParseException;
import com.lzx.kaleido.plugins.template.model.TemplateConfigInfo;
import com.lzx.kaleido.plugins.template.template.ITemplate;
import com.lzx.kaleido.plugins.template.template.impl.FreemarkerTemplate;
import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author lwp
 * @date 2023-12-14
 **/
public class FreemarkerTemplateEngineImpl implements ITemplateEngine {
    
    public final static String ENGINE_NAME = "Freemarker";
    
    private final static List<String> SUFFIX = Stream.of(".ftlx", ".ftl").toList();
    
    private final static SimpleCache<String, ITemplate> TEMPLATE_CACHE = new SimpleCache<>();
    
    /**
     * @return
     */
    @Override
    public String getEngineName() {
        return ENGINE_NAME;
    }
    
    /**
     * @param templateName
     * @param templateConfig
     * @return
     */
    @Override
    public ITemplate getTemplate(final String templateName, final TemplateConfigInfo templateConfig) {
        try {
            final String fullTemplateName = this.getFullTemplateName(templateName);
            if (templateConfig.isUseCache()) {
                final ITemplate template = TEMPLATE_CACHE.get(fullTemplateName);
                if (template != null) {
                    return template;
                }
            }
            final Configuration configuration = this.createConfiguration(fullTemplateName, templateConfig);
            final FreemarkerTemplate freemarkerTemplate = FreemarkerTemplate.wrap(configuration.getTemplate(fullTemplateName));
            TEMPLATE_CACHE.put(fullTemplateName, freemarkerTemplate);
            return freemarkerTemplate;
        } catch (Exception e) {
            throw new TemplateParseException("加载模板失败！", e);
        }
    }
    
    /**
     * 支持的模板后缀
     *
     * @return
     */
    @Override
    public List<String> supportTemplateSuffix() {
        return SUFFIX;
    }
    
    /**
     * @param templateName
     * @return
     */
    private String getFullTemplateName(String templateName) {
        if (SUFFIX.stream().anyMatch(templateName::endsWith)) {
            return templateName;
        }
        return "%s%s".formatted(templateName, SUFFIX.get(0));
    }
    
    /**
     * @param templateConfigInfo
     * @return
     */
    private Configuration createConfiguration(final String fullTemplateName, final TemplateConfigInfo templateConfigInfo) {
        final Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        configuration.setLocalizedLookup(false);
        configuration.setDefaultEncoding(templateConfigInfo.getEncoding());
        switch (templateConfigInfo.getResourceMode()) {
            case CLASSPATH:
                configuration.setTemplateLoader(new ClassTemplateLoader(ClassUtil.getClassLoader(), templateConfigInfo.getTemplatePath()));
                break;
            case FILE:
                try {
                    configuration.setTemplateLoader(new FileTemplateLoader(FileUtil.file(templateConfigInfo.getTemplatePath())));
                } catch (IOException e) {
                    throw new TemplateParseException("文件加载模板异常！", e);
                }
                break;
            case STRING:
                final StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
                stringTemplateLoader.putTemplate(fullTemplateName, templateConfigInfo.getTemplateContent());
                configuration.setTemplateLoader(stringTemplateLoader);
                break;
            case REMOTE:
                configuration.setTemplateLoader(new RemoteURLTemplateLoader(templateConfigInfo.getTemplatePath()));
                break;
            default:
                break;
        }
        return configuration;
    }
    
    /**
     *
     */
    static class RemoteURLTemplateLoader extends URLTemplateLoader {
        
        private final String url;
        
        public RemoteURLTemplateLoader(String url) {
            this.url = url;
        }
        
        @Override
        protected URL getURL(final String name) {
            return UrlBuilder.of(url).toURL();
        }
    }
}
