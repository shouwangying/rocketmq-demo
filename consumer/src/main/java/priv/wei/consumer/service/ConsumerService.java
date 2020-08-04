package priv.wei.consumer.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsumerService {
	public void test(String msg){
		log.info("消费了消息：{}",msg);
	}
}
