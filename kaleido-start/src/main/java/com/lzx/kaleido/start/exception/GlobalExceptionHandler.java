package com.lzx.kaleido.start.exception;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.AbsException;
import com.lzx.kaleido.infra.base.excption.CommonException;
import com.lzx.kaleido.infra.base.pojo.R;
import com.lzx.kaleido.infra.base.utils.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常
 *
 * @author lwp
 * @date 2023-11-17
 **/
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(AbsException.class)
    public R<Object> handleAbsException(AbsException exception) {
        return R.fail(exception.getErrorCode(), null);
    }
    
    /**
     * @param exception
     * @return
     */
    @ExceptionHandler(CommonException.class)
    public R<Object> handleCommonException(CommonException exception) {
        return R.fail(exception.getErrorCode(), null);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error("参数验证失败！");
        return errorResult(exception.getBindingResult());
    }
    
    /**
     * @param exception
     * @return
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public R<Object> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        log.error("接口 ContentType {} not supported！", exception.getContentType());
        return R.fail(ErrorCode.REQUEST_API_NOT_SUPPORTED, null);
    }
    
    @ExceptionHandler(NoHandlerFoundException.class)
    public R<Object> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("请求接口不存在！{}", e.getRequestURL());
        return R.fail(ErrorCode.REQUEST_API_NOT_EXISTS, e.getRequestURL());
    }
    
    /**
     * @param e
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public R<Object> handleThrowable(Throwable e) {
        log.error("系统异常", e);
        return R.fail(ErrorCode.FAILED, null);
    }
    
    /**
     * @param result
     * @return
     */
    private R<Object> errorResult(BindingResult result) {
        FieldError error = result.getFieldError();
        if (null == error) {
            return R.fail(ErrorCode.REQUEST_PARAMS_BINDING_ERROR, null);
        }
        String defaultMessage = error.getDefaultMessage();
        if (StrUtil.isNotBlank(defaultMessage)) {
            if (!PinyinUtil.isChinese(defaultMessage.charAt(0))) {
                defaultMessage = I18nUtil.getMessage(defaultMessage);
            }
        }
        return R.fail(ErrorCode.REQUEST_PARAMS_BINDING_ERROR, defaultMessage);
    }
}
