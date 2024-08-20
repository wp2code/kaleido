package com.lzx.kaleido.web.api.controller.code;

import cn.hutool.core.net.URLEncodeUtil;
import com.lzx.kaleido.domain.api.code.ICodeGenerationTemplateService;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateFileVO;
import com.lzx.kaleido.infra.base.constant.Constants;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.pojo.R;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import java.io.IOException;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 模板文件
 *
 * @author lwp
 * @date 2024-07-14
 **/
@Slf4j
@Controller
@RequestMapping(Constants.API_VERSION + "/code-tp/file")
public class CodeGenerationTemplateFileController {
    
    @Resource
    private ICodeGenerationTemplateService codeGenerationTemplateService;
    
    /**
     * 导出模板
     *
     * @param templateId 模板ID
     * @return 模板文件
     */
    @GetMapping("/export")
    public ResponseEntity<?> export(@RequestParam("templateId") String templateId) {
        try {
            final CodeGenerationTemplateFileVO templateVO = codeGenerationTemplateService.getCodeGenerationTemplateFile(
                    Long.parseLong(templateId));
            if (templateVO == null) {
                log.error("模板{}不存在", templateId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            final String templateStr = JsonUtil.toJson(templateVO, true);
            String fileName = URLEncodeUtil.encode("%s_%s".formatted(templateVO.getTemplateName(), System.currentTimeMillis()));
            final HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s.json".formatted(fileName));
            headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");
            return new ResponseEntity<>(templateStr, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("导出模板异常", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            
        }
    }
    
    /**
     * 导入模板文件
     *
     * @param file 文件
     * @return 模板ID
     */
    @PostMapping("/upload")
    public @ResponseBody R<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        final CodeGenerationTemplateFileVO vo = JsonUtil.toBean(file.getInputStream(), CodeGenerationTemplateFileVO.class);
        if (vo == null) {
            return R.fail(ErrorCode.CODE_TEMPLATE_PARSE_ERROR);
        }
        final Long templateId = codeGenerationTemplateService.addImportCodeGenerationTemplate(vo);
        return R.success(templateId);
    }
    
    /**
     * 保存导入上传模板信息
     *
     * @param vo 参数
     * @return 模板ID
     */
    @PostMapping("/upload/save")
    public @ResponseBody R<Long> saveUploadTemplate(@RequestBody CodeGenerationTemplateFileVO vo) {
        if (vo == null) {
            return R.fail(ErrorCode.CODE_TEMPLATE_CONFIG_ERROR);
        }
        final Long templateId = codeGenerationTemplateService.addImportCodeGenerationTemplate(vo);
        return R.success(templateId);
    }
}
