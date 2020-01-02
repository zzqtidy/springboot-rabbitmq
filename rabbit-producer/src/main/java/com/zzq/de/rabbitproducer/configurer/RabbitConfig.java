package com.zzq.de.rabbitproducer.configurer;

import lombok.Data;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author TYLER
 * @title: RabbitConfig
 * @description: rabbitmq配置类
 * @date 2019/12/31
 */

@Data
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Configuration
public class RabbitConfig {
	private String addresses;
	private String port;
	private String username;
	private String password;
	private String virtualHost;
	private String publisherReturns;
	private Integer connectionTimeout;
	private String publisherConfirmType;
	private Map<String,String> queueNameProducerMap;
	/**
	 * 定义队列名
	 */
	private final static String Q_NAME = "com.queue.notify.hello";


	/**
	 * 定义string队列
	 * @return
	 */
	@Bean
	public Queue string() {
		return new Queue(Q_NAME);
	}

	/**
	 * 创建rabbitmq的连接
	 * @return
	 */
	@Bean
	public ConnectionFactory connectionFactory(){
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setUsername(this.username);
		connectionFactory.setPassword(this.password);
		connectionFactory.setVirtualHost(this.virtualHost);
		connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
		//该方法可以配置多个host，如果当前host down掉之后会自动连接后面的host
		connectionFactory.setAddresses(this.addresses);
		return connectionFactory;
	}
	@Bean
	public RabbitAdmin rabbitAdmin() {
		return new RabbitAdmin(connectionFactory());
	}

	@Bean
	public TopicExchange exchange(RabbitAdmin rabbitAdmin){
		/**
		 * FanoutExchange、TopicExchange、HeadersExchange 等
		 * 都是同一个父类 AbstractExchange
		 */
		//FanoutExchange fanoutExchange = new FanoutExchange(exchangeName,true,false);

		//spring启动的时候创建Exchange
		TopicExchange dataExchange = new TopicExchange("com.exchange.notify.hello",true,false);
		rabbitAdmin.declareExchange(dataExchange);
		return dataExchange;
	}
}
