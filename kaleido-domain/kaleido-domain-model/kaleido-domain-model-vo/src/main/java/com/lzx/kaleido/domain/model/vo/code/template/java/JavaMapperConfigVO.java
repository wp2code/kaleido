package com.lzx.kaleido.domain.model.vo.code.template.java;

import cn.hutool.core.collection.CollUtil;
import com.lzx.kaleido.domain.model.vo.code.template.JavaConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.SuperclassVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author lwp
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class JavaMapperConfigVO extends JavaConfigVO {
    
    public final static String NAME = "Mapper";
    
    /**
     * 父类
     */
    private SuperclassVO superclass;
    
    
    /**
     * 生成接口方法
     */
    private List<String> methodList;
    
    /**
     *
     */
    private boolean useMybatisPlus;
    
    public JavaMapperConfigVO() {
        this.name = NAME;
        this.aliasName=NAME;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(useMybatisPlus);
        if (superclass != null) {
            sb.append(superclass);
        }
        if (CollUtil.isNotEmpty(methodList)) {
            sb.append(String.join("", methodList));
        }
        sb.append(super.toString());
        return sb.toString();
    }
}
