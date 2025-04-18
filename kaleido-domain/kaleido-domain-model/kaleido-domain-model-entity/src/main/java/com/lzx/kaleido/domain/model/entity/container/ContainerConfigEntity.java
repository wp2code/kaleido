package com.lzx.kaleido.domain.model.entity.container;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lzx.kaleido.infra.base.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lwp
 * @date 2025-03-13
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("container_config")
public class ContainerConfigEntity extends BaseEntity {
    
    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;
    
    /**
     *
     */
    @TableField(value = "name")
    private String name;
    
    /**
     * 地址
     */
    @TableField(value = "path")
    private String path;
    
    /**
     * 类型
     */
    @TableField(value = "type")
    private Integer type;
    
    /**
     * 配置模板
     */
    @TableField(value = "config")
    private String config;
    
    /**
     * 参数
     */
    @TableField(value = "params")
    private String params;
    
    /**
     * 状态【0-停止；1-启动；】
     */
    @TableField(value = "status")
    private Integer status;
    
    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
}
