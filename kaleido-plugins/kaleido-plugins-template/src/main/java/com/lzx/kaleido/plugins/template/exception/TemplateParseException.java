package com.lzx.kaleido.plugins.template.exception;

import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.AbsException;

/**
 * @author lwp
 * @date 2023-12-16
 **/
public class TemplateParseException extends AbsException {
    
    public TemplateParseException(final ErrorCode errorCode) {
        super(errorCode);
    }
    
    public TemplateParseException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
