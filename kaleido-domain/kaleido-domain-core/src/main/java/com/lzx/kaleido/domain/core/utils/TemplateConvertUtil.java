package com.lzx.kaleido.domain.core.utils;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.CaseFormat;
import com.lzx.kaleido.domain.model.vo.code.CodeGenerationViewVO;
import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import lombok.experimental.UtilityClass;

import java.util.function.Consumer;

/**
 * @author lwp
 * @date 2024-01-09
 **/
@UtilityClass
public class TemplateConvertUtil {
    
    
    /**
     * @param basicConfigJson
     * @return
     */
    public BasicConfigVO toBasicConfig(String basicConfigJson) {
        return JsonUtil.toBean(basicConfigJson, BasicConfigVO.class);
    }
    
    /**
     * @param value
     * @param defaultValues
     * @param consumer
     */
    public void setIfAbsent(Object value, Consumer<Object> consumer, Object... defaultValues) {
        final boolean isNotValueFlag = value == null || value.toString().length() <= 0 || StrUtil.isBlank(value.toString().trim());
        if (isNotValueFlag && defaultValues != null && consumer != null) {
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
     * 下划线和连接符
     *
     * @param str
     * @return
     */
    public String toCamelFirstToUpper(String str) {
        if (StrUtil.isBlank(str)) {
            return null;
        }
        if (!StrUtil.contains(str, StrUtil.UNDERLINE) && !StrUtil.contains(str, StrUtil.DASHED)) {
            return firstCharOnlyToUpper(str);
        }
        return StrUtil.contains(str, StrUtil.UNDERLINE) ? underlineToCamelFirstToUpper(str) : hyphenToCamelFirstToUpper(str);
    }
    
    /**
     * @param str
     * @return
     */
    public String hyphenToCamelFirstToUpper(String str) {
        return hyphenToCamel(str, true);
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
        if (StrUtil.containsAny(str, StrUtil.UNDERLINE)) {
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
        if (StrUtil.containsAny(str, StrUtil.DASHED)) {
            return CaseFormat.LOWER_HYPHEN.to(isUpper ? CaseFormat.UPPER_CAMEL : CaseFormat.LOWER_CAMEL, str);
        }
        return str;
    }
    
}
