package com.lzx.kaleido.domain.core.resolver.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.lzx.kaleido.domain.api.constants.CodeTemplateConstants;
import com.lzx.kaleido.domain.core.enums.TemplateParserEnum;
import com.lzx.kaleido.domain.core.resolver.AbsTemplateParser;
import com.lzx.kaleido.domain.core.utils.TemplateConvertUtil;
import com.lzx.kaleido.domain.model.dto.CodeClassDTO;
import com.lzx.kaleido.domain.model.dto.param.code.CodeGenerationTableParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaServiceConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.common.SuperclassVO;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author lwp
 * @date 2024-01-18
 **/
public class ServiceTemplateParserImpl extends AbsTemplateParser<JavaServiceConfigVO> {
    
    private static final String _SUFFIX = "ServiceImpl";
    
    @Override
    protected void fillCodeGenerationTableParam(final JavaServiceConfigVO config, final BasicConfigVO basicConfig,
            final CodeGenerationTableParam codeGenerationTableParam) {
        TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getUseMybatisPlus(),
                (v) -> codeGenerationTableParam.setUseMybatisPlus(Boolean.parseBoolean(v.toString())), config.isUseMybatisPlus());
        TemplateConvertUtil.setIfAbsent(codeGenerationTableParam.getSuperclassName(),
                (v) -> codeGenerationTableParam.setSuperclassName(String.valueOf(v)),
                Optional.of(config.getSuperclass()).map(SuperclassVO::getName).orElse(null));
    }
    
    @Override
    protected String getCodeName(String name, final String tableName) {
        return StrUtil.isNotBlank(name) ? name : TemplateConvertUtil.underlineToCamelFirstToUpper(tableName) + _SUFFIX;
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
            final CodeGenerationViewVO apiViewVO = refCodeGenerationViewList.stream()
                    .filter(v -> TemplateParserEnum.isServiceApi(v.getCodeType())).findFirst().orElse(null);
            params.put(CodeTemplateConstants.implInterface, apiViewVO.getName());
            packages.add(TemplateConvertUtil.getFullPackageName(apiViewVO));
        }
        params.put(CodeTemplateConstants.packages, packages);
        return params;
    }
}
