package com.lzx.kaleido.infra.base.enums;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lwp
 * @date 2024-04-15
 **/
@Getter
@RequiredArgsConstructor
public enum JavaTypeEnum implements Serializable {
    
    Byte("java.lang.Byte", "Byte", "Number"),
    Bytes("java.lang.byte[]", "byte[]", "Stream"),
    String("java.lang.String", "String", "String"),
    CHAR("java.lang.Character", "Character", "String"),
    Clob("java.sql.Clob", "Clob", "String"),
    Long("java.lang.Long", "Long", "Number"),
    Integer("java.lang.Integer", "Integer", "Number"),
    Float("java.lang.Float", "Float", "Number"),
    Double("java.lang.Double", "Double", "Number"),
    BigDecimal("java.math.BigDecimal", "BigDecimal", "Number"),
    Date("java.util.Date", "Date", "Date"),
    LocalDateTime("java.time.LocalDateTime", "LocalDateTime", "Date"),
    Time("java.sql.Time", "Time", "Date"),
    Boolean("java.lang.Boolean", "Boolean", "Boolean"),
    Object("java.lang.Object", "Object", "Object"),
    ;
    
    private final String type;
    
    /**
     *
     */
    private final String simpleType;
    
    /**
     * 归类
     */
    private final String classification;
    
    
    public static <T> List<T> convert(Function<JavaTypeEnum, T> function, Predicate<JavaTypeEnum> predicate) {
        return Arrays.stream(JavaTypeEnum.values()).filter(predicate).map(function).collect(Collectors.toList());
    }
    
}
