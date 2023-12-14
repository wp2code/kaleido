package com.lzx.kaleido.plugins.mapstruct.utils;

import cn.hutool.extra.spring.SpringUtil;
import io.github.zhaord.mapstruct.plus.IObjectMapper;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * 对象转化工具
 *
 * @author lwp
 * @date 2023-11-18
 **/
@UtilityClass
public class MsConvertUtil {
    
    private final static IObjectMapper objectMapper;
    
    static {
        objectMapper = SpringUtil.getBean(IObjectMapper.class);
    }
    
    /**
     * @param data
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T objectMap(Object data, Class<T> cls) {
        return objectMapper.map(data, cls);
    }
    
    /**
     * @param list
     * @param cls
     * @param <T>
     * @return
     */
    public <T> List<T> listMap(List<?> list, Class<T> cls) {
        return objectMapper.mapList(list, cls);
    }
}
