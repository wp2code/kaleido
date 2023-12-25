package com.lzx.kaleido.domain.core.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lzx.kaleido.domain.api.enums.CodeTemplateHideStatus;
import com.lzx.kaleido.domain.api.service.ICodeGenerationTemplateConfigService;
import com.lzx.kaleido.domain.model.entity.code.CodeGenerationTemplateConfigEntity;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.repository.mapper.ICodeGenerationTemplateConfigMapper;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.utils.PojoConvertUtil;
import com.lzx.kaleido.plugins.mp.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lwp
 **/
@Service
public class CodeGenerationTemplateConfigService
        extends BaseServiceImpl<ICodeGenerationTemplateConfigMapper, CodeGenerationTemplateConfigEntity>
        implements ICodeGenerationTemplateConfigService {
    
    
    /**
     * 新增代码配置-批量
     *
     * @param voList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCodeGenerationTemplateConfigBatch(final List<CodeGenerationTemplateConfigVO> voList) {
        final List<CodeGenerationTemplateConfigEntity> entities = PojoConvertUtil.vo2EntityList(voList,
                CodeGenerationTemplateConfigEntity.class);
        return this.saveBatch(entities);
    }
    
    /**
     * 更新配置-批量
     *
     * @param voList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateByIdBatch(final List<CodeGenerationTemplateConfigVO> voList) {
        for (final CodeGenerationTemplateConfigVO vo : voList) {
            final CodeGenerationTemplateConfigEntity entity = PojoConvertUtil.vo2Entity(vo, CodeGenerationTemplateConfigEntity.class);
            if (vo.getId() != null) {
                if (!this.updateById(entity)) {
                    throw new CommonRuntimeException(ErrorCode.UPDATE_FAILED);
                }
            } else {
                if (!this.save(entity)) {
                    throw new CommonRuntimeException(ErrorCode.SAVE_FAILED);
                }
            }
        }
        return true;
    }
    
    /**
     * 获取代码配置详情
     *
     * @param id
     * @return
     */
    @Override
    public CodeGenerationTemplateConfigVO getDetailById(final Long id) {
        final CodeGenerationTemplateConfigEntity entity = this.getById(id);
        return PojoConvertUtil.entity2Vo(entity, CodeGenerationTemplateConfigVO.class);
    }
    
    /**
     * 根据模板ID获取配置信息
     *
     * @param templateId
     * @return
     */
    @Override
    public List<CodeGenerationTemplateConfigVO> getByTemplateId(final Long templateId) {
        final LambdaQueryWrapper<CodeGenerationTemplateConfigEntity> wrapper = Wrappers.<CodeGenerationTemplateConfigEntity>lambdaQuery()
                .eq(CodeGenerationTemplateConfigEntity::getTemplateId, templateId)
                .eq(CodeGenerationTemplateConfigEntity::getHideStatus, CodeTemplateHideStatus.SHOW.getCode());
        return PojoConvertUtil.entity2VoList(this.list(wrapper), CodeGenerationTemplateConfigVO.class);
    }
    
    /**
     * 删除代码配置
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(final Long id) {
        final CodeGenerationTemplateConfigEntity detail = this.getById(id);
        if (detail != null && this.removeById(id)) {
            return true;
        }
        throw new CommonRuntimeException(ErrorCode.DELETED_FAILED);
    }
    
    /**
     * @param templateId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByTemplateId(final Long templateId) {
        final LambdaQueryWrapper<CodeGenerationTemplateConfigEntity> wrapper = Wrappers.<CodeGenerationTemplateConfigEntity>lambdaQuery()
                .eq(CodeGenerationTemplateConfigEntity::getTemplateId, templateId);
        final List<CodeGenerationTemplateConfigEntity> entities = this.list(wrapper);
        if (CollUtil.isNotEmpty(entities)) {
            for (final CodeGenerationTemplateConfigEntity entity : entities) {
                if (!this.removeById(entity.getId())) {
                    throw new CommonRuntimeException(ErrorCode.DELETED_FAILED);
                }
            }
        }
        return true;
    }
    
    /**
     * 更新配置隐藏状态
     *
     * @param id
     * @param hideStatus
     * @return
     */
    @Override
    public boolean updateHideStatus(final Long id, final Integer hideStatus) {
        final LambdaUpdateWrapper<CodeGenerationTemplateConfigEntity> updateWrapper = Wrappers.<CodeGenerationTemplateConfigEntity>lambdaUpdate()
                .eq(CodeGenerationTemplateConfigEntity::getId, id).set(CodeGenerationTemplateConfigEntity::getHideStatus, hideStatus);
        return this.update(updateWrapper);
    }
}
