package com.lzx.kaleido.domain.model.vo.code.template.java;

import cn.hutool.core.collection.CollUtil;
import com.lzx.kaleido.domain.model.vo.code.template.JavaConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.SuperclassVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author lwp
 * @date 2023-12-09
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class JavaEntityConfigVO extends JavaConfigVO {
    
    public final static String NAME = "Entity";
    
    /**
     * 父类
     */
    private SuperclassVO superclass;
    
    private boolean useLombok;
    
    private boolean useSwagger;
    
    private boolean useMybatisPlus;
    
    /**
     * 默认不生产的字段
     */
    private List<String> defaultIgFields;
    
    public JavaEntityConfigVO() {
        this.name = NAME;
        this.aliasName = NAME;
        this.useLombok = true;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(useLombok);
        sb.append(useSwagger);
        sb.append(useMybatisPlus);
        if (superclass != null) {
            sb.append(superclass);
        }
        if (CollUtil.isNotEmpty(defaultIgFields)) {
            sb.append(defaultIgFields);
        }
        sb.append(super.toString());
        return sb.toString();
    }
}
