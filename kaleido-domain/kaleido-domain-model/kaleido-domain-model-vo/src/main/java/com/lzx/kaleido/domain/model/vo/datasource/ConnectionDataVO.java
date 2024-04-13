package com.lzx.kaleido.domain.model.vo.datasource;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 连接响应结果
 *
 * @author lwp
 * @date 2024-03-10
 **/
@Data
public class ConnectionDataVO implements Serializable {
    
    /**
     * 连接ID
     */
    private String connectionId;
    
    /**
     * 数据库连接类型
     */
    private String type;
    
    /**
     * 数据库信息
     */
    private List<SimpleDatabase> databases;
    
    @Data
    public static class SimpleDatabase {
        
        /**
         * 数据库名称
         */
        private String dataBaseName;
        
        /**
         * 数据库说明
         */
        private String dataBaseComment;
    }
}
