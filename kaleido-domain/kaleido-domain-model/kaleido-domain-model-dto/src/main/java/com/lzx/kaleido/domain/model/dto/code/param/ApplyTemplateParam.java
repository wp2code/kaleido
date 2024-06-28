package com.lzx.kaleido.domain.model.dto.code.param;

import lombok.Data;

import java.util.List;

/**
 * @author lwp
 * @date 2024-06-15
 **/
@Data
public class ApplyTemplateParam {
    private Long templateId;
    private List<String> codeTypeList;
}
