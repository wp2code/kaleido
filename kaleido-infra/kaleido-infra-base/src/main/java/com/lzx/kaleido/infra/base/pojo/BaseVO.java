package com.lzx.kaleido.infra.base.pojo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lwp
 * @date 2023-11-18
 **/
@Data
public class BaseVO implements Serializable {
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 创建人
     */
    private String creatorId;
}
