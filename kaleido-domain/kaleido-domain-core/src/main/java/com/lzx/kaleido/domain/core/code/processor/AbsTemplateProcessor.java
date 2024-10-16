package com.lzx.kaleido.domain.core.code.processor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.lzx.kaleido.domain.core.utils.TemplateConvertUtil;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableFieldParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.JavaConfigVO;
import com.lzx.kaleido.domain.model.vo.datasource.TableFieldColumnVO;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import com.lzx.kaleido.plugins.template.enums.ResourceMode;
import com.lzx.kaleido.plugins.template.exception.TemplateParseException;
import com.lzx.kaleido.plugins.template.model.TemplateContext;
import com.lzx.kaleido.plugins.template.utils.CodeGenerationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lwp
 * @date 2024-01-19
 **/
public abstract class AbsTemplateProcessor<T extends JavaConfigVO> implements ITemplateProcessor {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AbsTemplateProcessor.class);
    
    /**
     * 代码配置解析
     *
     * @param codeConfig
     * @return
     */
    @Override
    public final JavaConfigVO parser(final String codeConfig) {
        if (StrUtil.isBlank(codeConfig) || !JsonUtil.isJson(codeConfig)) {
            throw new TemplateParseException(ErrorCode.CODE_TEMPLATE_PARSE_ERROR);
        }
        return doParser(codeConfig);
    }
    
    /**
     * @param javaConfigVO
     * @return
     */
    @Override
    public CodeGenerationTableParam toCodeGenerationTableParam(final JavaConfigVO javaConfigVO) {
        return convertCodeGenerationTableParam((T) javaConfigVO);
    }
    
    protected abstract CodeGenerationTableParam convertCodeGenerationTableParam(T javaConfigVO);
    
    /**
     * @param codeConfig
     * @return
     */
    protected T doParser(final String codeConfig) {
        return JsonUtil.toBean(codeConfig, getTypeArgumentClass());
    }
    
    /**
     * @param config
     * @param basicConfig
     * @param codeGenerationTableParam
     * @param resourceMode
     * @param refCodeGenerationViewList
     * @return
     */
    @Override
    public CodeGenerationViewVO generation(final CodeGenerationTemplateConfigVO config, final BasicConfigVO basicConfig,
            final boolean generateCodeFile, final CodeGenerationTableParam codeGenerationTableParam, final ResourceMode resourceMode,
            final List<CodeGenerationViewVO> refCodeGenerationViewList) {
        try {
            final String templateParams = config.getTemplateParams();
            //转换为模板配置对象
            final T templateConfig = JsonUtil.toBean(templateParams, this.getTypeArgumentClass());
            if (templateConfig == null) {
                throw new CommonRuntimeException(ErrorCode.CODE_TEMPLATE_PARSE_ERROR);
            }
            //设置模板参数
            this.autoFillCodeGenerationTableParam(templateConfig, basicConfig, codeGenerationTableParam, config);
            //代码名称
            String codeName = codeGenerationTableParam.getName();
            if (StrUtil.isBlank(codeName) || codeGenerationTableParam.isDirectUseTemplateConfig()) {
                codeName = this.getCodeName(codeGenerationTableParam.getName(), codeGenerationTableParam.getTableName(),
                        codeGenerationTableParam.getNameSuffix());
            }
            //构建模板参数
            final Map<String, Object> params = this.buildTemplateParams(codeName, templateConfig, basicConfig, codeGenerationTableParam,
                    refCodeGenerationViewList);
            String templateCode = null, codeOutPath = null;
            //生成代码（生成代码文件）
            if (generateCodeFile && codeGenerationTableParam.isGenerationCodeFile()) {
                codeOutPath = CodeGenerationUtil.processTemplate(config.getTemplateContent(), codeGenerationTableParam.getCodePath(),
                        codeName + getCodeFileType(), params, codeGenerationTableParam.getTemplateEngineName(),
                        getTemplateNameIfAbsent(codeGenerationTableParam.getTemplateName()), codeGenerationTableParam.getTemplatePath(),
                        resourceMode);
            }
            //生成代码
            templateCode = CodeGenerationUtil.processTemplateToStr(config.getTemplateContent(), params,
                    codeGenerationTableParam.getTemplateEngineName(), getTemplateNameIfAbsent(codeGenerationTableParam.getTemplateName()),
                    codeGenerationTableParam.getTemplatePath(), resourceMode);
            return CodeGenerationViewVO.builder().name(codeName).packageName(codeGenerationTableParam.getPackageName())
                    .sourceFolder(codeGenerationTableParam.getSourceFolder()).codePath(codeGenerationTableParam.getCodePath())
                    .superclassName(codeGenerationTableParam.getSuperclassName()).codeOutPath(codeOutPath).templateCode(templateCode)
                    .codeType(config.getName()).useLombok(codeGenerationTableParam.getUseLombok())
                    .implInterfaceName(codeGenerationTableParam.getImplInterfaceName()).namespace(codeGenerationTableParam.getNamespace())
                    .useSwagger(codeGenerationTableParam.getUseSwagger()).useMybatisPlus(codeGenerationTableParam.getUseMybatisPlus())
                    .tableFieldColumnMap(convertTableFieldColumnList(codeGenerationTableParam.getTableFieldColumnList(), templateConfig))
                    .build();
        } catch (Exception e) {
            log.error("模板解析{}失败！错误信息：{}", config.getName(), ExceptionUtil.getMessage(e));
            throw new TemplateParseException(ErrorCode.CODE_TEMPLATE_PARSE_ERROR);
        }
    }
    
    /**
     * @return
     */
    protected Class<T> getTypeArgumentClass() {
        return (Class<T>) ClassUtil.getTypeArgument(this.getClass());
    }
    
    /**
     * @param name
     * @param tableName
     * @param nameSuffix
     * @return
     */
    protected String getCodeName(String name, String tableName,String nameSuffix) {
        return StrUtil.isNotBlank(name) ? name : TemplateConvertUtil.toCamelFirstToUpper(tableName);
    }
    
    /**
     * @param tableName
     * @return
     */
    protected abstract String getTemplateNameIfAbsent(String tableName);
    
    /**
     * @return
     */
    protected String getCodeFileType() {
        return ".java";
    }
    
    /**
     * 补全参数信息
     *
     * @param templateParams
     * @param basicConfig
     * @param codeGenerationTableParam
     * @param configVO
     */
    protected void fillCodeGenerationTableParam(T templateParams, BasicConfigVO basicConfig,
            CodeGenerationTableParam codeGenerationTableParam, CodeGenerationTemplateConfigVO configVO) {
        // 子类实现
    }
    
    
    /**
     * 构建模板参数
     *
     * @param codeName
     * @param javaVoConfig
     * @param basicConfig
     * @param codeGenerationTableParam
     * @param refCodeGenerationViewList
     * @return
     */
    protected Map<String, Object> doBuildTemplateParams(final String codeName, final T javaVoConfig, final BasicConfigVO basicConfig,
            final CodeGenerationTableParam codeGenerationTableParam, final List<CodeGenerationViewVO> refCodeGenerationViewList) {
        // 子类实现
        return null;
    }
    
    /**
     * 补全参数信息
     *
     * @param templateParams
     * @param basicConfig
     * @param codeGenerationTableParam
     * @param configVO
     */
    private void autoFillCodeGenerationTableParam(final T templateParams, final BasicConfigVO basicConfig,
            final CodeGenerationTableParam codeGenerationTableParam, CodeGenerationTemplateConfigVO configVO) {
        TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getPackageName(),
                (v) -> codeGenerationTableParam.setPackageName(v.toString()), templateParams.getPackageName());
        TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getSourceFolder(),
                (v) -> codeGenerationTableParam.setSourceFolder(v.toString()), templateParams.getSourceFolder());
        TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getTemplateEngineName(),
                (v) -> codeGenerationTableParam.setTemplateEngineName(v.toString()), TemplateContext.DEFAULT_ENGINE);
        TemplateConvertUtil.setIfAbsent(templateParams.getCodePath(),
                (v) -> templateParams.setCodePath(v.toString()), basicConfig.getCodePath());
        TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getNameSuffix(),
                (v) -> codeGenerationTableParam.setNameSuffix(v.toString()), templateParams.getNameSuffix());
        this.fillCodeGenerationTableParam(templateParams, basicConfig, codeGenerationTableParam, configVO);
    }
    
    
    /**
     * 构建模板参数
     *
     * @param codeName
     * @param javaVoConfig
     * @param basicConfig
     * @param codeGenerationTableParam
     * @param refCodeGenerationViewList
     * @return
     */
    private Map<String, Object> buildTemplateParams(final String codeName, final T javaVoConfig, final BasicConfigVO basicConfig,
            final CodeGenerationTableParam codeGenerationTableParam, final List<CodeGenerationViewVO> refCodeGenerationViewList) {
        final Map<String, Object> params = Maps.newHashMap();
        params.put("author", basicConfig.getAuthor());
        params.put("license", basicConfig.getLicense());
        params.put("createTime", LocalDate.now());
        params.put("name", codeName);
        params.put("packageName", codeGenerationTableParam.getPackageName());
        params.put("comment", codeGenerationTableParam.getTableComment());
        final Map<String, Object> paramsMap = this.doBuildTemplateParams(codeName, javaVoConfig, basicConfig, codeGenerationTableParam,
                refCodeGenerationViewList);
        if (paramsMap != null) {
            params.putAll(paramsMap);
        }
        return params;
    }
    
    /**
     * @param tableFieldColumnMapList
     * @return
     */
    protected List<CodeGenerationTableFieldParam> convertCodeGenerationTableFieldParamList(
            List<TableFieldColumnVO> tableFieldColumnMapList) {
        if (CollUtil.isNotEmpty(tableFieldColumnMapList)) {
            return tableFieldColumnMapList.stream()
                    .map(v -> new CodeGenerationTableFieldParam().setJdbcTypeCode(v.getDataType()).setPrimaryKey(v.getPrimaryKey())
                            .setProperty(v.getProperty()).setJavaType(v.getJavaType()).setColumn(v.getColumn())
                            .setJavaTypeSimpleName(v.getJavaTypeSimpleName())).collect(Collectors.toList());
        }
        return null;
    }
    
    /**
     * @param paramList
     * @return
     */
    protected List<TableFieldColumnVO> convertTableFieldColumnList(List<CodeGenerationTableFieldParam> paramList, T templateConfig) {
        if (CollUtil.isNotEmpty(paramList)) {
            return paramList.stream()
                    .map(v -> new TableFieldColumnVO().setColumn(v.getColumn()).setJdbcType(v.getJdbcType()).setProperty(v.getProperty())
                            .setComment(v.getComment()).setJavaTypeSimpleName(v.getJavaTypeSimpleName()).setPrimaryKey(v.isPrimaryKey())
                            .setSelected(v.isSelected()).setDataType(v.getDataType()).setJavaType(v.getJavaType()))
                    .collect(Collectors.toList());
        }
        return null;
    }
    
}
