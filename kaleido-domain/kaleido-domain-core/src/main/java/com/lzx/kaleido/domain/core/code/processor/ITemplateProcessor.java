package com.lzx.kaleido.domain.core.code.processor;

import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.JavaConfigVO;
import com.lzx.kaleido.plugins.template.enums.ResourceMode;

import java.util.List;

/**
 * 解析器
 *
 * @author lwp
 * @date 2024-01-18
 **/
public interface ITemplateProcessor {
    
    /**
     * 代码生成
     *
     * @param config
     * @param basicConfig
     * @param generateCodeFile
     * @param codeGenerationTableParam
     * @param resourceMode
     * @param refCodeGenerationViewList
     * @return
     */
    CodeGenerationViewVO generation(final CodeGenerationTemplateConfigVO config, final BasicConfigVO basicConfig,
            final boolean generateCodeFile, final CodeGenerationTableParam codeGenerationTableParam, ResourceMode resourceMode,
            List<CodeGenerationViewVO> refCodeGenerationViewList);
    
    
    /**
     * 代码配置解析
     *
     * @param codeConfig
     * @return
     */
    JavaConfigVO parser(final String codeConfig);
    
    
    /**
     * @param javaConfigVO
     * @return
     */
    CodeGenerationTableParam toCodeGenerationTableParam(JavaConfigVO javaConfigVO);
}
