package com.lzx.kaleido.spi.db.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lwp
 * @date 2023-11-18
 **/
@FunctionalInterface
public interface ResultSetFunction<R> {
    
    /**
     * @param t
     * @return
     * @throws SQLException
     */
    R apply(ResultSet t) throws SQLException;
}
