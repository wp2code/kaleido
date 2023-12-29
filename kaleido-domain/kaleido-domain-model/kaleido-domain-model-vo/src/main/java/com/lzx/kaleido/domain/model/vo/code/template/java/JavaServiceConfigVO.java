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
public class JavaServiceConfigVO extends JavaConfigVO {
    public final static String NAME = "Service";
    /**
     * 父类名称
     */
    private String superclass;
    
    /**
     * 实现接口
     */
    private String implInterface;
    
    public JavaServiceConfigVO() {
        this.configName = NAME;
    }
}
