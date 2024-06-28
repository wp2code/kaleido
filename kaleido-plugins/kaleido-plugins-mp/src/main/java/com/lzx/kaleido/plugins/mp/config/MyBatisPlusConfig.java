package com.lzx.kaleido.plugins.mp.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.lzx.kaleido.infra.base.constant.BasePackageConstants;
import com.lzx.kaleido.plugins.mp.CustomIdentifierGenerator;
import com.lzx.kaleido.plugins.mp.DataMetaObjectHandler;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @author lwp
 * @date 2023-11-25
 **/
@Configuration(proxyBeanMethods = false)
@EnableTransactionManagement
public class MyBatisPlusConfig {
    
    
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
        scannerConfigurer.setBasePackage(BasePackageConstants.MP_BASE_PACKAGE);
        return scannerConfigurer;
    }
    
    @Bean
    public CustomMybatisPlusPropertiesCustomizer globalConfig() {
        return new CustomMybatisPlusPropertiesCustomizer();
    }
    
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        final PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        //乐观锁拦截器
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return mybatisPlusInterceptor;
    }
    
    /**
     * Map下划线自动转驼峰
     *
     * @return
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return i -> i.setObjectWrapperFactory(new MybatisMapWrapperFactory());
    }
    
    static class CustomMybatisPlusPropertiesCustomizer implements MybatisPlusPropertiesCustomizer {
        
        @Override
        public void customize(final MybatisPlusProperties properties) {
            final GlobalConfig globalConfig = properties.getGlobalConfig();
            globalConfig.setBanner(false);
            globalConfig.setSqlInjector(new DefaultSqlInjector());
            globalConfig.setMetaObjectHandler(new DataMetaObjectHandler());
//            final GlobalConfig.DbConfig dbConfig = globalConfig.getDbConfig();
//            dbConfig.setLogicDeleteField(DataMetaObjectHandler.DELETED);
//            globalConfig.setIdentifierGenerator(new CustomIdentifierGenerator());
//            dbConfig.setLogicDeleteValue("1");
//            dbConfig.setLogicNotDeleteValue("0");
        }
    }
}
