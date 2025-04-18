package com.lzx.kaleido.domain.model.vo.container;

import com.lzx.kaleido.domain.model.entity.container.ContainerConfigEntity;
import com.lzx.kaleido.infra.base.pojo.BaseVO;
import io.github.zhaord.mapstruct.plus.annotations.AutoMap;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lwp
 * @date 2025-03-13
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMap(targetType = ContainerConfigEntity.class)
public class ContainerConfigVO extends BaseVO {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     *
     */
    private String name;
    
    /**
     * 地址
     */
    private String path;
    
    /**
     * 类型
     */
    private Integer type;
    
    /**
     * 配置模板
     */
    private String config;
    
    /**
     * 参数
     */
    private String params;
    
    /**
     * 状态【0-停止；1-启动；】
     */
    private Integer status;
    
    /**
     * 备注
     */
    private String remark;
}
