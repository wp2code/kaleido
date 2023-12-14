package com.lzx.kaleido.infra.base.annotations;

import cn.hutool.core.annotation.Alias;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyField {
    
    String key() default "";
    
    @Alias("key") String value();
    
    String ftm() default "";
}
