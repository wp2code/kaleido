package com.lzx.kaleido.start.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
public class JacksonCustomConfig {
    
    /**
     *
     */
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
            jacksonObjectMapperBuilder.modules(new Jdk8Module(), simpleModule, javaTimeModule);
        }
        
        @Override
        public int getOrder() {
            // 在 StandardJackson2ObjectMapperBuilderCustomizer 后加载
            return 1;
        }
    }
}
