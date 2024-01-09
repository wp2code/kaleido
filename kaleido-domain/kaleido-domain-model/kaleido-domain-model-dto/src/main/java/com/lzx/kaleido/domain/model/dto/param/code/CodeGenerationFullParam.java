package com.lzx.kaleido.domain.model.dto.param.code;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author lwp
 * @date 2024-01-18
 **/
@Data
public class CodeGenerationFullParam {
    
    /**
     * 所属模板ID
     */
    @NotNull
    private Long templateId;
    
    /**
     *
     */
    @Valid
    @NotNull
    private List<CodeGenerationTableParam> codeGenerationList;
}
