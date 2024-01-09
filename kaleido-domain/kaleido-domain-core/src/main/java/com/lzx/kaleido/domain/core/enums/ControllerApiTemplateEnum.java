package com.lzx.kaleido.domain.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author lwp
 * @date 2024-01-13
 **/
@Getter
@AllArgsConstructor
public enum ControllerApiTemplateEnum {
    search("search", "查询", "/search", "objectList", "POST", true, false),
    searchPage("page", "分页查询", "/page", "objectList", "POST", true, false),
    detail("detail", "获取详情", "/get/{id}", "object", "GET", false, false),
    save("save", "保存", "/save", "Boolean", "POST", false, false),
    update("update", "根据主键更新", "/update/{id}", "Boolean", "PUT", false, false),
    delete("update", "根据主键删除", "/delete/{id}", "Boolean", "DELETE", false, false),
    ;
    
    private final String apiId;
    
    private final String describe;
    
    private final String path;
    
    private final String returnType;
    
    private final String method;
    
    private final boolean returnList;
    
    private final boolean parameterList;
    
    public static ControllerApiTemplateEnum getInstance(String apiId) {
        return Arrays.stream(values()).filter(v -> v.getApiId().equals(apiId)).findFirst().orElse(null);
    }
    
    public static boolean isPage(ControllerApiTemplateEnum apiTemplateEnum) {
        return searchPage == apiTemplateEnum;
    }
    
}
