package com.lzx.kaleido.web.api.controller.code;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.lzx.kaleido.domain.api.code.ICodeGeneration;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationAllParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationParam;
import com.lzx.kaleido.domain.model.dto.code.param.CodeGenerationTableParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationResultVO;
import com.lzx.kaleido.domain.model.vo.code.JavaTypeVO;
import com.lzx.kaleido.infra.base.constant.Constants;
import com.lzx.kaleido.infra.base.enums.JavaTypeEnum;
import com.lzx.kaleido.infra.base.pojo.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 代码生成管理接口
 *
 * @author lwp
 * @date 2023-12-14
 **/
@Slf4j
@Controller
@RequestMapping(Constants.API_VERSION + "/code-gen")
public class CodeGenerationController {
    
    @Resource
    private ICodeGeneration codeGeneration;
    
    /**
     * 代码预览-根据模板Id
     *
     * @param templateId
     * @param tableParam
     * @return
     */
    @Deprecated
    @PostMapping("/{templateId}/preview")
    public @ResponseBody R<CodeGenerationResultVO> previewByTemplateId(@PathVariable("templateId") Long templateId,
            @Validated @RequestBody CodeGenerationTableParam tableParam) {
        final CodeGenerationResultVO codeGenerationResultVO = codeGeneration.preview(templateId, tableParam);
        return R.success(codeGenerationResultVO);
    }
    
    /**
     * 代码预览
     *
     * @param codeGenerationAllParam
     * @return
     */
    @PostMapping("/preview")
    public @ResponseBody R<CodeGenerationResultVO> preview(@Validated @RequestBody CodeGenerationAllParam codeGenerationAllParam) {
        final CodeGenerationResultVO codeGenerationResultVO = codeGeneration.generationOrPreview(codeGenerationAllParam, true);
        return R.success(codeGenerationResultVO);
    }
    
    /**
     * 代码生成
     *
     * @param codeGenerationAllParam
     * @return
     */
    @PostMapping("/generation")
    public @ResponseBody R<CodeGenerationResultVO> generation(@Validated @RequestBody CodeGenerationAllParam codeGenerationAllParam) {
        final CodeGenerationResultVO codeGenerationResultVO = codeGeneration.generationOrPreview(codeGenerationAllParam, false);
        return R.success(codeGenerationResultVO);
    }
    
    /**
     * 代码生成-预览(响应流)
     *
     * @param vo
     * @return
     */
    @PostMapping("/preview/stream")
    public void preview(HttpServletResponse response, @Validated @RequestBody CodeGenerationParam vo) {
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType(ContentType.TEXT_HTML.toString());
            codeGeneration.preview(vo, out);
        } catch (IOException e) {
            log.error("生成代码预览异常", e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
    
    /**
     * @return
     */
    @GetMapping("/java/type")
    public @ResponseBody R<List<JavaTypeVO>> getJavaTypeList(@RequestParam(value = "cf", required = false) String classification) {
        final List<JavaTypeVO> javaTypeList = JavaTypeEnum.convert(v -> {
            final JavaTypeVO javaTypeVO = new JavaTypeVO();
            javaTypeVO.setType(v.getType());
            javaTypeVO.setSimpleType(v.getSimpleType());
            javaTypeVO.setClassification(v.getClassification());
            return javaTypeVO;
        }, (v) -> {
            if (StrUtil.isNotBlank(classification)) {
                return v.getClassification().equals(classification);
            }
            return true;
        });
        return R.success(javaTypeList);
    }
}
