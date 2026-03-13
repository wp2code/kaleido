package com.lzx.kaleido.plugins.dm.sql;

/**
 * 达梦数据库 SQL 常量
 *
 * @author lwp
 * @date 2024-03-11
 **/
public interface ISQL {

    /**
     * 查询指定 Schema 下的表列表（排除系统表）
     */
    String SELECT_TABLES =
            "SELECT TABLE_NAME, COMMENTS FROM DBA_TAB_COMMENTS WHERE OWNER = '%s' AND TABLE_TYPE = 'TABLE' ORDER BY TABLE_NAME";

    /**
     * 查询表列信息
     */
    String SELECT_TABLE_COLUMNS =
            "SELECT COL.COLUMN_NAME, COL.DATA_TYPE, COL.DATA_LENGTH, COL.DATA_PRECISION, COL.DATA_SCALE, "
                    + "COL.NULLABLE, COL.DATA_DEFAULT, COL.COLUMN_ID, CMT.COMMENTS "
                    + "FROM DBA_TAB_COLUMNS COL "
                    + "LEFT JOIN DBA_COL_COMMENTS CMT "
                    + "ON COL.OWNER=CMT.OWNER AND COL.TABLE_NAME=CMT.TABLE_NAME AND COL.COLUMN_NAME=CMT.COLUMN_NAME "
                    + "WHERE COL.OWNER='%s' AND COL.TABLE_NAME='%s' ORDER BY COL.COLUMN_ID";

    /**
     * 查询主键列
     */
    String SELECT_PRIMARY_KEY =
            "SELECT CC.COLUMN_NAME FROM DBA_CONSTRAINTS C "
                    + "JOIN DBA_CONS_COLUMNS CC ON C.CONSTRAINT_NAME=CC.CONSTRAINT_NAME AND C.OWNER=CC.OWNER "
                    + "WHERE C.OWNER='%s' AND C.TABLE_NAME='%s' AND C.CONSTRAINT_TYPE='P' ORDER BY CC.POSITION";

    /**
     * 查询索引（含约束类型）
     */
    String SELECT_TABLE_INDEXES =
            "SELECT I.INDEX_NAME, I.UNIQUENESS, IC.COLUMN_NAME, IC.COLUMN_POSITION, IC.DESCEND, C.CONSTRAINT_TYPE "
                    + "FROM DBA_INDEXES I "
                    + "JOIN DBA_IND_COLUMNS IC ON I.INDEX_NAME=IC.INDEX_NAME AND I.OWNER=IC.INDEX_OWNER "
                    + "LEFT JOIN DBA_CONSTRAINTS C ON I.INDEX_NAME=C.INDEX_NAME AND C.OWNER=I.OWNER "
                    + "WHERE I.OWNER='%s' AND I.TABLE_NAME='%s' ORDER BY I.INDEX_NAME, IC.COLUMN_POSITION";
}
