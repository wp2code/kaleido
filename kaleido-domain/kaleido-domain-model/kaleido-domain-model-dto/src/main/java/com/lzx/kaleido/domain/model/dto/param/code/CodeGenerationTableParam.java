package com.lzx.kaleido.domain.model.dto.param.code;

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
     *
     */
    private String configName;
    
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
     * 模板路径
     */
    private String templatePath;
    
    /**
     * 表名称
     */
    private String tableName;
    
    /**
     * 表说明
     */
    private String comment;
    
    /**
     * 表所属模式
     */
    private String schemaName;
    
    /**
     * 代码名称
     */
    private String name;
    
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
     * 表字段信息
     */
    private List<CodeGenerationTableFieldParam> tableFieldColumnList;
    
    /**
     * mapper和xml接口名称
     */
    private List<String> apiList;
    
    /**
     * controller接口名称
     */
    private List<String> webApiList;
    
    
    public CodeGenerationTableFieldParam getPrimaryKeyField() {
        if (tableFieldColumnList == null) {
            return null;
        }
        return tableFieldColumnList.stream().filter(CodeGenerationTableFieldParam::isPrimaryKey).findFirst().orElse(null);
    }
}
