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
public class JavaControllerConfigVO extends JavaConfigVO {
    
    public final static String NAME = "Controller";
    
    /**
     * 父类
     */
    private SuperclassVO superclass;
    
    /**
     * 响应泛型类； 例如：R<T>
     */
    private String responseGenericClass;
    
    private boolean useSwagger;
    
    private boolean useMybatisPlus;
    
    /**
     *
     */
    private List<String> methodList;
    
    public JavaControllerConfigVO() {
        this.name = NAME;
        this.aliasName = NAME;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(useSwagger);
        sb.append(responseGenericClass);
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
