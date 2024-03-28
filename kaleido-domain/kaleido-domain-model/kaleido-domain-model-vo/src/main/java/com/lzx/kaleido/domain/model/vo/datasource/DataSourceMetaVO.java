package com.lzx.kaleido.domain.model.vo.datasource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author lwp
 **/
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DataSourceMetaVO extends AbsConnectionVO {
    
    private DataSourceVO dataSource;
    
    private List<DatabaseVO> dateBaseList;
    
}
