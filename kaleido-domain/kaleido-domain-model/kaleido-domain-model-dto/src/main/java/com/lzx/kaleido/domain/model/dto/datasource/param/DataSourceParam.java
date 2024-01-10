package com.lzx.kaleido.domain.model.dto.datasource.param;

import com.lzx.kaleido.domain.model.entity.datasource.DataSourceEntity;
import io.github.zhaord.mapstruct.plus.annotations.AutoMap;
import lombok.Data;

/**
 * @author lwp
 * @date 2023-11-18
 **/
@Data
@AutoMap(targetType = DataSourceEntity.class)
public class DataSourceParam {
    
    private String name;
    
    private String type;
    
    private String icon;
    
    private String url;
    
    private Integer port;
    
    private String userName;
    
    private String password;
    
    private String dbName;
    
    private String extend;
    
    public String getDefaultName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(type);
        sb.append("@");
        sb.append(url);
        return sb.toString();
    }
}
