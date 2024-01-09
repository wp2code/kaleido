package com.lzx.kaleido.domain.model.vo.code;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.lzx.kaleido.domain.model.entity.code.CodeGenerationTemplateEntity;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.conversion.BasicConfigVOConversion;
import com.lzx.kaleido.infra.base.annotations.validation.AddGroup;
import com.lzx.kaleido.infra.base.annotations.validation.UpdateGroup;
import com.lzx.kaleido.infra.base.pojo.BaseVO;
import com.lzx.kaleido.plugins.mapstruct.annotations.json.JsonConvert;
import io.github.zhaord.mapstruct.plus.annotations.AutoMap;
import io.github.zhaord.mapstruct.plus.annotations.AutoMapField;
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
@AutoMap(targetType = CodeGenerationTemplateEntity.class, uses = BasicConfigVOConversion.class)
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
     * 模板基本配置
     */
    @AutoMapField(source = "basicConfig", target = "basicConfig", qualifiedBy = JsonConvert.class)
    private BasicConfigVO basicConfig;
    
    
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
    
    /**
     * @return
     */
    public String getDigestValue() {
        return DigestUtil.md5Hex(this.toString());
    }
    
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
