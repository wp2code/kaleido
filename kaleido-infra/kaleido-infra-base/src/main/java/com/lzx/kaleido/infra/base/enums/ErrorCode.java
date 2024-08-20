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
    REQUEST_PARAMS_BINDING_ERROR("PARAMS_BINDING_ERROR", "请求参数绑定异常"),
    REQUEST_API_NOT_EXISTS("REQUEST_API_NOT_EXISTS", "请求接口不存在"),
    REQUEST_API_NOT_SUPPORTED("REQUEST_API_NOT_SUPPORTED", "请求接口不支持"),
    
    SAVE_FAILED("SAVE_FAILED", "保存失败"),
    UPDATE_FAILED("UPDATE_FAILED", "更新失败"),
    DELETED_FAILED("DELETED_FAILED", "删除失败"),
    REQUEST_PARAMS_ERROR("REQUEST_PARAMS_ERROR", "请求参数错误"),
    CONNECTION_FAILED("CONNECTION_FAILED", "数据库连接失败"),
    CONNECTION_IS_NULL("CONNECTION_IS_NULL", "获取数据库连接失败"),
    CONNECTION_CONFIG_ABNORMAL("CONNECTION_CONFIG_FAILED", "数据库连接配置异常"),
    CONNECTION_JDBC_LOAD_FAILED("CONNECTION_JDBC_LOAD_FAILED", "数据库驱动加载失败"),
    CODE_TEMPLATE_CONFIG_NOT_EXITS("CODE_TEMPLATE_CONFIG_NOT_EXITS", "模板配置信息不存在"),
    CODE_TEMPLATE_PARSE_ERROR("CODE_TEMPLATE_PARSE_ERROR", "模板解析错误"),
    CODE_TEMPLATE_CONFIG_ERROR("CODE_TEMPLATE_CONFIG_ERROR", "模板配置错误"),
    CODE_TEMPLATE_NAME_EXISTS("CODE_TEMPLATE_NAME_EXISTS", "模板名称已存在"),
    ;
    
    ErrorCode(String code, String name) {
        this.initEnum(code, name);
    }
    
}
