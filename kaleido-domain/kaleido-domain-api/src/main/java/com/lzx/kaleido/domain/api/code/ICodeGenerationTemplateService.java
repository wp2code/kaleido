package com.lzx.kaleido.domain.api.code;

import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTemplateQueryParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateViewVO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author lwp
 * @date 2023-12-14
 **/
public interface ICodeGenerationTemplateService {
    
    /**
     * 新增标准的代码模板
     *
     * @param vo
     * @return
     */
    Long addStandardCodeGenerationTemplate(final CodeGenerationTemplateViewVO vo);
    
    
    /**
     * 新增代码模板
     *
     * @param vo
     * @return
     */
    Long addCodeGenerationTemplate(final CodeGenerationTemplateVO vo);
    
    
    /**
     * 更新代码模板
     *
     * @param id
     * @param vo
     * @return
     */
    boolean updateById(@NotNull final Long id, final CodeGenerationTemplateVO vo);
    
    
    /**
     * 更新模板名称
     *
     * @param id
     * @param templateName
     * @return
     */
    boolean updateTemplateNameById(@NotNull final Long id, String templateName);
    
    
    /**
     * 更新为默认模板
     *
     * @param id
     * @return
     */
    boolean updateDefaultTemplate(@NotNull final Long id);
    
    /**
     * 查询模板列表
     *
     * @param param
     * @return
     */
    List<CodeGenerationTemplateVO> queryByParam(final CodeGenerationTemplateQueryParam param);
    
    /**
     * 获取代码模板详情
     *
     * @param id
     * @param hideStatus
     * @return
     */
    CodeGenerationTemplateVO getDetailById(@NotNull final Long id, Integer hideStatus);
    
    
    /**
     * 删除代码模板
     *
     * @param id
     * @return
     */
    boolean deleteById(@NotNull final Long id);
    
    
}
