package com.lzx.kaleido.domain.model.dto.param.datasource;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lwp
 * @date 2023-11-18
 **/
@Data
public class DataSourceQueryParam implements Serializable {
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 类型
     */
    private String type;
}
