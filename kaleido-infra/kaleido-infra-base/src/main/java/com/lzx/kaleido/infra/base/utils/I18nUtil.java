package com.lzx.kaleido.infra.base.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;

/**
 * @author lwp
 * @date 2023-11-15
 **/
@Slf4j
@UtilityClass
public class I18nUtil {
    
    private static MessageSource messageSource;
    
    static {
        messageSource = SpringUtil.getBean(MessageSource.class);
    }
    
    /**
     * @param messageCode
     * @return
     */
    public String getMessage(String messageCode) {
        return getMessage(messageCode, null);
    }
    
    /**
     * @param messageCode
     * @param args
     * @return
     */
    public String getMessage(String messageCode, @Nullable Object[] args) {
        try {
            return messageSource.getMessage(messageCode, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            log.error("no message.", e);
        }
        return messageSource.getMessage(ErrorCode.FAILED.getCode(), args, LocaleContextHolder.getLocale());
    }
}
