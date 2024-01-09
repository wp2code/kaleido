package com.lzx.kaleido.domain.core.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lzx.kaleido.domain.api.enums.CodeTemplateDefaultStatus;
import com.lzx.kaleido.domain.api.service.ICodeGenerationTemplateConfigService;
import com.lzx.kaleido.domain.api.service.ICodeGenerationTemplateService;
import com.lzx.kaleido.domain.model.dto.param.code.CodeGenerationTemplateQueryParam;
import com.lzx.kaleido.domain.model.entity.code.CodeGenerationTemplateEntity;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateVO;
import com.lzx.kaleido.domain.repository.mapper.ICodeGenerationTemplateMapper;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.utils.PojoConvertUtil;
import com.lzx.kaleido.plugins.mp.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author lwp
 **/
@Service
public class CodeGenerationTemplateService extends BaseServiceImpl<ICodeGenerationTemplateMapper, CodeGenerationTemplateEntity>
        implements ICodeGenerationTemplateService {
    
    @Resource
    private ICodeGenerationTemplateConfigService codeGenerationTemplateConfigService;
    
    
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
     * 更新代码模板
     *
     * @param id
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(final Long id, final CodeGenerationTemplateVO vo) {
        vo.setId(id);
        final CodeGenerationTemplateEntity entity = PojoConvertUtil.vo2Entity(vo, CodeGenerationTemplateEntity.class);
        if (this.updateById(entity)) {
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
     * 更新模板名称
     *
     * @param id
     * @param templateName
     * @return
     */
    @Override
    public boolean updateTemplateNameById(final Long id, final String templateName) {
        final LambdaUpdateWrapper<CodeGenerationTemplateEntity> updateWrapper = Wrappers.<CodeGenerationTemplateEntity>lambdaUpdate()
                .set(CodeGenerationTemplateEntity::getId, id).set(CodeGenerationTemplateEntity::getTemplateName, templateName);
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
        final LambdaQueryWrapper<CodeGenerationTemplateEntity> queryWrapper = Wrappers.<CodeGenerationTemplateEntity>lambdaQuery()
                .eq(CodeGenerationTemplateEntity::getIsDefault, CodeTemplateDefaultStatus.DEFAULT.getCode())
                .select(CodeGenerationTemplateEntity::getId);
        final List<CodeGenerationTemplateEntity> list = this.list(queryWrapper);
        boolean isDefault = false;
        if (CollUtil.isNotEmpty(list) && (isDefault = list.stream().anyMatch(v -> !Objects.equals(v.getId(), id)))) {
            final LambdaUpdateWrapper<CodeGenerationTemplateEntity> updateWrapper = Wrappers.<CodeGenerationTemplateEntity>lambdaUpdate()
                    .eq(CodeGenerationTemplateEntity::getIsDefault, CodeTemplateDefaultStatus.DEFAULT.getCode())
                    .set(CodeGenerationTemplateEntity::getIsDefault, CodeTemplateDefaultStatus.NORMAL.getCode());
            if (!this.update(updateWrapper)) {
                throw new CommonRuntimeException(ErrorCode.UPDATE_FAILED);
            }
        }
        if (!isDefault) {
            final CodeGenerationTemplateEntity entity = new CodeGenerationTemplateEntity();
            entity.setId(id);
            entity.setIsDefault(CodeTemplateDefaultStatus.DEFAULT.getCode());
            if (!this.updateById(entity)) {
                throw new CommonRuntimeException(ErrorCode.UPDATE_FAILED);
            }
        }
        return true;
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
                        vo.getId(), param.getHideStatus());
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
        final CodeGenerationTemplateEntity entity = this.getById(id);
        if (entity != null) {
            final CodeGenerationTemplateVO vo = PojoConvertUtil.entity2Vo(entity, CodeGenerationTemplateVO.class);
            final List<CodeGenerationTemplateConfigVO> templateConfigList = codeGenerationTemplateConfigService.getByTemplateId(vo.getId(),
                    hideStatus);
            vo.setTemplateConfigList(templateConfigList);
            return vo;
        }
        return null;
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
}
