package com.lzx.kaleido.spi.db.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author lwp
 * @date 2023-11-13
 **/
@UtilityClass
public class ResultSetUtil {
    
    private static ObjectMapper mapper;
    
    static {
        mapper = Optional.ofNullable(SpringUtil.getBean(ObjectMapper.class)).orElseGet(() -> {
            final ObjectMapper objectMapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            return objectMapper;
        });
    }
    
    /**
     * @param resultSetMetaData
     * @param column
     * @return
     * @throws SQLException
     */
    public String getColumnName(final ResultSetMetaData resultSetMetaData, int column) throws SQLException {
        String columnLabel = resultSetMetaData.getColumnLabel(column);
        if (columnLabel != null) {
            return columnLabel;
        }
        return resultSetMetaData.getColumnName(column);
    }
    
    /**
     * @param rs
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> toObjectList(ResultSet rs, Class<T> clazz) {
        try {
            if (rs == null || clazz == null) {
                return new ArrayList<>();
            }
            List<T> list = new ArrayList<>();
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int col = rsMetaData.getColumnCount();
            List<String> headerList = getRsHeader(rs);
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i <= col; i++) {
                    map.put(headerList.get(i - 1), rs.getObject(i));
                }
                T obj = mapper.convertValue(map, clazz);
                list.add(obj);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    private List<String> getRsHeader(ResultSet rs) {
        try {
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            int col = resultSetMetaData.getColumnCount();
            List<String> headerList = new ArrayList<>(col);
            for (int i = 1; i <= col; i++) {
                headerList.add(getColumnName(resultSetMetaData, i));
            }
            return headerList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
