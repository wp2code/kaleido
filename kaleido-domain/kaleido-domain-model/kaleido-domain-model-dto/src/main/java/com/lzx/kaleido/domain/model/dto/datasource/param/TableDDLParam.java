package com.lzx.kaleido.domain.model.dto.datasource.param;

import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author lwp
 * @date 2024-10-19
 **/
@Data
public class TableDDLParam implements Serializable {
    @NotEmpty(message = "连接ID不能为空")
    private String connectionId;
    @NotEmpty(message = "数据库不能为空")
    private String dataBaseName;
    private String schemaName;
    @NotEmpty(message = "表名称不能为空")
    private String tableName;
}
