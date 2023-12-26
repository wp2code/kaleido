package com.lzx.kaleido.domain.api.service;

import com.lzx.kaleido.domain.model.dto.param.code.CodeGenerationParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationResultVO;

import java.io.OutputStream;
import java.util.List;

/**
 * @author lwp
 * @date 2023-12-18
 **/
public interface ICodeGeneration {
    
    /**
     * 代码生成-预览
     *
     * @param configParam
     * @param outputStream
     */
    void preview(final CodeGenerationParam configParam, final OutputStream outputStream);
    
    /**
     * 代码生成
     *
     * @param configParam
     * @return
     */
    CodeGenerationResultVO generation(final CodeGenerationParam configParam);
    
    
    /**
     * 代码生成-批量
     *
     * @param codeGenerationParams
     * @return
     */
    List<CodeGenerationResultVO> generation(final List<CodeGenerationParam> codeGenerationParams);
    
}
