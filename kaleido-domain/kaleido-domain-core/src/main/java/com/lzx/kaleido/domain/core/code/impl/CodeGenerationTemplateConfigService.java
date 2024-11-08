package com.lzx.kaleido.domain.core.code.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lzx.kaleido.domain.api.code.ICodeGenerationTemplateConfigService;
import com.lzx.kaleido.domain.core.datasource.DataSourceFactory;
import com.lzx.kaleido.domain.core.enums.TemplateParserEnum;
import com.lzx.kaleido.domain.core.utils.TemplateConvertUtil;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTemplateUpdateParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.TableFieldColumnParam;
import com.lzx.kaleido.domain.model.entity.code.CodeGenerationTemplateConfigEntity;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.SuperclassVO;
import com.lzx.kaleido.domain.model.vo.code.template.TemplateParamVO;
import com.lzx.kaleido.domain.model.vo.datasource.TableFieldColumnVO;
import com.lzx.kaleido.domain.repository.mapper.ICodeGenerationTemplateConfigMapper;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.pojo.spi.ITableColumnJava;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import com.lzx.kaleido.infra.base.utils.PojoConvertUtil;
import com.lzx.kaleido.plugins.mp.BaseServiceImpl;
import com.lzx.kaleido.spi.db.model.TableColumnJavaMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lwp
 **/
@Service
public class CodeGenerationTemplateConfigService
        extends BaseServiceImpl<ICodeGenerationTemplateConfigMapper, CodeGenerationTemplateConfigEntity>
        implements ICodeGenerationTemplateConfigService {
    
    
    /**
     * 新增代码配置-批量
     *
     * @param voList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCodeGenerationTemplateConfigBatch(final List<CodeGenerationTemplateConfigVO> voList) {
        if (CollUtil.isEmpty(voList)) {
            return false;
        }
        final List<CodeGenerationTemplateConfigEntity> entities = PojoConvertUtil.vo2EntityList(voList,
                CodeGenerationTemplateConfigEntity.class);
        return this.saveBatch(entities);
    }
    
    /**
     * 更新配置-批量
     *
     * @param voList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateByIdBatch(final List<CodeGenerationTemplateConfigVO> voList) {
        for (final CodeGenerationTemplateConfigVO vo : voList) {
            final CodeGenerationTemplateConfigEntity entity = PojoConvertUtil.vo2Entity(vo, CodeGenerationTemplateConfigEntity.class);
            if (vo.getId() != null) {
                if (!this.updateById(entity)) {
                    throw new CommonRuntimeException(ErrorCode.UPDATE_FAILED);
                }
            } else {
                if (!this.save(entity)) {
                    throw new CommonRuntimeException(ErrorCode.SAVE_FAILED);
                }
            }
        }
        return true;
    }
    
    /**
     * 跟新模板配置
     *
     * @param templateId
     * @return
     */
    @Override
    public boolean updateCodeGenerationTemplateConfig(final Long templateId, final CodeGenerationTemplateUpdateParam param) {
        final TemplateParserEnum instance = TemplateParserEnum.getInstance(param.getName());
        if (instance == null) {
            throw new CommonRuntimeException(ErrorCode.UPDATE_FAILED);
        }
        final LambdaQueryWrapper<CodeGenerationTemplateConfigEntity> wrapper = Wrappers.<CodeGenerationTemplateConfigEntity>lambdaQuery()
                .eq(CodeGenerationTemplateConfigEntity::getTemplateId, templateId)
                .eq(CodeGenerationTemplateConfigEntity::getName, param.getName());
        final CodeGenerationTemplateConfigEntity entity = this.getOne(wrapper);
        entity.setCodePath(param.getCodePath());
        String templateParams = entity.getTemplateParams();
        final TemplateParamVO templateParamVO = JsonUtil.toBean(templateParams, TemplateParamVO.class);
        templateParamVO.setUseLombok(param.getUseLombok());
        templateParamVO.setUseMybatisPlus(param.getUseMybatisPlus());
        templateParamVO.setUseSwagger(param.getUseSwagger());
        templateParamVO.setSourceFolder(param.getSourceFolder());
        templateParamVO.setDefaultGenerate(param.getDefaultGenerate());
        if (StrUtil.isNotBlank(param.getNameSuffix())) {
            param.setNameSuffix(TemplateConvertUtil.firstCharOnlyToUpper(param.getNameSuffix().trim()));
        }
        templateParamVO.setNameSuffix(param.getNameSuffix());
        templateParamVO.setResponseGenericClass(param.getResponseGenericClass());
        templateParamVO.setPackageName(param.getPackageName());
        templateParamVO.setDefaultIgFields(param.getDefaultIgFields());
        templateParamVO.setMethodList(param.getMethodList());
        SuperclassVO superclass = templateParamVO.getSuperclass();
        if (StrUtil.isNotBlank(param.getSuperclassName())) {
            if (superclass == null) {
                superclass = new SuperclassVO(param.getSuperclassName());
            } else {
                superclass.setName(param.getSuperclassName());
            }
        } else {
            superclass = null;
        }
        templateParamVO.setSuperclass(superclass);
        entity.setTemplateParams(JsonUtil.toJson(templateParamVO));
        return this.updateById(entity);
    }
    
    /**
     * 修改代码地址
     *
     * @param templateId
     * @param codePath
     * @return
     */
    @Override
    public boolean updateCodePathByTemplateId(final Long templateId, final String codePath, List<String> codeTypeList) {
        final LambdaUpdateWrapper<CodeGenerationTemplateConfigEntity> updateWrapper = Wrappers.<CodeGenerationTemplateConfigEntity>lambdaUpdate()
                .set(CodeGenerationTemplateConfigEntity::getCodePath, codePath)
                .eq(CodeGenerationTemplateConfigEntity::getTemplateId, templateId)
                .in(CollUtil.isNotEmpty(codeTypeList), CodeGenerationTemplateConfigEntity::getName, codeTypeList);
        return this.update(updateWrapper);
    }
    
    /**
     * 获取代码配置详情
     *
     * @param id
     * @return
     */
    @Override
    public CodeGenerationTemplateConfigVO getDetailById(final Long id) {
        final CodeGenerationTemplateConfigEntity entity = this.getById(id);
        return PojoConvertUtil.entity2Vo(entity, CodeGenerationTemplateConfigVO.class);
    }
    
    /**
     * 获取模板配置
     *
     * @param templateId
     * @param name
     * @return
     */
    @Override
    public CodeGenerationTemplateConfigVO getCodeGenerationTemplateConfig(final Long templateId, final String name) {
        final LambdaQueryWrapper<CodeGenerationTemplateConfigEntity> wrapper = Wrappers.<CodeGenerationTemplateConfigEntity>lambdaQuery()
                .eq(CodeGenerationTemplateConfigEntity::getTemplateId, templateId).eq(CodeGenerationTemplateConfigEntity::getName, name);
        return PojoConvertUtil.entity2Vo(this.getOne(wrapper), CodeGenerationTemplateConfigVO.class);
    }
    
    /**
     * 根据模板ID获取配置信息
     *
     * @param templateId
     * @param hideStatus
     * @param nameList
     * @return
     */
    @Override
    public List<CodeGenerationTemplateConfigVO> getByTemplateId(final Long templateId, final Integer hideStatus,
            final List<String> nameList) {
        final LambdaQueryWrapper<CodeGenerationTemplateConfigEntity> wrapper = Wrappers.<CodeGenerationTemplateConfigEntity>lambdaQuery()
                .eq(CodeGenerationTemplateConfigEntity::getTemplateId, templateId)
                .in(CollUtil.isNotEmpty(nameList), CodeGenerationTemplateConfigEntity::getName, nameList)
                .eq(hideStatus != null, CodeGenerationTemplateConfigEntity::getHideStatus, hideStatus);
        return PojoConvertUtil.entity2VoList(this.list(wrapper), CodeGenerationTemplateConfigVO.class);
    }
    
    /**
     * 删除代码配置
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(final Long id) {
        final CodeGenerationTemplateConfigEntity detail = this.getById(id);
        if (detail != null && this.removeById(id)) {
            return true;
        }
        throw new CommonRuntimeException(ErrorCode.DELETED_FAILED);
    }
    
    /**
     * @param templateId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByTemplateId(final Long templateId) {
        final LambdaQueryWrapper<CodeGenerationTemplateConfigEntity> wrapper = Wrappers.<CodeGenerationTemplateConfigEntity>lambdaQuery()
                .eq(CodeGenerationTemplateConfigEntity::getTemplateId, templateId);
        final List<CodeGenerationTemplateConfigEntity> entities = this.list(wrapper);
        if (CollUtil.isNotEmpty(entities)) {
            for (final CodeGenerationTemplateConfigEntity entity : entities) {
                if (!this.removeById(entity.getId())) {
                    throw new CommonRuntimeException(ErrorCode.DELETED_FAILED);
                }
            }
        }
        return true;
    }
    
    /**
     * 更新配置隐藏状态
     *
     * @param id
     * @param hideStatus
     * @return
     */
    @Override
    public boolean updateHideStatus(final Long id, final Integer hideStatus) {
        final LambdaUpdateWrapper<CodeGenerationTemplateConfigEntity> updateWrapper = Wrappers.<CodeGenerationTemplateConfigEntity>lambdaUpdate()
                .eq(CodeGenerationTemplateConfigEntity::getId, id).set(CodeGenerationTemplateConfigEntity::getHideStatus, hideStatus);
        return this.update(updateWrapper);
    }
    
    
    /**
     * 获取模板表字段
     *
     * @param templateId
     * @param name
     * @param tableFieldColumnParam
     * @return
     */
    @Override
    public List<TableFieldColumnVO> getTemplateTableFieldColumnList(final Long templateId, final String name,
            final TableFieldColumnParam tableFieldColumnParam) {
        final List<String> defaultIgFields = new ArrayList<>();
        CodeGenerationTemplateConfigVO config = getCodeGenerationTemplateConfig(templateId, name);
        if (config != null && StrUtil.isNotBlank(config.getTemplateParams())) {
            final TemplateParamVO templateParamVO = JsonUtil.toBean(config.getTemplateParams(), TemplateParamVO.class);
            if (templateParamVO != null && CollUtil.isNotEmpty(templateParamVO.getDefaultIgFields())) {
                defaultIgFields.addAll(templateParamVO.getDefaultIgFields());
            }
        }
        return getTemplateTableFieldColumnList(tableFieldColumnParam, (v) -> {
            final TableFieldColumnVO fieldColumnVO = new TableFieldColumnVO();
            fieldColumnVO.setComment(v.getComment());
            fieldColumnVO.setColumn(v.getColumn());
            fieldColumnVO.setJavaType(v.getJavaType());
            fieldColumnVO.setJavaTypeSimpleName(v.getJavaTypeSimpleName());
            fieldColumnVO.setJdbcType(v.getJdbcType());
            fieldColumnVO.setProperty(v.getProperty());
            fieldColumnVO.setPrimaryKey(v.getPrimaryKey());
            fieldColumnVO.setDataType(v.getDataType());
            if (!defaultIgFields.isEmpty()) {
                fieldColumnVO.setSelected(!defaultIgFields.contains(v.getColumn()) && !defaultIgFields.contains(v.getProperty()));
            } else {
                fieldColumnVO.setSelected(true);
            }
            return fieldColumnVO;
        });
    }
    
    /**
     * 获取默认需要生成的模板配置
     *
     * @param templateId
     * @return
     */
    @Override
    public List<String> getDefaultGenerateTemplateConfigList(final Long templateId) {
        List<CodeGenerationTemplateConfigVO> list = getByTemplateId(templateId, null, null);
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().filter(v -> {
                final TemplateParamVO templateParamVO = JsonUtil.toBean(v.getTemplateParams(), TemplateParamVO.class);
                return templateParamVO != null && Boolean.TRUE.equals(templateParamVO.getDefaultGenerate());
            }).map(CodeGenerationTemplateConfigVO::getName).collect(Collectors.toList());
        }
        return List.of();
    }
    
    /**
     * 获取表字段信息列表
     *
     * @param tableFieldColumnParam
     * @return
     */
    @Override
    public <T> List<T> getTemplateTableFieldColumnList(final TableFieldColumnParam tableFieldColumnParam,
            final Function<ITableColumnJava, T> function) {
        final List<TableColumnJavaMap> tableColumnJavaMapList = DataSourceFactory.getInstance()
                .getTableColumnJavaMapList(tableFieldColumnParam.getConnectionId(), tableFieldColumnParam.getDataBaseName(),
                        tableFieldColumnParam.getSchemaName(), tableFieldColumnParam.getTableName());
        if (CollUtil.isNotEmpty(tableColumnJavaMapList)) {
            return tableColumnJavaMapList.stream().map(function).collect(Collectors.toList());
        }
        return null;
    }
}
