package com.lzx.kaleido.domain.model.vo.code.template.java;

import cn.hutool.core.collection.CollUtil;
import com.lzx.kaleido.domain.model.vo.code.template.JavaConfigVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author lwp
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class JavaXmlConfigVO extends JavaConfigVO {
    
    public final static String NAME = "Xml";
    
    
    /**
     *
     */
    private boolean useMybatisPlus;
    
    /**
     *
     */
    private List<String> methodList;
    
    public JavaXmlConfigVO() {
        this.name = NAME;
        this.aliasName = NAME;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(useMybatisPlus);
        if (CollUtil.isNotEmpty(methodList)) {
            sb.append(String.join("", methodList));
        }
        sb.append(super.toString());
        return sb.toString();
    }
}
