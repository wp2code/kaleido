package com.lzx.kaleido.spi.db.template;

import com.lzx.kaleido.spi.db.model.TableColumnJavaMap;

import java.util.List;

/**
 * @author lwp
 * @date 2024-01-16
 **/
public interface IGenerationTemplate {
    
    /**
     * @param sqlId
     * @param tableName
     * @param tableColumnJavaMaps
     * @return
     */
    String insert(final String sqlId, final String tableName, final List<TableColumnJavaMap> tableColumnJavaMaps);
}
