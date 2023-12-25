package com.lzx.kaleido.domain.core.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lzx.kaleido.domain.api.service.IDataSourceService;
import com.lzx.kaleido.domain.core.DataSourceFactory;
import com.lzx.kaleido.domain.core.utils.ConvertUtil;
import com.lzx.kaleido.domain.model.dto.param.datasource.DataSourceConnectParam;
import com.lzx.kaleido.domain.model.dto.param.datasource.DataSourceParam;
import com.lzx.kaleido.domain.model.dto.param.datasource.DataSourceQueryParam;
import com.lzx.kaleido.domain.model.entity.datasource.DataSourceEntity;
import com.lzx.kaleido.domain.model.vo.datasource.DataSourceMetaVO;
import com.lzx.kaleido.domain.model.vo.datasource.DataSourceVO;
import com.lzx.kaleido.domain.repository.mapper.IDataSourceMapper;
import com.lzx.kaleido.infra.base.excption.CommonException;
import com.lzx.kaleido.infra.base.excption.CommonRuntimeException;
import com.lzx.kaleido.infra.base.utils.PojoConvertUtil;
import com.lzx.kaleido.plugins.mp.BaseServiceImpl;
import com.lzx.kaleido.spi.db.model.ConnectionInfo;
import com.lzx.kaleido.spi.db.model.metaData.Database;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;


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
     * 获取数量连接信息
     *
     * @param id
     * @return
     */
    @Override
    public DataSourceMetaVO getDataSourceMeta(final Long id, final boolean deepQuery) {
        final DataSourceVO dataSourceVO = this.getDetailById(id);
        if (dataSourceVO != null) {
            final ConnectionInfo connectionInfo = ConvertUtil.convertConnectionInfo(dataSourceVO);
            Connection connection = null;
            try {
                connection = DataSourceFactory.getInstance().getConnection(connectionInfo);
                final List<Database> dateBaseList = DataSourceFactory.getInstance().getDateBaseList(connectionInfo);
                return DataSourceMetaVO.builder().dataSource(dataSourceVO)
                        .dateBaseList(ConvertUtil.convertDataBaseList(dateBaseList, connectionInfo, deepQuery, false)).build();
            } catch (CommonException e) {
                log.error("getDataSource is failed! {}", e.getMessage());
                throw new CommonRuntimeException(e.getErrorCode());
            } finally {
                DataSourceFactory.getInstance().closeConnection(connection);
            }
        }
        return null;
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
            Connection connection = null;
            try {
                connection = DataSourceFactory.getInstance().getConnection(ConvertUtil.convertConnectionInfo(param));
                return connection != null;
            } catch (CommonException e) {
                log.error("connect test is failed! {}", e.getMessage());
            } finally {
                DataSourceFactory.getInstance().closeConnection(connection);
            }
        }
        return false;
    }
    
}
