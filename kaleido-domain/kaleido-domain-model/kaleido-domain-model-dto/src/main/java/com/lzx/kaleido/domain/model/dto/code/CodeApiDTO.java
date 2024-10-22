package com.lzx.kaleido.domain.model.dto.code;

import lombok.Data;

/**
 * @author lwp
 * @date 2024-01-19
 **/
@Data
public class CodeApiDTO {
    
    private String apiId;
    
    private String returnType;
    
    private Boolean returnIsList;
    
    private String parameterType;
    
    private Boolean parameterIsList;
    
    private String describe;
    
    private String path;
    
    private String method;
    
    private Boolean pageQuery;
    
    public static CodeApiDTO of(String apiId, String returnType, Boolean returnIsList, String parameterType, Boolean parameterIsList,
            String describe, String path, String method, Boolean pageQuery) {
        final CodeApiDTO codeApiDTO = new CodeApiDTO();
        codeApiDTO.setApiId(apiId);
        codeApiDTO.setReturnType(returnType);
        codeApiDTO.setReturnIsList(returnIsList);
        codeApiDTO.setParameterType(parameterType);
        codeApiDTO.setParameterIsList(parameterIsList);
        codeApiDTO.setDescribe(describe);
        codeApiDTO.setPath(path);
        codeApiDTO.setMethod(method);
        codeApiDTO.setPageQuery(pageQuery);
        return codeApiDTO;
    }
    
    public static CodeApiDTO of(String apiId, String returnType, Boolean returnIsList, String parameterType, Boolean parameterIsList,
            String describe) {
        return of(apiId, returnType, returnIsList, parameterType, parameterIsList, describe, null, null, false);
    }
}
