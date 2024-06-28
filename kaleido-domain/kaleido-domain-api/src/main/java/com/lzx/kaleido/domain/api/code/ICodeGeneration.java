package com.lzx.kaleido.domain.api.code;

import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationAllParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationResultVO;

import java.io.OutputStream;

/**
 * @author lwp
 * @date 2023-12-18
 **/
public interface ICodeGeneration {
    
    /**
     * 代码生成或预览
     *
     * @param codeGenerationTableParam
     * @param isPreview
     * @return
     */
    CodeGenerationResultVO generationOrPreview(final CodeGenerationAllParam codeGenerationTableParam, final boolean isPreview);
    
    
    /**
     * 代码生成-预览
     *
     * @param configParam
     * @param outputStream
     */
    void preview(final CodeGenerationParam configParam, final OutputStream outputStream);
    

}
