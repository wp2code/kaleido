package com.lzx.kaleido.domain.model.entity.datasource;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lzx.kaleido.infra.base.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lwp
 * @date 2023-11-17
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_source")
public class DataSourceEntity extends BaseEntity {
    @TableId(value = "id")
    private Long id;
    
    @TableField(value = "name")
    private String name;
    
    @TableField(value = "type")
    private String type;
    
    @TableField(value = "icon")
    private String icon;
    
    @TableField(value = "url")
    private String url;
    
    @TableField(value = "port")
    private Integer port;
    
    @TableField(value = "user_name")
    private String userName;
    
    @TableField(value = "password")
    private String password;
    
    @TableField(value = "db_name")
    private String dbName;
    
    @TableField(value = "extend")
    private String extend;
}
