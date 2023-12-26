package com.lzx.kaleido.domain.core.impl;

import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.domain.api.service.ICodeGeneration;
import com.lzx.kaleido.domain.api.service.ICodeGenerationTemplateConfigService;
import com.lzx.kaleido.domain.model.dto.param.code.CodeGenerationParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationResultVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import com.lzx.kaleido.plugins.template.model.TemplateContext;
import com.lzx.kaleido.plugins.template.utils.CodeGenerationUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lwp
 * @date 2023-12-18
 **/
@Service
public class CodeGenerationService implements ICodeGeneration {
    
    @Resource
    private ICodeGenerationTemplateConfigService codeGenerationTemplateConfigService;
    
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
     * 代码生成
     *
     * @param configParam
     * @return
     */
    @Override
    public CodeGenerationResultVO generation(final CodeGenerationParam configParam) {
        final CodeGenerationTemplateConfigVO config = getCodeGenerationTemplateConfig(configParam);
        final String path = CodeGenerationUtil.processTemplate(config.getTemplateContent(), configParam.getOutDirPath(),
                configParam.getOutFileName(), JsonUtil.toMap(configParam.getTemplateParams()),
                configParam.getEngineNameIfAbsent(TemplateContext.DEFAULT_ENGINE), config.getName());
        return new CodeGenerationResultVO().setPath(path).setCodeAlias(config.getAlias());
    }
    
    /**
     * 代码生成-批量
     *
     * @param codeGenerationParams
     * @return
     */
    @Override
    public List<CodeGenerationResultVO> generation(final List<CodeGenerationParam> codeGenerationParams) {
        return codeGenerationParams.stream().map(this::generation).collect(Collectors.toList());
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
