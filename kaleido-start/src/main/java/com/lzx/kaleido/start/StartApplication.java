package com.lzx.kaleido.start;

import com.lzx.kaleido.infra.base.constant.BasePackageConstants;
import com.lzx.kaleido.infra.base.utils.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @author lwp
 * @date 2023-11-11
 **/
@Slf4j
@SpringBootApplication(scanBasePackages = {BasePackageConstants.BASE_PACKAGE})
public class StartApplication {
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        final ConfigurableApplicationContext application = SpringApplication.run(StartApplication.class, args);
        final Environment env = application.getEnvironment();
        String logInfo = """
                \nApplication '{}' is running! Access URLs:
                \tLocal: http://localhost:{}{}/XXX"
                \tExternal: http://{}:{}{}/XXX"
                """;
        log.info(logInfo, env.getProperty("spring.application.name", "kaleido"), env.getProperty("server.port"),
                env.getProperty("server.servlet.context-path", ""), ServerUtil.getServerIp(), env.getProperty("server.port"),
                env.getProperty("server.servlet.context-path", ""));
    }
}
