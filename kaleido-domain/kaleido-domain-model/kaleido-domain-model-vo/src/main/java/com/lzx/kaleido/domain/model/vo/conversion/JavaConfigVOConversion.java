package com.lzx.kaleido.domain.model.vo.conversion;

import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.domain.model.vo.code.template.JavaConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaControllerConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaEntityConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaMapperConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaServiceConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaVoConfigVO;
import com.lzx.kaleido.domain.model.vo.code.template.java.JavaXmlConfigVO;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import com.lzx.kaleido.plugins.mapstruct.annotations.json.JsonConvert;
import com.lzx.kaleido.plugins.mapstruct.conversion.JsonConversion;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lwp
 * @date 2023-12-13
 **/
@Component
@JsonConvert
public class JavaConfigVOConversion implements JsonConversion<JavaConfigVO> {
    
    private final static String MAP_KEY = "configName";
    
    private static final Map<String, Class<? extends JavaConfigVO>> MAP = new HashMap<>();
    
    static {
        MAP.put(JavaControllerConfigVO.NAME, JavaControllerConfigVO.class);
        MAP.put(JavaMapperConfigVO.NAME, JavaMapperConfigVO.class);
        MAP.put(JavaVoConfigVO.NAME, JavaVoConfigVO.class);
        MAP.put(JavaEntityConfigVO.NAME, JavaEntityConfigVO.class);
        MAP.put(JavaServiceConfigVO.NAME, JavaServiceConfigVO.class);
        MAP.put(JavaXmlConfigVO.NAME, JavaXmlConfigVO.class);
    }
    
    /**
     * @param json
     * @return
     */
    @Override
    public JavaConfigVO jsonToBean(final String json) {
        if (StrUtil.isNotBlank(json)) {
            final Map<?, ?> map = JsonUtil.toMap(json);
            Object configName = null;
            if (map.containsKey(MAP_KEY) && (configName = map.get(MAP_KEY)) != null) {
                return JsonUtil.toBean(json, MAP.get(configName.toString()));
            }
        }
        return null;
    }
    
}
