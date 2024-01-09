package com.lzx.kaleido.domain.model.vo.code.template.java.common;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author lwp
 * @date 2024-01-15
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuperclassVO implements Serializable {
    
    /**
     * 父类名称
     */
    private String name;
    
    /**
     * 父类泛型
     */
    private List<String> generics;
    
    public SuperclassVO(final String name, String... generics) {
        this.name = name;
        if (generics != null) {
            this.generics = Arrays.stream(generics).toList();
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (CollUtil.isNotEmpty(generics)) {
            sb.append(String.join("", generics));
        }
        return sb.toString();
    }
}
