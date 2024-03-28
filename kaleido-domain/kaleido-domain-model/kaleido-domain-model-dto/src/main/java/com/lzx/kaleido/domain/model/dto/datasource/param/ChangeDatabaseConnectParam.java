package com.lzx.kaleido.domain.model.dto.datasource.param;

import lombok.Data;

/**
 * @author lwp
 * @date 2024-03-11
 **/
@Data
public class ChangeDatabaseConnectParam {
    
    private String dataBaseName;
    
    /**
     * 原始
     */
    private String connectionId;
    
    /**
     * 0-开启，1-关闭
     */
    private Integer status;
}
