package com.lzx.kaleido.domain.api.datasource;

import com.lzx.kaleido.domain.model.dto.datasource.param.DataSourceConnectParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.DataSourceParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.DataSourceQueryParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.TableFieldColumnParam;
import com.lzx.kaleido.domain.model.vo.datasource.DataSourceMetaVO;
import com.lzx.kaleido.domain.model.vo.datasource.DataSourceVO;
import com.lzx.kaleido.domain.model.vo.datasource.TableFieldColumnVO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author lwp
 **/
public interface IDataSourceService {
    
    /**
     * 新增数据源连接
     *
     * @param param
     * @return
     */
    Long addDataSource(final DataSourceParam param);
    
    /**
     * 更新数据源连接
     *
     * @param id
     * @param param
     * @return
     */
    boolean updateById(@NotNull final Long id, final DataSourceParam param);
    
    /**
     * 删除数据源连接
     *
     * @param id
     * @return
     */
    boolean deleteById(@NotNull final Long id);
    
    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    DataSourceVO getDetailById(@NotNull final Long id);
    
    /**
     * 查询数据源列表
     *
     * @param param
     * @return
     */
    List<DataSourceVO> queryByParam(final DataSourceQueryParam param);
    
    /**
     * 获取表字段信息列表
     *
     * @param tableFieldColumnParam
     * @return
     */
    List<TableFieldColumnVO> getTableFieldColumnList(final TableFieldColumnParam tableFieldColumnParam);
    
    
    /**
     * 获取数量连接信息
     *
     * @param id
     * @param deepQuery
     * @param autoClose
     * @return
     */
    DataSourceMetaVO getDataSourceMeta(@NotNull final Long id, final boolean deepQuery,final boolean autoClose);
    
    /**
     * 测试连接数据库
     *
     * @param param
     * @return
     */
    boolean connectTestDataSource(final DataSourceConnectParam param);
}
