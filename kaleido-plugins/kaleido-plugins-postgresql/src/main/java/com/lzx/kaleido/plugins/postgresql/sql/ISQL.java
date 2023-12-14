package com.lzx.kaleido.plugins.postgresql.sql;

/**
 *
 */
public interface ISQL {
    
    String SELECT_TABLE_INDEX = "SELECT tmp.INDISPRIMARY AS Index_primary, tmp.TABLE_SCHEM, tmp.TABLE_NAME, tmp.NON_UNIQUE, tmp.INDEX_QUALIFIER, tmp.INDEX_NAME AS Key_name, tmp.indisclustered, tmp.ORDINAL_POSITION AS Seq_in_index, TRIM ( BOTH '\"' FROM pg_get_indexdef ( tmp.CI_OID, tmp.ORDINAL_POSITION, FALSE ) ) AS Column_name,CASE  tmp.AM_NAME   WHEN 'btree' THEN CASE   tmp.I_INDOPTION [ tmp.ORDINAL_POSITION - 1 ] & 1 :: SMALLINT   WHEN 1 THEN  'D' ELSE'A'  END ELSE NULL  END AS Collation, tmp.CARDINALITY, tmp.PAGES, tmp.FILTER_CONDITION , tmp.AM_NAME AS Index_method, tmp.DESCRIPTION AS Index_comment FROM ( SELECT  n.nspname AS TABLE_SCHEM,  ct.relname AS TABLE_NAME,  NOT i.indisunique AS NON_UNIQUE, NULL AS INDEX_QUALIFIER,  ci.relname AS INDEX_NAME,i.INDISPRIMARY , i.indisclustered ,  ( information_schema._pg_expandarray ( i.indkey ) ).n AS ORDINAL_POSITION,  ci.reltuples AS CARDINALITY,   ci.relpages AS PAGES,  pg_get_expr ( i.indpred, i.indrelid ) AS FILTER_CONDITION,  ci.OID AS CI_OID, i.indoption AS I_INDOPTION,  am.amname AS AM_NAME , d.description  FROM   pg_class ct   JOIN pg_namespace n ON ( ct.relnamespace = n.OID )   JOIN pg_index i ON ( ct.OID = i.indrelid )   JOIN pg_class ci ON ( ci.OID = i.indexrelid )  JOIN pg_am am ON ( ci.relam = am.OID )      left outer join pg_description d on i.indexrelid = d.objoid  WHERE  n.nspname = '%s'   AND ct.relname = '%s'   ) AS tmp ;";
    
    String SELECT_KEY_INDEX = "SELECT ccu.table_schema AS Foreign_schema_name, ccu.table_name AS Foreign_table_name, ccu.column_name AS Foreign_column_name, constraint_type AS Constraint_type, tc.CONSTRAINT_NAME AS Key_name, tc.TABLE_NAME, kcu.Column_name, tc.is_deferrable, tc.initially_deferred FROM information_schema.table_constraints AS tc JOIN information_schema.key_column_usage AS kcu ON tc.CONSTRAINT_NAME = kcu.CONSTRAINT_NAME JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name WHERE tc.TABLE_SCHEMA = '%s'  AND tc.TABLE_NAME = '%s';";
    
    String FUNCTION_SQL = " CREATE OR REPLACE FUNCTION showcreatetable(namespace character varying, tablename character " + "varying)\n"
            + "        RETURNS character varying AS\n" + "\n" + "        $BODY$\n" + "        declare\n"
            + "        tableScript character varying default '';\n" + "\n" + "        begin\n" + "        -- columns\n"
            + "        tableScript:=tableScript || ' CREATE TABLE '|| tablename|| ' ( '|| chr(13)||chr(10) || " + "array_to_string" + "(\n"
            + "        array(\n" + "        select ' ' || concat_ws(' ',fieldName, fieldType, defaultValue, isNullStr" + " ) as "
            + "column_line\n" + "        from (\n"
            + "        select a.attname as fieldName,format_type(a.atttypid,a.atttypmod) as fieldType," + "        CASE WHEN \n"
            + "                (SELECT substring(pg_catalog.pg_get_expr(B.adbin, B.adrelid) for 128)\n"
            + "                 FROM pg_catalog.pg_attrdef B WHERE B.adrelid = A.attrelid AND B.adnum = A.attnum AND A.atthasdef) IS NOT NULL THEN\n"
            + "                'DEFAULT '|| (SELECT substring(pg_catalog.pg_get_expr(B.adbin, B.adrelid) for 128)\n"
            + "                              FROM pg_catalog.pg_attrdef B WHERE B.adrelid = A.attrelid AND B.adnum = A.attnum AND A.atthasdef)\n"
            + "            ELSE\n" + "                ''\n" + "            END as defaultValue,"
            + "        (case when a.attnotnull=true then 'not null' else 'null' end) as isNullStr\n"
            + "        from pg_attribute a where attstattarget=-1 and attrelid = (select c.oid from pg_class c," + "pg_namespace n"
            + " where\n" + "        c.relnamespace=n.oid and n.nspname =namespace and relname =tablename)\n" + "\n"
            + "        ) as string_columns\n" + "        ),','||chr(13)||chr(10)) || ',';\n" + "\n" + "\n" + "        -- 约束\n"
            + "        tableScript:= tableScript || chr(13)||chr(10) || array_to_string(\n" + "        array(\n"
            + "        select concat(' CONSTRAINT ',conname ,c ,u,p,f) from (\n" + "        select conname,\n"
            + "        case when contype='c' then ' CHECK('|| ( select findattname(namespace,tablename,'c') ) ||')' " + "end " + "as c "
            + ",\n" + "        case when contype='u' then ' UNIQUE('|| ( select findattname(namespace,tablename,'u') ) ||')' " + "end "
            + "as u" + " ,\n" + "        case when contype='p' then ' PRIMARY KEY ('|| ( select findattname(namespace,tablename,'p') ) "
            + "||')' " + "end as p ,\n"
            + "        case when contype='f' then ' FOREIGN KEY('|| ( select findattname(namespace,tablename,'u') ) " + "||') "
            + "REFERENCES '||\n" + "        (select p.relname from pg_class p where p.oid=c.confrelid ) || '('|| ( select\n"
            + "        findattname(namespace,tablename,'u') ) ||')' end as f\n" + "        from pg_constraint c\n"
            + "        where contype in('u','c','f','p') and conrelid=(\n"
            + "        select oid from pg_class where relname=tablename and relnamespace =(\n"
            + "        select oid from pg_namespace where nspname = namespace\n" + "        )\n" + "        )\n" + "        ) as t\n"
            + "        ) ,',' || chr(13)||chr(10) ) || chr(13)||chr(10) ||' ); ';\n" + "\n" + "        -- indexs\n"
            + "        -- CREATE UNIQUE INDEX pg_language_oid_index ON pg_language USING btree (oid); -- table " + "pg_language\n" + "\n"
            + "\n" + "        --\n" + "        /** **/\n" + "        --- 获取非约束索引 column\n"
            + "        -- CREATE UNIQUE INDEX pg_language_oid_index ON pg_language USING btree (oid); -- table " + "pg_language\n"
            + "        tableScript:= tableScript || chr(13)||chr(10) || chr(13)||chr(10) || array_to_string(\n" + "        array(\n"
            + "        select 'CREATE INDEX ' || indexrelname || ' ON ' || tablename || ' USING btree '|| '(' || " + "attname " + "|| "
            + "');' from (\n" + "        SELECT\n" + "        i.relname AS indexrelname , x.indkey,\n" + "\n"
            + "        ( select array_to_string (\n" + "        array(\n"
            + "        select a.attname from pg_attribute a where attrelid=c.oid and a.attnum in ( select unnest(x" + ".indkey) )\n" + "\n"
            + "        )\n" + "        ,',' ) )as attname\n" + "\n" + "        FROM pg_class c\n"
            + "        JOIN pg_index x ON c.oid = x.indrelid\n" + "        JOIN pg_class i ON i.oid = x.indexrelid\n"
            + "        LEFT JOIN pg_namespace n ON n.oid = c.relnamespace\n" + "        WHERE c.relname=tablename and i.relname not in\n"
            + "        ( select constraint_name from information_schema.key_column_usage where table_name=tablename )\n" + "        )as t\n"
            + "        ) ,','|| chr(13)||chr(10));\n" + "\n" + "\n" + "        -- COMMENT COMMENT ON COLUMN sys_activity.id IS '主键';\n"
            + "        tableScript:= tableScript || chr(13)||chr(10) || chr(13)||chr(10) || array_to_string(\n" + "        array(\n"
            + "        SELECT 'COMMENT ON COLUMN ' || 'namespace.tablename' || '.' || a.attname ||' IS '|| ''''|| d.description "
            + "||''''\n" + "        FROM pg_class c\n" + "        JOIN pg_description d ON c.oid=d.objoid\n"
            + "        JOIN pg_attribute a ON c.oid = a.attrelid\n" + "        WHERE c.relname=tablename\n"
            + "        AND a.attnum = d.objsubid),';'|| chr(13)||chr(10)) ;\n" + "\n" + "        return tableScript;\n" + "\n"
            + "        end\n" + "        $BODY$ LANGUAGE plpgsql;\n" + "\n"
            + "        CREATE OR REPLACE FUNCTION findattname(namespace character varying, tablename character " + "varying, " + "ctype"
            + " character\n" + "        varying)\n" + "        RETURNS character varying as $BODY$\n" + "\n" + "        declare\n"
            + "        tt oid ;\n" + "        aname character varying default '';\n" + "\n" + "        begin\n"
            + "        tt := oid from pg_class where relname= tablename and relnamespace =(select oid from " + "pg_namespace " + "where\n"
            + "        nspname=namespace) ;\n" + "        aname:= array_to_string(\n" + "        array(\n"
            + "        select a.attname from pg_attribute a\n" + "        where a.attrelid=tt and a.attnum in (\n"
            + "        select unnest(conkey) from pg_constraint c where contype=ctype\n"
            + "        and conrelid=tt and array_to_string(conkey,',') is not null\n" + "        )\n" + "        ),',');\n" + "\n"
            + "        return aname;\n" + "        end\n" + "        $BODY$ LANGUAGE plpgsql";
}
