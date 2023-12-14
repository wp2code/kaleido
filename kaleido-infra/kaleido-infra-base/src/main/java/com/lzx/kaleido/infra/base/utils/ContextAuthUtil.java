package com.lzx.kaleido.infra.base.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.lzx.kaleido.infra.base.model.AuthUserInfo;
import lombok.experimental.UtilityClass;

import java.util.Optional;

/**
 * 认证上下文 工具
 *
 * @author lwp
 * @date 2023-06-18
 **/
@UtilityClass
public class ContextAuthUtil {
    
    private final static TransmittableThreadLocal<AuthUserInfo> context = new TransmittableThreadLocal<>();
    
    public AuthUserInfo getUserInfo() {
        return context.get();
    }
    
    public String getUserId() {
        return Optional.ofNullable(getUserInfo()).map(AuthUserInfo::getUserId).orElse(null);
    }
    
    /**
     * @param defaultUserId
     * @return
     */
    public String getUserIdIfAbsent(String defaultUserId) {
        return Optional.ofNullable(getUserInfo()).map(AuthUserInfo::getUserId).orElse(defaultUserId);
    }
    
    /**
     * @param authUserInfo
     */
    public void addUserInfo(AuthUserInfo authUserInfo) {
        if (authUserInfo != null) {
            context.set(authUserInfo);
        }
    }
    
    /**
     *
     */
    public void remove() {
        context.remove();
    }
}
