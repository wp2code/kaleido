package com.lzx.kaleido.infra.base.enums;

/**
 * 错误码
 *
 * @author lwp
 * @date 2023-11-17
 **/
public enum ErrorCode implements IBaseEnum<String> {
    SUCCESS("success", "操作成功"),
    FAILED("failed", "操作失败"),
    SAVE_FAILED("SAVE_FAILED", "保存失败"),
    UPDATE_FAILED("UPDATE_FAILED", "更新失败"),
    DELETED_FAILED("DELETED_FAILED", "删除失败"),
    CONNECTION_FAILED("CONNECTION_FAILED", "数据库连接失败"),
    CONNECTION_CONFIG_ABNORMAL("CONNECTION_CONFIG_FAILED", "数据库连接配置异常"),
    CONNECTION_JDBC_LOAD_FAILED("CONNECTION_JDBC_LOAD_FAILED", "数据库驱动加载失败"),
    ;
    
    ErrorCode(String code, String name) {
        this.initEnum(code, name);
    }
    
}
