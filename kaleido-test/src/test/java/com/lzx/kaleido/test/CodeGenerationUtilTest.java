package com.lzx.kaleido.test;

import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import com.lzx.kaleido.plugins.template.code.impl.DefaultCodeTemplateProcessor;
import com.lzx.kaleido.plugins.template.engine.impl.FreemarkerTemplateEngineImpl;
import com.lzx.kaleido.plugins.template.utils.CodeGenerationUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lwp
 * @date 2023-12-09
 **/
@Slf4j
class CodeGenerationUtilTest {
    
    @BeforeEach
    void init() {
        final FreemarkerTemplateEngineImpl freemarkerTemplateEngine = new FreemarkerTemplateEngineImpl();
        final DefaultCodeTemplateProcessor defaultCodeTemplateProcessor = new DefaultCodeTemplateProcessor(
                Collections.singletonList(freemarkerTemplateEngine));
        CodeGenerationUtil.setCodeTemplateProcessor(defaultCodeTemplateProcessor);
    }
    
    @Test
    void processDynamicTemplateTest() {
        final String templateContent = "public class ${voName} extends ${baseVo}{\n" + "}";
        final String outDirPath = "D:\\opt";
        final String outFileName = "DemoVO.java";
        final Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("voName", "DemoVO");
        templateParams.put("baseVo", "BaseVO");
        log.info("templateParams is {}", JsonUtil.toJson(templateParams));
        String engineName = FreemarkerTemplateEngineImpl.ENGINE_NAME;
        final String outFullPath = CodeGenerationUtil.processTemplate(templateContent, outDirPath, outFileName, templateParams, engineName,
                null);
        log.info("输出地址：{}", outFullPath);
        Assertions.assertNotNull(outFullPath);
    }
    @Test
    void mockBasicConfig(){
        final BasicConfigVO basicConfigVO = new BasicConfigVO();
        basicConfigVO.setCodePath("C:\\user");
        basicConfigVO.setAuthor("刘伟平");
        basicConfigVO.setLicense("C:\\user");
        log.info(JsonUtil.toJson(basicConfigVO));
    }
}
