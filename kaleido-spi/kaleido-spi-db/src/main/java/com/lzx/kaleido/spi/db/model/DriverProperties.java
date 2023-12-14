package com.lzx.kaleido.spi.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author lwp
 * @date 2023-11-17
 **/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DriverProperties {
    
    private String jdbcUrl;
    
    /**
     * 驱动包
     */
    private String version;
    
    /**
     *
     */
    private String type;
    
    
    /**
     * jdbcDriverClass
     */
    private String jdbcDriverClass;
    
    /**
     * 下载地址
     */
    private List<String> downloadUrls;
    
    /**
     * 是否为默认驱动
     */
    private boolean defaultDriver;
    
    /**
     * 获取唯一标识
     *
     * @return
     */
    public String getUniqueId() {
        return type + "_" + version;
    }
}
