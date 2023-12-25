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
     * 是否为内部分组
     */
    @TableField("is_internal")
    private Integer isInternal;
}