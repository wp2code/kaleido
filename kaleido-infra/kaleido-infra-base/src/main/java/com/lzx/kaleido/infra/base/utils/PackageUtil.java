package com.lzx.kaleido.infra.base.utils;

import cn.hutool.core.util.StrUtil;
import com.lzx.kaleido.infra.base.pojo.PackageInfo;
import lombok.experimental.UtilityClass;

/**
 * @author lwp
 * @date 2024-04-15
 **/
@UtilityClass
public class PackageUtil {
    
    /**
     * @param packageName
     * @return
     */
    public PackageInfo getPackageInfo(String packageName) {
        if (StrUtil.isBlank(packageName)) {
            return null;
        }
        if (StrUtil.contains(packageName, ".")) {
            final String simpleName = StrUtil.subAfter(packageName, ".", true);
            return PackageInfo.builder().name(packageName).simpleName(simpleName).build();
        }
        return PackageInfo.builder().name(packageName).simpleName(packageName).build();
    }
}
