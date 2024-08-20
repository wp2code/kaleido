package com.lzx.kaleido.web.api.controller.system;

import cn.hutool.core.thread.ThreadUtil;
import com.lzx.kaleido.infra.base.constant.Constants;
import com.lzx.kaleido.infra.base.pojo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lwp
 * @date 2024-07-11
 **/
@Slf4j
@RestController
@RequestMapping(Constants.API_VERSION + "/system")
public class SystemController {
    
    @Resource
    private ApplicationContext applicationContext;
    
    /**
     * @return
     */
    @GetMapping("/stop")
    public R<Boolean> stop() {
        log.info("Exit application");
        stopApp();
        return R.success(true);
    }
    
    /**
     *
     */
    private void stopApp() {
        new Thread(() -> {
            ThreadUtil.safeSleep(200L);
            log.info("Start exiting Spring application");
            try {
                SpringApplication.exit(applicationContext);
            } catch (Exception ignore) {
            }
            log.info("Start exiting system applications");
            try {
                System.exit(0);
            } catch (Exception ignore) {
            }
            
        }).start();
    }
    
}
