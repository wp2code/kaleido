package com.lzx.kaleido.domain.model.vo.datasource;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author lwp
 * @date 2024-03-16
 **/
@Data
@SuperBuilder
public abstract class AbsConnectionVO implements Serializable {
    /**
     * 连接ID
     */
    private String connectionId;
}
