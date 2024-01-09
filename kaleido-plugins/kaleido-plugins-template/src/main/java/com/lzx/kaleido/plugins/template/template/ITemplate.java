package com.lzx.kaleido.plugins.template.template;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
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
    
    /**
     * 将模板与绑定参数融合后输出到Writer
     *
     * @param bindingMap 绑定的参数，此Map中的参数会替换模板中的变量
     * @param writer     输出
     */
    void render(Map<?, ?> bindingMap, Writer writer);
    
    /**
     * 写出到字符串
     *
     * @param bindingMap
     * @return
     */
    String render(Map<?, ?> bindingMap);
    
}
