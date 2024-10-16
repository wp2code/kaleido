package com.lzx.kaleido.domain.model.dto.code.param;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lwp
 * @date 2024-01-10
 **/
@Data
public class CodeGenerationTableParam implements Serializable {
    
    /**
     * 连接ID
     */
    private String connectionId;
    /**
     * 代码模板名称
     */
    @NotEmpty(message = "代码模板名称不能为空")
    private String configName;
    
    /**
     * 表名称
     */
    @NotEmpty(message = "生成代码表名不能为空")
    private String tableName;
    
    /**
     * 代码名称
     */
    private String name;
    
    /**
     * 代码名称后缀
     */
    private String nameSuffix;
    
    /**
     * 代码模板加载模式
     */
    private String templateResourceMode = "CLASSPATH";
    
    /**
     * 模板引擎
     */
    private String templateEngineName;
    
    /**
     * 模板名称
     */
    private String templateName;
    
    /**
     * 数据库名称
     */
    private String dataBaseName;
    
    /**
     * 表所属模式
     */
    private String schemaName;
    
    /**
     * 模板路径
     */
    private String templatePath;
    
    /**
     * 表说明
     */
    private String tableComment;
    
    
    /**
     * 代码地址
     */
    private String codePath;
    
    /**
     * 包名称; 例如：com.xxx
     */
    private String packageName;
    
    
    /**
     * 源文件夹 src/main/java
     */
    private String sourceFolder;
    
    /**
     * 父类名称
     */
    private String superclassName;
    
    /**
     *
     */
    private Boolean useLombok;
    
    /**
     *
     */
    private Boolean useSwagger;
    
    /**
     *
     */
    private Boolean useMybatisPlus;
    
    /**
     *
     */
    private String responseGenericClass;
    
    /**
     *
     */
    private String implInterfaceName;
    
    /**
     *
     */
    private String namespace;
    
    /**
     * 表字段信息
     */
    private List<CodeGenerationTableFieldParam> tableFieldColumnList;
    
    /**
     * mapper和xml接口名称
     */
    private List<String> methodList;
    
    /**
     * controller接口名称
     */
    private List<String> webMethodList;
    
    /**
     * 是否生成代码文件
     */
    private boolean generationCodeFile;
    
    /**
     * 直接使用模板配置
     */
    private boolean directUseTemplateConfig;
    
    public CodeGenerationTableFieldParam getPrimaryKeyField() {
        if (tableFieldColumnList == null) {
            return null;
        }
        return tableFieldColumnList.stream().filter(CodeGenerationTableFieldParam::isPrimaryKey).findFirst().orElse(null);
    }
}
