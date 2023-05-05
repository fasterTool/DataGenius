package cn.ly.archive.study.test;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.oas.annotations.EnableOpenApi;

import java.util.Date;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998">liu yun</a>
 * @classname TestApplication
 * @create 2023/4/30 11:37
 */
@SpringBootApplication(scanBasePackages = {"cn.ly.common.web.*", "cn.ly.archive.*"})
@MapperScan(basePackages = {"cn.ly.common.datasource.mapper",
        "cn.ly.common.datasource.service", "com.baidu.fsg.uid.worker.dao"})
@Configuration("cn.ly.archive.study.test.config")
@EnableOpenApi
public class TestApplication implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(TestApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    public void run(String... args) throws Exception {
        logger.info("--------------------- TestApplication server started on {}", new Date());
    }
}
