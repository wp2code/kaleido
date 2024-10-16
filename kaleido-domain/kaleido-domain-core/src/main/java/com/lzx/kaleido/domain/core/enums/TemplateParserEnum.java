package com.lzx.kaleido.domain.core.enums;

import cn.hutool.core.lang.Singleton;
import com.lzx.kaleido.domain.core.code.processor.ITemplateProcessor;
import com.lzx.kaleido.domain.core.code.processor.impl.ControllerTemplateProcessorImpl;
import com.lzx.kaleido.domain.core.code.processor.impl.EntityTemplateProcessorImpl;
import com.lzx.kaleido.domain.core.code.processor.impl.MapperTemplateProcessorImpl;
import com.lzx.kaleido.domain.core.code.processor.impl.ServiceApiTemplateProcessorImpl;
import com.lzx.kaleido.domain.core.code.processor.impl.ServiceTemplateProcessorImpl;
import com.lzx.kaleido.domain.core.code.processor.impl.VoTemplateProcessorImpl;
import com.lzx.kaleido.domain.core.code.processor.impl.XmlTemplateProcessorImpl;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lwp
 * @date 2024-01-18
 **/
@Getter
@RequiredArgsConstructor
public enum TemplateParserEnum {
    
    ENTITY("Entity", Singleton.get(EntityTemplateProcessorImpl.class), 1, "tp_entity.ftlx", "java", "Entity"),
    VO("VO", Singleton.get(VoTemplateProcessorImpl.class), 2, "tp_vo.ftlx", "java", "VO"),
    MAPPER("Mapper", Singleton.get(MapperTemplateProcessorImpl.class), 3, "tp_mapper.ftlx", "java", "Mapper"),
    XML("Xml", Singleton.get(XmlTemplateProcessorImpl.class), 4, "tp_xml.ftlx", "xml", "Mapper"),
    SERVICE_API("ServiceApi", Singleton.get(ServiceApiTemplateProcessorImpl.class), 5, "tp_service_api.ftlx", "java", "Service"),
    SERVICE("Service", Singleton.get(ServiceTemplateProcessorImpl.class), 6, "tp_service.ftlx", "java", "ServiceImpl"),
    CONTROLLER("Controller", Singleton.get(ControllerTemplateProcessorImpl.class), 7, "tp_controller.ftlx", "java", "Controller"),
    ;
    
    private final String codeType;
    
    private final ITemplateProcessor templateParser;
    
    /**
     * 优先权重 -越小越优先执行
     */
    private final int priority;
    
    /**
     * 默认模板名称
     */
    private final String defaultTemplateName;
    
    /**
     * 文件名称后缀
     */
    private final String fileSuffix;
    
    /**
     * 默认文件名称后缀
     */
    private final String defaultNameSuffix;
    
    public static TemplateParserEnum getInstance(String codeType) {
        return Arrays.stream(values()).filter(v -> v.getCodeType().equalsIgnoreCase(codeType)).findFirst().orElse(null);
    }
    
    public static boolean isEntity(String codeType) {
        return ENTITY.getCodeType().equalsIgnoreCase(codeType);
    }
    
    /**
     * @param codeType
     * @return
     */
    public static boolean isMapper(String codeType) {
        return MAPPER.getCodeType().equalsIgnoreCase(codeType);
    }
    
    /**
     * @param codeType
     * @return
     */
    public static boolean isXml(String codeType) {
        return XML.getCodeType().equalsIgnoreCase(codeType);
    }
    
    /**
     * @param codeType
     * @return
     */
    public static boolean isVo(String codeType) {
        return VO.getCodeType().equalsIgnoreCase(codeType);
    }
    
    public static boolean isServiceApi(String codeType) {
        return SERVICE_API.getCodeType().equalsIgnoreCase(codeType);
    }
}
