package com.zzq.de.rabbitconsumer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author TYLER
 * @title: StringConsumer
 * @description: TODO
 * @date 2019/12/31
 */
@Component
@RabbitListener(queues = "com.queue.notify.hello")
public class StringConsumer {
	/**
	 * 消息消费
	 * @RabbitHandler 代表此方法为接受到消息后的处理方法
	 */
	@RabbitHandler
	public void recieved(String msg) {
		System.out.println("消费者开始接受消息-" + msg);
	}
}
