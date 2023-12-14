package com.lzx.kaleido.start.config;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.sqlite.JDBC;

import java.io.File;

/**
 * @author lwp
 * @date 2023-11-16
 **/
@Slf4j
@Configuration
public class WebAutoConfig {
    
    /**
     * @return
     */
    @Bean
    @Primary
    public DataSourceProperties dataSourceProperties() {
        return new DataSourcePropertiesCustomizer();
    }
    
    static class DataSourcePropertiesCustomizer extends DataSourceProperties {
        
        private final static String SUFFIX = "_kaleido.db";
        
        @Override
        public void afterPropertiesSet() throws Exception {
            final String url = this.getUrl();
            if (JDBC.isValidURL(url)) {
                String name = url.substring(JDBC.PREFIX.length());
                final String urlNormalized = urlNormalized(name);
                this.marParentDir(urlNormalized);
                final String urlNormalizedJdbc = JDBC.PREFIX + urlNormalized;
                this.setUrl(urlNormalizedJdbc);
            }
            super.afterPropertiesSet();
        }
        
        /**
         * @param url
         * @return
         */
        public String urlNormalized(String url) {
            final String suffix = url.endsWith(".db") ? "" : SUFFIX;
            if (!url.startsWith("~") && (!FileUtil.isAbsolutePath(url) || url.startsWith("./"))) {
                return url.startsWith("./") ? url.substring(2) + suffix : url + suffix;
            }
            return FileUtil.getAbsolutePath(url + suffix);
        }
    
        /**
         * @param fileName
         */
        private void marParentDir(String fileName) {
            File file = new File(fileName).getAbsoluteFile();
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                FileUtil.mkdir(parent);
            }
        }
    }
}
