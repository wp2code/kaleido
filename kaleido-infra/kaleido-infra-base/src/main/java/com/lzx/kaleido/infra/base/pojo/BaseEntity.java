package com.lzx.kaleido.infra.base.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author lwp
 * @date 2023-11-18
 **/
@Data
public class BaseEntity {
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 删除标识
     */
    private Integer deleted;
}
