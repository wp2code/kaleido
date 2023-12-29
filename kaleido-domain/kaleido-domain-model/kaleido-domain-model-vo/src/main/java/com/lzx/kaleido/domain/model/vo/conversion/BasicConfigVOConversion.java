package com.lzx.kaleido.domain.model.vo.conversion;

import com.lzx.kaleido.domain.model.vo.code.template.BasicConfigVO;
import com.lzx.kaleido.infra.base.utils.JsonUtil;
import com.lzx.kaleido.plugins.mapstruct.annotations.json.JsonConvert;
import com.lzx.kaleido.plugins.mapstruct.conversion.JsonConversion;
import org.springframework.stereotype.Component;

/**
 * @author lwp
 * @date 2023-12-15
 **/
@Component
@JsonConvert
public class BasicConfigVOConversion implements JsonConversion<BasicConfigVO> {
    
    /**
     * @param json
     * @return
     */
    @Override
    public BasicConfigVO jsonToBean(final String json) {
        return JsonUtil.toBean(json, BasicConfigVO.class);
    }
}
