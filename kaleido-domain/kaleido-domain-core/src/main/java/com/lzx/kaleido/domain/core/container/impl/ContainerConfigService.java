package com.lzx.kaleido.domain.core.container.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lzx.kaleido.domain.api.container.IContainerConfigService;
import com.lzx.kaleido.domain.model.dto.container.param.ContainerConfigParam;
import com.lzx.kaleido.domain.model.entity.container.ContainerConfigEntity;
import com.lzx.kaleido.domain.model.vo.container.ContainerConfigVO;
import com.lzx.kaleido.domain.repository.mapper.IContainerConfigMapper;
import com.lzx.kaleido.infra.base.utils.PojoConvertUtil;
import com.lzx.kaleido.plugins.mp.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lwp
 * @date 2025-03-14
 **/
@Service
public class ContainerConfigService extends BaseServiceImpl<IContainerConfigMapper, ContainerConfigEntity>
        implements IContainerConfigService {
    
    
    /**
     * 新增容器配置
     *
     * @param vo
     * @return
     */
    @Override
    public boolean save(final ContainerConfigVO vo) {
        final ContainerConfigEntity entity = PojoConvertUtil.vo2Entity(vo, ContainerConfigEntity.class);
        if (entity != null) {
            this.save(entity);
        }
        return false;
    }
    
    /**
     * 跟新容器配置
     *
     * @param id
     * @param vo
     * @return
     */
    @Override
    public boolean updateById(final Long id, final ContainerConfigVO vo) {
        final LambdaQueryWrapper<ContainerConfigEntity> wrapper = Wrappers.<ContainerConfigEntity>lambdaQuery()
                .eq(ContainerConfigEntity::getId, id);
        final boolean isExists = this.exists(wrapper);
        if (isExists) {
            final LambdaUpdateWrapper<ContainerConfigEntity> updateWrapper = Wrappers.<ContainerConfigEntity>lambdaUpdate()
                    .set(StrUtil.isNotBlank(vo.getName()), ContainerConfigEntity::getName, vo.getName())
                    .set(StrUtil.isNotBlank(vo.getPath()), ContainerConfigEntity::getPath, vo.getPath())
                    .set(StrUtil.isNotBlank(vo.getConfig()), ContainerConfigEntity::getConfig, vo.getConfig())
                    .set(StrUtil.isNotBlank(vo.getRemark()), ContainerConfigEntity::getRemark, vo.getRemark())
                    .set(vo.getType() != null, ContainerConfigEntity::getType, vo.getType()).eq(ContainerConfigEntity::getId, id);
            return this.update(updateWrapper);
        }
        return false;
    }
    
    /**
     * 查询容器配置
     *
     * @param param
     * @return
     */
    @Override
    public List<ContainerConfigVO> queryByParam(final ContainerConfigParam param) {
        final LambdaQueryWrapper<ContainerConfigEntity> wrapper = Wrappers.<ContainerConfigEntity>lambdaQuery()
                .like(StrUtil.isNotBlank(param.getName()), ContainerConfigEntity::getName, param.getName());
        final List<ContainerConfigEntity> entities = this.list(wrapper);
        if (CollUtil.isNotEmpty(entities)) {
            return PojoConvertUtil.entity2VoList(entities, ContainerConfigVO.class);
        }
        return List.of();
    }
    
    /**
     * 删除容器配置
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(final Long id) {
        final LambdaQueryWrapper<ContainerConfigEntity> wrapper = Wrappers.<ContainerConfigEntity>lambdaQuery()
                .eq(ContainerConfigEntity::getId, id);
        return this.remove(wrapper);
    }
    
    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    @Override
    public ContainerConfigVO getDetailById(final Long id) {
        final LambdaQueryWrapper<ContainerConfigEntity> wrapper = Wrappers.<ContainerConfigEntity>lambdaQuery()
                .eq(ContainerConfigEntity::getId, id);
        final ContainerConfigEntity entity = this.getOne(wrapper);
        if (entity != null) {
            return PojoConvertUtil.entity2Vo(entity, ContainerConfigVO.class);
        }
        return null;
    }
    
    /**
     * 跟新容器状态
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public boolean updateContainerConfigStatus(final Long id, final Integer status) {
        return false;
    }
}
