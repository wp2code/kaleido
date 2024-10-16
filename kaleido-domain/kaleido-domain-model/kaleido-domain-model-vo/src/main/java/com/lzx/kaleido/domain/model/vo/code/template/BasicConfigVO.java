package com.lzx.kaleido.domain.model.vo.code.template;

import cn.hutool.core.util.StrUtil;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lwp
 * @date 2023-12-19
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
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
        return author + codePath + license;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BasicConfigVO that = (BasicConfigVO) o;
        if (!Objects.equals(author, that.author)) {
            return false;
        }
        if (!Objects.equals(codePath, that.codePath)) {
            return false;
        }
        return Objects.equals(license, that.license);
    }
    
    @Override
    public int hashCode() {
        int result = author != null ? author.hashCode() : 0;
        result = 31 * result + (codePath != null ? codePath.hashCode() : 0);
        result = 31 * result + (license != null ? license.hashCode() : 0);
        return result;
    }
    
    public boolean isFtmLicense() {
        if (StrUtil.isNotBlank(license)) {
            boolean isFtm = false;
            if (!StrUtil.startWith(license, "/*")) {
                license = "/*" + license;
                isFtm = true;
            }
            if (!StrUtil.endWith(license, "*/")) {
                license = license + "*/";
                isFtm = true;
            }
            return isFtm;
        }
        return false;
    }
}
