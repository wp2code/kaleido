package com.lzx.kaleido.domain.api.datasource;

import com.lzx.kaleido.domain.model.dto.datasource.param.DataSourceConnectParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.DataSourceParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.DataSourceQueryParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.TableDDLParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.TableFieldColumnParam;
import com.lzx.kaleido.domain.model.vo.datasource.ConnectionDataVO;
import com.lzx.kaleido.domain.model.vo.datasource.DataSourceMetaVO;
import com.lzx.kaleido.domain.model.vo.datasource.DataSourceVO;
import com.lzx.kaleido.domain.model.vo.datasource.DatabaseVO;
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
     * 获取数据连接数据
     *
     * @param id
     * @param deepQuery
     * @param autoClose
     * @return
     */
    DataSourceMetaVO getDataSourceMeta(@NotNull final Long id, final boolean deepQuery, final boolean autoClose);
    
    /**
     * @param param
     * @return
     */
    String getTableDDL(final TableDDLParam param);
    /**
     * 测试连接数据库
     *
     * @param param
     * @return
     */
    boolean connectTestDataSource(final DataSourceConnectParam param);
    
    /**
     * 校验是否连接
     *
     * @param dataSourceId
     * @return
     */
    String checkConnectDataSource(final Long dataSourceId);
    
    /**
     * 打开连接
     *
     * @param dataSourceId
     * @return
     */
    ConnectionDataVO openConnectDataSource(final Long dataSourceId);
    
    /**
     * 关闭连接
     *
     * @param connectionId
     * @param isCloseCurrent
     */
    void closeConnectDataSource(final String connectionId,final boolean isCloseCurrent);
    
    
    /**
     * 打开数据库
     *
     * @param connectionId
     * @param dataBaseName
     * @return
     */
    DatabaseVO openDataBase(final String connectionId, final String dataBaseName);
    
    /**
     * 关闭数据库
     *
     * @param connectionId
     * @param dataBaseName
     */
    void closeDataBase(final String connectionId, final String dataBaseName);
    
    /**
     * 获取数据连接消息
     *
     * @param connectionId
     * @return
     */
    DataSourceVO getDataSource(final String connectionId);
}
