package com.lzx.kaleido.domain.core.utils;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.CaseFormat;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import lombok.experimental.UtilityClass;

import java.util.function.Consumer;

/**
 * @author lwp
 * @date 2024-01-09
 **/
@UtilityClass
public class TemplateConvertUtil {
    
    
    /**
     * @param value
     * @param defaultValues
     * @param consumer
     */
    public void setIfAbsent(Object value, Consumer<Object> consumer, Object... defaultValues) {
        if (value == null && defaultValues != null && consumer != null) {
            for (final Object defaultValue : defaultValues) {
                if (defaultValue != null) {
                    consumer.accept(defaultValue);
                    break;
                }
            }
        }
    }
    
    /**
     * @param viewVO
     * @return
     */
    public static String getFullPackageName(CodeGenerationViewVO viewVO) {
        if (viewVO != null) {
            StringBuilder sb = new StringBuilder();
            if (StrUtil.isNotBlank(viewVO.getPackageName())) {
                sb.append(viewVO.getPackageName());
                sb.append(".");
            }
            if (StrUtil.isNotBlank(viewVO.getName())) {
                sb.append(viewVO.getName());
            }
            return sb.toString();
        }
        return null;
    }
    
    /**
     * 下划线转驼峰
     * <p>
     * 例如：user_name
     * </P>
     *
     * @param str
     * @return
     */
    public String underlineToCamel(String str) {
        return underlineToCamel(str, false);
    }
    
    /**
     * @param str
     * @return
     */
    public String underlineToCamelToLower(String str) {
        return underlineToCamel(str, false).toLowerCase();
    }
    
    /**
     * @param str
     * @return
     */
    public String underlineToCamelFirstToUpper(String str) {
        return underlineToCamel(str, true);
    }
    
    /**
     * 下划线转驼峰
     *
     * @param str
     * @param isUpper 是否大写
     * @return
     */
    public String underlineToCamel(String str, boolean isUpper) {
        if (StrUtil.isBlank(str)) {
            return null;
        }
        if (StrUtil.containsAny(str, "_")) {
            return CaseFormat.LOWER_UNDERSCORE.to(isUpper ? CaseFormat.UPPER_CAMEL : CaseFormat.LOWER_CAMEL, str);
        }
        return str;
    }
    
    /**
     * @param str
     * @return
     */
    public String firstCharOnlyToUpper(String str) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, str);
    }
    
    /**
     * @param str
     * @return
     */
    public String firstCharOnlyToLower(String str) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, str);
    }
    
    /**
     * 中划线逻辑符转驼峰 例如：user-name
     *
     * @param str
     * @param isUpper 是否大写
     * @return
     */
    public String hyphenToCamel(String str, boolean isUpper) {
        if (StrUtil.isBlank(str)) {
            return null;
        }
        if (StrUtil.containsAny(str, "-")) {
            return CaseFormat.LOWER_HYPHEN.to(isUpper ? CaseFormat.UPPER_CAMEL : CaseFormat.LOWER_CAMEL, str);
        }
        return str;
    }
    
}
