package com.lzx.kaleido.plugins.template.template;

import java.io.File;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author lwp
 * @date 2023-12-13
 **/
public interface ITemplate {
    
    /**
     * 写出到输出流
     *
     * @param bindingMap 绑定的参数
     * @param out        输出流
     */
    void render(Map<?, ?> bindingMap, OutputStream out);
    
    /**
     * 写出到文件
     *
     * @param bindingMap 绑定的参数
     * @param file       输出到的文件
     */
    void render(Map<?, ?> bindingMap, File file);
    
    
    /**
     * 写出到指定路径
     *
     * @param bindingMap 绑定的参数
     * @param path       出到的文件地址（绝对路径）
     */
    void render(Map<?, ?> bindingMap, String path);
    
}
