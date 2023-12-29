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
public class JavaModelConfigVO extends JavaConfigVO {
    public final static String NAME = "Model";
    /**
     * 父类名称
     */
    private String superclass;
    
    /**
     * 配置类型名称
     */


    public JavaModelConfigVO() {
        this.configName = NAME;
    }
}
