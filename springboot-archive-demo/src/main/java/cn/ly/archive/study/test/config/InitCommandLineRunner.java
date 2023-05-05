package cn.ly.archive.study.test.config;

import cn.ly.archive.study.test.config.datasource.OtherCRUDGeneratorBuilder;
import cn.fasterTool.common.datasource.service.builder.CRUDGeneratorFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://blog.csdn.net/weixin_44929998">liu yun</a>
 * @classname InitCommandLineRunner
 * @create 2023/5/3 21:09
 */

@Component
public class InitCommandLineRunner implements CommandLineRunner {


    public void run(String... args) throws Exception {
        OtherCRUDGeneratorBuilder otherCRUDGeneratorBuilder = new OtherCRUDGeneratorBuilder();
        CRUDGeneratorFactory.addSqlGenerator(otherCRUDGeneratorBuilder.type(), otherCRUDGeneratorBuilder);
    }
}
