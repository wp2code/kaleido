package com.lzx.kaleido.domain.repository.mapper;

import com.lzx.kaleido.domain.model.entity.code.CodeGenerationTemplateEntity;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateFileVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateVO;
import com.lzx.kaleido.plugins.mp.IBaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lwp
 * @date 2023-12-14
 **/
public interface ICodeGenerationTemplateMapper extends IBaseMapper<CodeGenerationTemplateEntity> {
    
    /**
     * @param templateId
     * @return
     */
    CodeGenerationTemplateFileVO getCodeGenerationTemplateFile(@Param("templateId") Long templateId);
    
    /**
     * @return
     */
    CodeGenerationTemplateVO getDefaultCodeGenerationTemplate();
}
