package com.lzx.kaleido.domain.model.vo.code;

import com.lzx.kaleido.domain.model.entity.code.CodeGenerationTemplateConfigEntity;
import com.lzx.kaleido.infra.base.annotations.validation.AddGroup;
import com.lzx.kaleido.infra.base.annotations.validation.UpdateGroup;
import com.lzx.kaleido.infra.base.pojo.BaseVO;
import io.github.zhaord.mapstruct.plus.annotations.AutoMap;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lwp
 * @date 2023-12-09
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMap(targetType = CodeGenerationTemplateConfigEntity.class)
public class CodeGenerationTemplateConfigVO extends BaseVO {
    
    /**
     * 模板配置ID
     */
    @NotNull(groups = {UpdateGroup.class})
    private Long id;
    
    /**
     * 模板ID
     */
    @NotNull(groups = {UpdateGroup.class})
    private Long templateId;
    
    /**
     * 模板配置名称
     */
    @NotNull(groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    
    /**
     * 模板配置别名
     */
    @NotNull(groups = {AddGroup.class, UpdateGroup.class})
    private String alias;
    
    /**
     * 模板内容（json）
     */
    private String templateContent;
    
    /**
     * 隐藏状态：0-显示；1-隐藏
     */
    private Integer hideStatus;
    
    /**
     * 模板参数配置
     */
    private String templateParams;
    
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (id != null) {
            sb.append(id);
        }
        if (templateId != null) {
            sb.append(templateId);
        }
        sb.append(name);
        sb.append(alias);
        sb.append(templateContent);
        sb.append(hideStatus);
        sb.append(templateParams);
        return sb.toString();
    }
    
}
