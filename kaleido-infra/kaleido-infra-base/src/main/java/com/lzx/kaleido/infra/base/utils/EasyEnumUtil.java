package com.lzx.kaleido.infra.base.utils;

import com.lzx.kaleido.infra.base.enums.IBaseEnum;
import com.lzx.kaleido.infra.base.model.EnumBean;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * @author lwp
 * @date 2023-11-16
 **/
@UtilityClass
public class EasyEnumUtil {
    
    /**
     * 枚举缓存
     */
    private static final Map<IBaseEnum<?>, EnumBean<?>> ENUM_BEAN_MAP = new ConcurrentHashMap<>();
    
    /**
     * @param IBaseEnum
     * @param code
     * @param name
     * @param <T>
     */
    public <T> void putEnum(IBaseEnum<T> IBaseEnum, T code, String name) {
        ENUM_BEAN_MAP.put(IBaseEnum, new EnumBean<>(code, name));
    }
    
    /**
     * @param clazz
     * @param code
     * @param <T>
     * @param <E>
     * @return
     */
    public <T, E extends IBaseEnum<T>> E getEnumByCode(Class<? extends IBaseEnum<T>> clazz, T code) {
        return Stream.of(clazz.getEnumConstants()).filter(baseEnum -> baseEnum.getCode().equals(code)).map(v -> (E) v).findAny()
                .orElse(null);
    }
    
    /**
     * @param key
     * @param <E>
     * @param <T>
     * @return
     */
    public <E extends IBaseEnum<T>, T> EnumBean<T> getEnum(E key) {
        final EnumBean<?> enumBean = ENUM_BEAN_MAP.get(key);
        if (enumBean != null) {
            return (EnumBean<T>) enumBean;
        }
        return null;
    }
}
