package com.lzx.kaleido.domain.model.vo.code.template;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lwp
 * @date 2023-12-19
 **/
@Data
public class JavaConfigVO implements Serializable {
    
    /**
     * 生成代码名称: 例如：xxx.java
     */
    protected String name;
    
    /**
     * 源文件夹 src/main/java
     */
    protected String sourceFolder;
    
    /**
     * 代码存储地址
     */
    protected String codePath;
    
    /**
     * 包名称; 例如：com.xxx
     */
    protected String packageName;
    
    /**
     * 配置名称
     */
    protected String configName;
    
}
