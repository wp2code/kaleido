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
    
    private LocalDateTime updateTime;
    
    private LocalDateTime createTime;
}
