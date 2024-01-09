package com.lzx.kaleido.domain.core.enums;

import cn.hutool.core.lang.Singleton;
import com.lzx.kaleido.domain.core.resolver.ITemplateParser;
import com.lzx.kaleido.domain.core.resolver.impl.ControllerTemplateParserImpl;
import com.lzx.kaleido.domain.core.resolver.impl.EntityTemplateParserImpl;
import com.lzx.kaleido.domain.core.resolver.impl.MapperTemplateParserImpl;
import com.lzx.kaleido.domain.core.resolver.impl.ServiceApiTemplateParserImpl;
import com.lzx.kaleido.domain.core.resolver.impl.ServiceTemplateParserImpl;
import com.lzx.kaleido.domain.core.resolver.impl.VoTemplateParserImpl;
import com.lzx.kaleido.domain.core.resolver.impl.XmlTemplateParserImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * @author lwp
 * @date 2024-01-18
 **/
@Getter
@RequiredArgsConstructor
public enum TemplateParserEnum {
    
    ENTITY("entity", Singleton.get(EntityTemplateParserImpl.class), 1),
    VO("vo", Singleton.get(VoTemplateParserImpl.class), 2),
    MAPPER("mapper", Singleton.get(MapperTemplateParserImpl.class), 3),
    XML("xml", Singleton.get(XmlTemplateParserImpl.class), 4),
    SERVICE_API("serviceApi", Singleton.get(ServiceApiTemplateParserImpl.class), 5),
    SERVICE("service", Singleton.get(ServiceTemplateParserImpl.class), 6),
    CONTROLLER("controller", Singleton.get(ControllerTemplateParserImpl.class), 7);
    
    private final String codeType;
    
    private final ITemplateParser templateParser;
    
    /**
     * 优先权重 -越小越优先执行
     */
    private final int priority;
    
    public static TemplateParserEnum getInstance(String codeType) {
        return Arrays.stream(values()).filter(v -> v.getCodeType().equals(codeType)).findFirst().orElse(null);
    }
    
    public static boolean isEntity(String codeType) {
        return ENTITY.getCodeType().equals(codeType);
    }
    
    /**
     * @param codeType
     * @return
     */
    public static boolean isMapper(String codeType) {
        return MAPPER.getCodeType().equals(codeType);
    }
    
    /**
     * @param codeType
     * @return
     */
    public static boolean isVo(String codeType) {
        return VO.getCodeType().equals(codeType);
    }
    
    public static boolean isService(String codeType) {
        return SERVICE.getCodeType().equals(codeType);
    }
    
    public static boolean isServiceApi(String codeType) {
        return SERVICE_API.getCodeType().equals(codeType);
    }
}
