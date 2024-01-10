package com.lzx.kaleido.domain.core.code.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.domain.api.code.ICodeGeneration;
import com.lzx.kaleido.domain.api.code.ICodeGenerationTemplateConfigService;
import com.lzx.kaleido.domain.api.code.ICodeGenerationTemplateService;
import com.lzx.kaleido.domain.api.enums.CodeTemplateHideEnum;
import com.lzx.kaleido.domain.core.enums.TemplateParserEnum;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationAllParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationResultVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import com.lzx.kaleido.plugins.template.enums.ResourceMode;
import com.lzx.kaleido.plugins.template.exception.TemplateParseException;
import com.lzx.kaleido.plugins.template.model.TemplateContext;
import com.lzx.kaleido.plugins.template.utils.CodeGenerationUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lwp
 * @date 2023-12-18
 **/
@Service
public class CodeGenerationService implements ICodeGeneration {
    
    @Resource
    private ICodeGenerationTemplateConfigService codeGenerationTemplateConfigService;
    
    @Resource
    private ICodeGenerationTemplateService codeGenerationTemplateService;
    
    /**
     * 代码生成或预览
     *
     * @param codeGenerationTableParam
     * @param isPreview
     */
    @Override
    public CodeGenerationResultVO generationOrPreview(final CodeGenerationAllParam codeGenerationTableParam, final boolean isPreview) {
        final List<CodeGenerationTableParam> codeGenerationList = codeGenerationTableParam.getCodeGenerationList();
        if (CollUtil.isNotEmpty(codeGenerationList)) {
            final CodeGenerationTemplateVO templateVO = codeGenerationTemplateService.getDetailById(
                    codeGenerationTableParam.getTemplateId(), CodeTemplateHideEnum.SHOW.getCode());
            if (templateVO == null) {
                throw new CommonRuntimeException(ErrorCode.CODE_TEMPLATE_CONFIG_NOT_EXITS);
            }
            final String basicConfig = templateVO.getBasicConfig();
            final BasicConfigVO basicConfigVO = JsonUtil.toBean(basicConfig, BasicConfigVO.class);
            final List<CodeGenerationTemplateConfigVO> templateConfigList = templateVO.getTemplateConfigList();
            final List<CodeGenerationViewVO> codeGenerationViewVOS = new ArrayList<>();
            codeGenerationList.sort((o1, o2) -> {
                final TemplateParserEnum instance1 = TemplateParserEnum.getInstance(o1.getConfigName());
                final TemplateParserEnum instance2 = TemplateParserEnum.getInstance(o2.getConfigName());
                return instance1.getPriority() - instance2.getPriority();
            });
            for (final CodeGenerationTableParam generationTableParam : codeGenerationList) {
                final CodeGenerationTemplateConfigVO configVO = templateConfigList.stream()
                        .filter(v -> v.getName().equals(generationTableParam.getConfigName())).findFirst().orElse(null);
                if (configVO != null) {
                    final TemplateParserEnum instance = TemplateParserEnum.getInstance(configVO.getName());
                    if (instance != null) {
                        CodeGenerationViewVO codeGenerationViewVO = instance.getTemplateParser()
                                .generation(configVO, basicConfigVO, !isPreview, generationTableParam,
                                        ResourceMode.getInstance(generationTableParam.getTemplateResourceMode()), codeGenerationViewVOS);
                        if (codeGenerationViewVO != null) {
                            System.out.println(codeGenerationViewVO.getTemplateCode());
                            codeGenerationViewVOS.add(codeGenerationViewVO);
                        }
                    }
                }
            }
            return new CodeGenerationResultVO(templateVO, codeGenerationViewVOS);
        }
        throw new TemplateParseException(ErrorCode.CODE_TEMPLATE_CONFIG_ERROR);
    }
    
    /**
     * 代码生成-预览
     *
     * @param configParam
     * @param outputStream
     */
    @Override
    public void preview(final CodeGenerationParam configParam, final OutputStream outputStream) {
        final CodeGenerationTemplateConfigVO config = getCodeGenerationTemplateConfig(configParam);
        CodeGenerationUtil.processTemplateOutStream(config.getTemplateContent(), outputStream,
                JsonUtil.toMap(configParam.getTemplateParams()), configParam.getEngineNameIfAbsent(TemplateContext.DEFAULT_ENGINE),
                config.getName());
    }
    
    /**
     * @param configParam
     * @return
     */
    private CodeGenerationTemplateConfigVO getCodeGenerationTemplateConfig(final CodeGenerationParam configParam) {
        //使用参数模板
        if (StrUtil.isBlank(configParam.getTemplateContent())) {
            if (configParam.getTemplateConfigId() == null) {
                throw new CommonRuntimeException(ErrorCode.REQUEST_PARAMS_ERROR);
            }
            final CodeGenerationTemplateConfigVO config = codeGenerationTemplateConfigService.getDetailById(
                    configParam.getTemplateConfigId());
            if (config == null) {
                throw new CommonRuntimeException(ErrorCode.CODE_TEMPLATE_CONFIG_NOT_EXITS);
            }
            return config;
        }
        final CodeGenerationTemplateConfigVO vo = new CodeGenerationTemplateConfigVO();
        vo.setTemplateContent(configParam.getTemplateContent());
        vo.setAlias(configParam.getTemplateName());
        vo.setName(configParam.getTemplateName());
        return vo;
    }
}
