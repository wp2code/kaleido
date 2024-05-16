package com.lzx.kaleido.domain.core.code.processor.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.lzx.kaleido.domain.api.constants.CodeTemplateConstants;
import com.lzx.kaleido.domain.core.code.processor.AbsTemplateProcessor;
import com.lzx.kaleido.domain.core.enums.TemplateParserEnum;
import com.lzx.kaleido.domain.core.utils.TemplateConvertUtil;
import com.lzx.kaleido.domain.model.dto.code.CodeClassDTO;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableFieldParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaVoConfigVO;
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
public class VoTemplateProcessorImpl extends AbsTemplateProcessor<JavaVoConfigVO> {
    
    private static final String _SUFFIX = "VO";
    
    /**
     * @param config
     * @param basicConfig
     * @param codeGenerationTableParam
     */
    @Override
    protected void fillCodeGenerationTableParam(final JavaVoConfigVO config, BasicConfigVO basicConfig,
            CodeGenerationTableParam codeGenerationTableParam) {
        TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getUseLombok(),
                (v) -> codeGenerationTableParam.setUseLombok(Boolean.parseBoolean(v.toString())), config.isUseLombok());
        TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getUseSwagger(),
                (v) -> codeGenerationTableParam.setUseSwagger(Boolean.parseBoolean(v.toString())), config.isUseSwagger());
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
        return StrUtil.isNotBlank(tableName) ? tableName : TemplateParserEnum.VO.getDefaultTemplateName();
    }
    
    @Override
    protected CodeGenerationTableParam convertCodeGenerationTableParam(final JavaVoConfigVO javaConfigVO) {
        final CodeGenerationTableParam param = new CodeGenerationTableParam();
        param.setCodePath(javaConfigVO.getCodePath());
        param.setSourceFolder(javaConfigVO.getSourceFolder());
        param.setConfigName(TemplateParserEnum.VO.getCodeType());
        param.setPackageName(javaConfigVO.getPackageName());
        if (javaConfigVO.getSuperclass() != null) {
            param.setSuperclassName(javaConfigVO.getSuperclass().getName());
        }
        return param;
    }
    
    /**
     * @return
     */
    @Override
    protected String getCodeName(String name, final String tableName) {
        return StrUtil.isNotBlank(name) ? name : TemplateConvertUtil.toCamelFirstToUpper(tableName) + _SUFFIX;
    }
    
    
    /**
     * @param codeName
     * @param javaVoConfig
     * @param basicConfig
     * @param codeGenerationTableParam
     * @return
     */
    @Override
    protected Map<String, Object> doBuildTemplateParams(final String codeName, final JavaVoConfigVO javaVoConfig,
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
        if (Boolean.TRUE.equals(codeGenerationTableParam.getUseSwagger())) {
            params.put(CodeTemplateConstants.useSwagger, true);
            packages.add("io.swagger.annotations.ApiModel");
            packages.add("io.swagger.annotations.ApiModelProperty");
        } else {
            params.put(CodeTemplateConstants.useSwagger, false);
        }
        if (Boolean.TRUE.equals(codeGenerationTableParam.getUseLombok())) {
            params.put(CodeTemplateConstants.useLombok, true);
            packages.add("lombok.Data");
            packages.add("lombok.experimental.Accessors");
            if (StrUtil.isNotBlank(codeGenerationTableParam.getSuperclassName())) {
                packages.add("lombok.EqualsAndHashCode");
            }
        } else {
            params.put(CodeTemplateConstants.useLombok, false);
        }
        final List<CodeGenerationTableFieldParam> tableFieldColumnList = codeGenerationTableParam.getTableFieldColumnList();
        if (CollUtil.isNotEmpty(tableFieldColumnList)) {
            final List<String> fieldPackages = tableFieldColumnList.stream().map(CodeGenerationTableFieldParam::getJavaType)
                    .filter(javaType -> !StrUtil.startWith(javaType, "java.lang")).distinct().toList();
            if (CollUtil.isNotEmpty(fieldPackages)) {
                packages.addAll(fieldPackages);
            }
            params.put(CodeTemplateConstants.tableFieldColumnList, tableFieldColumnList);
        }
        params.put(CodeTemplateConstants.packages, packages);
        return params;
    }
    
}
