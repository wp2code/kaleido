package com.lzx.kaleido.infra.base.utils;

import cn.hutool.core.util.StrUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;

/**
 * Id 生成器
 *
 * @author lwp
 * @date 2024-08-18
 **/
@UtilityClass
public class IdGenUtil {
    
    /**
     * 获取时间ID
     *
     * @param prefix
     * @return
     */
    public synchronized String getTimeId(String prefix) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return StrUtil.isBlank(prefix) ? time : "%s%s".formatted(prefix, time);
    }
}
