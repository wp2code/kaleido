package com.lzx.kaleido.domain.model.vo.datasource;

import com.lzx.kaleido.domain.model.entity.datasource.DataSourceEntity;
import com.lzx.kaleido.infra.base.pojo.BaseVO;
import io.github.zhaord.mapstruct.plus.annotations.AutoMap;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lwp
 * @date 2023-11-18
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMap(targetType = DataSourceEntity.class)
public class DataSourceVO extends BaseVO {
    
    private Long id;
    
    private String name;
    
    private String type;
    
    private String icon;
    
    private String url;
    
    private Integer port;
    
    private String userName;
    
    private String password;
    
    private String dbName;
    
    private String extend;

}
