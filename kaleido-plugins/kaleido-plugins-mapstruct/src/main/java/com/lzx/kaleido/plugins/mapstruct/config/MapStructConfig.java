package com.lzx.kaleido.plugins.mapstruct.config;

import com.lzx.kaleido.plugins.mapstruct.AutoMultiObjectMapper;
import io.github.zhaord.mapstruct.plus.AutoMapperFactory;
import io.github.zhaord.mapstruct.plus.IObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

/**
 * @author lwp
 * @date 2023-11-19
 **/
@Configuration(proxyBeanMethods = false)
@Import(AutoMapperFactory.class)
public class MapStructConfig {
    
    @Bean
    @Primary
    public IObjectMapper autoFullObjectMapper(AutoMapperFactory autoMapperFactory) {
        return new AutoMultiObjectMapper(autoMapperFactory);
    }
}
