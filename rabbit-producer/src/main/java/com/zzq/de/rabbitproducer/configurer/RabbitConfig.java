package com.zzq.de.rabbitproducer.configurer;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * @author TYLER
 * @title: RabbitConfig
 * @description: rabbitmq配置类
 * @date 2019/12/31
 */
@Configurable
public class RabbitConfig {
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
}
