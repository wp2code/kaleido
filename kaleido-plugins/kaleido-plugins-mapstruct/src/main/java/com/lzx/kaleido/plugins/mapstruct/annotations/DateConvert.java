package com.lzx.kaleido.plugins.mapstruct.annotations;

import org.mapstruct.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lwp
 * @date 2022-07-05
 */
@Qualifier
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
@Component
public @interface DateConvert {
}
