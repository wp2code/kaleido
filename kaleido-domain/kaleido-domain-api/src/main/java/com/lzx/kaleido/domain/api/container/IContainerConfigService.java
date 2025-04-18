package com.lzx.kaleido.domain.api.container;

import com.lzx.kaleido.domain.model.dto.container.param.ContainerConfigParam;
import com.lzx.kaleido.domain.model.vo.container.ContainerConfigVO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author lwp
 * @date 2025-03-14
 **/
public interface IContainerConfigService {
    
    /**
     * 新增容器配置
     *
     * @param vo
     * @return
     */
    boolean save(ContainerConfigVO vo);
    
    /**
     * 跟新容器配置
     *
     * @param id
     * @param vo
     * @return
     */
    boolean updateById(@NotNull Long id, ContainerConfigVO vo);
    
    /**
     * 查询容器配置
     *
     * @param param
     * @return
     */
    List<ContainerConfigVO> queryByParam(ContainerConfigParam param);
    
    /**
     * 删除容器配置
     *
     * @param id
     * @return
     */
    boolean deleteById(@NotNull final Long id);
    
    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    ContainerConfigVO getDetailById(@NotNull final Long id);
    
    /**
     * 跟新容器状态
     *
     * @param id
     * @param status
     * @return
     */
    boolean updateContainerConfigStatus(Long id, Integer status);
}
