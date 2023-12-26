package com.lzx.kaleido.plugins.template.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.util.Map;

/**
 * @author lwp
 * @date 2023-12-13
 **/
public abstract class AbsTemplate implements ITemplate {
    
    /**
     * 写出到文件
     *
     * @param bindingMap 绑定的参数
     * @param file       输出到的文件
     */
    @Override
    public void render(Map<?, ?> bindingMap, File file) {
        BufferedOutputStream out = null;
        try {
            out = FileUtil.getOutputStream(file);
            this.render(bindingMap, out);
        } finally {
            IoUtil.close(out);
        }
    }
    
    /**
     * @param bindingMap 绑定的参数
     * @param path       出到的文件地址（绝对路径）
     */
    @Override
    public void render(final Map<?, ?> bindingMap, final String path) {
        BufferedOutputStream out = null;
        try {
            out = FileUtil.getOutputStream(path);
            this.render(bindingMap, out);
        } finally {
            IoUtil.close(out);
        }
    }
}
