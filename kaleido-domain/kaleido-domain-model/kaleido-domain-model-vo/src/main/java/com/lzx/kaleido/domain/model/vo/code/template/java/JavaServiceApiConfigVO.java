package com.lzx.kaleido.domain.model.vo.code.template.java;

import com.lzx.kaleido.domain.model.vo.code.template.JavaConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.SuperclassVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lwp
 * @date 2023-12-09
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class JavaServiceApiConfigVO extends JavaConfigVO {
    
    public final static String NAME = "ServiceApi";
    
    /**
     * 父类
     */
    private SuperclassVO superclass;
    
    
    public JavaServiceApiConfigVO() {
        this.name = NAME;
        this.aliasName=NAME;
    }
    
    @Override
    public String toString() {
        return superclass + super.toString();
    }
}
