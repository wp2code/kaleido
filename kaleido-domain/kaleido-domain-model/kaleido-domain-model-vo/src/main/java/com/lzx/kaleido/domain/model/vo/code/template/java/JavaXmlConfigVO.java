package com.lzx.kaleido.domain.model.vo.code.template.java;

import com.lzx.kaleido.domain.model.vo.code.template.JavaConfigVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lwp
 * @date 2023-12-09
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class JavaXmlConfigVO extends JavaConfigVO {
    
    public final static String NAME = "Xml";
    
    /**
     * xml 所属命名空间（Mapper）
     */
    private String namespace;
    
    public JavaXmlConfigVO() {
        this.configName = NAME;
    }
}
