package com.lzx.kaleido.domain.model.vo.code;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 多个
 *
 * @author lwp
 * @date 2023-12-18
 **/
@Data
@Accessors(chain = true)
public class CodeGenerationResultVO {
    
    /**
     * 生成代码地址
     */
    private String path;
    
    
    /**
     *
     */
    private String codeAlias;
}
