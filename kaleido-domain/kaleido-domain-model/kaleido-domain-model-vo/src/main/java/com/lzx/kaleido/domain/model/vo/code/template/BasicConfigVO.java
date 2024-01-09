package com.lzx.kaleido.domain.model.vo.code.template;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lwp
 * @date 2023-12-19
 **/
@Data
public class BasicConfigVO implements Serializable {
    
    /**
     * 作者
     */
    private String author;
    
    /**
     * 代码保存路径
     */
    private String codePath;
    
    /**
     * 代码license
     */
    private String license;
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(author);
        sb.append(codePath);
        sb.append(license);
        return sb.toString();
    }
}
