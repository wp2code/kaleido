package com.lzx.kaleido.plugins.mysql.sql;

/**
 * @author lwp
 * @date 2023-11-19
 **/
public interface ISQL {
    
    /**
     *
     */
    String SELECT_TABLE_COLUMNS = "SELECT * FROM information_schema.COLUMNS  WHERE TABLE_SCHEMA =  '%s'  AND TABLE_NAME =  '%s'  order by ORDINAL_POSITION";

}
