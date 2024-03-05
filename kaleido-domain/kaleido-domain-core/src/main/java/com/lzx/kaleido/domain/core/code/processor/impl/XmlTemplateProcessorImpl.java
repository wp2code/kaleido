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
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableFieldParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaXmlConfigVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author lwp
 * @date 2024-01-18
 **/
public class XmlTemplateProcessorImpl extends AbsTemplateProcessor<JavaXmlConfigVO> {
    
    private static final String _SUFFIX = "Mapper";
    
    /**
     * @param config
     * @param basicConfig
     * @param codeGenerationTableParam
     */
    @Override
    protected void fillCodeGenerationTableParam(final JavaXmlConfigVO config, BasicConfigVO basicConfig,
            CodeGenerationTableParam codeGenerationTableParam) {
        TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getMethodList(),
                (v) -> codeGenerationTableParam.setMethodList((List<String>) v), config.getMethodList());
    }
    
    protected String getCodeFileType() {
        return ".xml";
    }
    
    @Override
    protected CodeGenerationTableParam convertCodeGenerationTableParam(final JavaXmlConfigVO javaConfigVO) {
        final CodeGenerationTableParam param = new CodeGenerationTableParam();
        param.setCodePath(javaConfigVO.getCodePath());
        param.setSourceFolder(javaConfigVO.getSourceFolder());
        param.setConfigName(TemplateParserEnum.XML.getCodeType());
        param.setPackageName(javaConfigVO.getPackageName());
        return param;
    }
    
    @Override
    protected String getCodeName(String name, final String tableName) {
        return StrUtil.isNotBlank(name) ? name : TemplateConvertUtil.underlineToCamelFirstToUpper(tableName) + _SUFFIX;
    }
    
    /**
     * @param tableName
     * @return
     */
    @Override
    protected String getTemplateNameIfAbsent(final String tableName) {
        return StrUtil.isNotBlank(tableName) ? tableName : TemplateParserEnum.XML.getDefaultTemplateName();
    }
    
    /**
     * @param codeName
     * @param javaVoConfig
     * @param basicConfig
     * @param codeGenerationTableParam
     * @return
     */
    @Override
    protected Map<String, Object> doBuildTemplateParams(final String codeName, final JavaXmlConfigVO javaVoConfig,
            final BasicConfigVO basicConfig, final CodeGenerationTableParam codeGenerationTableParam,
            final List<CodeGenerationViewVO> refCodeGenerationViewList) {
        final Map<String, Object> params = Maps.newHashMap();
        CodeGenerationViewVO entityViewVO = null;
        CodeGenerationViewVO mapperViewVO = null;
        if (CollUtil.isNotEmpty(refCodeGenerationViewList)) {
            entityViewVO = refCodeGenerationViewList.stream().filter(v -> TemplateParserEnum.isEntity(v.getCodeType())).findFirst()
                    .orElse(null);
            mapperViewVO = refCodeGenerationViewList.stream().filter(v -> TemplateParserEnum.isMapper(v.getCodeType())).findFirst()
                    .orElse(null);
        }
        final String schemaName = codeGenerationTableParam.getSchemaName();
        params.put(CodeTemplateConstants.tableName,
                StrUtil.isNotBlank(schemaName) ? schemaName + "." + codeGenerationTableParam.getTableName()
                        : codeGenerationTableParam.getTableName());
        params.put(CodeTemplateConstants.namespace, TemplateConvertUtil.getFullPackageName(mapperViewVO));
        params.put(CodeTemplateConstants.resultMapType, TemplateConvertUtil.getFullPackageName(entityViewVO));
        final List<CodeGenerationTableFieldParam> tableFieldColumnList = codeGenerationTableParam.getTableFieldColumnList();
        params.put(CodeTemplateConstants.tableFieldColumnList, tableFieldColumnList);
        params.put(CodeTemplateConstants.tablePK, codeGenerationTableParam.getPrimaryKeyField());
        final List<String> apiList = codeGenerationTableParam.getMethodList();
        if (CollUtil.isNotEmpty(apiList)) {
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
                            return javaTypeParam.getName();
                        }).orElse("Object");
                    }
                    apiParamList.add(CodeApiDTO.of(apiTemplateEnum.getApiId(), returnType, apiTemplateEnum.isReturnList(), parameterType,
                            apiTemplateEnum.isParameterList(), apiTemplateEnum.getDescribe()));
                }
            }
            params.put(CodeTemplateConstants.apiList, apiParamList);
        }
        return params;
    }
    
}
