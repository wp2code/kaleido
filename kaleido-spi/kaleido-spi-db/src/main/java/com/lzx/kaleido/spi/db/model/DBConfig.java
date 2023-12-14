package com.lzx.kaleido.spi.db.model;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author lwp
 * @date 2023-11-16
 **/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DBConfig implements Serializable {
    
    /**
     *
     */
    private String dbType;
    
    /**
     *
     */
    private String name;
    
    
    private boolean supportDatabase;
    
    
    private boolean supportSchema;
    
    /**
     * 连接配置
     */
    private List<DriverProperties> driverPropertiesList;
    
    /**
     *
     */
    private DriverProperties defaultDriverProperties;
    
    /**
     * @param specifyVersion
     * @return
     */
    public DriverProperties getDefaultDriverProperties(String specifyVersion) {
        //优先指定的驱动参数
        if (defaultDriverProperties != null) {
            return defaultDriverProperties;
        }
        if (CollUtil.isNotEmpty(driverPropertiesList)) {
            //优先指定的版本
            if (StrUtil.isNotBlank(specifyVersion)) {
                return driverPropertiesList.stream().filter(v -> specifyVersion.equals(v.getVersion())).findFirst()
                        .orElse(driverPropertiesList.get(0));
            }
            //配置的默认驱动
            return driverPropertiesList.stream().filter(DriverProperties::isDefaultDriver).findFirst().orElse(driverPropertiesList.get(0));
        }
        return null;
    }
}
