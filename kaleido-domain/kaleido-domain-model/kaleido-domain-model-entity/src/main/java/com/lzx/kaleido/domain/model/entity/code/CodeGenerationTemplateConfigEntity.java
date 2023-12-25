package com.lzx.kaleido.domain.model.entity.code;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lzx.kaleido.infra.base.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lwp
 * @date 2023-12-09
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("code_generation_template_config")
public class CodeGenerationTemplateConfigEntity extends BaseEntity {
    
    /**
     * 模板配置ID
     */
    @TableId(value = "id")
    private Long id;
    
    /**
     * 模板ID
     */
    @TableField("template_id")
    private Long templateId;
    
    /**
     * 类型
     */
    @TableField("type")
    private String type;
    
    /**
     * 配置参数
     */
    @TableField("config_params")
    private String configParams;
    
    /**
     * 代码模板
     */
    @TableField("template_content")
    private String templateContent;
    
    /**
     * 隐藏状态：0-显示；1-隐藏
     */
    @TableField("hide_status")
    private Integer hideStatus;
}