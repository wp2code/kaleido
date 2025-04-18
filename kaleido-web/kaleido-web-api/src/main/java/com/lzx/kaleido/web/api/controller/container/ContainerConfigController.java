package com.lzx.kaleido.web.api.controller.container;

import com.lzx.kaleido.domain.api.container.IContainerConfigService;
import com.lzx.kaleido.domain.model.dto.container.param.ContainerConfigParam;
import com.lzx.kaleido.domain.model.vo.container.ContainerConfigVO;
import com.lzx.kaleido.infra.base.constant.Constants;
import com.lzx.kaleido.infra.base.pojo.R;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 容器配置管理接口
 *
 * @author lwp
 * @date 2025-03-14
 **/
@RestController
@RequestMapping(Constants.API_VERSION + "/container-config")
public class ContainerConfigController {
    
    @Resource
    private IContainerConfigService containerConfigService;
    
    /**
     * 查询容器配置
     *
     * @param param
     * @return
     */
    @PostMapping("/list")
    public R<List<ContainerConfigVO>> queryContainerConfig(@RequestBody ContainerConfigParam param) {
        return R.success(containerConfigService.queryByParam(param));
    }
    
    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}/info")
    public R<ContainerConfigVO> getContainerConfigInfo(@PathVariable("id") Long id) {
        return R.success(containerConfigService.getDetailById(id));
    }
    
    /**
     * 新增容器配置
     *
     * @param vo
     * @return
     */
    @PostMapping("/add")
    public R<Boolean> addContainerConfig(@RequestBody ContainerConfigVO vo) {
        return R.success(containerConfigService.save(vo));
    }
    
    /**
     * 跟新容器配置
     *
     * @param vo
     * @return
     */
    @PutMapping("/{id}/update")
    public R<Boolean> updateContainerConfig(@PathVariable("id") Long id, @RequestBody ContainerConfigVO vo) {
        return R.success(containerConfigService.updateById(id, vo));
    }
    
    /**
     * 删除容器配置
     *
     * @param id
     * @return
     */
    @PutMapping("/{id}/delete")
    public R<Boolean> deleteContainerConfig(@PathVariable("id") Long id) {
        return R.success(containerConfigService.deleteById(id));
    }
}
