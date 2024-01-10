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
public class JavaVoConfigVO extends JavaConfigVO {
    
    public final static String NAME = "VO";
    
    /**
     * 父类
     */
    private SuperclassVO superclass;
    
    private boolean useLombok;
    
    private boolean useSwagger;
    
    
    /**
     * 配置类型名称
     */
    
    
    public JavaVoConfigVO() {
        this.name = NAME;
        this.aliasName=NAME;
        this.useLombok = true;
    }
    
    @Override
    public String toString() {
        return String.valueOf(useLombok) + useSwagger + super.toString();
    }
}
