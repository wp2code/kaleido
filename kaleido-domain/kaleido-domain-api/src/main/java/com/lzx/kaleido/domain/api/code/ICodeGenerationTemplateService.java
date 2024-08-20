package com.lzx.kaleido.domain.api.code;

import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationGlobalConfigParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationSimpleParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTemplateQueryParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTemplateUpdateParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateFileVO;
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
     * 新增导入的代码模板
     *
     * @param vo
     * @return
     */
    Long addImportCodeGenerationTemplate(final CodeGenerationTemplateFileVO vo);
    
    /**
     * @param id
     * @return
     */
    Long addTemplateWithCopy(@NotNull final Long id, final String templateName);
    
    
    /**
     * 新增代码模板
     *
     * @param vo
     * @return
     */
    Long addCodeGenerationTemplate(final CodeGenerationTemplateVO vo);
    
    /**
     * 校验模板名称
     *
     * @param templateId
     * @param templateName
     * @return
     */
    boolean checkTemplateName(Long templateId, String templateName);
    
    
    /**
     * 校验默认模板是否存在
     *
     * @return
     */
    boolean checkInitDefaultTemplate();
    
    /**
     * 更新代码模板
     *
     * @param id
     * @param vo
     * @return
     */
    boolean updateById(@NotNull final Long id, final CodeGenerationTemplateVO vo);
    
    /**
     * 更新部分代码模板
     *
     * @param param
     * @return
     */
    boolean updateCodeGenerationTemplateOfPartition(@NotNull CodeGenerationTemplateUpdateParam param);
    
    
    /**
     * 更新全局基本配置
     *
     * @param param
     * @return
     */
    boolean updateGlobalConfig(CodeGenerationGlobalConfigParam param);
    
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
     * 获取模板信息
     *
     * @param param
     * @return
     */
    CodeGenerationTemplateVO getCodeGenerationTemplate(CodeGenerationSimpleParam param);
    
    /**
     * 获取默认模板
     *
     * @return
     */
    CodeGenerationTemplateVO getDefaultCodeGenerationTemplate();
    
    /**
     * 删除代码模板
     *
     * @param id
     * @return
     */
    boolean deleteById(@NotNull final Long id);
    
    /**
     * 获取导出模板信息
     *
     * @param templateId
     * @return
     */
    CodeGenerationTemplateFileVO getCodeGenerationTemplateFile(@NotNull final Long templateId);
}
