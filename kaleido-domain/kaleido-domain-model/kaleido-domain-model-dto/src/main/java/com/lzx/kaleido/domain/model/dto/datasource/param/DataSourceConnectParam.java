package com.lzx.kaleido.domain.model.dto.datasource.param;

import lombok.Data;

/**
 * @author lwp
 * @date 2023-11-18
 **/
@Data
public class DataSourceConnectParam {
    
    private String name;
    
    private String type;
    
    private String url;
    
    private Integer port;
    
    private String userName;
    
    private String password;
    
    private String dbName;
    
    private String extend;
    
}
