package com.lzx.kaleido.domain.model.vo.serializer;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.lzx.kaleido.domain.model.vo.code.template.TemplateParamVO;

import java.io.IOException;

/**
 * @author lwp
 * @date 2024-06-19
 **/
public class TemplateParamSerializer extends JsonSerializer<String> {
    
    @Override
    public void serialize(final String value, final JsonGenerator gen, final SerializerProvider serializers) throws IOException {
        final TemplateParamVO templateParamVO = JSONUtil.toBean(value, TemplateParamVO.class);
        gen.writeObject(templateParamVO);
    }
}
