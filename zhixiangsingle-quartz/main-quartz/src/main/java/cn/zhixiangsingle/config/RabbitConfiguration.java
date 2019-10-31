package cn.zhixiangsingle.config;

import cn.zhixiangsingle.constant.RabbitConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hhp
 * @Title: RabbitConfiguration
 * @Description: mq 队列，配置
 * @return:
 * @throws
 */
@Configuration
public class RabbitConfiguration {

	// 声明三离警报队列
	@Bean
	public Queue tlAlarmQueue() {
		return new Queue(RabbitConstant.SITE_PICTURE_QUEUE, true); // true表示持久化该队列
	}
	// 声明交互器
	@Bean
    TopicExchange topicExchange() {
		return new TopicExchange(RabbitConstant.TOPICEXCHANGE_SITEPICTURE);
	}

	@Bean
	public Binding bindingThreeLeaveAlarm() {
		return BindingBuilder.bind(tlAlarmQueue()).to(topicExchange()).with(RabbitConstant.SITE_PICTURE_KEY);
	}
	//接收对象时使用
	/*@Bean
	public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(new MessageConverter() {
			@Override
			public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
				return null;
			}

			@Override
			public Object fromMessage(Message message) throws MessageConversionException {
				System.out.println(message+",,,,,,,,,,,,,,,,,");
				try(ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(message.getBody()))){
					//return (User)ois.readObject();
					return ois.readObject();
				}catch (Exception e){
					e.printStackTrace();
					return null;
				}
			}
		});

		return factory;
	}*/

}
