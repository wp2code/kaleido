package com.lzx.kaleido.test;

import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.domain.core.code.processor.impl.ControllerTemplateProcessorImpl;
import com.lzx.kaleido.domain.core.code.processor.impl.EntityTemplateProcessorImpl;
import com.lzx.kaleido.domain.core.code.processor.impl.MapperTemplateProcessorImpl;
import com.lzx.kaleido.domain.core.code.processor.impl.ServiceApiTemplateProcessorImpl;
import com.lzx.kaleido.domain.core.code.processor.impl.ServiceTemplateProcessorImpl;
import com.lzx.kaleido.domain.core.code.processor.impl.VoTemplateProcessorImpl;
import com.lzx.kaleido.domain.core.code.processor.impl.XmlTemplateProcessorImpl;
import com.lzx.kaleido.domain.core.enums.ApiTemplateEnum;
import com.lzx.kaleido.domain.core.enums.ControllerApiTemplateEnum;
import com.lzx.kaleido.domain.core.enums.TemplateParserEnum;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableFieldParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateViewConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateViewVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.JavaConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.SuperclassVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaControllerConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaEntityConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaMapperConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaServiceApiConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaServiceConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaVoConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaXmlConfigVO;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import com.lzx.kaleido.plugins.template.code.impl.DefaultCodeTemplateProcessor;
import com.lzx.kaleido.plugins.template.engine.impl.FreemarkerTemplateEngineImpl;
import com.lzx.kaleido.plugins.template.enums.ResourceMode;
import com.lzx.kaleido.plugins.template.utils.CodeGenerationUtil;
import com.lzx.kaleido.spi.db.enums.DataType;
import com.lzx.kaleido.spi.db.utils.JdbcUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lwp
 * @date 2024-01-15
 **/
@Slf4j
public class CodeGenerationUtilTest {
    
    private static final BasicConfigVO basicConfigVO = new BasicConfigVO();
    
    private static final Map<String, JavaConfigVO> configMap = new HashMap<>();
    
    @BeforeEach
    void initTemplate() {
        basicConfigVO.setAuthor("欧冶子");
        basicConfigVO.setLicense("/*\n" + " *    Copyright 2009-2023 the original author or authors.\n" + " *\n"
                + " *    Licensed under the Apache License, Version 2.0 (the \"License\");\n"
                + " *    you may not use this file except in compliance with the License.\n"
                + " *    You may obtain a copy of the License at\n" + " *\n" + " *       https://www.apache.org/licenses/LICENSE-2.0\n"
                + " *\n" + " *    Unless required by applicable law or agreed to in writing, software\n"
                + " *    distributed under the License is distributed on an \"AS IS\" BASIS,\n"
                + " *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"
                + " *    See the License for the specific language governing permissions and\n" + " *    limitations under the License.\n"
                + " */");
        final FreemarkerTemplateEngineImpl freemarkerTemplateEngine = new FreemarkerTemplateEngineImpl();
        final DefaultCodeTemplateProcessor defaultCodeTemplateProcessor = new DefaultCodeTemplateProcessor(
                Collections.singletonList(freemarkerTemplateEngine));
        CodeGenerationUtil.setCodeTemplateProcessor(defaultCodeTemplateProcessor);
    }
    
    @AfterAll
    public static void genTemplateConfig() {
        CodeGenerationTemplateViewVO vo = new CodeGenerationTemplateViewVO();
        vo.setBasicConfig(basicConfigVO);
        final List<CodeGenerationTemplateViewConfigVO> codeConfigList = configMap.entrySet().stream().map(v -> {
            final CodeGenerationTemplateViewConfigVO configVO = new CodeGenerationTemplateViewConfigVO();
            configVO.setName(v.getKey());
            configVO.setConfig(JsonUtil.toJson(v.getValue()));
            return configVO;
        }).toList();
        vo.setCodeConfigList(codeConfigList);
        vo.setTemplateName("测试模板");
        vo.setLanguage("java");
        System.out.println("-------------------------------");
        System.out.println(JsonUtil.toJson(vo));
        System.out.println("-------------------------------");
    }
    
    @Test
    void VoTemplateParserImplTest() {
        boolean generateCodeFile = true;
        final CodeGenerationTemplateConfigVO configVO = new CodeGenerationTemplateConfigVO();
        final JavaVoConfigVO javaVoConfigVO = new JavaVoConfigVO();
        javaVoConfigVO.setPackageName("com.lzx.kaleido.test");
        javaVoConfigVO.setSourceFolder("src/main/java");
        javaVoConfigVO.setCodePath("D:\\opt\\gencode");
        javaVoConfigVO.setUseSwagger(true);
        javaVoConfigVO.setUseLombok(true);
        javaVoConfigVO.setSuperclass(new SuperclassVO("com.lzx.kaleido.test.pojo.BaseVO"));
        final String json = JsonUtil.toJson(javaVoConfigVO);
        configMap.put(javaVoConfigVO.getName(), javaVoConfigVO);
        configVO.setTemplateParams(json);
        final CodeGenerationTableParam codeGenerationTableParam = new CodeGenerationTableParam();
        codeGenerationTableParam.setTemplateName("tp_vo.ftlx");
        codeGenerationTableParam.setTableName("test_demo");
        codeGenerationTableParam.setComment("设备信息表");
        codeGenerationTableParam.setTableFieldColumnList(mockCodeGenerationTableFieldParams());
        codeGenerationTableParam.setUseSwagger(true);
        codeGenerationTableParam.setUseLombok(true);
        final CodeGenerationViewVO result = new VoTemplateProcessorImpl().generation(configVO, basicConfigVO, generateCodeFile,
                codeGenerationTableParam, ResourceMode.CLASSPATH, null);
        final String templateCode = result.getTemplateCode();
        System.out.println(templateCode);
        log.info("VoTemplateParserImplTest is {}", JsonUtil.toJson(result));
        Assertions.assertTrue(generateCodeFile ? StrUtil.isNotBlank(result.getCodePath()) : StrUtil.isNotBlank(templateCode));
    }
    
    @Test
    void EntityTemplateParserImplTest() {
        boolean generateCodeFile = true;
        final CodeGenerationTemplateConfigVO configVO = new CodeGenerationTemplateConfigVO();
        final JavaEntityConfigVO javaEntityConfigVO = new JavaEntityConfigVO();
        javaEntityConfigVO.setPackageName("com.lzx.kaleido.test");
        javaEntityConfigVO.setSourceFolder("src/main/java");
        javaEntityConfigVO.setCodePath("D:\\opt\\gencode");
        javaEntityConfigVO.setUseSwagger(true);
        javaEntityConfigVO.setUseLombok(true);
        javaEntityConfigVO.setUseMybatisPlus(true);
        javaEntityConfigVO.setSuperclass(new SuperclassVO("com.lzx.kaleido.test.pojo.BaseVO"));
        final String json = JsonUtil.toJson(javaEntityConfigVO);
        configMap.put(javaEntityConfigVO.getName(), javaEntityConfigVO);
        configVO.setTemplateParams(json);
        final CodeGenerationTableParam codeGenerationTableParam = new CodeGenerationTableParam();
        codeGenerationTableParam.setTemplateName("tp_entity.ftlx");
        codeGenerationTableParam.setTableName("test_demo");
        codeGenerationTableParam.setComment("设备信息表");
        codeGenerationTableParam.setTableFieldColumnList(mockCodeGenerationTableFieldParams());
        codeGenerationTableParam.setUseSwagger(false);
        codeGenerationTableParam.setUseLombok(true);
        final CodeGenerationViewVO result = new EntityTemplateProcessorImpl().generation(configVO, basicConfigVO, generateCodeFile,
                codeGenerationTableParam, ResourceMode.CLASSPATH, null);
        final String templateCode = result.getTemplateCode();
        System.out.println(templateCode);
        log.info("EntityTemplateParserImplTest is {}", JsonUtil.toJson(result));
        Assertions.assertTrue(generateCodeFile ? StrUtil.isNotBlank(result.getCodePath()) : StrUtil.isNotBlank(templateCode));
    }
    
    @Test
    void MapperTemplateParserImplTest() {
        boolean generateCodeFile = true;
        final CodeGenerationTemplateConfigVO configVO = new CodeGenerationTemplateConfigVO();
        final JavaMapperConfigVO javaEntityConfigVO = new JavaMapperConfigVO();
        javaEntityConfigVO.setPackageName("com.lzx.kaleido.test");
        javaEntityConfigVO.setSourceFolder("src/main/java");
        javaEntityConfigVO.setCodePath("D:\\opt\\gencode");
        javaEntityConfigVO.setUseMybatisPlus(true);
        javaEntityConfigVO.setSuperclass(new SuperclassVO("com.lzx.kaleido.test.IBaseMapper"));
        final String json = JsonUtil.toJson(javaEntityConfigVO);
        configMap.put(javaEntityConfigVO.getName(), javaEntityConfigVO);
        configVO.setTemplateParams(json);
        final CodeGenerationTableParam codeGenerationTableParam = new CodeGenerationTableParam();
        codeGenerationTableParam.setTemplateName("tp_mapper.ftlx");
        codeGenerationTableParam.setTableName("test_demo");
        codeGenerationTableParam.setComment("设备信息表");
        codeGenerationTableParam.setWebMethodList(Arrays.stream(ApiTemplateEnum.values()).map(ApiTemplateEnum::getApiId).toList());
        codeGenerationTableParam.setTableFieldColumnList(mockCodeGenerationTableFieldParams());
        final CodeGenerationViewVO testDemoEntity = CodeGenerationViewVO.builder().packageName("com.lzx.kaleido.test")
                .name("TestDemoEntity").codeType(TemplateParserEnum.ENTITY.getCodeType()).build();
        final CodeGenerationViewVO result = new MapperTemplateProcessorImpl().generation(configVO, basicConfigVO, generateCodeFile,
                codeGenerationTableParam, ResourceMode.CLASSPATH, Stream.of(testDemoEntity).collect(Collectors.toList()));
        final String templateCode = result.getTemplateCode();
        System.out.println(templateCode);
        log.info("MapperTemplateParserImplTest is {}", JsonUtil.toJson(result));
        Assertions.assertTrue(generateCodeFile ? StrUtil.isNotBlank(result.getCodePath()) : StrUtil.isNotBlank(templateCode));
    }
    
    @Test
    void XmlTemplateParserImplTest() {
        boolean generateCodeFile = true;
        final CodeGenerationTemplateConfigVO configVO = new CodeGenerationTemplateConfigVO();
        final JavaXmlConfigVO javaXmlConfigVO = new JavaXmlConfigVO();
        javaXmlConfigVO.setPackageName("mapping");
        javaXmlConfigVO.setSourceFolder("src/main/resources");
        javaXmlConfigVO.setCodePath("D:\\opt\\gencode");
        javaXmlConfigVO.setUseMybatisPlus(true);
        javaXmlConfigVO.setMethodList(Arrays.stream(ApiTemplateEnum.values()).map(ApiTemplateEnum::getApiId).toList());
        final String json = JsonUtil.toJson(javaXmlConfigVO);
        configMap.put(javaXmlConfigVO.getName(), javaXmlConfigVO);
        configVO.setTemplateParams(json);
        final CodeGenerationTableParam codeGenerationTableParam = new CodeGenerationTableParam();
        codeGenerationTableParam.setTableFieldColumnList(mockCodeGenerationTableFieldParams());
        codeGenerationTableParam.setTemplateName("tp_xml.ftlx");
        codeGenerationTableParam.setTableName("test_demo");
        codeGenerationTableParam.setComment("设备信息表");
        codeGenerationTableParam.setMethodList(new ArrayList<>());
        final CodeGenerationViewVO testDemoEntity = CodeGenerationViewVO.builder().packageName("com.lzx.kaleido.test")
                .name("TestDemoEntity").codeType(TemplateParserEnum.ENTITY.getCodeType()).build();
        final CodeGenerationViewVO testDemoMapper = CodeGenerationViewVO.builder().packageName("com.lzx.kaleido.test")
                .name("ITestDemoMapper").codeType(TemplateParserEnum.MAPPER.getCodeType()).build();
        final CodeGenerationViewVO result = new XmlTemplateProcessorImpl().generation(configVO, basicConfigVO, generateCodeFile,
                codeGenerationTableParam, ResourceMode.CLASSPATH, Stream.of(testDemoEntity, testDemoMapper).collect(Collectors.toList()));
        final String templateCode = result.getTemplateCode();
        System.out.println(templateCode);
        log.info("XmlTemplateParserImplTest is {}", JsonUtil.toJson(result));
        Assertions.assertTrue(generateCodeFile ? StrUtil.isNotBlank(result.getCodePath()) : StrUtil.isNotBlank(templateCode));
    }
    
    @Test
    void ControllerTemplateParserImplTest() {
        boolean generateCodeFile = true;
        final CodeGenerationTemplateConfigVO configVO = new CodeGenerationTemplateConfigVO();
        final JavaControllerConfigVO javaControllerConfigVO = new JavaControllerConfigVO();
        javaControllerConfigVO.setPackageName("com.lzx.kaleido.test");
        javaControllerConfigVO.setSourceFolder("src/main/java");
        javaControllerConfigVO.setCodePath("D:\\opt\\gencode");
        javaControllerConfigVO.setSuperclass(new SuperclassVO("com.lzx.kaleido.test.BaseController"));
        javaControllerConfigVO.setUseSwagger(true);
        javaControllerConfigVO.setUseMybatisPlus(true);
        javaControllerConfigVO.setResponseGenericClass("com.lzx.kaleido.test.pojo.R");
        javaControllerConfigVO.setMethodList(
                Arrays.stream(ControllerApiTemplateEnum.values()).map(ControllerApiTemplateEnum::getApiId).toList());
        final String json = JsonUtil.toJson(javaControllerConfigVO);
        configMap.put(javaControllerConfigVO.getName(), javaControllerConfigVO);
        configVO.setTemplateParams(json);
        final CodeGenerationTableParam codeGenerationTableParam = new CodeGenerationTableParam();
        codeGenerationTableParam.setTableFieldColumnList(mockCodeGenerationTableFieldParams());
        codeGenerationTableParam.setTemplateName("tp_controller1.ftlx");
        codeGenerationTableParam.setTemplatePath("D:\\opt");
        codeGenerationTableParam.setTableName("test_demo");
        codeGenerationTableParam.setComment("设备信息表");
        final CodeGenerationViewVO testDemoService = CodeGenerationViewVO.builder().packageName("com.lzx.kaleido.test")
                .name("ITestDemoService").codeType(TemplateParserEnum.SERVICE_API.getCodeType()).build();
        final CodeGenerationViewVO testDemoMapper = CodeGenerationViewVO.builder().packageName("com.lzx.kaleido.test").name("TestDemoVO")
                .codeType(TemplateParserEnum.VO.getCodeType()).build();
        final CodeGenerationViewVO result = new ControllerTemplateProcessorImpl().generation(configVO, basicConfigVO, generateCodeFile,
                codeGenerationTableParam, ResourceMode.COMPOSITE, Stream.of(testDemoService, testDemoMapper).collect(Collectors.toList()));
        final String templateCode = result.getTemplateCode();
        System.out.println(templateCode);
        log.info("ControllerTemplateParserImplTest is {}", JsonUtil.toJson(result));
        Assertions.assertTrue(generateCodeFile ? StrUtil.isNotBlank(result.getCodePath()) : StrUtil.isNotBlank(templateCode));
    }
    
    @Test
    void ServiceApiTemplateParserImplTest() {
        boolean generateCodeFile = true;
        final CodeGenerationTemplateConfigVO configVO = new CodeGenerationTemplateConfigVO();
        final JavaServiceApiConfigVO javaServiceApiConfigVO = new JavaServiceApiConfigVO();
        javaServiceApiConfigVO.setPackageName("com.lzx.kaleido.test");
        javaServiceApiConfigVO.setSourceFolder("src/main/java");
        javaServiceApiConfigVO.setCodePath("D:\\opt\\gencode");
        javaServiceApiConfigVO.setSuperclass(new SuperclassVO("com.baomidou.mybatisplus.extension.service.IService"));
        final String json = JsonUtil.toJson(javaServiceApiConfigVO);
        configMap.put(javaServiceApiConfigVO.getName(), javaServiceApiConfigVO);
        configVO.setTemplateParams(json);
        final CodeGenerationTableParam codeGenerationTableParam = new CodeGenerationTableParam();
        codeGenerationTableParam.setTableFieldColumnList(mockCodeGenerationTableFieldParams());
        codeGenerationTableParam.setTemplateName("tp_service_api.ftlx");
        codeGenerationTableParam.setTableName("test_demo");
        codeGenerationTableParam.setComment("设备信息表");
        final CodeGenerationViewVO testDemoEntity = CodeGenerationViewVO.builder().packageName("com.lzx.kaleido.test")
                .name("TestDemoEntity").codeType(TemplateParserEnum.ENTITY.getCodeType()).build();
        final CodeGenerationViewVO result = new ServiceApiTemplateProcessorImpl().generation(configVO, basicConfigVO, generateCodeFile,
                codeGenerationTableParam, ResourceMode.CLASSPATH, Stream.of(testDemoEntity).collect(Collectors.toList()));
        final String templateCode = result.getTemplateCode();
        System.out.println(templateCode);
        log.info("ServiceApiTemplateParserImplTest is {}", JsonUtil.toJson(result));
        Assertions.assertTrue(generateCodeFile ? StrUtil.isNotBlank(result.getCodePath()) : StrUtil.isNotBlank(templateCode));
    }
    
    @Test
    void ServiceTemplateParserImplTest() {
        boolean generateCodeFile = true;
        final CodeGenerationTemplateConfigVO configVO = new CodeGenerationTemplateConfigVO();
        final JavaServiceConfigVO javaServiceConfigVO = new JavaServiceConfigVO();
        javaServiceConfigVO.setPackageName("com.lzx.kaleido.test");
        javaServiceConfigVO.setSourceFolder("src/main/java");
        javaServiceConfigVO.setCodePath("D:\\opt\\gencode");
        javaServiceConfigVO.setSuperclass(new SuperclassVO("com.lzx.kaleido.test.BaseService"));
        javaServiceConfigVO.setUseMybatisPlus(true);
        final String json = JsonUtil.toJson(javaServiceConfigVO);
        configMap.put(javaServiceConfigVO.getName(), javaServiceConfigVO);
        configVO.setTemplateParams(json);
        final CodeGenerationTableParam codeGenerationTableParam = new CodeGenerationTableParam();
        codeGenerationTableParam.setTableFieldColumnList(mockCodeGenerationTableFieldParams());
        codeGenerationTableParam.setTemplateName("tp_service.ftlx");
        codeGenerationTableParam.setTableName("test_demo");
        codeGenerationTableParam.setComment("设备信息表");
        final CodeGenerationViewVO testDemoEntity = CodeGenerationViewVO.builder().packageName("com.lzx.kaleido.test")
                .name("TestDemoEntity").codeType(TemplateParserEnum.ENTITY.getCodeType()).build();
        final CodeGenerationViewVO testDemoMapper = CodeGenerationViewVO.builder().packageName("com.lzx.kaleido.test")
                .name("ITestDemoMapper").codeType(TemplateParserEnum.MAPPER.getCodeType()).build();
        final CodeGenerationViewVO testDemoService = CodeGenerationViewVO.builder().packageName("com.lzx.kaleido.test")
                .name("ITestDemoService").codeType(TemplateParserEnum.SERVICE_API.getCodeType()).build();
        final CodeGenerationViewVO result = new ServiceTemplateProcessorImpl().generation(configVO, basicConfigVO, generateCodeFile,
                codeGenerationTableParam, ResourceMode.CLASSPATH,
                Stream.of(testDemoEntity, testDemoMapper, testDemoService).collect(Collectors.toList()));
        final String templateCode = result.getTemplateCode();
        System.out.println(templateCode);
        log.info("ServiceTemplateParserImplTest is {}", JsonUtil.toJson(result));
        Assertions.assertTrue(generateCodeFile ? StrUtil.isNotBlank(result.getCodePath()) : StrUtil.isNotBlank(templateCode));
    }
    @Test
    void mockTableParams(){
        final List<CodeGenerationTableFieldParam> codeGenerationTableFieldParams = mockCodeGenerationTableFieldParams();
        System.out.println(JsonUtil.toJson(codeGenerationTableFieldParams));
    }
    private List<CodeGenerationTableFieldParam> mockCodeGenerationTableFieldParams() {
        final CodeGenerationTableFieldParam v1 = new CodeGenerationTableFieldParam();
        v1.setComment("设备ID");
        v1.setColumn("device_id");
        v1.setJdbcType("varchar");
        v1.setProperty("deviceId");
        v1.setJavaType("java.lang.String");
        v1.setJdbcTypeCode(Types.VARCHAR);
        final DataType dataType = JdbcUtil.resolveDataType(v1.getJdbcType(), v1.getJdbcTypeCode());
        v1.setXmlJdbcType(dataType.getName());
        v1.setPrimaryKey(true);
        final CodeGenerationTableFieldParam v2 = new CodeGenerationTableFieldParam();
        v2.setComment("厂家ID");
        v2.setColumn("factory_id");
        v2.setJdbcType("int8");
        v2.setXmlJdbcType("");
        v2.setProperty("factoryId");
        v2.setJavaType("java.lang.Long");
        v2.setJdbcTypeCode(Types.BIGINT);
        final DataType dataType2 = JdbcUtil.resolveDataType(v2.getJdbcType(), v2.getJdbcTypeCode());
        v2.setXmlJdbcType(dataType2.getName());
        final CodeGenerationTableFieldParam v3 = new CodeGenerationTableFieldParam();
        v3.setComment("创建时间");
        v3.setColumn("create_time");
        v3.setJdbcType("timestamp");
        v3.setProperty("createTime");
        v3.setJavaType("java.time.LocalDateTime");
        v3.setJdbcTypeCode(Types.TIMESTAMP);
        final DataType dataType3 = JdbcUtil.resolveDataType(v3.getJdbcType(), v3.getJdbcTypeCode());
        v3.setXmlJdbcType(dataType3.getName());
        return Stream.of(v1, v2, v3).collect(Collectors.toList());
    }
}
