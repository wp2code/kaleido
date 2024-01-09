package com.lzx.kaleido.spi.db.model;

import com.lzx.kaleido.spi.db.model.metaData.Row;
import com.lzx.kaleido.spi.db.model.metaData.TableColumn;
import lombok.Data;

import java.util.List;

/**
 * @author lwp
 * @date 2023-11-14
 **/
@Data
public class JdbcResultInfo {
    
    private boolean query;
    
    /**
     *
     */
    private List<TableColumn> tableColumnList;
    
    /**
     * 数据
     */
    private List<List<Row>> dataList;
    
    /**
     * 执行数量（非查询语句）
     */
    private Integer updateCount;
}
