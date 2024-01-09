package com.lzx.kaleido.domain.model.vo.code.template.java;

import cn.hutool.crypto.digest.DigestUtil;
import com.lzx.kaleido.domain.model.vo.code.template.JavaConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.common.SuperclassVO;
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
        this.configName = NAME;
    }
    
    @Override
    public String getDigestValue() {
        final StringBuilder sb = new StringBuilder();
        sb.append(sourceFolder);
        sb.append(codePath);
        sb.append(packageName);
        sb.append(configName);
        if (superclass != null) {
            sb.append(superclass);
        }
        return DigestUtil.md5Hex(sb.toString());
    }
}
