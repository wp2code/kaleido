package com.lzx.kaleido.domain.model.vo.code.template.java;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.digest.DigestUtil;
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
     * xml 所属命名空间（Mapper）
     */
    private String namespace;
    
    /**
     *
     */
    private boolean useMybatisPlus;
    
    /**
     * 生成sql
     */
    private List<String> sqlList;
    
    public JavaXmlConfigVO() {
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
        sb.append(namespace);
        if (CollUtil.isNotEmpty(sqlList)) {
            sb.append(String.join("", sqlList));
        }
        return DigestUtil.md5Hex(sb.toString());
    }
}
