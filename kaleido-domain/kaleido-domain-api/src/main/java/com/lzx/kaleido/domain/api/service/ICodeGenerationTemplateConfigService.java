package com.lzx.kaleido.domain.api.service;

import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author lwp
 * @date 2023-12-14
 **/
public interface ICodeGenerationTemplateConfigService {
    
    /**
     * 新增代码配置-批量
     *
     * @param voList
     * @return
     */
    boolean addCodeGenerationTemplateConfigBatch(final List<CodeGenerationTemplateConfigVO> voList);
    
    
    /**
     * 更新配置-批量
     *
     * @param voList
     * @return
     */
    boolean updateByIdBatch(final List<CodeGenerationTemplateConfigVO> voList);
    
    /**
     * 获取代码配置详情
     *
     * @param id
     * @return
     */
    CodeGenerationTemplateConfigVO getDetailById(@NotNull final Long id);
    
    /**
     * 根据模板ID获取配置信息
     *
     * @param templateId
     * @param hideStatus
     * @param needParseTemplate
     * @return
     */
    List<CodeGenerationTemplateConfigVO> getByTemplateId(@NotNull final Long templateId, final Integer hideStatus);
    
    /**
     * 删除代码配置
     *
     * @param id
     * @return
     */
    boolean deleteById(@NotNull final Long id);
    
    /**
     * @param templateId
     * @return
     */
    boolean deleteByTemplateId(@NotNull final Long templateId);
    
    /**
     * 更新配置隐藏状态
     *
     * @param id
     * @param hideStatus
     * @return
     */
    boolean updateHideStatus(@NotNull final Long id, Integer hideStatus);
}
