package cn.zhixiangsingle.config;

import cn.zhixiangsingle.constant.RabbitConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.config
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/9/12 11:25
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Configuration
public class RabbitConfiguration {
    @Bean
    public Queue sitePictureQueue() {return new Queue(RabbitConstant.SITE_PICTURE_QUEUE, true);}
    @Bean
    TopicExchange topicExchangeSitePicture() {
        return new TopicExchange(RabbitConstant.TOPICEXCHANGE_SITEPICTURE);
    }
    @Bean
    public Binding sitePictureBind() {
        return BindingBuilder.bind(sitePictureQueue()).to(topicExchangeSitePicture()).with(RabbitConstant.SITE_PICTURE_KEY);
    }
}
