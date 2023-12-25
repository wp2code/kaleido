package com.lzx.kaleido.domain.model.dto.param.code;

import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import lombok.Data;

import java.util.List;

/**
 * @author lwp
 * @date 2023-12-14
 **/
@Data
public class CodeGenerationTemplateParam {
    
    /**
     * 名称
     */
    private String templateName;
    
    /**
     * 配置详情
     */
    private List<CodeGenerationTemplateConfigVO> templateConfigList;
    
}
