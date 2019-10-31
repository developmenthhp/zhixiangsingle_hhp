package cn.zhixiangsingle.config;

import cn.zhixiangsingle.entity.base.IsEmptyUtils;
import cn.zhixiangsingle.service.MqttReceiveService;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 * All rights Reserved, Designed By www.zhixiangyun.net
 *
 * @version V1.0
 * @Title: zhixiangpingtai
 * @Package cn.zhixiangsingle.config
 * @Description: ${todo}
 * @author: hhp
 * @date: 2019/10/28 10:25
 * @Copyright: 2019 www.zhixiangyun.net Inc. All rights reserved.
 * 注意：本内容仅限于浙江智飨科技内部传阅，禁止外泄以及用于其他的商业目
 */
@Configuration
@IntegrationComponentScan
public class MqttReceiveConfig {

    /*@Resource(name = "defaultThreadPool")
    private ThreadPoolTaskExecutor executor;*/

    @Value("${spring.mqtt.username}")
    private String username;

    @Value("${spring.mqtt.password}")
    private String password;

    @Value("${spring.mqtt.url}")
    private String hostUrl;

    @Value("${spring.mqtt.client.id}")
    private String clientId;

    @Value("${spring.mqtt.default.topic}")
    private String defaultTopic;

    @Value("${spring.mqtt.completionTimeout}")
    private int completionTimeout ;

    private MqttReceiveService mqttReceiveService;

    private MqttPahoMessageDrivenChannelAdapter adapter;

    public MqttReceiveConfig(){

    }

    public void setMqttReceiveService(MqttReceiveService mqttReceiveService){
        this.mqttReceiveService = mqttReceiveService;
    }

    public void addListenTopic(String[] topicArray){
        if(IsEmptyUtils.isEmpty(adapter)){
            adapter = new MqttPahoMessageDrivenChannelAdapter(clientId+"_inbound", mqttClientFactory(),"testTop");
        }
        for(String topic:topicArray){
            adapter.addTopic(topic,1);
        }
        adapter.removeTopic();
    }
    public void removeListenTopic(String topic){
        if(IsEmptyUtils.isEmpty(adapter)){
            adapter = new MqttPahoMessageDrivenChannelAdapter(clientId+"_inbound", mqttClientFactory(),"testTop");
        }
        adapter.removeTopic(topic);
    }

    @Bean
    public MqttConnectOptions getMqttConnectOptions(){
        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        //心跳时间,单位秒
        mqttConnectOptions.setKeepAliveInterval(20);
        //自动重连,默认是false
        mqttConnectOptions.setAutomaticReconnect(true);
        return mqttConnectOptions;
    }
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    //接收通道
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    //配置client,监听的topic
    @Bean
    public MessageProducer inbound() {
        adapter = new MqttPahoMessageDrivenChannelAdapter(clientId+"_inbound", mqttClientFactory(),"testTop");
        String[] topics = defaultTopic.split(",");
        for(String topic:topics){
            if(!IsEmptyUtils.isEmpty(topic)){
                adapter.addTopic(topic,1);
            }
        }
        adapter.setCompletionTimeout(completionTimeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        //adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    /*通过通道获取数据*/
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
                String type = topic.substring(topic.lastIndexOf("/")+1, topic.length());
                mqttReceiveService.handlerMqttMessage(topic,message.getPayload().toString());
                /*//使用Future方式执行多任务
                //生成一个集合
                List<Future> futures = Lists.newArrayList();

                //获取后台全部有效运营人员的集合
                Future<?> future = executor.submit(() -> {
                    //处理消息
                    mqttReceiveService.handlerMqttMessage(topic,message.getPayload().toString());
                });
                futures.add(future);*/
                //查询任务执行的结果
                /*try {
                    for (Future<?> future : futures) {
                        while (true) {//CPU高速轮询：每个future都并发轮循，判断完成状态然后获取结果，这一行，是本实现方案的精髓所在。即有10个future在高速轮询，完成一个future的获取结果，就关闭一个轮询
                            if (future.isDone() && !future.isCancelled()) {//获取future成功完成状态，如果想要限制每个任务的超时时间，取消本行的状态判断+future.get(1000*1, TimeUnit.MILLISECONDS)+catch超时异常使用即可。
                                //Integer i = future.get();//获取结果
                                System.out.println("任务i=" + i + "获取完成!" + new Date());
                                break;//当前future获取结果完毕，跳出while
                            } else {
                                Thread.sleep(1);//每次轮询休息1毫秒（CPU纳秒级），避免CPU高速轮循耗空CPU---》新手别忘记这个
                            }
                        }
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage()+"......send msg error");
                }*/
            }
        };
    }
}
