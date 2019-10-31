package cn.zhixiangsingle.component;

import cn.zhixiangsingle.constant.RabbitConstant;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.component
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/22 17:08
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public class Sender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    private static Logger logger = Logger.getLogger(Sender.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            logger.info("消息发送成功:" + correlationData);
        } else {
            logger.info("消息发送失败:" + cause);
        }

    }
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.error(message.getMessageProperties() + " 发送失败");
    }
    public void send(Object obj) {
        /*CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        logger.info("开始发送消息 : " + obj.toString());
        rabbitTemplate.convertAndSend(RabbitConstant.TOPICEXCHANGE_TLALARM, RabbitConstant.THREE_LEAVE_ALARM_KEY, obj, correlationId);
        logger.info("rabbitmq消息已经发送到交换机, 等待交换机接受...");
        logger.info("结束发送消息 ");*/
    }
}
