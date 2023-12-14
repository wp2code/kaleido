package com.lzx.kaleido.start;

import com.lzx.kaleido.infra.base.constant.BasePackageConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lwp
 * @date 2023-11-11
 **/
@SpringBootApplication(scanBasePackages = {BasePackageConstants.BASE_PACKAGE})
public class StartApplication {
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }
}
