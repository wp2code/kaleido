package com.lzx.kaleido.domain.core.code.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.domain.api.code.ICodeGeneration;
import com.lzx.kaleido.domain.api.code.ICodeGenerationTemplateConfigService;
import com.lzx.kaleido.domain.api.code.ICodeGenerationTemplateService;
import com.lzx.kaleido.domain.api.datasource.IDataSourceService;
import com.lzx.kaleido.domain.api.enums.CodeTemplateHideEnum;
import com.lzx.kaleido.domain.core.code.processor.ITemplateProcessor;
import com.lzx.kaleido.domain.core.enums.TemplateParserEnum;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationAllParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableFieldParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.TableFieldColumnParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationResultVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.JavaConfigVO;
import com.lzx.kaleido.domain.model.vo.datasource.TableFieldColumnVO;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.enums.IBaseEnum;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.utils.EasyEnumUtil;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import com.lzx.kaleido.plugins.template.enums.ResourceMode;
import com.lzx.kaleido.plugins.template.exception.TemplateParseException;
import com.lzx.kaleido.plugins.template.model.TemplateContext;
import com.lzx.kaleido.plugins.template.utils.CodeGenerationUtil;
import com.lzx.kaleido.spi.db.enums.DataType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.OutputStream;
import java.util.ArrayList;
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
    
    @Resource
    private ICodeGenerationTemplateService codeGenerationTemplateService;
    
    @Resource
    private IDataSourceService dataSourceService;
    
    /**
     * 代码预览-根据模板Id
     *
     * @param templateId
     * @param generationTableParam
     * @return
     */
    @Deprecated
    @Override
    public CodeGenerationResultVO preview(final Long templateId, final CodeGenerationTableParam generationTableParam) {
        final CodeGenerationTemplateVO templateVO = codeGenerationTemplateService.getDetailById(templateId,
                CodeTemplateHideEnum.SHOW.getCode());
        if (templateVO != null) {
            final BasicConfigVO basicConfig = templateVO.toBasicConfig();
            final List<CodeGenerationTemplateConfigVO> templateConfigList = templateVO.getTemplateConfigList();
            final List<CodeGenerationViewVO> codeGenerationViewVOS = new ArrayList<>();
            for (final CodeGenerationTemplateConfigVO configVO : templateConfigList) {
                final TemplateParserEnum instance = TemplateParserEnum.getInstance(configVO.getName());
                if (instance != null) {
                    final ITemplateProcessor templateParser = instance.getTemplateParser();
                    final JavaConfigVO javaConfigVO = templateParser.parser(configVO.getTemplateContent());
                    templateParser.toCodeGenerationTableParam(javaConfigVO);
                    CodeGenerationViewVO codeGenerationViewVO = instance.getTemplateParser()
                            .generation(configVO, basicConfig, false, generationTableParam,
                                    ResourceMode.getInstance(generationTableParam.getTemplateResourceMode()), codeGenerationViewVOS);
                    if (codeGenerationViewVO != null) {
                        codeGenerationViewVOS.add(codeGenerationViewVO);
                    }
                }
            }
            return new CodeGenerationResultVO(templateVO, codeGenerationViewVOS);
        }
        return null;
    }
    
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
            final BasicConfigVO basicConfig = templateVO.toBasicConfig();
            final List<CodeGenerationTemplateConfigVO> templateConfigList = templateVO.getTemplateConfigList();
            List<CodeGenerationViewVO> codeGenerationViewVOS = new ArrayList<>();
            codeGenerationList.sort((o1, o2) -> {
                final TemplateParserEnum instance1 = TemplateParserEnum.getInstance(o1.getConfigName());
                final TemplateParserEnum instance2 = TemplateParserEnum.getInstance(o2.getConfigName());
                return instance1.getPriority() - instance2.getPriority();
            });
            for (final CodeGenerationTableParam tableParam : codeGenerationList) {
                final CodeGenerationTemplateConfigVO configVO = templateConfigList.stream()
                        .filter(v -> v.getName().equals(tableParam.getConfigName())).findFirst().orElse(null);
                if (configVO != null) {
                    final TemplateParserEnum parserEnum = TemplateParserEnum.getInstance(configVO.getName());
                    if (parserEnum != null) {
                        if (CollUtil.isEmpty(tableParam.getTableFieldColumnList()) && (TemplateParserEnum.isEntity(parserEnum.getCodeType())
                                || TemplateParserEnum.isVo(parserEnum.getCodeType()) || TemplateParserEnum.isXml(
                                parserEnum.getCodeType()))) {
                            final List<TableFieldColumnVO> tableFieldColumnList = dataSourceService.getTableFieldColumnList(
                                    TableFieldColumnParam.builder().connectionId(codeGenerationTableParam.getConnectionId())
                                            .schemaName(tableParam.getSchemaName()).tableName(tableParam.getTableName())
                                            .dataBaseName(tableParam.getDataBaseName()).build());
                            tableParam.setTableFieldColumnList(convertCodeGenerationTableFieldParamList(tableFieldColumnList));
                        }
                        final CodeGenerationViewVO codeGenerationViewVO = parserEnum.getTemplateParser()
                                .generation(configVO, basicConfig, !isPreview, tableParam,
                                        ResourceMode.getInstance(tableParam.getTemplateResourceMode()), codeGenerationViewVOS);
                        if (codeGenerationViewVO != null) {
                            System.out.println(codeGenerationViewVO.getTemplateCode());
                            codeGenerationViewVOS.add(codeGenerationViewVO);
                        }
                    }
                }
            }
            final List<String> responseTemplateCodeList = codeGenerationTableParam.getResponseTemplateCodeList();
            if (CollUtil.isNotEmpty(responseTemplateCodeList)) {
                codeGenerationViewVOS = codeGenerationViewVOS.stream().filter(v -> responseTemplateCodeList.contains(v.getCodeType()))
                        .collect(Collectors.toList());
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
    
    
    /**
     * @param tableFieldColumnList
     * @return
     */
    private List<CodeGenerationTableFieldParam> convertCodeGenerationTableFieldParamList(List<TableFieldColumnVO> tableFieldColumnList) {
        if (CollUtil.isNotEmpty(tableFieldColumnList)) {
            return tableFieldColumnList.stream().map(v -> {
                final IBaseEnum<Integer> dataTypeEnum = EasyEnumUtil.getEnumByCode(DataType.class, v.getDataType());
                return new CodeGenerationTableFieldParam().setDataType(v.getDataType()).setColumn(v.getColumn()).setComment(v.getComment())
                        .setJdbcType(v.getJdbcType()).setJavaType(v.getJavaType()).setJavaTypeSimpleName(v.getJavaTypeSimpleName())
                        .setPrimaryKey(v.getPrimaryKey()).setProperty(v.getProperty())
                        .setXmlJdbcType(dataTypeEnum != null ? dataTypeEnum.getName() : v.getJdbcType()).setJdbcTypeCode(v.getDataType());
            }).collect(Collectors.toList());
        }
        return null;
    }
}
