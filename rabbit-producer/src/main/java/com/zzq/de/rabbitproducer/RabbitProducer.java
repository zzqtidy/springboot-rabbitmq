package com.zzq.de.rabbitproducer;


import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author TYLER
 * @title: RabbitProducer
 * @description: rabbit消息生产者
 * @date 2019/12/31
 */
@Component
public class RabbitProducer {
	/**
	 * 定义队列名
	 */
	private final static String Q_NAME = "com.queue.notify.hello";
	//这里注入一个AmqpTemplate来发布消息
	@Autowired
	private RabbitTemplate rabbitTemplate;

	//回调函数: confirm确认
	final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
		@Override
		public void confirm(CorrelationData correlationData, boolean ack, String cause)
		{
			System.out.println("correlationData: " + correlationData);
			System.out.println("ack: " + ack);
			if(!ack){
				//可以进行日志记录、异常处理、补偿处理等
				System.out.println("异常处理....");
			}else {
				//更新数据库，可靠性投递机制
				System.out.println("更新数据库，可靠性投递机制 ");
			}
		}
	};

	//回调函数: return返回
	final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
		@Override
		public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText,
		                            String exchange, String routingKey) {
			System.out.println("return exchange: " + exchange + ", routingKey: "
					+ routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
		}
	};

	//发送消息方法调用: 构建Message消息
	public void send(Object message, Map<String, Object> properties) throws Exception {
		MessageHeaders mhs = new MessageHeaders(properties);
		Message msg = MessageBuilder.createMessage(message, mhs);
		rabbitTemplate.setConfirmCallback(confirmCallback);
		rabbitTemplate.setReturnCallback(returnCallback);
		//id + 时间戳 全局唯一  用于ack保证唯一一条消息,在做补偿策略的时候，必须保证这是全局唯一的消息。
		long timestamp = System.currentTimeMillis();
		String timestampStr = String.valueOf(timestamp);
		CorrelationData correlationData = new CorrelationData(timestampStr);
		rabbitTemplate.convertAndSend("com.exchange.notify.hello", "hello.abc", msg, correlationData);
	}

	public void stringSend() {
		Date date = new Date();
		String dateString = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss").format(date);
		System.out.println("发送的消息为:" + dateString);
		// 第一个参数为刚刚定义的队列名称
		this.rabbitTemplate.convertAndSend(Q_NAME, dateString);
	}
}
