package com.lzx.kaleido.domain.model.vo.code.template;

import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationTemplateConfigVO;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import java.io.Serializable;
import lombok.Data;

/**
 * @author lwp
 * @date 2023-12-19
 **/
@Data
public class JavaConfigVO implements Serializable {
    
    /**
     * 源文件夹 src/main/java
     */
    protected String sourceFolder;
    
    /**
     * 代码存储地址
     */
    protected String codePath;
    
    /**
     * 包名称; 例如：com.xxx
     */
    protected String packageName;
    
    /**
     * 配置名称
     */
    protected String name;
    
    /**
     * 配置别名
     */
    protected String aliasName;
    
    /**
     * 名称后缀
     */
    protected String nameSuffix;
    
    public boolean validate() {
        return StrUtil.isNotBlank(name) && StrUtil.isNotBlank(sourceFolder) && StrUtil.isNotBlank(packageName);
    }
    
    @Override
    public String toString() {
        return sourceFolder + codePath + packageName + name + aliasName;
    }
    
    /**
     * @param templateId
     * @param templateContent
     * @return
     */
    public CodeGenerationTemplateConfigVO swapper(final Long templateId, final String templateContent) {
        if (!validate()) {
            throw new CommonRuntimeException(ErrorCode.CODE_TEMPLATE_CONFIG_ERROR, null);
        }
        final CodeGenerationTemplateConfigVO config = new CodeGenerationTemplateConfigVO();
        config.setName(this.name);
        config.setTemplateId(templateId);
        config.setTemplateContent(templateContent);
        config.setAlias(this.aliasName);
        config.setCodePath(this.codePath);
        config.setHideStatus(0);
        config.setTemplateParams(JsonUtil.toJson(this));
        return config;
    }
}
