package cn.zhixiangsingle;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Logger logger = Logger.getLogger(App.class);
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        logger.info("main_mqtt+++++++ Start Success");
    }
}
