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
     * 类型
     */
    @NotNull(groups = {AddGroup.class, UpdateGroup.class})
    private String type;
    
    /**
     * 配置参数
     */
    private String configParams;
    
    /**
     * 代码模板
     */
    private String templateContent;
    
    /**
     * 隐藏状态：0-显示；1-隐藏
     */
    private Integer hideStatus;
    
}
