package com.lzx.kaleido.infra.base.pojo;

import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.enums.IBaseEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author lwp
 * @date 2023-11-17
 **/
@Data
public class R<T> implements Serializable {
    
    private String code;
    
    private String message;
    
    private T data;
    
    /**
     * @param data
     * @param <T>
     * @return
     */
    public static <T> R<T> success(T data) {
        final R<T> r = new R<>();
        r.setCode(ErrorCode.SUCCESS.getCode());
        r.setMessage(ErrorCode.SUCCESS.getName());
        r.setData(data);
        return r;
    }
    
    /**
     * @param message
     * @param <T>
     * @return
     */
    public static <T> R<T> fail(String message) {
        return fail(ErrorCode.FAILED, message);
    }
    
    /**
     * @param errorCode
     * @param message
     * @param <T>
     * @return
     */
    public static <T> R<T> fail(ErrorCode errorCode, String message) {
        final R<T> r = new R<>();
        r.setCode(errorCode.getCode());
        r.setMessage(StrUtil.isNotBlank(message) ? message : errorCode.getName());
        return r;
    }
    
    /**
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> R<T> fail(ErrorCode errorCode) {
        return fail(errorCode,null);
    }
    /**
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> R<T> error(ErrorCode errorCode) {
        final R<T> r = new R<>();
        r.setCode(errorCode.getCode());
        r.setMessage(errorCode.getName());
        return r;
    }
    
    /**
     * @param success
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> R<T> result(boolean success, ErrorCode errorCode, T data) {
        final R<T> r = new R<>();
        r.setCode(success ? (errorCode = ErrorCode.SUCCESS).getCode()
                : Optional.ofNullable(errorCode).map(IBaseEnum::getCode).orElse(ErrorCode.FAILED.getCode()));
        r.setMessage(Optional.ofNullable(errorCode).map(IBaseEnum::getName).orElse(ErrorCode.FAILED.getName()));
        r.setData(data);
        return r;
    }
    
    /**
     * @param success
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> R<T> result(boolean success, ErrorCode errorCode) {
        return result(success, errorCode, null);
    }
}
