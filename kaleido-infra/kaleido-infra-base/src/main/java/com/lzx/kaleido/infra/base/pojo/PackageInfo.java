package com.lzx.kaleido.infra.base.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * @author lwp
 * @date 2024-04-15
 **/
@Data
@Builder
public class PackageInfo {
    
    private String name;
    
    private String simpleName;
}
