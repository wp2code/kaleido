package com.lzx.kaleido.plugins.template.enums;

import java.util.Arrays;

/**
 * @author lwp
 * @date 2023-12-18
 **/
public enum ResourceMode {
    /**
     * 从ClassPath加载模板
     */
    CLASSPATH,
    /**
     * 从File目录加载模板
     */
    FILE,
    /**
     * 从模板文本加载模板
     */
    STRING,
    /**
     * 远程地址加载模板
     */
    REMOTE,
    /**
     *
     */
    COMPOSITE;
    
    public static ResourceMode getInstance(String name) {
        return Arrays.stream(values()).filter(v -> v.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
