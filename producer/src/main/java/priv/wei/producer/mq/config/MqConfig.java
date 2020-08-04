package priv.wei.producer.mq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "mq")
public class MqConfig {
	private String nameServer;
	private String  producerGroup;
}
