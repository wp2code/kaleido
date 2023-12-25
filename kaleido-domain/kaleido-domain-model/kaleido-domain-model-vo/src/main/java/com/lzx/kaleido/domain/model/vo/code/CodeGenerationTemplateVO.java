package com.lzx.kaleido.domain.model.vo.code;

import com.lzx.kaleido.domain.model.entity.code.CodeGenerationTemplateEntity;
import com.lzx.kaleido.infra.base.annotations.validation.AddGroup;
import com.lzx.kaleido.infra.base.annotations.validation.UpdateGroup;
import com.lzx.kaleido.infra.base.pojo.BaseVO;
import io.github.zhaord.mapstruct.plus.annotations.AutoMap;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

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
    @NotEmpty(groups = {AddGroup.class, UpdateGroup.class})
    private String templateName;
    
    /**
     * 编程语言
     */
    @NotEmpty(groups = {AddGroup.class, UpdateGroup.class})
    private String language;
    
    /**
     * 是否为内部模板 （0-不是；1-是）
     */
    private Integer isInternal = 0;
    
    /**
     * 模板信息
     */
    @Valid
    private List<CodeGenerationTemplateConfigVO> templateConfigList;
}
