package priv.wei.consumer.mq.listener;

import javax.annotation.PostConstruct;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import priv.wei.consumer.mq.config.MqConfig;
import priv.wei.consumer.service.ConsumerService;
import priv.wei.consumer.util.ExpUtil;

/**
 * 监听消息，并消费
 */
@Slf4j
@Component
public class TestDateConsumer {
	
	@Autowired
	private MqConfig mc;
	
	@Autowired
	private ConsumerService consumerService;
	
	private DefaultMQPushConsumer consumer;
	
	/**
	 * 项目启动时初始化监听test_topic-test_tag
	 */
	@PostConstruct
	private void init(){
		try {
			consumer = new DefaultMQPushConsumer();
			consumer.setNamesrvAddr(mc.getNameServer());
			consumer.setConsumerGroup(mc.getConsumerGroup());
			//监听指定的topic，tag消息
			consumer.subscribe("test_topic", "test_tag");
            consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
                try {
                    for (MessageExt messageExt : list) {
                    	//消费内容
                    	consumerService.test(new String(messageExt.getBody()));
                    }
                } catch (Exception e) {
                	log.info("消费异常：{}",ExpUtil.getStackMsg(e));
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER; //稍后再试
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; //消费成功
            });
            consumer.start();
			log.info("初始化监听：topic:test_topic,tag:test_tag启动成功！");
		} catch (Exception e) {
			log.error("初始化监听：topic:test_topic,tag:test_tag启动异常：{}",ExpUtil.getStackMsg(e));
		}
	}
}
