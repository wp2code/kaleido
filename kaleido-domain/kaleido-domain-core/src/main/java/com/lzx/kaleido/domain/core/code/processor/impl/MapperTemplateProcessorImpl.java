package com.lzx.kaleido.domain.core.code.processor.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.lzx.kaleido.domain.api.constants.CodeTemplateConstants;
import com.lzx.kaleido.domain.core.code.processor.AbsTemplateProcessor;
import com.lzx.kaleido.domain.core.enums.ApiTemplateEnum;
import com.lzx.kaleido.domain.core.enums.TemplateParserEnum;
import com.lzx.kaleido.domain.core.utils.TemplateConvertUtil;
import com.lzx.kaleido.domain.model.dto.code.CodeApiDTO;
import com.lzx.kaleido.domain.model.dto.code.CodeClassDTO;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaMapperConfigVO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author lwp
 * @date 2024-01-18
 **/
public class MapperTemplateProcessorImpl extends AbsTemplateProcessor<JavaMapperConfigVO> {
    
    @Override
    protected void fillCodeGenerationTableParam(final JavaMapperConfigVO config, final BasicConfigVO basicConfig,
            final CodeGenerationTableParam codeGenerationTableParam, final CodeGenerationTemplateConfigVO configVO) {
        if (codeGenerationTableParam.isDirectUseTemplateConfig()) {
            codeGenerationTableParam.setPackageName(config.getPackageName());
            codeGenerationTableParam.setSourceFolder(config.getSourceFolder());
            codeGenerationTableParam.setUseMybatisPlus(config.isUseMybatisPlus());
            codeGenerationTableParam.setSuperclassName(config.getSuperclass() != null ? config.getSuperclass().getName() : null);
            if (StrUtil.isNotBlank(configVO.getCodePath())) {
                codeGenerationTableParam.setCodePath(configVO.getCodePath());
            }
            if (!config.isUseMybatisPlus()) {
                codeGenerationTableParam.setMethodList(config.getMethodList());
            }
        } else {
            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getUseMybatisPlus(),
                    (v) -> codeGenerationTableParam.setUseMybatisPlus(Boolean.parseBoolean(v.toString())), config.isUseMybatisPlus());
            //            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getMethodList(),
            //                    (v) -> codeGenerationTableParam.setMethodList((List<String>) v), config.getMethodList(),
            //                    !codeGenerationTableParam.getUseMybatisPlus() ? ApiTemplateEnum.getAllApi() : null);
            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getSuperclassName(),
                    (v) -> codeGenerationTableParam.setSuperclassName(String.valueOf(v)),
                    config.getSuperclass() != null ? config.getSuperclass().getName() : null);
            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getCodePath(),
                    (v) -> codeGenerationTableParam.setCodePath(v.toString()), config.getCodePath());
        }
        
    }
    
    
    @Override
    protected CodeGenerationTableParam convertCodeGenerationTableParam(final JavaMapperConfigVO javaConfigVO) {
        final CodeGenerationTableParam param = new CodeGenerationTableParam();
        param.setCodePath(javaConfigVO.getCodePath());
        param.setSourceFolder(javaConfigVO.getSourceFolder());
        param.setConfigName(TemplateParserEnum.MAPPER.getCodeType());
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
                .orElse(TemplateParserEnum.MAPPER.getDefaultNameSuffix());
    }
    
    /**
     * @param tableName
     * @return
     */
    @Override
    protected String getTemplateNameIfAbsent(final String tableName) {
        return StrUtil.isNotBlank(tableName) ? tableName : TemplateParserEnum.MAPPER.getDefaultTemplateName();
    }
    
    @Override
    protected Map<String, Object> doBuildTemplateParams(final String codeName, final JavaMapperConfigVO javaMapperConfigVO,
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
            if (entityViewVO != null && (codeGenerationTableParam.getUseMybatisPlus() || ApiTemplateEnum.containsObjectMethod(
                    codeGenerationTableParam.getMethodList()))) {
                params.put(CodeTemplateConstants.genericsClass, entityViewVO.getName());
                packages.add(TemplateConvertUtil.getFullPackageName(entityViewVO));
            }
            if (CollUtil.isEmpty(codeGenerationTableParam.getTableFieldColumnList()) && entityViewVO != null) {
                codeGenerationTableParam.setTableFieldColumnList(
                        convertCodeGenerationTableFieldParamList(entityViewVO.getTableFieldColumnMap()));
            }
        }
        final List<String> apiList = codeGenerationTableParam.getMethodList();
        if (CollUtil.isNotEmpty(apiList) && !codeGenerationTableParam.getUseMybatisPlus()) {
            final List<CodeApiDTO> apiParamList = new ArrayList<>(apiList.size());
            for (final String apiId : apiList) {
                final ApiTemplateEnum apiTemplateEnum = ApiTemplateEnum.getInstance(apiId);
                if (apiTemplateEnum != null) {
                    String returnType = apiTemplateEnum.getReturnType();
                    if (("object".equals(returnType) || "objectList".equals(returnType))) {
                        returnType = Optional.ofNullable(entityViewVO).map(CodeGenerationViewVO::getName).orElse("Object");
                    }
                    String parameterType = apiTemplateEnum.getParameterType();
                    if (!"pk".equals(parameterType)) {
                        parameterType = Optional.ofNullable(entityViewVO).map(CodeGenerationViewVO::getName).orElse("Object");
                    } else {
                        parameterType = Optional.ofNullable(codeGenerationTableParam.getPrimaryKeyField()).map(v -> {
                            final CodeClassDTO javaTypeParam = new CodeClassDTO(v.getJavaType());
                            packages.add(javaTypeParam.getFullName());
                            return javaTypeParam.getName();
                        }).orElse("Object");
                    }
                    if (apiTemplateEnum.isParameterList() || apiTemplateEnum == ApiTemplateEnum.selectPage) {
                        packages.add("org.apache.ibatis.annotations.Param");
                    }
                    if (apiTemplateEnum.isParameterList() || apiTemplateEnum.isReturnList()) {
                        packages.add("java.util.List");
                    }
                    apiParamList.add(CodeApiDTO.of(apiTemplateEnum.getApiId(), returnType, apiTemplateEnum.isReturnList(), parameterType,
                            apiTemplateEnum.isParameterList(), apiTemplateEnum.getDescribe(), null, null,
                            apiTemplateEnum == ApiTemplateEnum.selectPage));
                }
            }
            params.put(CodeTemplateConstants.apiList, apiParamList);
        }
        params.put(CodeTemplateConstants.packages, packages);
        return params;
    }
}
