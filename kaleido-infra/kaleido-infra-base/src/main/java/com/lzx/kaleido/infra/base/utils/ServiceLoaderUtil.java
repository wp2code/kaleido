package com.lzx.kaleido.infra.base.utils;

import cn.hutool.core.util.ObjectUtil;
import com.lzx.kaleido.infra.base.annotations.SpiSingleton;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lwp
 * @date 2023-11-17
 **/
@UtilityClass
public class ServiceLoaderUtil {
    
    private static final Map<Class<?>, Collection<Object>> SERVICES = new ConcurrentHashMap<>();
    
    public void register(final Class<?> serviceInterface) {
        if (!SERVICES.containsKey(serviceInterface)) {
            SERVICES.put(serviceInterface, load(serviceInterface));
        }
    }
    
    /**
     * @param serviceInterface
     * @param <T>
     * @return
     */
    public <T> Collection<T> getServiceInstances(final Class<T> serviceInterface) {
        return null == serviceInterface.getAnnotation(SpiSingleton.class) ? createNewServiceInstances(serviceInterface)
                : getSingletonServiceInstances(serviceInterface);
        
    }
    
    /**
     * @param serviceInterface
     * @param <T>
     * @return
     */
    @SneakyThrows(ReflectiveOperationException.class)
    private <T> Collection<T> createNewServiceInstances(final Class<T> serviceInterface) {
        if (!SERVICES.containsKey(serviceInterface)) {
            return Collections.emptyList();
        }
        Collection<Object> services = SERVICES.get(serviceInterface);
        if (services.isEmpty()) {
            return Collections.emptyList();
        }
        Collection<T> result = new LinkedList<>();
        for (Object each : services) {
            //尝试构建
            result.add((T) each.getClass().getDeclaredConstructor().newInstance());
        }
        return result;
    }
    
    private <T> Collection<T> getSingletonServiceInstances(final Class<T> serviceInterface) {
        return (Collection<T>) SERVICES.getOrDefault(serviceInterface, Collections.emptyList());
    }
    
    /**
     * @param serviceInterface
     * @param <T>
     * @return
     */
    private <T> Collection<Object> load(final Class<T> serviceInterface) {
        final Collection<Object> result = new LinkedList<>();
        final ServiceLoader<T> serviceLoader = ServiceLoader.load(serviceInterface);
        for (T each : serviceLoader) {
            result.add(each);
        }
        return result;
    }
    
    private <T> ServiceLoader<T> load(Class<T> clazz, ClassLoader loader) {
        return ServiceLoader.load(clazz, ObjectUtil.defaultIfNull(loader, ServiceLoaderUtil::getClassLoader));
    }
    
    /**
     * @return
     */
    private ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = ServiceLoaderUtil.class.getClassLoader();
            if (null == classLoader) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
        }
        return classLoader;
    }
}
