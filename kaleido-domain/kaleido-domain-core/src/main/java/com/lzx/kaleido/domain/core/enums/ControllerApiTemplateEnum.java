package com.lzx.kaleido.domain.core.enums;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lwp
 * @date 2024-01-13
 **/
@Getter
@AllArgsConstructor
public enum ControllerApiTemplateEnum {
    search("search", "查询", "/search", "objectList", "object", "POST", true, false),
    searchPage("page", "分页查询", "/page/{pageSize}/{pageNumber}", "objectList", "object", "POST", true, false),
    detail("detail", "获取详情", "/get/{id}", "object", "pk", "GET", false, false),
    save("save", "保存", "/save", "Boolean", "POST", "object", false, false),
    update("update", "根据主键更新", "/update/{id}", "Boolean", "object_pk", "PUT", false, false),
    delete("delete", "根据主键删除", "/delete/{id}", "Boolean", "pk", "DELETE", false, false),
    ;
    
    private final String apiId;
    
    private final String describe;
    
    private final String path;
    
    private final String returnType;
    
    private final String parameterType;
    
    private final String method;
    
    private final boolean returnList;
    
    private final boolean parameterList;
    
    public static ControllerApiTemplateEnum getInstance(String apiId) {
        return Arrays.stream(values()).filter(v -> v.getApiId().equals(apiId)).findFirst().orElse(null);
    }
    
    public static boolean isPage(ControllerApiTemplateEnum apiTemplateEnum) {
        return searchPage == apiTemplateEnum;
    }
    
    public static List<String> getAllApi() {
        return Arrays.stream(values()).map(v -> v.apiId).toList();
    }
}
