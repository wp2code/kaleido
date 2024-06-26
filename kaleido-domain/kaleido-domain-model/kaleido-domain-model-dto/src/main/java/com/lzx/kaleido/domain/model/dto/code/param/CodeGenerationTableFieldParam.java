package com.lzx.kaleido.domain.model.dto.code.param;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lwp
 * @date 2024-01-10
 **/
@Data
@Accessors(chain = true)
public class CodeGenerationTableFieldParam implements Serializable {
    
    /**
     * 字段注释
     */
    private String comment;
    
    /**
     * 数据库字段
     */
    private String column;
    
    /**
     * 数据库类型
     */
    private String jdbcType;
    
    /**
     * 类型code
     */
    private Integer jdbcTypeCode;
    
    /**
     * mybatis 类型
     */
    private String xmlJdbcType;
    
    /**
     * 字段属性
     */
    private String property;
    
    /**
     * 属性类型
     */
    private String javaType;
    
    private String javaTypeSimpleName;
    
    /**
     * 是否为主键
     */
    private boolean primaryKey;
    
    /**
     *
     */
    private transient Integer dataType;
    
    /**
     * 是否选择（前端渲染）
     */
    private boolean selected;
    
    /**
     * @return
     */
    public String getJavaTypeSimpleName() {
        if (StrUtil.isNotBlank(javaTypeSimpleName)) {
            return javaTypeSimpleName;
        }
        if (javaType != null && javaType.contains(".")) {
            return this.javaTypeSimpleName = javaType.substring(javaType.lastIndexOf(".") + 1);
        }
        return javaType;
    }
}
