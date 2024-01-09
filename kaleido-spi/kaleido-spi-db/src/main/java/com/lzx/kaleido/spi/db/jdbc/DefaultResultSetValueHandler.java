package com.lzx.kaleido.spi.db.jdbc;

import cn.hutool.core.io.unit.DataSizeUtil;
import com.lzx.kaleido.infra.base.utils.I18nUtil;
import com.lzx.kaleido.spi.db.IResultSetValueHandler;
import com.lzx.kaleido.spi.db.model.metaData.TableColumn;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lwp
 * @date 2023-11-14
 **/
@Slf4j
public class DefaultResultSetValueHandler implements IResultSetValueHandler {
    
    private static final long MAX_RESULT_SIZE = 256 * 1024;
    
    /**
     * @param rs
     * @param limitSize
     * @return
     */
    @Override
    public String getString(final ResultSet rs, final TableColumn tableColumn, final boolean limitSize) throws SQLException {
        Object obj = rs.getObject(tableColumn.getColumnIndex());
        if (obj == null) {
            return null;
        }
        try {
            if (obj instanceof BigDecimal bigDecimal) {
                return bigDecimal.toPlainString();
            } else if (obj instanceof Double d) {
                return BigDecimal.valueOf(d).toPlainString();
            } else if (obj instanceof Float f) {
                return BigDecimal.valueOf(f).toPlainString();
            } else if (obj instanceof Clob) {
                return largeString(rs, tableColumn.getColumnIndex(), limitSize);
            } else if (obj instanceof byte[]) {
                return largeString(rs, tableColumn.getColumnIndex(), limitSize);
            } else if (obj instanceof Blob blob) {
                return largeStringBlob(blob, limitSize);
            }
            return rs.getString(tableColumn.getColumnIndex());
        } catch (Exception e) {
            log.warn("解析数失败:{},{}", tableColumn.getColumnIndex(), obj, e);
            return obj.toString();
        }
    }
    
    /**
     * @param blob
     * @param limitSize
     * @return
     * @throws SQLException
     */
    private String largeStringBlob(Blob blob, boolean limitSize) throws SQLException {
        if (blob == null) {
            return null;
        }
        int length = Math.toIntExact(blob.length());
        if (limitSize && length > MAX_RESULT_SIZE) {
            length = Math.toIntExact(MAX_RESULT_SIZE);
        }
        byte[] data = blob.getBytes(1, length);
        String result = new String(data);
        
        if (length > MAX_RESULT_SIZE) {
            return "[ " + DataSizeUtil.format(MAX_RESULT_SIZE) + " of " + DataSizeUtil.format(length) + " ," + I18nUtil.getMessage(
                    "execute.exportCsv") + " ] " + result;
        }
        return result;
    }
    
    private static String largeString(ResultSet rs, int index, boolean limitSize) throws SQLException {
        String result = rs.getString(index);
        if (result == null) {
            return null;
            
        }
        if (!limitSize) {
            return result;
        }
        
        if (result.length() > MAX_RESULT_SIZE) {
            return "[ " + DataSizeUtil.format(MAX_RESULT_SIZE) + " of " + DataSizeUtil.format(result.length()) + " ," + I18nUtil.getMessage(
                    "execute.exportCsv") + " ] " + result.substring(0, Math.toIntExact(MAX_RESULT_SIZE));
        }
        return result;
    }
}
