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
@TableName("code_generation_template")
public class CodeGenerationTemplateEntity extends BaseEntity {
    
    /**
     * 分组ID
     */
    @TableId(value = "id")
    private Long id;
    
    /**
     * 分组名称
     */
    @TableField("template_name")
    private String templateName;
    
    /**
     * 编程语言
     */
    @TableField("language")
    private String language;
    
    /**
     * 模板基本配置
     */
    @TableField("basic_config")
    private String basicConfig;
    
    /**
     * 是否为内部模板 （0-不是；1-是）
     */
    @TableField("is_internal")
    private Integer isInternal;
    
    /**
     * 是否为默认模板（0-不是；1-是）
     */
    @TableField("is_default")
    private Integer isDefault;
    
}
