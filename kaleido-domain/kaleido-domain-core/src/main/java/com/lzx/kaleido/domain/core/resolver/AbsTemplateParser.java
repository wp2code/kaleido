package com.lzx.kaleido.domain.core.resolver;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.lzx.kaleido.domain.core.utils.TemplateConvertUtil;
import com.lzx.kaleido.domain.model.dto.param.code.CodeGenerationTableParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.JavaConfigVO;
import com.lzx.kaleido.plugins.template.enums.ResourceMode;
import com.lzx.kaleido.plugins.template.model.TemplateContext;
import com.lzx.kaleido.plugins.template.utils.CodeGenerationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author lwp
 * @date 2024-01-19
 **/
public abstract class AbsTemplateParser<T extends JavaConfigVO> implements ITemplateParser {
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AbsTemplateParser.class);
    
    
    /**
     * @param config
     * @param basicConfig
     * @param codeGenerationTableParam
     * @param resourceMode
     * @param refCodeGenerationViewList
     * @return
     */
    @Override
    public CodeGenerationViewVO parser(final CodeGenerationTemplateConfigVO config, final BasicConfigVO basicConfig,
            final boolean generateCodeFile, final CodeGenerationTableParam codeGenerationTableParam, final ResourceMode resourceMode,
            final List<CodeGenerationViewVO> refCodeGenerationViewList) {
        try {
            final JavaConfigVO templateParams = config.getTemplateParams();
            final T templateConfig = (T) templateParams;
            //设置模板参数
            this.autoFillCodeGenerationTableParam(templateConfig, basicConfig, codeGenerationTableParam);
            //代码名称
            final String codeName = this.getCodeName(codeGenerationTableParam.getName(), codeGenerationTableParam.getTableName());
            //构建模板参数
            final Map<String, Object> params = this.buildTemplateParams(codeName, templateConfig, basicConfig, codeGenerationTableParam,
                    refCodeGenerationViewList);
            String templateCode = null, codeOutPath = null;
            //生成代码（生成代码文件）
            if (generateCodeFile) {
                codeOutPath = CodeGenerationUtil.processTemplate(config.getTemplateContent(), codeGenerationTableParam.getCodePath(),
                        codeName + getCodeFileType(), params, codeGenerationTableParam.getTemplateEngineName(),
                        codeGenerationTableParam.getTemplateName(), codeGenerationTableParam.getTemplatePath(), resourceMode);
            } else {
                //生成代码
                templateCode = CodeGenerationUtil.processTemplateToStr(config.getTemplateContent(), params,
                        codeGenerationTableParam.getTemplateEngineName(), codeGenerationTableParam.getTemplateName(),
                        codeGenerationTableParam.getTemplatePath(), resourceMode);
            }
            return CodeGenerationViewVO.builder().name(codeName).packageName(codeGenerationTableParam.getPackageName())
                    .sourceFolder(codeGenerationTableParam.getSourceFolder()).codePath(codeGenerationTableParam.getCodePath())
                    .superclassName(codeGenerationTableParam.getSuperclassName()).codeOutPath(codeOutPath).templateCode(templateCode)
                    .useLombok(codeGenerationTableParam.getUseLombok()).useSwagger(codeGenerationTableParam.getUseSwagger())
                    .useMybatisPlus(codeGenerationTableParam.getUseMybatisPlus()).build();
        } catch (Exception e) {
            log.error("解析{}失败！错误信息：{}", config.getName(), ExceptionUtil.getMessage(e));
        }
        return null;
    }
    
    /**
     * @param name
     * @param tableName
     * @return
     */
    protected String getCodeName(String name, String tableName) {
        return StrUtil.isNotBlank(name) ? name : TemplateConvertUtil.underlineToCamelFirstToUpper(tableName);
    }
    
    /**
     * @return
     */
    protected String getCodeFileType() {
        return ".java";
    }
    
    protected void fillCodeGenerationTableParam(T templateParams, BasicConfigVO basicConfig,
            CodeGenerationTableParam codeGenerationTableParam) {
    }
    
    
    /**
     * @param codeName
     * @param javaVoConfig
     * @param basicConfig
     * @param codeGenerationTableParam
     * @param refCodeGenerationViewList
     * @return
     */
    protected Map<String, Object> doBuildTemplateParams(final String codeName, final T javaVoConfig, final BasicConfigVO basicConfig,
            final CodeGenerationTableParam codeGenerationTableParam, final List<CodeGenerationViewVO> refCodeGenerationViewList) {
        return null;
    }
    
    /**
     * 补全参数信息
     *
     * @param templateParams
     * @param basicConfig
     * @param codeGenerationTableParam
     */
    private void autoFillCodeGenerationTableParam(final T templateParams, final BasicConfigVO basicConfig,
            final CodeGenerationTableParam codeGenerationTableParam) {
        TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getCodePath(), (v) -> codeGenerationTableParam.setCodePath(v.toString()),
                templateParams.getCodePath(), basicConfig.getCodePath());
        TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getPackageName(),
                (v) -> codeGenerationTableParam.setPackageName(v.toString()), templateParams.getPackageName());
        TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getSourceFolder(),
                (v) -> codeGenerationTableParam.setSourceFolder(v.toString()), templateParams.getSourceFolder());
        TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getTemplateEngineName(),
                (v) -> codeGenerationTableParam.setTemplateEngineName(v.toString()), TemplateContext.DEFAULT_ENGINE);
        this.fillCodeGenerationTableParam(templateParams, basicConfig, codeGenerationTableParam);
    }
    
    
    /**
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
        params.put("comment", codeGenerationTableParam.getComment());
        final Map<String, Object> paramsMap = this.doBuildTemplateParams(codeName, javaVoConfig, basicConfig, codeGenerationTableParam,
                refCodeGenerationViewList);
        if (paramsMap != null) {
            params.putAll(paramsMap);
        }
        return params;
    }
    
}
