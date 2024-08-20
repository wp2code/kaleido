package com.lzx.kaleido.domain.model.vo.code;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.infra.base.utils.IdGenUtil;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.Data;


/**
 * @author lwp
 * @date 2024-07-15
 **/
@Data
public class CodeGenerationTemplateFileVO implements Serializable {
    
    /**
     * 来源模板ID
     */
    private Long sourceTemplateId;
    
    /**
     *
     */
    private String templateName;
    
    /**
     * 开发语言 默认java
     */
    private String language;
    
    /**
     * 全局配置
     */
    private String basicConfig;
    
    /**
     * 操作人
     */
    private String creatorId;
    
    /**
     * 代码配置信息
     */
    private List<CodeGenerationTemplateFileConfigVO> codeConfigList;
    
    /**
     * @param predicate
     * @return
     */
    public boolean validate(Predicate<String> predicate) {
        return StrUtil.isNotBlank(templateName) && StrUtil.isNotBlank(basicConfig) && JsonUtil.validateJson(basicConfig, "author")
                && CollUtil.isNotEmpty(codeConfigList) && codeConfigList.stream().allMatch(v -> v.validate(predicate));
    }
    
    public void fill(Function<String, String> function) {
        if (StrUtil.isBlank(language)) {
            language = "java";
        }
        this.basicConfig = function.apply(basicConfig);
        if (StrUtil.isNotBlank(templateName)) {
            templateName = "%s_导入（%s）".formatted(templateName, IdGenUtil.getTimeId(null));
        } else {
            templateName = "模板_导入（%s）".formatted(IdGenUtil.getTimeId(null));
        }
    }
}
