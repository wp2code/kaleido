package com.lzx.kaleido.domain.core.resolver;

import com.lzx.kaleido.domain.model.dto.param.code.CodeGenerationTableParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.plugins.template.enums.ResourceMode;

import java.util.List;

/**
 * 解析器
 *
 * @author lwp
 * @date 2024-01-18
 **/
public interface ITemplateParser {
    
    /**
     * @param config
     * @param basicConfig
     * @param generateCodeFile
     * @param codeGenerationTableParam
     * @param resourceMode
     * @param refCodeGenerationViewList
     * @return
     */
    CodeGenerationViewVO parser(final CodeGenerationTemplateConfigVO config, final BasicConfigVO basicConfig,
            final boolean generateCodeFile, final CodeGenerationTableParam codeGenerationTableParam, ResourceMode resourceMode,
            List<CodeGenerationViewVO> refCodeGenerationViewList);
    
}
