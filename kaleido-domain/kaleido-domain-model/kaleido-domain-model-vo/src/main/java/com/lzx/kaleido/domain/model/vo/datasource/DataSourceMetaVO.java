package com.lzx.kaleido.domain.model.vo.datasource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author lwp
 * @date 2023-11-18
 **/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceMetaVO {

    private DataSourceVO dataSource;
    
    private List<DatabaseVO> dateBaseList;
}
