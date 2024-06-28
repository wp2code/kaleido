package com.lzx.kaleido.domain.model.vo.code;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lwp
 * @date 2024-01-14
 **/
@Data
public class CodeGenerationTemplateViewConfigVO implements Serializable {
    
    /**
     * 模板名称
     * <ul>
     *     <li>Entity</li>
     *     <li>VO</li>
     *     <li>Mapper</li>
     *     <li>Xml</li>
     *     <li>ServiceApi</li>
     *     <li>Service</li>
     *     <li>Controller</li>
     * </ul>
     */
    private String name;
    
    /**
     * 代码地址
     */
    private String codePath;
    
    /**
     * 模板配置
     */
    private String config;
}
