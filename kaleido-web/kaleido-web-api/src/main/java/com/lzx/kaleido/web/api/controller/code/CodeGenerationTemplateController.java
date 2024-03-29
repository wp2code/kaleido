package com.lzx.kaleido.web.api.controller.code;

import com.lzx.kaleido.domain.api.annotations.LogRecord;
import com.lzx.kaleido.domain.api.code.ICodeGenerationTemplateConfigService;
import com.lzx.kaleido.domain.api.code.ICodeGenerationTemplateService;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTemplateQueryParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateVO;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateViewVO;
import com.lzx.kaleido.infra.base.annotations.validation.AddGroup;
import com.lzx.kaleido.infra.base.annotations.validation.UpdateGroup;
import com.lzx.kaleido.infra.base.constant.Constants;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.pojo.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
     * 新增代码模板
     *
     * @param vo
     * @return
     */
    @LogRecord
    @PostMapping("/add")
    public R<Long> addCodeGenerationTemplate(@RequestBody @Validated(AddGroup.class) CodeGenerationTemplateVO vo) {
        final Long id = codeGenerationGroupService.addCodeGenerationTemplate(vo);
        return R.result(id != null, ErrorCode.SAVE_FAILED, id);
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
        return R.result(isSuccess, ErrorCode.UPDATE_FAILED);
    }
    
    /**
     * 更新代码模板名称
     *
     * @param id
     * @param templateName
     * @return
     */
    @PutMapping("/{id}/{templateName}/update")
    public R<Boolean> updateTemplateNameById(@PathVariable("id") Long id, @PathVariable("templateName") String templateName) {
        final boolean isSuccess = codeGenerationGroupService.updateTemplateNameById(id, templateName);
        return R.result(isSuccess, ErrorCode.UPDATE_FAILED);
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
     * 查询模板详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}/get")
    public R<CodeGenerationTemplateVO> getCodeGenerationTemplate(@PathVariable("id") Long id) {
        return R.success(codeGenerationGroupService.getDetailById(id, null));
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
}
