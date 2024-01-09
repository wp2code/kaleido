package com.lzx.kaleido.plugins.mapstruct.conversion;


import com.lzx.kaleido.plugins.mapstruct.annotations.date.DateConvert;
import com.lzx.kaleido.plugins.mapstruct.annotations.date.DateToStr;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * @author lwp
 * @date 2022-07-05
 */

@DateConvert
@Component
public class DateConversion {
    
    /**
     * @param dateStr
     * @return
     */
    @DateToStr
    public Date dateToStr(String dateStr) {
        if (Objects.isNull(dateStr) || "".equals(dateStr)) {
            return null;
        }
        try {
            return Date.from(LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.systemDefault())
                    .toInstant());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @DateToStr
    public String strToDate(Date date) {
        if (date == null) {
            return null;
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
