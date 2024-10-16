package com.lzx.kaleido.start.config;

import cn.hutool.core.io.FileUtil;
import com.lzx.kaleido.domain.api.code.ICodeGenerationTemplateService;
import com.lzx.kaleido.domain.api.enums.CodeTemplateDefaultEnum;
import com.lzx.kaleido.domain.api.enums.CodeTemplateHideEnum;
import com.lzx.kaleido.domain.api.enums.CodeTemplateInternalEnum;
import com.lzx.kaleido.domain.api.enums.CodeTemplateSourceTypeEnum;
import com.lzx.kaleido.domain.core.enums.TemplateParserEnum;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.TemplateParamVO;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lwp
 * @date 2024-06-13
 **/
@Slf4j
@Component
public class KaleidoApplicationInitializer implements ApplicationListener<ApplicationStartedEvent> {
    
    
    private final static String DEFAULT_PACKAGE_NAME = "com.lzx.kaleido.test";
    
    private final static String DEFAULT_SOURCE_FOLDER = "src/main/java";
    
    @Resource
    private ICodeGenerationTemplateService codeGenerationGroupService;
    
    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(final ApplicationStartedEvent event) {
        new Thread(this::initTemplate).start();
    }
    
    /**
     * 初始化默认模板
     */
    private void initTemplate() {
        if (codeGenerationGroupService.checkInitDefaultTemplate()) {
            return;
        }
        final String codePath = FileUtil.getTmpDirPath();
        final CodeGenerationTemplateVO vo = new CodeGenerationTemplateVO();
        vo.setSourceType(CodeTemplateSourceTypeEnum.INIT_ADD.getCode());
        vo.setIsInternal(CodeTemplateInternalEnum.Y.getCode());
        vo.setIsDefault(CodeTemplateDefaultEnum.DEFAULT.getCode());
        vo.setLanguage("java");
        vo.setTemplateName("默认模板");
        vo.setDigestValue("应用默认配置");
        final BasicConfigVO basicConfig = new BasicConfigVO();
        basicConfig.setAuthor("Kaleido");
        basicConfig.setCodePath(codePath);
        vo.setBasicConfig(JsonUtil.toJson(basicConfig));
        final CodeGenerationTemplateConfigVO entityConfig = buildTemplateConfig(codePath, TemplateParserEnum.ENTITY,
                "com.lzx.kaleido.test.pojo.BaseEntity", (v) -> {
                    v.setUseMybatisPlus(true);
                    v.setUseLombok(true);
                });
        final CodeGenerationTemplateConfigVO voConfig = buildTemplateConfig(codePath, TemplateParserEnum.VO,
                "com.lzx.kaleido.test.pojo.BaseVO", (v) -> {
                    v.setUseLombok(true);
                    v.setUseSwagger(true);
                });
        final CodeGenerationTemplateConfigVO mapperConfig = buildTemplateConfig(codePath, TemplateParserEnum.MAPPER,
                "com.lzx.kaleido.test.IBaseMapper", (v) -> {
                    v.setUseMybatisPlus(true);
                });
        final CodeGenerationTemplateConfigVO serviceApiConfig = buildTemplateConfig(codePath, TemplateParserEnum.SERVICE_API,
                "com.baomidou.mybatisplus.extension.service.IService", null);
        final CodeGenerationTemplateConfigVO serviceConfig = buildTemplateConfig(codePath, TemplateParserEnum.SERVICE,
                "com.lzx.kaleido.test.BaseService", null);
        final CodeGenerationTemplateConfigVO xmlConfig = buildTemplateConfig(codePath, TemplateParserEnum.XML, null, (v) -> {
            final List<String> methodList = Stream.of("insertSelective", "insertBatch", "insertOrUpdateSelective",
                    "insertOrUpdateSelectiveBatch", "deleteByPrimaryKey", "updateByPrimaryKey", "updateByPrimaryKeySelective",
                    "selectByEntity", "selectByPrimaryKey", "selectPage").collect(Collectors.toList());
            v.setMethodList(methodList);
            v.setUseMybatisPlus(false);
        });
        final CodeGenerationTemplateConfigVO controllerConfig = buildTemplateConfig(codePath, TemplateParserEnum.CONTROLLER,
                "com.lzx.kaleido.test.BaseController", (v) -> {
                    v.setResponseGenericClass("com.lzx.kaleido.test.pojo.R");
                    final List<String> methodList = Stream.of("search", "page", "detail", "save", "update", "update")
                            .collect(Collectors.toList());
                    v.setMethodList(methodList);
                    v.setUseMybatisPlus(true);
                    v.setUseSwagger(true);
                });
        vo.setTemplateConfigList(
                Stream.of(entityConfig, voConfig, mapperConfig, xmlConfig, serviceApiConfig, serviceConfig, controllerConfig)
                        .collect(Collectors.toList()));
        final Long templateId = codeGenerationGroupService.addCodeGenerationTemplate(vo);
        if (templateId != null) {
            log.info("init default template config templateId is {}", templateId);
        }
    }
    
    /**
     * @param codePath
     * @param parserEnum
     * @param superclass
     * @param consumer
     * @return
     */
    private CodeGenerationTemplateConfigVO buildTemplateConfig(String codePath, TemplateParserEnum parserEnum, String superclass,
            Consumer<TemplateParamVO> consumer) {
        final CodeGenerationTemplateConfigVO config = new CodeGenerationTemplateConfigVO();
        config.setName(parserEnum.getCodeType());
        config.setAlias(parserEnum.getCodeType());
        config.setCodePath(codePath);
        config.setHideStatus(CodeTemplateHideEnum.SHOW.getCode());
        final TemplateParamVO templateParam = new TemplateParamVO();
        templateParam.setAliasName(parserEnum.getCodeType());
        templateParam.setName(parserEnum.getCodeType());
        templateParam.setNameSuffix(parserEnum.getDefaultNameSuffix());
        templateParam.setPackageName(DEFAULT_PACKAGE_NAME);
        templateParam.setSourceFolder(DEFAULT_SOURCE_FOLDER);
        templateParam.buildSuperclass(superclass, new ArrayList<>());
        if (consumer != null) {
            consumer.accept(templateParam);
        }
        config.setTemplateParams(JsonUtil.toJson(templateParam));
        return config;
    }
}
