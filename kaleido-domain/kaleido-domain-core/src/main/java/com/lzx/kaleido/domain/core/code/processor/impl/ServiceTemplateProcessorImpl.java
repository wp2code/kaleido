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
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaServiceConfigVO;
import com.lzx.kaleido.infra.base.pojo.PackageInfo;
import com.lzx.kaleido.infra.base.utils.PackageUtil;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author lwp
 * @date 2024-01-18
 **/
public class ServiceTemplateProcessorImpl extends AbsTemplateProcessor<JavaServiceConfigVO> {
    
    @Override
    protected void fillCodeGenerationTableParam(final JavaServiceConfigVO config, final BasicConfigVO basicConfig,
            final CodeGenerationTableParam codeGenerationTableParam, final CodeGenerationTemplateConfigVO configVO) {
        if (codeGenerationTableParam.isDirectUseTemplateConfig()) {
            codeGenerationTableParam.setUseMybatisPlus(config.isUseMybatisPlus());
            codeGenerationTableParam.setSuperclassName(config.getSuperclass() != null ? config.getSuperclass().getName() : null);
            if (StrUtil.isNotBlank(configVO.getCodePath())) {
                codeGenerationTableParam.setCodePath(configVO.getCodePath());
            }
        } else {
            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getUseMybatisPlus(),
                    (v) -> codeGenerationTableParam.setUseMybatisPlus(Boolean.parseBoolean(v.toString())), config.isUseMybatisPlus());
            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getSuperclassName(),
                    (v) -> codeGenerationTableParam.setSuperclassName(String.valueOf(v)),
                    config.getSuperclass() != null ? config.getSuperclass().getName() : null);
            TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getCodePath(),
                    (v) -> codeGenerationTableParam.setCodePath(v.toString()), configVO.getCodePath());
        }
        
    }
    
    /**
     * @param tableName
     * @return
     */
    @Override
    protected String getTemplateNameIfAbsent(final String tableName) {
        return StrUtil.isNotBlank(tableName) ? tableName : TemplateParserEnum.SERVICE.getDefaultTemplateName();
    }
    
    @Override
    protected CodeGenerationTableParam convertCodeGenerationTableParam(final JavaServiceConfigVO javaConfigVO) {
        final CodeGenerationTableParam param = new CodeGenerationTableParam();
        param.setCodePath(javaConfigVO.getCodePath());
        param.setSourceFolder(javaConfigVO.getSourceFolder());
        param.setConfigName(TemplateParserEnum.SERVICE.getCodeType());
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
        return StrUtil.isNotBlank(name) ? name : TemplateConvertUtil.toCamelFirstToUpper(tableName) + Optional.ofNullable(nameSuffix)
                .orElse(TemplateParserEnum.SERVICE.getDefaultNameSuffix());
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
    protected Map<String, Object> doBuildTemplateParams(final String codeName, final JavaServiceConfigVO javaVoConfig,
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
        String genericsClass = null;
        if (CollUtil.isNotEmpty(refCodeGenerationViewList)) {
            if (codeGenerationTableParam.getUseMybatisPlus() && StrUtil.isNotBlank(superclassName)) {
                final CodeGenerationViewVO mapperViewVO = refCodeGenerationViewList.stream()
                        .filter(v -> TemplateParserEnum.isMapper(v.getCodeType())).findFirst().orElse(null);
                if (mapperViewVO != null) {
                    genericsClass = mapperViewVO.getName();
                    final CodeGenerationViewVO entityViewVO = refCodeGenerationViewList.stream()
                            .filter(v -> TemplateParserEnum.isEntity(v.getCodeType())).findFirst().orElse(null);
                    if (entityViewVO != null) {
                        packages.add(TemplateConvertUtil.getFullPackageName(entityViewVO));
                        genericsClass += ("," + entityViewVO.getName());
                        packages.add(TemplateConvertUtil.getFullPackageName(mapperViewVO));
                        packages.add(TemplateConvertUtil.getFullPackageName(entityViewVO));
                        params.put(CodeTemplateConstants.genericsClass, genericsClass);
                    }
                }
            }
            if (StrUtil.isNotBlank(codeGenerationTableParam.getImplInterfaceName())) {
                final PackageInfo packageInfo = PackageUtil.getPackageInfo(codeGenerationTableParam.getImplInterfaceName());
                packages.add(packageInfo.getName());
                params.put(CodeTemplateConstants.implInterface, packageInfo.getSimpleName());
            } else {
                final CodeGenerationViewVO apiViewVO = refCodeGenerationViewList.stream()
                        .filter(v -> TemplateParserEnum.isServiceApi(v.getCodeType())).findFirst().orElse(null);
                if (apiViewVO != null) {
                    final String fullPackageName = TemplateConvertUtil.getFullPackageName(apiViewVO);
                    codeGenerationTableParam.setImplInterfaceName(fullPackageName);
                    params.put(CodeTemplateConstants.implInterface, apiViewVO.getName());
                    packages.add(fullPackageName);
                }
            }
        }
        params.put(CodeTemplateConstants.packages, packages);
        return params;
    }
}
