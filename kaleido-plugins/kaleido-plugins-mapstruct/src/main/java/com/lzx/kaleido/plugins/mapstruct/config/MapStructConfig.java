package com.lzx.kaleido.plugins.mapstruct.config;

import com.lzx.kaleido.plugins.mapstruct.AutoMultiObjectMapper;
import io.github.zhaord.mapstruct.plus.AutoMapperFactory;
import io.github.zhaord.mapstruct.plus.IObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

/**
 * @author lwp
 * @date 2023-11-19
 **/
@Configuration(proxyBeanMethods = false)
@ComponentScan(MapStructConfig.BASE_MAPPING)
@Import(AutoMapperFactory.class)
public class MapStructConfig {
    public static final String BASE_MAPPING = "com.devbox.server.domain.model.*";
    
    @Bean
    @Primary
    public IObjectMapper autoFullObjectMapper(AutoMapperFactory autoMapperFactory) {
        return new AutoMultiObjectMapper(autoMapperFactory);
    }
}
