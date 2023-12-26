package com.lzx.kaleido.web.api.controller.code;

import cn.hutool.http.ContentType;
import com.lzx.kaleido.domain.api.service.ICodeGeneration;
import com.lzx.kaleido.domain.model.dto.param.code.CodeGenerationParam;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationResultVO;
import com.lzx.kaleido.infra.base.constant.Constants;
import com.lzx.kaleido.infra.base.pojo.R;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * 代码生成-预览
     *
     * @param vo
     * @return
     */
    @PostMapping("/preview")
    public void preview(HttpServletResponse response, @RequestBody CodeGenerationParam vo) {
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
     * 代码生成
     *
     * @param vo
     * @return
     */
    
    @PostMapping("/generation")
    public @ResponseBody R<CodeGenerationResultVO> generation(@RequestBody CodeGenerationParam vo) {
        return R.success(codeGeneration.generation(vo));
    }
    
    /**
     * 代码生成-批量
     *
     * @param paramList
     * @return
     */
    @PostMapping("/generation/batch")
    public @ResponseBody R<List<CodeGenerationResultVO>> generationBatch(@RequestBody List<CodeGenerationParam> paramList) {
        return R.success(codeGeneration.generation(paramList));
    }
}
