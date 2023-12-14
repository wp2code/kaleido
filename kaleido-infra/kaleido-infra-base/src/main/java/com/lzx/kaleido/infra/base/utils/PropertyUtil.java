package com.lzx.kaleido.infra.base.utils;

import cn.hutool.core.util.ReflectUtil;
import com.lzx.kaleido.infra.base.annotations.PropertyField;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @author lwp
 * @date 2023-12-19
 **/
@UtilityClass
public class PropertyUtil {
    
    /**
     * @param path
     * @return
     */
    public Properties load(String path) {
        try {
            final EncodedResource encodedResource = new EncodedResource(new ClassPathResource(path), "UTF-8");
            return PropertiesLoaderUtils.loadProperties(encodedResource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * @param path
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T loadToBean(String path, Class<T> cls) {
        final Properties properties = load(path);
        final Field[] declaredFields = cls.getDeclaredFields();
        try {
            T newInstance = null;
            for (final Field field : declaredFields) {
                final PropertyField propertyField = field.getAnnotation(PropertyField.class);
                if (propertyField != null) {
                    if (newInstance == null) {
                        newInstance = ReflectUtil.newInstance(cls);
                    }
                    final String key = propertyField.key();
                    final Object value = properties.computeIfPresent(key, (k, v) -> v);
                    ReflectUtil.setFieldValue(newInstance, field, value);
                }
            }
            return newInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
