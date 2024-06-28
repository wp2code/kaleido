package com.lzx.kaleido.domain.model.vo.datasource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author lwp
 * @date 2024-02-18
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TableFieldColumnVO implements Serializable {
    private String comment;
    private String column;
    private String jdbcType;
    private String property;
    private String javaType;
    private String javaTypeSimpleName;
    private Boolean primaryKey;
    private Integer dataType;
    private boolean selected=true;
}
