package com.lzx.kaleido.domain.core.utils;

import cn.hutool.core.collection.CollUtil;
import com.lzx.kaleido.domain.core.datasource.DataSourceFactory;
import com.lzx.kaleido.domain.model.dto.datasource.param.DataSourceConnectParam;
import com.lzx.kaleido.domain.model.vo.datasource.DataSourceVO;
import com.lzx.kaleido.domain.model.vo.datasource.DatabaseVO;
import com.lzx.kaleido.domain.model.vo.datasource.SchemaVO;
import com.lzx.kaleido.domain.model.vo.datasource.TableVO;
import com.lzx.kaleido.infra.base.excption.CommonException;
import com.lzx.kaleido.spi.db.model.ConnectionInfo;
import com.lzx.kaleido.spi.db.model.DBConfig;
import com.lzx.kaleido.spi.db.model.metaData.Database;
import com.lzx.kaleido.spi.db.model.metaData.Schema;
import com.lzx.kaleido.spi.db.model.metaData.Table;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author lwp
 * @date 2023-12-13
 **/
@Slf4j
@UtilityClass
public class DataSourceConvertUtil {
    
    /**
     * @param dataSourceInfo
     * @return
     */
    public ConnectionInfo convertConnectionInfo(DataSourceVO dataSourceInfo) {
        return ConnectionInfo.of(dataSourceInfo.getId() != null ? String.valueOf(dataSourceInfo.getId()) : null, dataSourceInfo.getDbName(),
                dataSourceInfo.getType(), dataSourceInfo.getName(), dataSourceInfo.getPort(), dataSourceInfo.getUrl(),
                dataSourceInfo.getUserName(), dataSourceInfo.getPassword(), dataSourceInfo.getExtend());
    }
    
    /**
     * @param connectParam
     * @return
     */
    public ConnectionInfo convertConnectionInfo(DataSourceConnectParam connectParam) {
        return ConnectionInfo.of(null, connectParam.getDbName(), connectParam.getType(), connectParam.getName(), connectParam.getPort(),
                connectParam.getUrl(), connectParam.getUserName(), connectParam.getPassword(), connectParam.getExtend());
    }
    
    /**
     * @param dateBaseList
     * @param connectionInfo
     * @param deepQuery
     * @param queryTableDetailMore
     * @return
     * @throws CommonException
     */
    public List<DatabaseVO> convertDataBaseList(List<Database> dateBaseList, ConnectionInfo connectionInfo, boolean deepQuery,
            boolean queryTableDetailMore) throws CommonException {
        final List<DatabaseVO> list = new ArrayList<>();
        for (final Database database : dateBaseList) {
            final DatabaseVO vo = new DatabaseVO();
            vo.setName(database.getName());
            if (deepQuery) {
                final DBConfig propertiesConfig = connectionInfo.getPropertiesConfig();
                vo.setSupportSchema(propertiesConfig.isSupportSchema());
                if (propertiesConfig.isSupportSchema()) {
                    final List<Schema> schemasList = DataSourceFactory.getInstance().getSchemasList(connectionInfo, vo.getName());
                    final List<SchemaVO> schemaVOList = convertSchemaList(schemasList, (schemaName) -> {
                        final List<Table> tableList = DataSourceFactory.getInstance()
                                .getTableList(connectionInfo, database.getName(), schemaName, null, queryTableDetailMore);
                        tableList.forEach(v -> v.setDatabaseName(database.getName()));
                        return tableList;
                    });
                    vo.setSchemaList(schemaVOList);
                } else {
                    final List<Table> tableList = DataSourceFactory.getInstance()
                            .getTableList(connectionInfo, database.getName(), queryTableDetailMore);
                    vo.setTableList(convertTableList(tableList));
                }
            }
            list.add(vo);
        }
        return list;
    }
    
    /**
     * @param tableList
     * @return
     */
    public List<TableVO> convertTableList(final List<Table> tableList) {
        if (CollUtil.isNotEmpty(tableList)) {
            return tableList.stream().map(v -> new TableVO(v.getName(), v.getDatabaseName(), v.getSchemaName(), v.getComment())).toList();
        }
        return null;
    }
    
    /**
     * @param schemasList
     * @param function
     * @return
     */
    public List<SchemaVO> convertSchemaList(final List<Schema> schemasList, final Function<String, List<Table>> function) {
        if (CollUtil.isNotEmpty(schemasList)) {
            final List<SchemaVO> schemaVOList = new ArrayList<>();
            for (final Schema schema : schemasList) {
                final List<TableVO> tableList = function != null ? convertTableList(function.apply(schema.getName())) : null;
                schemaVOList.add(new SchemaVO(schema.getName(), tableList));
            }
            return schemaVOList;
        }
        return null;
    }
}
