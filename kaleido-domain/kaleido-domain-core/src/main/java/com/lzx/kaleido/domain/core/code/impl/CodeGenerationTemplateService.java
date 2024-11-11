package com.lzx.kaleido.domain.core.code.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lzx.kaleido.domain.api.code.ICodeGenerationTemplateConfigService;
import com.lzx.kaleido.domain.api.code.ICodeGenerationTemplateService;
import com.lzx.kaleido.domain.api.enums.CodeTemplateDefaultEnum;
import com.lzx.kaleido.domain.api.enums.CodeTemplateHideEnum;
import com.lzx.kaleido.domain.api.enums.CodeTemplateInternalEnum;
import com.lzx.kaleido.domain.api.enums.CodeTemplateSourceTypeEnum;
import com.lzx.kaleido.domain.core.enums.TemplateParserEnum;
import com.lzx.kaleido.domain.core.utils.TemplateConvertUtil;
import com.lzx.kaleido.domain.model.dto.code.param.ApplyTemplateParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationGlobalConfigParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationSimpleParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTemplateQueryParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTemplateUpdateParam;
import com.lzx.kaleido.domain.model.entity.code.CodeGenerationTemplateEntity;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateFileConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateFileVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateViewConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.JavaConfigVO;
import com.lzx.kaleido.domain.repository.mapper.ICodeGenerationTemplateMapper;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import com.lzx.kaleido.infra.base.utils.PojoConvertUtil;
import com.lzx.kaleido.plugins.mp.BaseServiceImpl;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lwp
 **/
@Service
public class CodeGenerationTemplateService extends BaseServiceImpl<ICodeGenerationTemplateMapper, CodeGenerationTemplateEntity>
        implements ICodeGenerationTemplateService {
    
    @Resource
    private ICodeGenerationTemplateConfigService codeGenerationTemplateConfigService;
    
    
    /**
     * 新增标准的代码模板
     *
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addStandardCodeGenerationTemplate(final CodeGenerationTemplateViewVO vo) {
        final CodeGenerationTemplateEntity entity = new CodeGenerationTemplateEntity();
        entity.setTemplateName(vo.getTemplateName());
        entity.setLanguage(vo.getLanguage());
        entity.setIsDefault(CodeTemplateDefaultEnum.NORMAL.getCode());
        entity.setIsInternal(CodeTemplateInternalEnum.N.getCode());
        entity.setBasicConfig(JsonUtil.toJson(vo.getBasicConfig()));
        final List<CodeGenerationTemplateViewConfigVO> codeConfigList = vo.getCodeConfigList();
        if (this.save(entity)) {
            final Long templateId = entity.getId();
            final List<CodeGenerationTemplateConfigVO> configList = codeConfigList.stream().map(v -> {
                final String name = v.getName();
                final TemplateParserEnum templateParserEnum = TemplateParserEnum.getInstance(name);
                if (templateParserEnum != null) {
                    final JavaConfigVO javaConfig = templateParserEnum.getTemplateParser().parser(v.getConfig());
                    javaConfig.setCodePath(v.getCodePath());
                    return javaConfig.swapper(templateId, null);
                }
                return null;
            }).filter(Objects::nonNull).toList();
            if (codeGenerationTemplateConfigService.addCodeGenerationTemplateConfigBatch(configList)) {
                return templateId;
            }
        }
        throw new CommonRuntimeException(ErrorCode.SAVE_FAILED);
    }
    
    /**
     * 新增导入的代码模板
     *
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addImportCodeGenerationTemplate(final CodeGenerationTemplateFileVO vo) {
        vo.fill((basicConfig) -> {
            if (StrUtil.isBlank(basicConfig)) {
                CodeGenerationTemplateVO defaultCodeGenerationTemplate = getDefaultCodeGenerationTemplate();
                if (defaultCodeGenerationTemplate != null) {
                    return defaultCodeGenerationTemplate.getBasicConfig();
                }
            }
            return basicConfig;
        });
        if (!vo.validate((name) -> TemplateParserEnum.getInstance(name) != null)) {
            throw new CommonRuntimeException(ErrorCode.CODE_TEMPLATE_CONFIG_ERROR);
        }
        final BasicConfigVO basicConfigVO = JsonUtil.toBean(vo.getBasicConfig(), BasicConfigVO.class);
        if (basicConfigVO == null) {
            throw new CommonRuntimeException(ErrorCode.CODE_TEMPLATE_PARSE_ERROR);
        }
        if (basicConfigVO.isFtmLicense()) {
            vo.setBasicConfig(JsonUtil.toJson(basicConfigVO));
        }
        final CodeGenerationTemplateEntity entity = new CodeGenerationTemplateEntity();
        entity.setTemplateName(vo.getTemplateName());
        entity.setLanguage(vo.getLanguage());
        entity.setIsDefault(CodeTemplateDefaultEnum.NORMAL.getCode());
        entity.setIsInternal(CodeTemplateInternalEnum.N.getCode());
        entity.setBasicConfig(vo.getBasicConfig());
        entity.setSourceType(CodeTemplateSourceTypeEnum.IMPORT_ADD.getCode());
        if (vo.getSourceTemplateId() != null) {
            entity.setSource(String.valueOf(vo.getSourceTemplateId()));
        }
        if (checkTemplateName(null, vo.getTemplateName())) {
            throw new CommonRuntimeException(ErrorCode.CODE_TEMPLATE_NAME_EXISTS);
        }
        if (this.save(entity)) {
            final Long templateId = entity.getId();
            final List<CodeGenerationTemplateFileConfigVO> codeConfigList = vo.getCodeConfigList();
            final List<CodeGenerationTemplateConfigVO> configList = codeConfigList.stream().map(v -> {
                final CodeGenerationTemplateConfigVO configVO = new CodeGenerationTemplateConfigVO();
                configVO.setTemplateId(templateId);
                configVO.setCodePath(v.getCodePath());
                configVO.setHideStatus(CodeTemplateHideEnum.SHOW.getCode());
                configVO.setName(v.getName());
                configVO.setAlias(v.getAlias());
                configVO.setTemplateParams(v.getTemplateParams());
                configVO.setTemplateContent(v.getTemplateContent());
                return configVO;
            }).collect(Collectors.toList());
            if (codeGenerationTemplateConfigService.addCodeGenerationTemplateConfigBatch(configList)) {
                return templateId;
            }
        }
        throw new CommonRuntimeException(ErrorCode.SAVE_FAILED);
    }
    
    /**
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addTemplateWithCopy(final Long id, final String templateName) {
        final CodeGenerationTemplateVO template = getCodeGenerationTemplate(id, null, null);
        if (template == null) {
            throw new CommonRuntimeException(ErrorCode.SAVE_FAILED);
        }
        if (checkTemplateName(null, templateName)) {
            throw new CommonRuntimeException(ErrorCode.CODE_TEMPLATE_NAME_EXISTS);
        }
        LocalDateTime now = LocalDateTime.now();
        final CodeGenerationTemplateEntity entity = PojoConvertUtil.vo2Entity(template, CodeGenerationTemplateEntity.class);
        entity.setId(null);
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        entity.setIsDefault(CodeTemplateDefaultEnum.NORMAL.getCode());
        entity.setIsInternal(CodeTemplateInternalEnum.N.getCode());
        entity.setSourceType(CodeTemplateSourceTypeEnum.COPY_ADD.getCode());
        entity.setSource(String.valueOf(id));
        entity.setTemplateName(templateName);
        if (this.save(entity)) {
            final List<CodeGenerationTemplateConfigVO> templateConfigList = template.getTemplateConfigList();
            templateConfigList.forEach(v -> {
                v.setId(null);
                v.setTemplateId(entity.getId());
            });
            if (!codeGenerationTemplateConfigService.addCodeGenerationTemplateConfigBatch(templateConfigList)) {
                throw new CommonRuntimeException(ErrorCode.SAVE_FAILED);
            }
            return entity.getId();
        }
        throw new CommonRuntimeException(ErrorCode.SAVE_FAILED);
    }
    
    /**
     * 新增代码模板
     *
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addCodeGenerationTemplate(final CodeGenerationTemplateVO vo) {
        final CodeGenerationTemplateEntity entity = PojoConvertUtil.vo2Entity(vo, CodeGenerationTemplateEntity.class);
        if (!this.exists(Wrappers.<CodeGenerationTemplateEntity>lambdaQuery()
                .eq(CodeGenerationTemplateEntity::getTemplateName, vo.getTemplateName()))) {
            final List<CodeGenerationTemplateConfigVO> templateConfigList = vo.getTemplateConfigList();
            if (this.save(entity)) {
                final Long id = entity.getId();
                if (CollUtil.isNotEmpty(templateConfigList)) {
                    templateConfigList.forEach(v -> v.setTemplateId(id));
                    codeGenerationTemplateConfigService.addCodeGenerationTemplateConfigBatch(templateConfigList);
                }
                return id;
            }
        }
        throw new CommonRuntimeException(ErrorCode.SAVE_FAILED);
    }
    
    /**
     * 校验模板名称
     *
     * @param templateId
     * @param templateName
     * @return
     */
    @Override
    public boolean checkTemplateName(final Long templateId, final String templateName) {
        final LambdaQueryWrapper<CodeGenerationTemplateEntity> wrapper = Wrappers.<CodeGenerationTemplateEntity>lambdaQuery()
                .eq(CodeGenerationTemplateEntity::getTemplateName, templateName)
                .ne(templateId != null, CodeGenerationTemplateEntity::getId, templateId);
        return this.getBaseMapper().exists(wrapper);
    }
    
    /**
     * 校验默认模板是否存在
     *
     * @return
     */
    @Override
    public boolean checkInitDefaultTemplate() {
        final LambdaQueryWrapper<CodeGenerationTemplateEntity> wrapper = Wrappers.<CodeGenerationTemplateEntity>lambdaQuery()
                .eq(CodeGenerationTemplateEntity::getSourceType, CodeTemplateSourceTypeEnum.INIT_ADD.getCode())
                .eq(CodeGenerationTemplateEntity::getIsInternal, CodeTemplateInternalEnum.Y.getCode());
        return this.getBaseMapper().exists(wrapper);
    }
    
    /**
     * 更新全局基本配置
     *
     * @param param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGlobalConfig(final CodeGenerationGlobalConfigParam param) {
        if (param == null) {
            return false;
        }
        final Map<Long, List<String>> applyTemplateParamMap =
                CollUtil.isNotEmpty(param.getApplyTemplateList()) ? param.getApplyTemplateList().stream()
                        .collect(Collectors.toMap(ApplyTemplateParam::getTemplateId, ApplyTemplateParam::getCodeTypeList, (k1, k2) -> k1))
                        : new HashMap<>();
        final LambdaQueryWrapper<CodeGenerationTemplateEntity> wrapper = Wrappers.<CodeGenerationTemplateEntity>lambdaQuery()
                .in(CollUtil.isNotEmpty(applyTemplateParamMap.keySet()), CodeGenerationTemplateEntity::getId,
                        applyTemplateParamMap.keySet());
        final List<CodeGenerationTemplateEntity> list = this.list(wrapper);
        if (CollUtil.isNotEmpty(list)) {
            for (final CodeGenerationTemplateEntity entity : list) {
                final BasicConfigVO dbBasicConfig = TemplateConvertUtil.toBasicConfig(entity.getBasicConfig());
                if (!StrUtil.equals(dbBasicConfig.getCodePath(), param.getCodePath())) {
                    final List<String> codeTypeList = applyTemplateParamMap.get(entity.getId());
                    //统一修改代码地址
                    if (!codeGenerationTemplateConfigService.updateCodePathByTemplateId(entity.getId(), param.getCodePath(),
                            codeTypeList)) {
                        throw new CommonRuntimeException(ErrorCode.UPDATE_FAILED);
                    }
                }
                if (!StrUtil.equals(entity.toString(), param.toString())) {
                    final BasicConfigVO vo = new BasicConfigVO(param.getAuthor(), param.getCodePath(), param.getLicense());
                    entity.setBasicConfig(JsonUtil.toJson(vo));
                    if (!this.updateById(entity)) {
                        throw new CommonRuntimeException(ErrorCode.UPDATE_FAILED);
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * 更新代码模板
     *
     * @param id
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(final Long id, final CodeGenerationTemplateVO vo) {
        final CodeGenerationTemplateEntity dbEntity = getById(id);
        if (dbEntity == null) {
            return false;
        }
        if (StrUtil.isNotBlank(vo.getTemplateName())) {
            dbEntity.setTemplateName(vo.getTemplateName());
        }
        if (this.updateById(dbEntity)) {
            if (CollUtil.isNotEmpty(vo.getTemplateConfigList())) {
                vo.getTemplateConfigList().forEach(v -> v.setTemplateId(id));
                if (codeGenerationTemplateConfigService.updateByIdBatch(vo.getTemplateConfigList())) {
                    return true;
                }
            } else {
                return true;
            }
        }
        throw new CommonRuntimeException(ErrorCode.UPDATE_FAILED);
    }
    
    /**
     * 更新部分代码模板
     *
     * @param param
     * @return
     */
    @Override
    public boolean updateCodeGenerationTemplateOfPartition(final CodeGenerationTemplateUpdateParam param) {
        final CodeGenerationTemplateEntity dbEntity = getById(param.getTemplateId());
        if (dbEntity == null) {
            return false;
        }
        return codeGenerationTemplateConfigService.updateCodeGenerationTemplateConfig(param.getTemplateId(), param);
    }
    
    /**
     * 更新模板名称
     *
     * @param id
     * @param templateName
     * @return
     */
    @Override
    public boolean updateTemplateNameById(final Long id, final String templateName) {
        if (checkTemplateName(id, templateName)) {
            throw new CommonRuntimeException(ErrorCode.CODE_TEMPLATE_NAME_EXISTS);
        }
        final LambdaUpdateWrapper<CodeGenerationTemplateEntity> updateWrapper = Wrappers.<CodeGenerationTemplateEntity>lambdaUpdate()
                .eq(CodeGenerationTemplateEntity::getId, id).set(CodeGenerationTemplateEntity::getTemplateName, templateName);
        return this.update(updateWrapper);
    }
    
    /**
     * 更新为默认模板
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDefaultTemplate(final Long id) {
        LambdaUpdateWrapper<CodeGenerationTemplateEntity> updateWrapper = Wrappers.<CodeGenerationTemplateEntity>lambdaUpdate()
                .set(CodeGenerationTemplateEntity::getIsDefault, CodeTemplateDefaultEnum.DEFAULT.getCode())
                .eq(CodeGenerationTemplateEntity::getId, id);
        if (this.update(updateWrapper)) {
            LambdaUpdateWrapper<CodeGenerationTemplateEntity> updateWrapper2 = Wrappers.<CodeGenerationTemplateEntity>lambdaUpdate()
                    .set(CodeGenerationTemplateEntity::getIsDefault, CodeTemplateDefaultEnum.NORMAL.getCode())
                    .ne(CodeGenerationTemplateEntity::getId, id);
            if (this.update(updateWrapper2)) {
                return true;
            }
        }
        throw new CommonRuntimeException(ErrorCode.UPDATE_FAILED);
    }
    
    /**
     * 查询模板列表
     *
     * @param param
     * @return
     */
    @Override
    public List<CodeGenerationTemplateVO> queryByParam(final CodeGenerationTemplateQueryParam param) {
        final LambdaQueryWrapper<CodeGenerationTemplateEntity> queryWrapper = Wrappers.<CodeGenerationTemplateEntity>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getTemplateName()), CodeGenerationTemplateEntity::getTemplateName, param.getTemplateName())
                .eq(CodeGenerationTemplateEntity::getLanguage, param.getLanguage()).exists(param.getHideStatus() != null,
                        "SELECT id FROM code_generation_template_config WHERE template_id=code_generation_template.id "
                                + "AND  hide_status = {0}", param.getHideStatus()).orderByDesc(CodeGenerationTemplateEntity::getIsInternal);
        final List<CodeGenerationTemplateEntity> entities = this.getBaseMapper().selectList(queryWrapper);
        if (CollUtil.isNotEmpty(entities)) {
            final List<CodeGenerationTemplateVO> voList = PojoConvertUtil.entity2VoList(entities, CodeGenerationTemplateVO.class);
            for (final CodeGenerationTemplateVO vo : voList) {
                final List<CodeGenerationTemplateConfigVO> templateConfigList = codeGenerationTemplateConfigService.getByTemplateId(
                        vo.getId(), param.getHideStatus(), null);
                vo.setTemplateConfigList(templateConfigList);
            }
            return voList;
        }
        return null;
    }
    
    /**
     * 获取代码模板详情
     *
     * @param id
     * @param hideStatus
     * @return
     */
    @Override
    public CodeGenerationTemplateVO getDetailById(final Long id, final Integer hideStatus) {
        return getCodeGenerationTemplate(id, hideStatus, null);
    }
    
    /**
     * @param param
     * @return
     */
    @Override
    public CodeGenerationTemplateVO getCodeGenerationTemplate(final CodeGenerationSimpleParam param) {
        return getCodeGenerationTemplate(param.getTemplateId(), CodeTemplateHideEnum.SHOW.getCode(), param.getNameList());
    }
    
    /**
     * 获取默认模板
     *
     * @return
     */
    @Override
    public CodeGenerationTemplateVO getDefaultCodeGenerationTemplate() {
        return getBaseMapper().getDefaultCodeGenerationTemplate();
    }
    
    /**
     * 删除代码模板
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(final Long id) {
        final CodeGenerationTemplateEntity entity = this.getById(id);
        if (entity != null) {
            if (this.removeById(entity.getId()) && codeGenerationTemplateConfigService.deleteByTemplateId(entity.getId())) {
                return true;
            }
        }
        throw new CommonRuntimeException(ErrorCode.DELETED_FAILED);
    }
    
    /**
     * 获取导出模板信息
     *
     * @param templateId
     * @return
     */
    @Override
    public CodeGenerationTemplateFileVO getCodeGenerationTemplateFile(final Long templateId) {
        return this.getBaseMapper().getCodeGenerationTemplateFile(templateId);
    }
    
    /**
     * @param id
     * @param hideStatus
     * @param nameList
     * @return
     */
    private CodeGenerationTemplateVO getCodeGenerationTemplate(final Long id, final Integer hideStatus, List<String> nameList) {
        final CodeGenerationTemplateEntity entity = this.getById(id);
        if (entity != null) {
            final CodeGenerationTemplateVO vo = PojoConvertUtil.entity2Vo(entity, CodeGenerationTemplateVO.class);
            final List<CodeGenerationTemplateConfigVO> templateConfigList = codeGenerationTemplateConfigService.getByTemplateId(vo.getId(),
                    hideStatus, nameList);
            vo.setTemplateConfigList(templateConfigList);
            return vo;
        }
        return null;
    }
}
