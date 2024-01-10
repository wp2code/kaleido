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
public class JavaServiceConfigVO extends JavaConfigVO {
    
    public final static String NAME = "Service";
    
    /**
     * 父类
     */
    private SuperclassVO superclass;
    
    private boolean useMybatisPlus;
    
    public JavaServiceConfigVO() {
        this.name = NAME;
        this.aliasName=NAME;
    }
    
    @Override
    public String toString() {
        return String.valueOf(superclass) + useMybatisPlus + super.toString();
    }
}
