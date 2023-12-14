package com.lzx.kaleido.infra.base.utils;

import com.lzx.kaleido.infra.base.pojo.BaseEntity;
import com.lzx.kaleido.infra.base.pojo.BaseVO;
import com.lzx.kaleido.plugins.mapstruct.utils.MsConvertUtil;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * 对象转化工具
 *
 * @author lwp
 * @date 2023-11-18
 **/
@UtilityClass
public class PojoConvertUtil {
    
    /**
     * @param source
     * @param cls
     * @param <T>
     * @param <E>
     * @return
     */
    public <T extends BaseEntity, E extends BaseVO> E entity2Vo(T source, Class<E> cls) {
        return objectMap(source, cls);
    }
    
    /**
     * @param source
     * @param cls
     * @param <T>
     * @param <E>
     * @return
     */
    public <T extends BaseEntity, E extends BaseVO> List<E> entity2VoList(List<T> source, Class<E> cls) {
        return listMap(source, cls);
    }
    
    /**
     * @param source
     * @param cls
     * @param <T>
     * @param <E>
     * @return
     */
    public <T extends BaseEntity, E extends BaseVO> T vo2Entity(E source, Class<T> cls) {
        return objectMap(source, cls);
    }
    
    /**
     * @param source
     * @param cls
     * @param <T>
     * @param <E>
     * @return
     */
    public <T extends BaseEntity, E extends BaseVO> List<T> vo2EntityList(List<E> source, Class<T> cls) {
        return listMap(source, cls);
    }
    
    /**
     * @param source
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T objectMap(Object source, Class<T> cls) {
        return MsConvertUtil.objectMap(source, cls);
    }
    
    /**
     * @param source
     * @param cls
     * @param <T>
     * @return
     */
    public <T> List<T> listMap(List<?> source, Class<T> cls) {
        return MsConvertUtil.listMap(source, cls);
    }
}
