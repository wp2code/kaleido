package com.lzx.kaleido.web.api.controller.datasource;

import com.lzx.kaleido.domain.api.datasource.IDataSourceService;
import com.lzx.kaleido.domain.model.dto.datasource.param.DataSourceConnectParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.DataSourceParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.DataSourceQueryParam;
import com.lzx.kaleido.domain.model.dto.datasource.param.TableFieldColumnParam;
import com.lzx.kaleido.domain.model.vo.datasource.DataSourceMetaVO;
import com.lzx.kaleido.domain.model.vo.datasource.DataSourceVO;
import com.lzx.kaleido.domain.model.vo.datasource.TableFieldColumnVO;
import com.lzx.kaleido.infra.base.constant.Constants;
import com.lzx.kaleido.infra.base.enums.ErrorCode;
import com.lzx.kaleido.infra.base.pojo.R;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
     * 创建
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
     * 更新
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
     * 删除
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
     * 查询详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}/get")
    public R<DataSourceVO> getDataSource(@PathVariable("id") Long id) {
        return R.success(dataSourceService.getDetailById(id));
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
     * 获取数据连接数据
     *
     * @param id
     * @return
     */
    @GetMapping("/meta/{id}/info")
    public R<DataSourceMetaVO> getDataSourceMeta(@PathVariable("id") Long id) {
        final DataSourceMetaVO dataSourceMeta = dataSourceService.getDataSourceMeta(id, false, false);
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
        final DataSourceMetaVO dataSourceMeta = dataSourceService.getDataSourceMeta(id, true, false);
        return R.success(dataSourceMeta);
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
