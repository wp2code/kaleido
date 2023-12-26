package com.lzx.kaleido.plugins.template.model;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.lzx.kaleido.plugins.template.engine.impl.FreemarkerTemplateEngineImpl;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.Nullable;

import java.io.File;
import java.util.Map;

/**
 * @author lwp
 **/
@Getter
@SuperBuilder
public class TemplateContext {
    
    /**
     * 默认解析引擎
     */
    public final static String DEFAULT_ENGINE = FreemarkerTemplateEngineImpl.ENGINE_NAME;
    
    /**
     *
     */
    private static String DEFAULT_OUT_DIR_PATH =
            System.getProperty("user.home") + File.separator + ".kaleido" + File.separator + "template-out" + File.separator;
    
    /**
     * 模板参数信息
     */
    private Map<?, ?> templateParams;
    
    /**
     * 加载的模板名称
     */
    private String templateName;
    
    /**
     * 模板引擎名称
     */
    @Builder.Default
    private String engineName = TemplateContext.DEFAULT_ENGINE;
    
    /**
     * 输出到的文件地址
     */
    @Nullable
    private String outDirPath;
    
    /**
     * 输出到的文件名称
     */
    @Nullable
    private String outFileName;
    
    
    /**
     * 模板配置信息
     */
    private TemplateConfigInfo templateConfig;
    
    /**
     * @return
     */
    public String getFileFullPath() {
        if (StrUtil.isBlank(outDirPath)) {
            this.outDirPath = "%s%s".formatted(DEFAULT_OUT_DIR_PATH, getTemplateName());
            final File file = new File(outDirPath);
            if (!file.exists() && !file.mkdirs()) {
                return null;
            }
        }
        return "%s%s%s".formatted(outDirPath, File.separator, outFileName);
    }
    
    /**
     * @return
     */
    public String getTemplateName() {
        //根据模板类容摘要生成
        if (StrUtil.isBlank(templateName) && templateConfig != null && StrUtil.isNotBlank(templateConfig.getTemplateContent())) {
            this.templateName = DigestUtil.md5Hex16(templateConfig.getTemplateContent());
        }
        return this.templateName;
    }
}
