package com.lzx.kaleido.domain.core.enums;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lwp
 * @date 2024-01-19
 **/
@Getter
@AllArgsConstructor
public enum ApiTemplateEnum {
    insert("insertSelective", "添加", "int", "object", false, false),
    insertOne("insertOne", "添加单个", "int", "object", false, false),
    insertList("insertList", "批量添加", "int", "objectList", false, true),
    insertOrUpdate("insertOrUpdateSelective", "添加或更新", "int", "object", false, false),
//    insertOrUpdateBatch("insertOrUpdateSelectiveBatch", "批量添加或更新", "int", "objectList", false, true),
    delete("deleteByPrimaryKey", "根据主键删除", "int", "pk", false, false),
    update("updateByPrimaryKey", "根据主键更新", "int", "object", false, false),
    updateNotNull("updateByPrimaryKeySelective", "更新不为空的数据", "int", "object", false, false),
    select("selectByEntity", "根据对象查询", "objectList", "object", true, false),
    selectOne("selectByPrimaryKey", "根据主键查询", "object", "pk", false, false),
    selectPage("selectPage", "分页查询", "objectList", "object", true, false);
    
    private final String apiId;
    
    private final String describe;
    
    private final String returnType;
    
    private final String parameterType;
    
    private final boolean returnList;
    
    private final boolean parameterList;
    
    
    public static ApiTemplateEnum getInstance(String apiId) {
        return Arrays.stream(values()).filter(v -> v.getApiId().equals(apiId)).findFirst().orElse(null);
    }
    
    public static List<String> getAllApi() {
        return Arrays.stream(values()).map(v -> v.apiId).toList();
    }
}
