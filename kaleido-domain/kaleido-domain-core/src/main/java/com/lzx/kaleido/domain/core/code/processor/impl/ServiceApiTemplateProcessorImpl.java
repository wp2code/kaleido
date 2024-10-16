package com.lzx.kaleido.domain.core.code.processor.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.lzx.kaleido.domain.api.constants.CodeTemplateConstants;
import com.lzx.kaleido.domain.core.code.processor.AbsTemplateProcessor;
import com.lzx.kaleido.domain.core.enums.TemplateParserEnum;
import com.lzx.kaleido.domain.core.utils.TemplateConvertUtil;
import com.lzx.kaleido.domain.model.dto.code.CodeClassDTO;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaServiceApiConfigVO;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author lwp
 * @date 2024-01-18
 **/
public class ServiceApiTemplateProcessorImpl extends AbsTemplateProcessor<JavaServiceApiConfigVO> {
    
    @Override
    protected void fillCodeGenerationTableParam(final JavaServiceApiConfigVO config, final BasicConfigVO basicConfig,
            final CodeGenerationTableParam codeGenerationTableParam, final CodeGenerationTemplateConfigVO configVO) {
        if (codeGenerationTableParam.isDirectUseTemplateConfig()) {
            codeGenerationTableParam.setUseMybatisPlus(config.isUseMybatisPlus());
            codeGenerationTableParam.setPackageName(config.getPackageName());
            codeGenerationTableParam.setSourceFolder(config.getSourceFolder());
            codeGenerationTableParam.setSuperclassName(config.getSuperclass() != null ? config.getSuperclass().getName() : null);
            if (StrUtil.isNotBlank(configVO.getCodePath())) {
                codeGenerationTableParam.setCodePath(configVO.getCodePath());
            }
        } else {
            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getSuperclassName(),
                    (v) -> codeGenerationTableParam.setSuperclassName(String.valueOf(v)),
                    config.getSuperclass() != null ? config.getSuperclass().getName() : null);
            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getCodePath(),
                    (v) -> codeGenerationTableParam.setCodePath(v.toString()), config.getCodePath());
        }
        
    }
    
    /**
     * @param tableName
     * @return
     */
    @Override
    protected String getTemplateNameIfAbsent(final String tableName) {
        return StrUtil.isNotBlank(tableName) ? tableName : TemplateParserEnum.SERVICE_API.getDefaultTemplateName();
    }
    
    @Override
    protected CodeGenerationTableParam convertCodeGenerationTableParam(final JavaServiceApiConfigVO javaConfigVO) {
        final CodeGenerationTableParam param = new CodeGenerationTableParam();
        param.setCodePath(javaConfigVO.getCodePath());
        param.setSourceFolder(javaConfigVO.getSourceFolder());
        param.setConfigName(TemplateParserEnum.SERVICE_API.getCodeType());
        param.setPackageName(javaConfigVO.getPackageName());
        if (javaConfigVO.getSuperclass() != null) {
            param.setSuperclassName(javaConfigVO.getSuperclass().getName());
        }
        return param;
    }
    
    /**
     * @param name
     * @param tableName
     * @param nameSuffix
     * @return
     */
    @Override
    protected String getCodeName(String name, final String tableName, String nameSuffix) {
        return StrUtil.isNotBlank(name) ? name : "I" + TemplateConvertUtil.toCamelFirstToUpper(tableName) + Optional.ofNullable(nameSuffix)
                .orElse(TemplateParserEnum.SERVICE_API.getDefaultNameSuffix());
    }
    
    /**
     * @param codeName
     * @param javaVoConfig
     * @param basicConfig
     * @param codeGenerationTableParam
     * @param refCodeGenerationViewList
     * @return
     */
    @Override
    protected Map<String, Object> doBuildTemplateParams(final String codeName, final JavaServiceApiConfigVO javaVoConfig,
            final BasicConfigVO basicConfig, final CodeGenerationTableParam codeGenerationTableParam,
            final List<CodeGenerationViewVO> refCodeGenerationViewList) {
        final Map<String, Object> params = Maps.newHashMap();
        final Set<String> packages = new HashSet<>();
        final String superclassName = codeGenerationTableParam.getSuperclassName();
        if (StrUtil.isNotBlank(superclassName)) {
            final CodeClassDTO superclass = new CodeClassDTO(superclassName);
            params.put(CodeTemplateConstants.superclassName, superclass.getName());
            packages.add(superclass.getFullName());
        }
        CodeGenerationViewVO entityViewVO = null;
        if (CollUtil.isNotEmpty(refCodeGenerationViewList) && Boolean.TRUE.equals(codeGenerationTableParam.getUseMybatisPlus())) {
            entityViewVO = refCodeGenerationViewList.stream().filter(v -> TemplateParserEnum.isEntity(v.getCodeType())).findFirst()
                    .orElse(null);
            if (entityViewVO != null) {
                params.put(CodeTemplateConstants.genericsClass, entityViewVO.getName());
                packages.add(TemplateConvertUtil.getFullPackageName(entityViewVO));
            }
        }
        params.put(CodeTemplateConstants.packages, packages);
        return params;
    }
}
