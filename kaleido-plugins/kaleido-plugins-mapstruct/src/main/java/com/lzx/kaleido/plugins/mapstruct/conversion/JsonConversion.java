package com.lzx.kaleido.plugins.mapstruct.conversion;

import cn.hutool.json.JSONUtil;
import com.lzx.kaleido.plugins.mapstruct.annotations.json.BeanToJson;
import com.lzx.kaleido.plugins.mapstruct.annotations.json.JsonToBean;

/**
 * @author lwp
 * @date 2023-12-09
 **/
public interface JsonConversion<T> {
    
    /**
     * @param json
     * @param
     * @return
     */
    @JsonToBean
    T jsonToBean(String json);
    
    @BeanToJson
    default String beanToJson(Object data) {
        if (data == null) {
            return null;
        }
        try {
            return JSONUtil.toJsonStr(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
