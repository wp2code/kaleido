package com.lzx.kaleido.domain.model.vo.code;

import cn.hutool.core.collection.CollUtil;
import com.lzx.kaleido.domain.model.entity.code.CodeGenerationTemplateEntity;
import com.lzx.kaleido.infra.base.annotations.validation.AddGroup;
import com.lzx.kaleido.infra.base.pojo.BaseVO;
import io.github.zhaord.mapstruct.plus.annotations.AutoMap;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lwp
 * @date 2023-12-09
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMap(targetType = CodeGenerationTemplateEntity.class)
public class CodeGenerationTemplateVO extends BaseVO {
    
    /**
     * 模板ID
     */
    private Long id;
    
    /**
     * 模板名称
     */
    @NotEmpty(groups = {AddGroup.class})
    private String templateName;
    
    /**
     * 编程语言
     */
    private String language = "java";
    
    /**
     * 模板基本配置
     */
    private String basicConfig;
    
    /**
     * 来源类型（0-初始自动创建；1-手动创建；2-复制创建；3-导入创建）
     */
    private Integer sourceType;
    
    /**
     * 来源
     */
    private String source;
    
    
    /**
     * 是否为内部模板 （0-不是；1-是）
     */
    private Integer isInternal = 0;
    
    /**
     * 是否为默认模板（0-不是；1-是）
     */
    private Integer isDefault = 0;
    
    /**
     * 模板摘要信息
     */
    private String digestValue;
    
    /**
     * 模板信息
     */
    @Valid
    private List<CodeGenerationTemplateConfigVO> templateConfigList;
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (id != null) {
            sb.append(id);
        }
        sb.append(templateName);
        sb.append(language);
        if (basicConfig != null) {
            sb.append(basicConfig);
        }
        sb.append(isInternal);
        sb.append(isDefault);
        if (CollUtil.isNotEmpty(templateConfigList)) {
            for (final CodeGenerationTemplateConfigVO vo : templateConfigList) {
                sb.append(vo.toString());
            }
        }
        return sb.toString();
    }
    
}
