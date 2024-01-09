package com.lzx.kaleido.test.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 设备信息表
 *
 * @author 欧冶子
 * @date Created in 2024-01-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "test_demo")
public class TestDemoEntity extends BaseVO {
    
    /**
     * 设备ID
     */
    @TableId(value = "device_id", type = IdType.NONE)
    private String deviceId;
    
    /**
     * 厂家ID
     */
    @TableField(value = "factory_id")
    private Long factoryId;
    
    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
    
}
