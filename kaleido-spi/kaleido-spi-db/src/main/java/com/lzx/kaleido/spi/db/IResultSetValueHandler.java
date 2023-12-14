package com.lzx.kaleido.spi.db;

import com.lzx.kaleido.spi.db.model.metaData.TableColumn;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lwp
 * @date 2023-11-13
 **/
public interface IResultSetValueHandler {
    
    /**
     * @param rs
     * @param index
     * @param limitSize
     * @return
     */
    String getString(ResultSet rs, TableColumn tableColumn, boolean limitSize) throws SQLException;
}
