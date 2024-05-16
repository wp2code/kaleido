package com.lzx.kaleido.domain.model.vo.code;

import com.lzx.kaleido.domain.model.vo.datasource.TableFieldColumnVO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author lwp
 * @date 2024-01-19
 **/
@Data
@SuperBuilder
public class CodeGenerationViewVO implements Serializable {
    
    /**
     * 生成代码名称
     */
    protected String name;
    
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
     * 模板代码
     */
    private String templateCode;
    
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
     * 代码路径
     */
    private String codeOutPath;
    
    /**
     *
     */
    private String codeType;
    
    /**
     * 主键字段
     */
    private String primaryKey;
    
    /**
     * 实现接口（serviceImpl）
     */
    private String implInterfaceName;
    
    /**
     * 空间（xml）
     */
    private String namespace;
    
    /**
     * 字段映射
     */
    private List<TableFieldColumnVO> tableFieldColumnMap;
    
}
