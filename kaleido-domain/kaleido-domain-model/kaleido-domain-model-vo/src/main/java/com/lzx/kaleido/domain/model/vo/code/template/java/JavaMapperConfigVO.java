package com.lzx.kaleido.domain.model.vo.code.template.java;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.lzx.kaleido.domain.model.vo.code.template.JavaConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.common.SuperclassVO;
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
        this.configName = NAME;
    }
    
    @Override
    public String getDigestValue() {
        final StringBuilder sb = new StringBuilder();
        sb.append(sourceFolder);
        sb.append(codePath);
        sb.append(packageName);
        sb.append(configName);
        sb.append(useMybatisPlus);
        if (superclass != null) {
            sb.append(superclass);
        }
        if (CollUtil.isNotEmpty(methodList)) {
            sb.append(String.join("", methodList));
        }
        return DigestUtil.md5Hex(sb.toString());
    }
}
