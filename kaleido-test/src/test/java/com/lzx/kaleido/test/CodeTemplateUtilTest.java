package com.lzx.kaleido.test;

import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaControllerConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaEntityConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaMapperConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaServiceConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaVoConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaXmlConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.common.SuperclassVO;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import com.lzx.kaleido.plugins.template.code.impl.DefaultCodeTemplateProcessor;
import com.lzx.kaleido.plugins.template.engine.impl.FreemarkerTemplateEngineImpl;
import com.lzx.kaleido.plugins.template.enums.ResourceMode;
import com.lzx.kaleido.plugins.template.utils.CodeGenerationUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lwp
 * @date 2023-12-09
 **/
@Slf4j
class CodeTemplateUtilTest {
    
    
    @Test
    void processDynamicTemplateTest() {
        initTemplate();
        final String templateContent = "public class ${voName} extends ${baseVo}{\n" + "}";
        final String outDirPath = "D:\\opt";
        final String outFileName = "DemoVO.java";
        final Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("voName", "DemoVO");
        templateParams.put("baseVo", "BaseVO");
        log.info("templateParams is {}", JsonUtil.toJson(templateParams));
        String engineName = FreemarkerTemplateEngineImpl.ENGINE_NAME;
        final String outFullPath = CodeGenerationUtil.processTemplate(templateContent, outDirPath, outFileName, templateParams, engineName,
                null, null, ResourceMode.STRING);
        log.info("输出地址：{}", outFullPath);
        Assertions.assertNotNull(outFullPath);
    }
    
    @Test
    void mockBasicConfig() {
        final BasicConfigVO basicConfigVO = new BasicConfigVO();
        basicConfigVO.setCodePath("D:\\opt\\gencode");
        basicConfigVO.setAuthor("刘伟平");
        basicConfigVO.setLicense("");
        log.info(JsonUtil.toJson(basicConfigVO));
    }
    
    @Test
    void mockJavaModelConfigOfVo() {
        final JavaVoConfigVO javaVoConfigVO = new JavaVoConfigVO();
        javaVoConfigVO.setSourceFolder("src/main/java");
        javaVoConfigVO.setPackageName("com.lzx.kaleido.domain.model.vo");
        javaVoConfigVO.setSuperclass(new SuperclassVO("DemoVO"));
        javaVoConfigVO.setCodePath("D:\\opt\\gencode");
        log.info(JsonUtil.toJson(javaVoConfigVO));
    }
    
    @Test
    void mockJavaModelConfigOfEntity() {
        final JavaEntityConfigVO javaEntityConfigVO = new JavaEntityConfigVO();
        javaEntityConfigVO.setSourceFolder("src/main/java");
        javaEntityConfigVO.setPackageName("com.lzx.kaleido.domain.model.entity");
        javaEntityConfigVO.setSuperclass(new SuperclassVO("DemoEntity"));
        javaEntityConfigVO.setCodePath("D:\\opt\\gencode");
        log.info(JsonUtil.toJson(javaEntityConfigVO));
    }
    
    @Test
    void mockJavaMapperConfigVO() {
        final JavaMapperConfigVO javaMapperConfigVO = new JavaMapperConfigVO();
        javaMapperConfigVO.setSourceFolder("src/main/java");
        javaMapperConfigVO.setPackageName("com.lzx.kaleido.domain.repository.mapper");
        javaMapperConfigVO.setSuperclass(new SuperclassVO("IBaseMapper", "DemoEntity"));
        javaMapperConfigVO.setUseMybatisPlus(true);
        if (!javaMapperConfigVO.isUseMybatisPlus()) {
            javaMapperConfigVO.setMethodList(Stream.of(
                            "insertSelective,insertOrUpdateSelective,updateByPrimaryKey,updateByPrimaryKeySelective,selectByEntity,selectByPrimaryKey,deleteByPrimaryKey")
                    .collect(Collectors.toList()));
        }
        javaMapperConfigVO.setCodePath("D:\\opt\\gencode");
        log.info(JsonUtil.toJson(javaMapperConfigVO));
    }
    
    @Test
    void mockJavaXmlConfigVO() {
        final JavaXmlConfigVO javaXmlConfigVO = new JavaXmlConfigVO();
        javaXmlConfigVO.setSourceFolder("src/main/java");
        javaXmlConfigVO.setPackageName("com.lzx.kaleido.domain.repository.mapper");
        javaXmlConfigVO.setNamespace("com.lzx.kaleido.domain.repository.mapper.DemoMapper");
        javaXmlConfigVO.setUseMybatisPlus(true);
        if (!javaXmlConfigVO.isUseMybatisPlus()) {
            javaXmlConfigVO.setSqlList(Stream.of(
                            "insertSelective,insertOrUpdateSelective,updateByPrimaryKey,updateByPrimaryKeySelective,selectByEntity,selectByPrimaryKey,deleteByPrimaryKey")
                    .collect(Collectors.toList()));
        }
        javaXmlConfigVO.setCodePath("D:\\opt\\gencode");
        log.info(JsonUtil.toJson(javaXmlConfigVO));
    }
    
    
    @Test
    void mockJavaServiceConfigVO() {
        final JavaServiceConfigVO javaServiceConfigVO = new JavaServiceConfigVO();
        javaServiceConfigVO.setSourceFolder("src/main/java");
        javaServiceConfigVO.setPackageName("com.lzx.kaleido.domain.core.impl");
        javaServiceConfigVO.setSuperclass(
                new SuperclassVO("BaseServiceImpl", "ICodeGenerationTemplateConfigMapper", "CodeGenerationTemplateConfigEntity"));
        javaServiceConfigVO.setCodePath("D:\\opt\\gencode");
        log.info(JsonUtil.toJson(javaServiceConfigVO));
    }
    
    @Test
    void mockJavaControllerConfigVO() {
        final JavaControllerConfigVO javaServiceConfigVO = new JavaControllerConfigVO();
        javaServiceConfigVO.setSourceFolder("src/main/java");
        javaServiceConfigVO.setPackageName("com.lzx.kaleido.web.api.controller");
        javaServiceConfigVO.setSuperclass(new SuperclassVO("BaseController"));
        javaServiceConfigVO.setCodePath("D:\\opt\\gencode");
        log.info(JsonUtil.toJson(javaServiceConfigVO));
    }
    
    private void initTemplate() {
        final FreemarkerTemplateEngineImpl freemarkerTemplateEngine = new FreemarkerTemplateEngineImpl();
        final DefaultCodeTemplateProcessor defaultCodeTemplateProcessor = new DefaultCodeTemplateProcessor(
                Collections.singletonList(freemarkerTemplateEngine));
        CodeGenerationUtil.setCodeTemplateProcessor(defaultCodeTemplateProcessor);
    }
}
