package com.lzx.kaleido.domain.core.code.processor.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.lzx.kaleido.domain.api.constants.CodeTemplateConstants;
import com.lzx.kaleido.domain.core.code.processor.AbsTemplateProcessor;
import com.lzx.kaleido.domain.core.datasource.DataSourceFactory;
import com.lzx.kaleido.domain.core.enums.ApiTemplateEnum;
import com.lzx.kaleido.domain.core.enums.TemplateParserEnum;
import com.lzx.kaleido.domain.core.utils.TemplateConvertUtil;
import com.lzx.kaleido.domain.model.dto.code.CodeApiDTO;
import com.lzx.kaleido.domain.model.dto.code.CodeClassDTO;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableFieldParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaXmlConfigVO;
import com.lzx.kaleido.spi.db.constants.DBConstant;
import com.lzx.kaleido.spi.db.model.ConnectionWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author lwp
 * @date 2024-01-18
 **/
public class XmlTemplateProcessorImpl extends AbsTemplateProcessor<JavaXmlConfigVO> {
    
    /**
     * @param config
     * @param basicConfig
     * @param codeGenerationTableParam
     */
    @Override
    protected void fillCodeGenerationTableParam(final JavaXmlConfigVO config, final BasicConfigVO basicConfig,
            final CodeGenerationTableParam codeGenerationTableParam, final CodeGenerationTemplateConfigVO configVO) {
        if (codeGenerationTableParam.isDirectUseTemplateConfig()) {
            codeGenerationTableParam.setPackageName(config.getPackageName());
            codeGenerationTableParam.setSourceFolder(config.getSourceFolder());
            codeGenerationTableParam.setUseMybatisPlus(config.isUseMybatisPlus());
            if (StrUtil.isNotBlank(configVO.getCodePath())) {
                codeGenerationTableParam.setCodePath(configVO.getCodePath());
            }
            if (!config.isUseMybatisPlus()) {
                codeGenerationTableParam.setMethodList(
                        CollUtil.isEmpty(config.getMethodList()) ? ApiTemplateEnum.getAllApi() : config.getMethodList());
            }
        } else {
            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getUseMybatisPlus(),
                    (v) -> codeGenerationTableParam.setUseMybatisPlus(Boolean.parseBoolean(v.toString())), config.isUseMybatisPlus());
            //            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getMethodList(),
            //                    (v) -> codeGenerationTableParam.setMethodList((List<String>) v), config.getMethodList(),
            //                    !codeGenerationTableParam.getUseMybatisPlus() ? ApiTemplateEnum.getAllApi() : null);
            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getCodePath(),
                    (v) -> codeGenerationTableParam.setCodePath(v.toString()), config.getCodePath());
        }
    }
    
    protected String getCodeFileType() {
        return "." + TemplateParserEnum.XML.getFileSuffix();
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
    
    /**
     * @param name
     * @param tableName
     * @param nameSuffix
     * @return
     */
    @Override
    protected String getCodeName(String name, final String tableName, String nameSuffix) {
        return StrUtil.isNotBlank(name) ? name : TemplateConvertUtil.toCamelFirstToUpper(tableName) + Optional.ofNullable(nameSuffix)
                .orElse(TemplateParserEnum.XML.getDefaultNameSuffix());
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
            if (CollUtil.isEmpty(codeGenerationTableParam.getTableFieldColumnList()) && entityViewVO != null) {
                codeGenerationTableParam.setTableFieldColumnList(
                        convertCodeGenerationTableFieldParamList(entityViewVO.getTableFieldColumnMap()));
            }
        }
        final String namespace = StrUtil.isNotBlank(codeGenerationTableParam.getNamespace()) ? codeGenerationTableParam.getNamespace()
                : TemplateConvertUtil.getFullPackageName(mapperViewVO);
        if (StrUtil.isBlank(codeGenerationTableParam.getNamespace())) {
            codeGenerationTableParam.setNamespace(namespace);
        }
        String connectionId = codeGenerationTableParam.getConnectionId();
        String dbType = null;
        ConnectionWrapper activeConnection = DataSourceFactory.getInstance().getActiveConnection(connectionId);
        if (activeConnection != null) {
            dbType = activeConnection.getDbType();
        }
        params.put(CodeTemplateConstants.dbType, Optional.ofNullable(dbType).orElse(DBConstant.MYSQL_DB_TYPE));
        final String schemaName = codeGenerationTableParam.getSchemaName();
        params.put(CodeTemplateConstants.tableName,
                StrUtil.isNotBlank(schemaName) ? schemaName + "." + codeGenerationTableParam.getTableName()
                        : codeGenerationTableParam.getTableName());
        params.put(CodeTemplateConstants.namespace, codeGenerationTableParam.getNamespace());
        params.put(CodeTemplateConstants.resultMapType, TemplateConvertUtil.getFullPackageName(entityViewVO));
        final List<CodeGenerationTableFieldParam> tableFieldColumnList = codeGenerationTableParam.getTableFieldColumnList();
        params.put(CodeTemplateConstants.tableFieldColumnList, tableFieldColumnList);
        params.put(CodeTemplateConstants.tablePK, codeGenerationTableParam.getPrimaryKeyField());
        final List<String> apiList = codeGenerationTableParam.getMethodList();
        if (CollUtil.isNotEmpty(apiList) && (codeGenerationTableParam.getUseMybatisPlus() == null || Boolean.FALSE.equals(
                codeGenerationTableParam.getUseMybatisPlus()))) {
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
