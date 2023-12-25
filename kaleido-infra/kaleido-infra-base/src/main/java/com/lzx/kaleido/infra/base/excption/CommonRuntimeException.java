package com.lzx.kaleido.infra.base.excption;

import com.lzx.kaleido.infra.base.enums.ErrorCode;

/**
 * 代码生成配置
 * @author lwp
 * @date 2023-11-18
 **/
public class CommonRuntimeException extends AbsException {
    
    
    public CommonRuntimeException(final ErrorCode errorCode) {
        super(errorCode);
    }
    
    public CommonRuntimeException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
    
    public CommonRuntimeException(final Throwable throwable) {
        super(throwable.getMessage(), throwable);
    }
    
    /**
     * @param errorCode
     * @param throwable
     */
    public CommonRuntimeException(final ErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }
}
