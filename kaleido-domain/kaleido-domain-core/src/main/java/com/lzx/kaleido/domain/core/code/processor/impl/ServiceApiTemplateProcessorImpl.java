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
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaServiceApiConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.SuperclassVO;

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
    
    private static final String _SUFFIX = "Service";
    
    @Override
    protected void fillCodeGenerationTableParam(final JavaServiceApiConfigVO config, final BasicConfigVO basicConfig,
            final CodeGenerationTableParam codeGenerationTableParam) {
        TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getSuperclassName(),
                (v) -> codeGenerationTableParam.setSuperclassName(String.valueOf(v)),
                Optional.of(config.getSuperclass()).map(SuperclassVO::getName).orElse(null));
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
    
    @Override
    protected String getCodeName(String name, final String tableName) {
        return StrUtil.isNotBlank(name) ? name : "I" + TemplateConvertUtil.underlineToCamelFirstToUpper(tableName) + _SUFFIX;
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
        if (CollUtil.isNotEmpty(refCodeGenerationViewList)) {
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
