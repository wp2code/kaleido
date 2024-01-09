package com.lzx.kaleido.domain.model.vo.code.template.java;

import cn.hutool.core.util.StrUtil;
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
    
    public JavaControllerConfigVO() {
        this.configName = NAME;
    }
    
    @Override
    public String getDigestValue() {
        final StringBuilder sb = new StringBuilder();
        sb.append(sourceFolder);
        sb.append(codePath);
        sb.append(packageName);
        sb.append(useMybatisPlus);
        sb.append(configName);
        if (superclass != null) {
            sb.append(superclass);
        }
        if (StrUtil.isNotBlank(responseGenericClass)) {
            sb.append(responseGenericClass);
        }
        return DigestUtil.md5Hex(sb.toString());
    }
}
