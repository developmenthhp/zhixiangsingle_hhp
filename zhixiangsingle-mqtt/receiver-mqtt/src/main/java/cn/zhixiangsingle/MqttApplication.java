package cn.zhixiangsingle;

import cn.zhixiangsingle.config.DynamicDataSourceRegister;
import cn.zhixiangsingle.config.MqttReceiveConfig;
import cn.zhixiangsingle.impl.MqttReceiveServiceImpl;
import org.apache.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("cn.zhixiangsingle.resources")
@Import(DynamicDataSourceRegister.class)
@ComponentScan({"cn.zhixiangsingle.config","cn.zhixiangsingle.component"})
@RestController
public class MqttApplication
{
    static MqttReceiveConfig mqttReceiveConfig;

    private static Logger logger = Logger.getLogger(MqttApplication.class);
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(MqttApplication.class, args);
        mqttReceiveConfig = configurableApplicationContext.getBean(MqttReceiveConfig.class,"mqttReceiveConfig");
        mqttReceiveConfig.setMqttReceiveService(new MqttReceiveServiceImpl());
        logger.info("receiver_mqtt+++++++ Start Success");
    }
    @RequestMapping("/addTopic")
    public String addTopic(String[] topics){
        mqttReceiveConfig.addListenTopic(topics);
        logger.info("add topic success:"+topics.toString());
        return "success";
    }
}
