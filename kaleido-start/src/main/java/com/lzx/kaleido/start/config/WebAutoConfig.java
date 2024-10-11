package com.lzx.kaleido.start.config;

import cn.hutool.core.io.FileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import com.lzx.kaleido.infra.base.utils.ServiceLoaderUtil;
import com.lzx.kaleido.spi.db.IDBPlugin;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.sqlite.JDBC;

/**
 * @author lwp
 * @date 2023-11-16
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebAutoConfig implements WebMvcConfigurer, InitializingBean {
    
    private final ObjectMapper objectMapper;
    
    @Override
    public void afterPropertiesSet() {
        if (objectMapper != null) {
            JsonUtil.setObjectMapper(objectMapper);
        }
        ServiceLoaderUtil.register(IDBPlugin.class);
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOriginPatterns("*").allowedMethods("GET", "POST", "PUT", "DELETE").allowedHeaders("*")
                .allowCredentials(true).allowCredentials(true).maxAge(168000);
    }
    
    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
        final MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        converters.add(jackson2HttpMessageConverter);
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }
    
    /**
     * @return
     */
    @Bean
    @Primary
    public DataSourceProperties dataSourceProperties(Environment environment) {
        return new DataSourcePropertiesCustomizer(environment);
    }
    
    @RequiredArgsConstructor
    static class DataSourcePropertiesCustomizer extends DataSourceProperties {
        
        private final Environment environment;
        
        private final static String SUFFIX = "_kaleido.db";
        
        @Override
        public void afterPropertiesSet() throws Exception {
            final String activeProfiles = environment.getProperty("spring.profiles.active");
            final String url = this.getUrl();
            if (JDBC.isValidURL(url)) {
                final String jdbcUrl = url.substring(JDBC.PREFIX.length());
                final String urlNormalized = this.urlNormalized(jdbcUrl, activeProfiles);
                final File jdbcFile = this.createParentDirIfAbsent(urlNormalized);
                final String urlNormalizedJdbcUrl = JDBC.PREFIX + jdbcFile.getPath();
                log.info("jdbcUrl is  {}", urlNormalizedJdbcUrl);
                this.setUrl(urlNormalizedJdbcUrl);
            }
            super.afterPropertiesSet();
        }
        
        /**
         * @param url
         * @return
         */
        public String urlNormalized(String url, String activeProfiles) {
            final String suffix = url.endsWith(".db") ? "" : File.separator + activeProfiles + SUFFIX;
            //当前路径
            if (url.startsWith("./")) {
                return url.substring(2) + suffix;
            }
            //用户路径
            if (url.startsWith("~/")) {
                return FileUtil.getUserHomePath() + File.separator + url.substring(2) + suffix;
            }
            //绝对路径
            if (FileUtil.isAbsolutePath(url)) {
                return url + suffix;
            }
            return url + suffix;
        }
        
        /**
         * @param fileName
         */
        private File createParentDirIfAbsent(String fileName) {
            final File file = new File(fileName);
            final File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            return file;
        }
    }
}
