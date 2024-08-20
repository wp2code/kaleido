package com.lzx.kaleido.domain.model.vo.code;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import java.io.Serializable;
import java.util.function.Predicate;
import lombok.Data;

/**
 * @author lwp
 * @date 2024-07-15
 **/
@Data
public class CodeGenerationTemplateFileConfigVO implements Serializable {
    
    /**
     * 模板类型名称（支持：Entity|VO|Mapper|Xml|ServiceApi|Service|Controller）
     */
    private String name;
    
    /**
     * 模板类型别名
     */
    private String alias;
    
    /**
     * 代码保存路径 默认临时文件夹
     */
    private String codePath;
    
    /**
     * 模板信息
     */
    private String templateContent;
    
    /**
     * 模板参数
     */
    private String templateParams;
    
    public boolean validate(Predicate<String> predicate) {
        if (StrUtil.isBlank(codePath)) {
            codePath = FileUtil.getTmpDirPath();
        }
        return StrUtil.isNotBlank(name) && StrUtil.isNotBlank(templateParams) && predicate.test(name) && JsonUtil.validateJson(
                templateParams, "sourceFolder", "packageName", "name");
    }
}
