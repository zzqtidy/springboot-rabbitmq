package com.zzq.de.rabbitconsumer;


import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author TYLER
 * @title: StringConsumer
 * @description: TODO
 * @date 2019/12/31
 */
@Slf4j
@Component
public class StringConsumer {
	/**
	 * 消息消费
	 *
	 * @RabbitHandler 代表此方法为接受到消息后的处理方法
	 */
	@RabbitListener
	(
		bindings = @QueueBinding
		(
			value = @Queue(value = "com.queue.notify.hello", durable = "true"),
			exchange = @Exchange(value = "com.exchange.notify.hello", durable = "true", type = "topic", ignoreDeclarationExceptions = "true"),
			key = "hello.*"
		)
	)
	@RabbitHandler
	public void recieved(Message message, Channel channel) {
		Long deliveryTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
		log.info("消费端Payload:{},消息体：{}，deliveryTag：{}", message.getPayload(),message.toString(),deliveryTag);
		//手工ACK,获取deliveryTag
		try {
			channel.basicAck(deliveryTag, false);
			channel.basicRecover(true);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
