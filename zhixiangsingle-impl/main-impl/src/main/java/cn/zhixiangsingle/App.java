package cn.zhixiangsingle;

import cn.zhixiangsingle.config.DynamicDataSourceRegister;
import org.apache.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@ImportResource({"classpath:config/spring-dubbo.xml"})
@MapperScan("cn.zhixiangsingle.resources")
@Import(DynamicDataSourceRegister.class)
@ComponentScan({"cn.zhixiangsingle.config", "cn.zhixiangsingle.component"})
public class App
{
    private static Logger logger = Logger.getLogger(App.class);
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        logger.info("main-impl_|_|_|_| Start Success");
    }
}
