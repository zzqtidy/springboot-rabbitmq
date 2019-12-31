package com.zzq.de.rabbitproducer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	public void stringSend() {
		Date date = new Date();
		String dateString = new SimpleDateFormat("yyyy-mm-dd hh:MM:ss").format(date);
		System.out.println("发送的消息为:" + dateString);
		// 第一个参数为刚刚定义的队列名称
		this.rabbitTemplate.convertAndSend(Q_NAME, dateString);
	}
}
