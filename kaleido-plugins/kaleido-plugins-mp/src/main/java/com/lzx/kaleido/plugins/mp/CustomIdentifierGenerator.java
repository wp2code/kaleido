package com.lzx.kaleido.plugins.mp;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

/**
 * @author lwp
 * @date 2023-11-15
 **/
public class CustomIdentifierGenerator implements IdentifierGenerator {
    
    /**
     * 生成Id
     *
     * @param entity 实体
     * @return id
     */
    @Override
    public Number nextId(final Object entity) {
        return IdUtil.getSnowflakeNextId();
    }
}
