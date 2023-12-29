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
public class JavaControllerConfigVO extends JavaConfigVO {
    
    public final static String NAME = "Controller";
    
    /**
     * 父类名称
     */
    private String superclass;
    
    /**
     * 响应泛型类； 例如：R<T>
     */
    private String responseGenericClass;
    
    public JavaControllerConfigVO() {
        this.configName = NAME;
    }
    
}
