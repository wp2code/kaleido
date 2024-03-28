package com.lzx.kaleido.domain.core.datasource.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lzx.kaleido.domain.api.datasource.IDataSourceService;
import com.lzx.kaleido.domain.core.datasource.DataSourceFactory;
import com.lzx.kaleido.domain.core.utils.DataSourceConvertUtil;
import com.lzx.kaleido.domain.model.dto.datasource.param.DataSourceConnectParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.DataSourceParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.DataSourceQueryParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.TableFieldColumnParam;
import com.lzx.kaleido.domain.model.entity.datasource.DataSourceEntity;
import com.lzx.kaleido.domain.model.vo.datasource.DataSourceMetaVO;
import com.lzx.kaleido.domain.model.vo.datasource.DataSourceVO;
import com.lzx.kaleido.domain.model.vo.datasource.TableFieldColumnVO;
import com.lzx.kaleido.domain.repository.mapper.IDataSourceMapper;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.excption.CommonException;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.utils.PojoConvertUtil;
import com.lzx.kaleido.plugins.mp.BaseServiceImpl;
import com.lzx.kaleido.spi.db.model.ConnectionInfo;
import com.lzx.kaleido.spi.db.model.ConnectionWrapper;
import com.lzx.kaleido.spi.db.model.TableColumnJavaMap;
import com.lzx.kaleido.spi.db.model.metaData.Database;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author lwp
 * @date 2023-11-17
 **/
@Slf4j
@Service
public class DataSourceService extends BaseServiceImpl<IDataSourceMapper, DataSourceEntity> implements IDataSourceService {
    
    /**
     * 新增数据源连接
     *
     * @param param
     * @return
     */
    @Override
    public Long addDataSource(final DataSourceParam param) {
        final String name = param.getName();
        if (StrUtil.isBlank(name)) {
            param.setName(param.getDefaultName());
        }
        final DataSourceEntity entity = PojoConvertUtil.objectMap(param, DataSourceEntity.class);
        if (this.save(entity)) {
            return entity.getId();
        }
        return null;
    }
    
    /**
     * 更新数据源连接
     *
     * @param id
     * @param param
     * @return
     */
    @Override
    public boolean updateById(final Long id, final DataSourceParam param) {
        final DataSourceEntity entity = PojoConvertUtil.objectMap(param, DataSourceEntity.class);
        entity.setId(id);
        return this.updateById(entity);
    }
    
    /**
     * 删除数据源连接
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(final Long id) {
        return this.removeById(id);
    }
    
    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    @Override
    public DataSourceVO getDetailById(final Long id) {
        final DataSourceEntity entity = this.getById(id);
        return PojoConvertUtil.entity2Vo(entity, DataSourceVO.class);
    }
    
    /**
     * 查询数据源列表
     *
     * @param param
     * @return
     */
    @Override
    public List<DataSourceVO> queryByParam(final DataSourceQueryParam param) {
        final LambdaQueryWrapper<DataSourceEntity> wrapper = Wrappers.<DataSourceEntity>lambdaQuery()
                .eq(StrUtil.isNotBlank(param.getName()), DataSourceEntity::getName, param.getName())
                .eq(StrUtil.isNotBlank(param.getType()), DataSourceEntity::getType, param.getType());
        final List<DataSourceEntity> entities = this.list(wrapper);
        return PojoConvertUtil.entity2VoList(entities, DataSourceVO.class);
    }
    
    /**
     * @param tableFieldColumnParam
     * @return
     */
    @Override
    public List<TableFieldColumnVO> getTableFieldColumnList(final TableFieldColumnParam tableFieldColumnParam) {
        final List<TableColumnJavaMap> tableColumnJavaMapList = DataSourceFactory.getInstance()
                .getTableColumnJavaMapList(tableFieldColumnParam.getConnectionId(), tableFieldColumnParam.getDataBaseName(),
                        tableFieldColumnParam.getSchemaName(), tableFieldColumnParam.getTableName());
        if (CollUtil.isNotEmpty(tableColumnJavaMapList)) {
            return tableColumnJavaMapList.stream().map(v -> {
                final TableFieldColumnVO vo = new TableFieldColumnVO();
                vo.setComment(v.getComment());
                vo.setColumn(v.getColumn());
                vo.setJavaType(v.getJavaType());
                vo.setJavaTypeSimpleName(v.getJavaTypeSimpleName());
                vo.setJdbcType(v.getJdbcType());
                vo.setProperty(v.getProperty());
                vo.setPrimaryKey(v.getPrimaryKey());
                return vo;
            }).collect(Collectors.toList());
        }
        return null;
    }
    
    /**
     * 获取数量连接信息
     *
     * @param id
     * @param deepQuery
     * @param autoClose
     * @return
     */
    @Override
    public DataSourceMetaVO getDataSourceMeta(final Long id, final boolean deepQuery, final boolean autoClose) {
        final DataSourceVO dataSourceVO = this.getDetailById(id);
        if (dataSourceVO != null) {
            final ConnectionInfo connectionInfo = DataSourceConvertUtil.convertConnectionInfo(dataSourceVO);
            ConnectionWrapper connectionWrapper = null;
            try {
                connectionWrapper = DataSourceFactory.getInstance().getConnection(connectionInfo, false);
                final List<Database> dateBaseList = DataSourceFactory.getInstance().getDateBaseList(connectionInfo);
                dataSourceVO.setPassword(null);
                return DataSourceMetaVO.builder().dataSource(dataSourceVO).connectionId(connectionWrapper.getId())
                        .dateBaseList(DataSourceConvertUtil.convertDataBaseList(dateBaseList, connectionInfo, deepQuery, false)).build();
            } catch (CommonException e) {
                log.error("getDataSource is failed! {}", e.getMessage());
                throw new CommonRuntimeException(e.getErrorCode());
            } finally {
                if (autoClose) {
                    DataSourceFactory.getInstance().closeConnection(connectionWrapper);
                }
            }
        }
        throw new CommonRuntimeException(ErrorCode.CONNECTION_IS_NULL);
    }
    
    /**
     * 测试连接数据库
     *
     * @param param
     * @return
     */
    @Override
    public boolean connectTestDataSource(final DataSourceConnectParam param) {
        if (param != null) {
            ConnectionWrapper connectionWrapper = null;
            final ConnectionInfo connectionInfo = DataSourceConvertUtil.convertConnectionInfo(param);
            try {
                connectionWrapper = DataSourceFactory.getInstance().getConnection(connectionInfo, true);
                return connectionWrapper != null;
            } catch (CommonException e) {
                log.error("connect test is failed! {}", e.getMessage());
            } finally {
                DataSourceFactory.getInstance().closeConnection(connectionWrapper);
            }
        }
        return false;
    }
    
}
