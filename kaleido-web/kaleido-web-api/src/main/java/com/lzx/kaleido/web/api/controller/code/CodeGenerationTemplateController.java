package com.lzx.kaleido.web.api.controller.code;

import com.lzx.kaleido.domain.api.annotations.LogRecord;
import com.lzx.kaleido.domain.api.code.ICodeGenerationTemplateConfigService;
import com.lzx.kaleido.domain.api.code.ICodeGenerationTemplateService;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationGlobalConfigParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationSimpleParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTemplateExportParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTemplateQueryParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTemplateUpdateParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.TableFieldColumnParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateViewVO;
import com.lzx.kaleido.domain.model.vo.datasource.TableFieldColumnVO;
import com.lzx.kaleido.infra.base.annotations.validation.AddGroup;
import com.lzx.kaleido.infra.base.annotations.validation.UpdateGroup;
import com.lzx.kaleido.infra.base.constant.Constants;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.pojo.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 代码模板管理接口
 *
 * @author lwp
 * @date 2023-12-15
 **/
@RestController
@RequestMapping(Constants.API_VERSION + "/code-tp")
public class CodeGenerationTemplateController {
    
    @Resource
    private ICodeGenerationTemplateService codeGenerationGroupService;
    
    @Resource
    private ICodeGenerationTemplateConfigService codeGenerationTemplateConfigService;
    
    /**
     * 新增标准协议模板
     *
     * @param vo
     * @return
     */
    @LogRecord
    @PostMapping("/standard/add")
    public R<Long> addStandardCodeGenerationTemplate(@RequestBody @Validated(AddGroup.class) CodeGenerationTemplateViewVO vo) {
        final Long id = codeGenerationGroupService.addStandardCodeGenerationTemplate(vo);
        return R.result(id != null, ErrorCode.SAVE_FAILED, id);
    }
    
    /**
     * 复制新增代码模板
     *
     * @param param
     * @return
     */
    @PostMapping("/add/copy")
    public R<Long> addWithCopy(@RequestBody CodeGenerationSimpleParam param) {
        final Long id = codeGenerationGroupService.addTemplateWithCopy(param.getTemplateId(), param.getTemplateName());
        return R.result(id != null, ErrorCode.SAVE_FAILED, id);
    }
    
    /**
     * 模板名称是否存在
     *
     * @param param
     * @return
     */
    @PostMapping("/templateName/exists")
    public R<Boolean> checkTemplateNameExists(@RequestBody CodeGenerationSimpleParam param) {
        final boolean isExist = codeGenerationGroupService.checkTemplateName(param.getTemplateId(), param.getTemplateName());
        return R.success(isExist);
    }
    
    
    /**
     * 模板导出
     *
     * @param vo
     */
    @PostMapping("/export")
    public void export(@RequestBody CodeGenerationTemplateExportParam vo) {
        //TODO
    }
    
    /**
     * 查询
     *
     * @param param
     * @return
     */
    @PostMapping("/list")
    public R<List<CodeGenerationTemplateVO>> queryCodeGenerationTemplate(@RequestBody CodeGenerationTemplateQueryParam param) {
        return R.success(codeGenerationGroupService.queryByParam(param));
    }
    
    
    /**
     * 更新代码模板
     *
     * @param id
     * @param vo
     * @return
     */
    @PutMapping("/{id}/update")
    public R<Boolean> updateCodeGenerationTemplate(@PathVariable("id") Long id,
            @RequestBody @Validated(UpdateGroup.class) CodeGenerationTemplateVO vo) {
        final boolean isSuccess = codeGenerationGroupService.updateById(id, vo);
        return R.result(isSuccess, ErrorCode.UPDATE_FAILED, isSuccess);
    }
    
    /**
     * 更新部分代码模板
     *
     * @param vo
     * @return
     */
    @PutMapping("/partition/update")
    public R<Boolean> updateCodeGenerationTemplateOfPartition(@RequestBody @Validated CodeGenerationTemplateUpdateParam vo) {
        final boolean isSuccess = codeGenerationGroupService.updateCodeGenerationTemplateOfPartition(vo);
        return R.result(isSuccess, ErrorCode.UPDATE_FAILED, isSuccess);
    }
    
    /**
     * 更新全局配置
     *
     * @param vo
     * @return
     */
    @PutMapping("/updateGlobalConfig")
    public R<Boolean> updateGlobalConfig(@RequestBody CodeGenerationGlobalConfigParam param) {
        final boolean isSuccess = codeGenerationGroupService.updateGlobalConfig(param);
        return R.result(isSuccess, ErrorCode.UPDATE_FAILED, isSuccess);
    }
    
    /**
     * 更新代码模板名称
     *
     * @param param
     * @return
     */
    @PutMapping("/templateName/update")
    public R<Boolean> updateTemplateNameById(@RequestBody CodeGenerationSimpleParam param) {
        final boolean isSuccess = codeGenerationGroupService.updateTemplateNameById(param.getTemplateId(), param.getTemplateName());
        return R.result(isSuccess, ErrorCode.UPDATE_FAILED, isSuccess);
    }
    
    /**
     * 更新为默认模板
     *
     * @param id
     * @return
     */
    @LogRecord
    @PutMapping("/default/{id}/update")
    public R<Boolean> updateDefaultTemplate(@PathVariable("id") Long id) {
        final boolean isSuccess = codeGenerationGroupService.updateDefaultTemplate(id);
        return R.result(isSuccess, ErrorCode.UPDATE_FAILED);
    }
    
    
    /**
     * 获取模板详情
     *
     * @param param
     * @return
     */
    @PostMapping("/template/info")
    public R<CodeGenerationTemplateVO> getCodeGenerationTemplate(@RequestBody CodeGenerationSimpleParam param) {
        return R.success(codeGenerationGroupService.getCodeGenerationTemplate(param));
    }
    
    /**
     * 删除模板
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}/delete")
    public R<Boolean> deleteCodeGenerationTemplate(@PathVariable("id") Long id) {
        final boolean isSuccess = codeGenerationGroupService.deleteById(id);
        return R.result(isSuccess, ErrorCode.DELETED_FAILED);
    }
    
    /**
     * 更新模板配置显示/隐藏状态
     *
     * @param id
     * @param hideStatus
     * @return
     */
    @PutMapping("/config/{id}/{hideStatus}/status")
    public R<Boolean> updateCodeGenerationTemplateConfigHideStatus(@PathVariable("id") Long id,
            @PathVariable("hideStatus") Integer hideStatus) {
        final boolean isSuccess = codeGenerationTemplateConfigService.updateHideStatus(id, hideStatus);
        return R.result(isSuccess, ErrorCode.UPDATE_FAILED);
    }
    
    /**
     * 获取模板表字段
     *
     * @param templateId
     * @param name
     * @param param
     * @return
     */
    @PostMapping("{templateId}/{name}/table/column/fields")
    public R<List<TableFieldColumnVO>> getTemplateTableFieldColumnList(@PathVariable("templateId") Long templateId,
            @PathVariable("name") String name, @RequestBody TableFieldColumnParam param) {
        final List<TableFieldColumnVO> templateTableFieldColumnList = codeGenerationTemplateConfigService.getTemplateTableFieldColumnList(
                templateId, name, param);
        return R.success(templateTableFieldColumnList);
    }
}
