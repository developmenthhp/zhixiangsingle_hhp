package cn.zhixiangsingle.component;

import cn.zhixiangsingle.constant.RabbitConstant;
import com.rabbitmq.client.Channel;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.component
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/8/22 17:06
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Component
public class Receiver {
    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(Receiver.class);
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /*//因为使用的是异步消息机制，故返回值是void
    @RabbitListener(queues = RabbitConstant.THREE_LEAVE_ALARM_QUEUE)
    public void processEmail(Object object, Message message, Channel channel) throws IOException {
        logger.info(Thread.currentThread().getName() + " 接收到来自email队列的消息：" + object.toString());
        //这段代码表示，这次消息，我已经接受并消费掉了，不会再重复发送消费
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        logger.warn("[Consumer Message 01] ===============> ");
    }*/
}
