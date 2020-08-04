package priv.wei.producer.mq;

import javax.annotation.PostConstruct;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import priv.wei.producer.mq.config.MqConfig;
import priv.wei.producer.util.ExpUtil;

/**
 * 生产者
 */
@Slf4j
@Component
public class Producer {
	
	@Autowired
	private MqConfig mc;
	
    private DefaultMQProducer producer;
    
    /**
     * 系统启动时自动初始化生产者
     * @throws MQClientException
     */
    @PostConstruct
    private void init() throws MQClientException{
    	 //示例生产者
        producer = new DefaultMQProducer(mc.getProducerGroup());
        //不开启vip通道 开通口端口会减2
        producer.setVipChannelEnabled(false);
        //绑定name server
        producer.setNamesrvAddr(mc.getNameServer());
    	//初始化启动
        producer.start();
        
        log.info("mq生产者初始化成功！");
    }
  
    /**
     * 发送请求
     * @param topic 主题
     * @param tag 标签
     * @param content 消息内容
     */
    public void send(String topic,String tag,String content){
    	try {
    		Message msg = new Message(topic, tag, content.getBytes(RemotingHelper.DEFAULT_CHARSET));
    		SendResult sr = producer.send(msg);
    		log.info("发送响应：MsgId:{},发送状态SendStatus:{}",sr.getMsgId(),sr.getSendStatus());
		} catch (Exception e) {
			log.error("发送信息异常:{}",ExpUtil.getStackMsg(e));
		}
    }
    
    /**
     * 单独获取生产者
     * @return DefaultMQProducer
     */
    public DefaultMQProducer get(){
        return this.producer;
    }
    
    /**
     * 关闭生产者
     */
    public void shutdown(){
        this.producer.shutdown();
    }
}