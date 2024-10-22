package com.lzx.kaleido.domain.core.code.processor.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.lzx.kaleido.domain.api.constants.CodeTemplateConstants;
import com.lzx.kaleido.domain.core.code.processor.AbsTemplateProcessor;
import com.lzx.kaleido.domain.core.enums.ControllerApiTemplateEnum;
import com.lzx.kaleido.domain.core.enums.TemplateParserEnum;
import com.lzx.kaleido.domain.core.utils.TemplateConvertUtil;
import com.lzx.kaleido.domain.model.dto.code.CodeApiDTO;
import com.lzx.kaleido.domain.model.dto.code.CodeClassDTO;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaControllerConfigVO;
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
public class ControllerTemplateProcessorImpl extends AbsTemplateProcessor<JavaControllerConfigVO> {
    
    
    /**
     * @return
     */
    @Override
    protected String getCodeName(String name, final String tableName, String nameSuffix) {
        return StrUtil.isNotBlank(name) ? name : TemplateConvertUtil.toCamelFirstToUpper(tableName) + Optional.ofNullable(nameSuffix)
                .orElse(TemplateParserEnum.CONTROLLER.getDefaultNameSuffix());
    }
    
    /**
     * @param tableName
     * @return
     */
    @Override
    protected String getTemplateNameIfAbsent(final String tableName) {
        return StrUtil.isNotBlank(tableName) ? tableName : TemplateParserEnum.CONTROLLER.getDefaultTemplateName();
    }
    
    /**
     * @param config
     * @param basicConfig
     * @param codeGenerationTableParam
     */
    @Override
    protected void fillCodeGenerationTableParam(final JavaControllerConfigVO config, final BasicConfigVO basicConfig,
            final CodeGenerationTableParam codeGenerationTableParam, final CodeGenerationTemplateConfigVO configVO) {
        if (codeGenerationTableParam.isDirectUseTemplateConfig()) {
            codeGenerationTableParam.setUseMybatisPlus(config.isUseMybatisPlus());
            codeGenerationTableParam.setUseSwagger(config.isUseSwagger());
            codeGenerationTableParam.setPackageName(config.getPackageName());
            codeGenerationTableParam.setSourceFolder(config.getSourceFolder());
            if (StrUtil.isNotBlank(configVO.getCodePath())) {
                codeGenerationTableParam.setCodePath(configVO.getCodePath());
            }
            codeGenerationTableParam.setSuperclassName(config.getSuperclass() != null ? config.getSuperclass().getName() : null);
            codeGenerationTableParam.setResponseGenericClass(config.getResponseGenericClass());
            codeGenerationTableParam.setWebMethodList(config.getMethodList());
        } else {
//            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getWebMethodList(),
//                    (v) -> codeGenerationTableParam.setWebMethodList((List<String>) v), config.getMethodList());
            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getResponseGenericClass(),
                    (v) -> codeGenerationTableParam.setResponseGenericClass(v.toString()), config.getResponseGenericClass());
            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getUseSwagger(),
                    (v) -> codeGenerationTableParam.setUseSwagger(Boolean.parseBoolean(v.toString())), config.isUseSwagger());
            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getUseMybatisPlus(),
                    (v) -> codeGenerationTableParam.setUseMybatisPlus(Boolean.parseBoolean(v.toString())), config.isUseMybatisPlus());
            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getSuperclassName(),
                    (v) -> codeGenerationTableParam.setSuperclassName(String.valueOf(v)),
                    config.getSuperclass() != null ? config.getSuperclass().getName() : null);
            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getCodePath(),
                    (v) -> codeGenerationTableParam.setCodePath(v.toString()), config.getCodePath());
        }
        
    }
    
    /**
     * @param codeName
     * @param javaControllerConfig
     * @param basicConfig
     * @param codeGenerationTableParam
     * @return
     */
    @Override
    protected Map<String, Object> doBuildTemplateParams(final String codeName, final JavaControllerConfigVO javaControllerConfig,
            final BasicConfigVO basicConfig, final CodeGenerationTableParam codeGenerationTableParam,
            final List<CodeGenerationViewVO> refCodeGenerationViewList) {
        final Set<String> packages = new HashSet<>();
        packages.add("org.springframework.web.bind.annotation.RequestMapping");
        CodeGenerationViewVO serviceApiViewVO = null;
        CodeGenerationViewVO voViewVO = null;
        if (CollUtil.isNotEmpty(refCodeGenerationViewList)) {
            serviceApiViewVO = refCodeGenerationViewList.stream().filter(v -> TemplateParserEnum.isServiceApi(v.getCodeType())).findFirst()
                    .orElse(null);
            voViewVO = refCodeGenerationViewList.stream().filter(v -> TemplateParserEnum.isVo(v.getCodeType())).findFirst().orElse(null);
            final String fullPackageName = TemplateConvertUtil.getFullPackageName(serviceApiViewVO);
            if (StrUtil.isNotBlank(fullPackageName)) {
                packages.add(fullPackageName);
            }
            if (CollUtil.isEmpty(codeGenerationTableParam.getTableFieldColumnList()) && serviceApiViewVO != null) {
                codeGenerationTableParam.setTableFieldColumnList(
                        convertCodeGenerationTableFieldParamList(serviceApiViewVO.getTableFieldColumnMap()));
            }
        }
        final List<String> apiList = codeGenerationTableParam.getWebMethodList();
        final List<CodeApiDTO> apiParamList = new ArrayList<>();
        if (CollUtil.isNotEmpty(apiList)) {
            CodeClassDTO responseGenericClassParam = null;
            final String responseGenericClass = codeGenerationTableParam.getResponseGenericClass();
            if (StrUtil.isNotBlank(responseGenericClass)) {
                packages.add((responseGenericClassParam = new CodeClassDTO(responseGenericClass)).getFullName());
            }
            for (final String apiId : apiList) {
                final ControllerApiTemplateEnum apiTemplateEnum = ControllerApiTemplateEnum.getInstance(apiId);
                if (apiTemplateEnum != null) {
                    String returnType = apiTemplateEnum.getReturnType();
                    if ("object".equals(returnType) || "objectList".equals(returnType)) {
                        returnType = Optional.ofNullable(voViewVO).map(CodeGenerationViewVO::getName).orElse("Object");
                        packages.add(TemplateConvertUtil.getFullPackageName(voViewVO));
                        if (ControllerApiTemplateEnum.isPage(apiTemplateEnum) && Boolean.TRUE.equals(
                                codeGenerationTableParam.getUseMybatisPlus())) {
                            packages.add("com.baomidou.mybatisplus.extension.plugins.pagination.Page");
                        }
                    }
                    String parameterType = apiTemplateEnum.getParameterType();
                    String parameterTypeValue = "";
                    parameterTypeValue = "@Validated @RequestBody %s condition".formatted(
                            Optional.ofNullable(voViewVO).map(CodeGenerationViewVO::getName).orElse("Object"));
                    packages.add("org.springframework.web.bind.annotation.RequestBody");
                    packages.add("org.springframework.validation.annotation.Validated");
                    packages.add(TemplateConvertUtil.getFullPackageName(voViewVO));
                    if (parameterType.contains("pk")) {
                        String pkIdType = "Object";
                        if (codeGenerationTableParam.getPrimaryKeyField() != null) {
                            final CodeClassDTO primaryClassParam = new CodeClassDTO(
                                    codeGenerationTableParam.getPrimaryKeyField().getJavaType());
                            packages.add(primaryClassParam.getFullName());
                            pkIdType = primaryClassParam.getName();
                        }
                        if (StrUtil.isNotBlank(parameterTypeValue)) {
                            parameterTypeValue += ",";
                        }
                        parameterTypeValue += "@PathVariable(value =\"%s\") %s id".formatted("id", pkIdType);
                        packages.add("org.springframework.web.bind.annotation.PathVariable");
                    }
                    if (apiTemplateEnum == ControllerApiTemplateEnum.searchPage) {
                        parameterTypeValue += ", @PathVariable(value =\"pageSize\") pageSize, @PathVariable(value =\"pageNumber\") pageNumber";
                        packages.add("org.springframework.web.bind.annotation.PathVariable");
                    }
                    packages.add("org.springframework.web.bind.annotation.RequestMethod");
                    apiParamList.add(CodeApiDTO.of(apiTemplateEnum.getApiId(),
                            ftmReturnType(responseGenericClassParam, returnType, apiTemplateEnum.isReturnList(),
                                    ControllerApiTemplateEnum.isPage(apiTemplateEnum)), apiTemplateEnum.isReturnList(), parameterTypeValue,
                            apiTemplateEnum.isParameterList(), apiTemplateEnum.getDescribe(), apiTemplateEnum.getPath(),
                            "RequestMethod." + apiTemplateEnum.getMethod(), false));
                }
            }
        }
        final Map<String, Object> params = Maps.newHashMap();
        if (StrUtil.isNotBlank(codeGenerationTableParam.getSuperclassName())) {
            final CodeClassDTO superclass = new CodeClassDTO(codeGenerationTableParam.getSuperclassName());
            params.put(CodeTemplateConstants.superclassName, superclass.getName());
            packages.add(superclass.getFullName());
        }
        if (Boolean.TRUE.equals(codeGenerationTableParam.getUseSwagger())) {
            params.put(CodeTemplateConstants.useSwagger, true);
            packages.add("io.swagger.annotations.ApiModel");
            packages.add("io.swagger.annotations.ApiOperation");
        } else {
            params.put(CodeTemplateConstants.useSwagger, false);
        }
        params.put(CodeTemplateConstants.packages, packages);
        params.put(CodeTemplateConstants.serviceApiName,
                Optional.ofNullable(serviceApiViewVO).map(CodeGenerationViewVO::getName).orElse(null));
        params.put(CodeTemplateConstants.apiList, apiParamList);
        params.put(CodeTemplateConstants.rootPath,
                "/api/" + TemplateConvertUtil.underlineToCamelToLower(codeGenerationTableParam.getTableName()));
        return params;
    }
    
    /**
     * 格式化 返回参数
     *
     * @param codeClassDTO
     * @param returnType
     * @param isList
     * @param isPage
     * @return
     */
    private String ftmReturnType(CodeClassDTO codeClassDTO, String returnType, boolean isList, boolean isPage) {
        if (codeClassDTO != null) {
            if (isList) {
                if (isPage) {
                    return "%s<Page<%s>>".formatted(codeClassDTO.getName(), returnType);
                }
                return "%s<List<%s>>".formatted(codeClassDTO.getName(), returnType);
            }
            return "%s<%s>".formatted(codeClassDTO.getName(), returnType);
        }
        return returnType;
    }
    
    
    /**
     * @param javaConfigVO
     * @return
     */
    @Override
    public CodeGenerationTableParam convertCodeGenerationTableParam(final JavaControllerConfigVO javaConfigVO) {
        final CodeGenerationTableParam param = new CodeGenerationTableParam();
        param.setCodePath(javaConfigVO.getCodePath());
        param.setSourceFolder(javaConfigVO.getSourceFolder());
        param.setConfigName(TemplateParserEnum.ENTITY.getCodeType());
        param.setPackageName(javaConfigVO.getPackageName());
        param.setUseSwagger(javaConfigVO.isUseSwagger());
        param.setUseMybatisPlus(javaConfigVO.isUseMybatisPlus());
        if (javaConfigVO.getSuperclass() != null) {
            param.setSuperclassName(javaConfigVO.getSuperclass().getName());
        }
        param.setResponseGenericClass(javaConfigVO.getResponseGenericClass());
        return param;
    }
}
