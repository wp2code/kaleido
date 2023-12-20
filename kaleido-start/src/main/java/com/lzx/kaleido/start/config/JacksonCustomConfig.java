package com.lzx.kaleido.start.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * jackson 配置
 *
 * @author lwp
 * @date 2023-05-16
 * @see org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
 **/
@Configuration(proxyBeanMethods = false)
@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
@RequiredArgsConstructor
public class JacksonCustomConfig implements WebMvcConfigurer, InitializingBean {
    
    private final ObjectMapper objectMapper;
    
    @Override
    public void afterPropertiesSet() {
        if (objectMapper != null) {
            JsonUtil.setObjectMapper(objectMapper);
        }
    }
    
    
    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
        final MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        converters.add(jackson2HttpMessageConverter);
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }
    
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(Jackson2ObjectMapperBuilder.class)
    static class CustomJackson2ObjectMapperBuilderCustomizer implements Jackson2ObjectMapperBuilderCustomizer, Ordered {
        
        @Override
        public void customize(final Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
            jacksonObjectMapperBuilder.locale(Locale.CHINA);
            jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            jacksonObjectMapperBuilder.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
            final JavaTimeModule javaTimeModule = new JavaTimeModule();
            LocalDateTimeDeserializer dateTimeDeserializer = new LocalDateTimeDeserializer(DatePattern.NORM_DATETIME_FORMATTER);
            LocalDateTimeSerializer dateTimeSerializer = new LocalDateTimeSerializer(DatePattern.NORM_DATETIME_FORMATTER);
            javaTimeModule.addDeserializer(LocalDateTime.class, dateTimeDeserializer);
            javaTimeModule.addSerializer(LocalDateTime.class, dateTimeSerializer);
            SimpleModule simpleModule = new SimpleModule();
            //BigInteger、Long序列化字符串
            simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
            simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.modules(simpleModule, javaTimeModule);
        }
        
        @Override
        public int getOrder() {
            // 在 StandardJackson2ObjectMapperBuilderCustomizer 后加载
            return 1;
        }
    }
}
