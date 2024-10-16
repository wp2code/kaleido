package com.lzx.kaleido.web.api.controller.datasource;

import com.lzx.kaleido.domain.api.datasource.IDataSourceService;
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
import com.lzx.kaleido.infra.base.constant.Constants;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.pojo.R;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据源管理接口
 *
 * @author lwp
 * @date 2023-11-10
 **/
@RestController
@RequestMapping(Constants.API_VERSION + "/datasource")
public class DataSourceController {
    
    @Resource
    private IDataSourceService dataSourceService;
    
    /**
     * 创建数据源
     *
     * @param param
     * @return
     */
    @PostMapping("/add")
    public R<Long> addDataSource(@RequestBody DataSourceParam param) {
        final Long id = dataSourceService.addDataSource(param);
        return R.result(id != null, ErrorCode.SAVE_FAILED, id);
    }
    
    /**
     * 更新数据源
     *
     * @param id
     * @param param
     * @return
     */
    @PutMapping("/{id}/update")
    public R<Boolean> updateDataSource(@PathVariable("id") Long id, @RequestBody DataSourceParam param) {
        final boolean isSuccess = dataSourceService.updateById(id, param);
        return R.result(isSuccess, ErrorCode.UPDATE_FAILED);
    }
    
    /**
     * 删除数据源
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}/delete")
    public R<Boolean> deleteDataSource(@PathVariable("id") Long id) {
        final boolean isSuccess = dataSourceService.deleteById(id);
        return R.result(isSuccess, ErrorCode.DELETED_FAILED);
    }
    
    /**
     * 查询数据源详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}/get")
    public R<DataSourceVO> getDataSource(@PathVariable("id") Long id) {
        return R.success(dataSourceService.getDetailById(id));
    }
    
    /**
     * 获取连接的数据源
     *
     * @param connectionId
     * @return
     */
    @GetMapping("/connection/{connectionId}")
    public R<DataSourceVO> getDataSource(@PathVariable("connectionId") String connectionId) {
        final DataSourceVO dataSource = dataSourceService.getDataSource(connectionId);
        return R.success(dataSource);
    }
    
    /**
     * 查询
     *
     * @param param
     * @return
     */
    @PostMapping("/list")
    public R<List<DataSourceVO>> queryDataSource(@RequestBody DataSourceQueryParam param) {
        final List<DataSourceVO> list = dataSourceService.queryByParam(param);
        return R.success(list);
    }
    
    
    /**
     * 测试数据库连接
     *
     * @param param
     * @return
     */
    @PostMapping("/test/connect")
    public R<Boolean> connectTestDataSource(@RequestBody DataSourceConnectParam param) {
        final boolean isSuccess = dataSourceService.connectTestDataSource(param);
        return R.result(isSuccess, ErrorCode.CONNECTION_FAILED);
    }
    
    /**
     * 校验是否连接
     *
     * @param id
     * @return
     */
    @GetMapping("/connect/{id}/checkOpen")
    public R<String> checkConnectDataSource(@PathVariable("id") Long id) {
        final String connectionId = dataSourceService.checkConnectDataSource(id);
        return R.success(connectionId);
    }
    
    /**
     * 打开连接
     *
     * @param id
     * @return
     */
    @GetMapping("/connect/{id}/open")
    public R<ConnectionDataVO> openConnectDataSource(@PathVariable("id") Long id) {
        final ConnectionDataVO connectionData = dataSourceService.openConnectDataSource(id);
        return R.success(connectionData);
    }
    
    /**
     * 关闭其它连接
     *
     * @param connectionId
     * @return
     */
    @DeleteMapping("/connect/other/{connectionId}/close")
    public R<Boolean> closeOtherConnectDataSource(@PathVariable("connectionId") String connectionId) {
        dataSourceService.closeConnectDataSource(connectionId, false);
        return R.success(true);
    }
    
    /**
     * 关闭当前连接
     *
     * @param connectionId
     * @return
     */
    @DeleteMapping("/connect/current/{connectionId}/close")
    public R<Boolean> closeCurrConnectDataSource(@PathVariable("connectionId") String connectionId) {
        dataSourceService.closeConnectDataSource(connectionId, true);
        return R.success(true);
    }
    
    /**
     * 打开数据库
     *
     * @param databaseName 数据库名称
     * @param connectionId 连接ID
     * @return
     */
    @GetMapping("/db/{connectionId}/{databaseName}/open")
    public R<DatabaseVO> openDataBase(@PathVariable("databaseName") String databaseName,
            @PathVariable("connectionId") String connectionId) {
        final DatabaseVO databaseVO = dataSourceService.openDataBase(connectionId, databaseName);
        return R.success(databaseVO);
    }
    
    /**
     * 关闭数据库
     *
     * @param databaseName 数据库名称
     * @param connectionId 连接ID
     * @return
     */
    @GetMapping("/db/{connectionId}/{databaseName}/close")
    public R<Boolean> closeDataBase(@PathVariable("databaseName") String databaseName, @PathVariable("connectionId") String connectionId) {
        dataSourceService.closeDataBase(connectionId, databaseName);
        return R.success(true);
    }
    
    
    /**
     * 获取数据连接数据
     *
     * @param id
     * @return
     */
    @GetMapping("/meta/{id}/info")
    public R<DataSourceMetaVO> getDataSourceMeta(@PathVariable("id") Long id) {
        final DataSourceMetaVO dataSourceMeta = dataSourceService.getDataSourceMeta(id, false, true);
        return R.success(dataSourceMeta);
    }
    
    /**
     * 获取数据连接数据（全量获取）
     *
     * @param id
     * @return
     */
    @GetMapping("/meta/{id}/all/info")
    public R<DataSourceMetaVO> getDataSourceMetaAll(@PathVariable("id") Long id) {
        final DataSourceMetaVO dataSourceMeta = dataSourceService.getDataSourceMeta(id, true, true);
        return R.success(dataSourceMeta);
    }
    
    /**
     * 获取表DDL
     *
     * @param param
     * @return
     */
    @PostMapping("/meta/table/ddl")
    public R<String> getTableDDL(@Validated @RequestBody TableDDLParam param) {
        String tableDDL = dataSourceService.getTableDDL(param);
        return R.success(tableDDL);
    }
    
    /**
     * 获取表字段信息列表
     *
     * @param param
     * @return
     */
    @PostMapping("/table/column/fields")
    public R<List<TableFieldColumnVO>> getTableFieldColumnList(@RequestBody TableFieldColumnParam param) {
        final List<TableFieldColumnVO> tableFieldColumnList = dataSourceService.getTableFieldColumnList(param);
        return R.success(tableFieldColumnList);
    }
    
}
