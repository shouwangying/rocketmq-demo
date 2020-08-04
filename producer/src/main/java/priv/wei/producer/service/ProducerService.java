package priv.wei.producer.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import priv.wei.producer.mq.Producer;

/**
 * 模拟消息发送服务
 */
@Slf4j
@Service
public class ProducerService {
	
	@Autowired
	private Producer producer;
	
	/**
	 * 定时器定时发送消息
	 */
	@Scheduled(cron = "0/2 * * * * ?")
	public void test(){
		log.info("开始生产消息。。");
		//发送当前时间
		Map<String,Object> msg = new HashMap<>();
		msg.put("test_date", new Date());
		producer.send("test_topic", "test_tag", JSON.toJSONString(msg));
	}
	
}