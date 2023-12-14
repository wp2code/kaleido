package com.lzx.kaleido.infra.base.excption;

import com.lzx.kaleido.infra.base.enums.ErrorCode;

/**
 * @author lwp
 * @date 2023-11-18
 **/
public abstract class AbsException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    public AbsException(ErrorCode errorCode) {
        super(errorCode.getName());
        this.errorCode = errorCode;
    }
    
    public AbsException(ErrorCode errorCode, Throwable throwable) {
        super(throwable);
        this.errorCode = errorCode;
    }
    
    public AbsException(String error, Throwable throwable) {
        super(error, throwable);
        this.errorCode = ErrorCode.FAILED;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
