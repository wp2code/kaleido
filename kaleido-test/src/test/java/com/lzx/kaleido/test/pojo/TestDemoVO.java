package com.lzx.kaleido.test.pojo;

import java.time.LocalDateTime;

/**
 * 设备信息表
 *
 * @author 欧冶子
 * @date Created in 2024-01-08
 */
public class TestDemoVO extends BaseVO {
    
    /**
     * 设备ID
     */
    private String deviceId;
    
    /**
     * 厂家ID
     */
    private Long factoryId;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    public String getDeviceId() {
        return deviceId;
    }
    
    public void setDeviceId(final String deviceId) {
        this.deviceId = deviceId;
    }
    
    public Long getFactoryId() {
        return factoryId;
    }
    
    public void setFactoryId(final Long factoryId) {
        this.factoryId = factoryId;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(final LocalDateTime createTime) {
        this.createTime = createTime;
    }
}