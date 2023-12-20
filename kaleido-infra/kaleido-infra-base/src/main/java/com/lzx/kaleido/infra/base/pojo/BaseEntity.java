package com.lzx.kaleido.infra.base.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lwp
 * @date 2023-11-18
 **/
@Data
public class BaseEntity {
    
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorId;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 删除标识
     */
    private Integer deleted;
    
}
