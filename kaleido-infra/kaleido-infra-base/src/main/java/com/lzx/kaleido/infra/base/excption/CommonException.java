package com.lzx.kaleido.infra.base.excption;

import com.lzx.kaleido.infra.base.enums.ErrorCode;

/**
 * @author lwp
 * @date 2023-11-18
 **/
public class CommonException extends Exception {
    
    private final ErrorCode errorCode;
    
    public CommonException(ErrorCode errorCode) {
        super(errorCode.getName());
        this.errorCode = errorCode;
    }
    
    public CommonException(ErrorCode errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }
    
    public CommonException(String message, Throwable throwable) {
        super(message, throwable);
        this.errorCode = ErrorCode.FAILED;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
