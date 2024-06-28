package com.lzx.kaleido.domain.api.code;

import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTemplateUpdateParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.TableFieldColumnParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.datasource.TableFieldColumnVO;
import com.lzx.kaleido.infra.base.pojo.spi.ITableColumnJava;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.function.Function;

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
     * 跟新模板配置
     *
     * @param templateId
     * @param configVO
     * @return
     */
    boolean updateCodeGenerationTemplateConfig(Long templateId, CodeGenerationTemplateUpdateParam configVO);
    
    /**
     * 修改代码地址
     *
     * @param templateId
     * @param codePath
     * @param codeTypeList
     * @return
     */
    boolean updateCodePathByTemplateId(Long templateId, String codePath, List<String> codeTypeList);
    
    /**
     * 获取代码配置详情
     *
     * @param id
     * @return
     */
    CodeGenerationTemplateConfigVO getDetailById(@NotNull final Long id);
    
    /**
     * 获取模板配置
     *
     * @param templateId
     * @param name
     * @return
     */
    CodeGenerationTemplateConfigVO getCodeGenerationTemplateConfig(@NotNull final Long templateId, @NotNull final String name);
    
    /**
     * 根据模板ID获取配置信息
     *
     * @param templateId
     * @param hideStatus
     * @param nameList
     * @return
     */
    List<CodeGenerationTemplateConfigVO> getByTemplateId(@NotNull final Long templateId, final Integer hideStatus,
            final List<String> nameList);
    
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
    
    /**
     * 获取表字段信息列表
     *
     * @param tableFieldColumnParam
     * @param function
     * @param <T>
     * @return
     */
    <T> List<T> getTemplateTableFieldColumnList(final TableFieldColumnParam tableFieldColumnParam,
            final Function<ITableColumnJava, T> function);
    
    
    /**
     * 获取模板表字段
     *
     * @param templateId
     * @param name
     * @param tableFieldColumnParam
     * @return
     */
    List<TableFieldColumnVO> getTemplateTableFieldColumnList(final Long templateId, final String name,
            final TableFieldColumnParam tableFieldColumnParam);
}
