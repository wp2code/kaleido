package com.lzx.kaleido.plugins.template.config;

import com.lzx.kaleido.plugins.template.code.ICodeTemplateProcessor;
import com.lzx.kaleido.plugins.template.code.impl.DefaultCodeTemplateProcessor;
import com.lzx.kaleido.plugins.template.engine.ITemplateEngine;
import com.lzx.kaleido.plugins.template.engine.impl.FreemarkerTemplateEngineImpl;
import com.lzx.kaleido.plugins.template.utils.CodeGenerationUtil;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author lwp
 * @date 2023-12-15
 **/
@Configuration(proxyBeanMethods = false)
public class TemplateConfig {
    
    
    /**
     * @param objectProvider
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public ICodeTemplateProcessor codeTemplateProcessor(ObjectProvider<List<ITemplateEngine>> objectProvider) {
        final DefaultCodeTemplateProcessor defaultCodeTemplateProcessor = new DefaultCodeTemplateProcessor(objectProvider.getIfAvailable());
        CodeGenerationUtil.setCodeTemplateProcessor(defaultCodeTemplateProcessor);
        return defaultCodeTemplateProcessor;
    }
    
    @Configuration(proxyBeanMethods = false)
    static class FreemarkerTemplateEngineConfig {
        
        /**
         * @param templateConfiguration
         * @return
         */
        @Bean
        @ConditionalOnMissingBean
        public ITemplateEngine freemarkerTemplateEngine() {
            return new FreemarkerTemplateEngineImpl();
        }
    }
    
}
