package com.lzx.kaleido.domain.model.dto.code;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lwp
 * @date 2024-01-15
 **/
@Data
@NoArgsConstructor
public class CodeClassDTO {
    
    private String name;
    
    private String fullName;
    
    public CodeClassDTO(String fullName) {
        this.fullName = fullName;
        if (fullName != null) {
            if (fullName.contains(".")) {
                this.name = fullName.substring(fullName.lastIndexOf(".") + 1);
            } else {
                this.name = fullName;
            }
        }
    }
}
