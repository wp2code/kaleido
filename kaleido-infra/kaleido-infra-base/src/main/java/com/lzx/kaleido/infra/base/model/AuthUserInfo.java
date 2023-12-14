package com.lzx.kaleido.infra.base.model;

import lombok.Data;

import javax.annotation.Resource;

/**
 * @author lwp
 * @date 2023-11-11
 **/
@Data
public class AuthUserInfo {
    private String appId;
    private String userId;
}
