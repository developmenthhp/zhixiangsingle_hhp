package cn.zhixiangsingle;

import cn.zhixiangsingle.config.DynamicDataSourceRegister;
import org.apache.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Hello world!
 *
 */
@SpringBootApplication/*(exclude = {DataSourceAutoConfiguration.class})*/
@MapperScan("cn.zhixiangsingle.resource")
@Import(DynamicDataSourceRegister.class)
@ComponentScan({"cn.zhixiangsingle.config","cn.zhixiangsingle.impl"})
public class QuartzApplication
{
    private static Logger logger = Logger.getLogger(QuartzApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(QuartzApplication.class, args);
        logger.info("quartz..... Start Success");
    }
}
