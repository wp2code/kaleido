package com.lzx.kaleido.domain.model.vo.code.template;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author lwp
 * @date 2024-06-19
 **/
@Data
public class TemplateParamVO {
    
    private String sourceFolder;
    
    private String packageName;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String aliasName;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String codePath;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SuperclassVO superclass;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean useMybatisPlus;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean useLombok;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean useSwagger;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> methodList;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String responseGenericClass;
    /**
     * 忽略生成的字段
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> defaultIgFields;
    
    public void buildSuperclass(String name, List<String> generics) {
        if (StrUtil.isNotBlank(name)) {
            final SuperclassVO superclass = new SuperclassVO();
            superclass.setName(name);
            superclass.setGenerics(generics);
            this.setSuperclass(superclass);
        }
    }
}
