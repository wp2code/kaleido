package com.lzx.kaleido.infra.base.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Json 工具类
 *
 * @author lwp
 * @date 2023-05-10
 **/
@Slf4j
@UtilityClass
public class JsonUtil {
    
    @Setter
    private static ObjectMapper objectMapper;
    
    /**
     * 是否为json
     *
     * @param jsonStr json 字符串
     * @return
     */
    public boolean isJson(String jsonStr) {
        return JSONUtil.isTypeJSON(jsonStr);
    }
    
    /**
     * json转 bean
     *
     * @param jsonStr
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T toBean(String jsonStr, Class<T> cls) {
        if (StrUtil.isNotBlank(jsonStr)) {
            try {
                return getObjectMapperIfAbsent().readValue(jsonStr, cls);
            } catch (Exception e) {
                log.error("json error:" + e.getMessage(), e);
            }
        }
        return null;
    }
    
    /**
     * json 转对象
     *
     * @param jsonStr json字符串
     * @param cls     Class<?>
     * @return
     */
    public Object toObject(String jsonStr, Class<?> cls) {
        if (StrUtil.isNotBlank(jsonStr)) {
            try {
                return getObjectMapperIfAbsent().readValue(jsonStr, cls);
            } catch (Exception e) {
                log.error("json error:" + e.getMessage(), e);
            }
        }
        return null;
    }
    
    /**
     * 对象转 map
     *
     * @param data 参数
     * @return
     */
    public Map<?, ?> toMap(Object data) {
        if (data instanceof Map map) {
            return map;
        }
        if (data instanceof String json) {
            return JsonUtil.toBean(json, Map.class);
        }
        return JsonUtil.toBean(JsonUtil.toJson(data), Map.class);
    }
    
    /**
     * Map 转 bean
     *
     * @param map Map对象
     * @param cls bean Class
     * @param <T> Class<T>
     * @return
     */
    public <T> T mapToBean(Map<?, ?> map, Class<T> cls) {
        return JsonUtil.toBean(JsonUtil.toJson(map), cls);
    }
    
    /**
     * json转 bean
     *
     * @param jsonStr       json字符串
     * @param typeReference TypeReference
     * @param <T>           Class<T>
     * @return
     */
    public <T> T toBean(String jsonStr, TypeReference<T> typeReference) {
        if (StrUtil.isNotBlank(jsonStr)) {
            try {
                return (T) getObjectMapperIfAbsent().readValue(jsonStr, typeReference);
            } catch (Exception e) {
                log.error("json error:" + e.getMessage(), e);
            }
        }
        return null;
    }
    
    /**
     * json转 bean (List)
     *
     * @param jsonStr json字符串
     * @param cls     bean Class
     * @param <T>     Class<T>
     * @return
     */
    public <T> List<T> toBeanList(String jsonStr, Class<T> cls) {
        if (StrUtil.isNotBlank(jsonStr)) {
            try {
                return getObjectMapperIfAbsent().readValue(jsonStr, getCollectionType(List.class, cls));
            } catch (Exception e) {
                log.error("json error:" + e.getMessage(), e);
            }
        }
        return null;
    }
    
    /**
     * 对象转Json
     *
     * @param object 参数对象
     * @return
     */
    public String toJson(Object object) {
        return toJson(object, false);
    }
    
    /**
     * 对象转Json
     *
     * @param object 参数对象
     * @param pretty 是否格式化
     * @return
     */
    public String toJson(Object object, boolean pretty) {
        if (object != null) {
            try {
                if (pretty) {
                    return getObjectMapperIfAbsent().writerWithDefaultPrettyPrinter().writeValueAsString(object);
                } else {
                    return getObjectMapperIfAbsent().writeValueAsString(object);
                }
            } catch (Exception e) {
                log.error("json error:" + e.getMessage());
            }
        }
        return null;
    }
    
    /**
     * 拼接Json字符串
     *
     * @param propertyMap map
     * @param args        参数 格式： key1,value1,key2,value2 ...
     * @return
     */
    public String appendJson(Map<String, Object> propertyMap, Object... args) {
        if (propertyMap != null && !propertyMap.isEmpty()) {
            final List<Object> list = new ArrayList<>(propertyMap.size() * 2);
            for (final Map.Entry<String, Object> entry : propertyMap.entrySet()) {
                list.add(entry.getKey());
                list.add(entry.getValue());
            }
            if (args != null) {
                args = ArrayUtil.addAll(args, list.toArray(new Object[0]));
            } else {
                args = list.toArray(new Object[0]);
            }
        }
        return appendJson(args);
    }
    
    /**
     * 拼接Json字符串
     *
     * @param args 参数 格式： key1,value1,key2,value2 ...
     * @return
     */
    public String appendJson(Object... args) {
        if (args != null && args.length > 0) {
            StringBuilder sb = new StringBuilder("{");
            for (int i = 0; i < args.length; i += 2) {
                String key = (String) args[i];
                sb.append("\"");
                sb.append(key);
                sb.append("\":");
                final Object value = args[i + 1];
                if (value instanceof String || value instanceof Temporal || value instanceof Date) {
                    sb.append("\"");
                    sb.append(value);
                    sb.append("\"");
                } else {
                    sb.append(value);
                }
                if (i != args.length - 2) {
                    sb.append(",");
                }
            }
            sb.append("}");
            return sb.toString();
        }
        return null;
    }
    
    public JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return getObjectMapperIfAbsent().getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
    
    /**
     * @return
     */
    private ObjectMapper getObjectMapperIfAbsent() {
        if (JsonUtil.objectMapper == null) {
            JsonUtil.objectMapper = new ObjectMapper();
        }
        return JsonUtil.objectMapper;
    }
}
