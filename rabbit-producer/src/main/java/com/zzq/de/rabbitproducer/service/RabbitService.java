package com.zzq.de.rabbitproducer.service;

import org.springframework.amqp.core.AbstractExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Message;

/**
 * @author TYLER
 * @title: RabbitService
 * @description: TODO
 * @date 2020/1/2
 */
public interface RabbitService {
	/**
	 * 转换Message对象
	 *
	 * @param messageType 返回消息类型 MessageProperties类中常量
	 * @param msg
	 * @return
	 */
	Message getMessage(String messageType, Object msg);

	/**
	 * 有绑定Key的Exchange发送
	 *
	 * @param routingKey
	 * @param msg
	 */
	void sendMessageToExchange(String routingKey, Object msg);

	/**
	 * 没有绑定KEY的Exchange发送
	 *
	 * @param exchange
	 * @param msg
	 */
	void sendMessageToExchange(AbstractExchange exchange, String msg);

	/**
	 * 给queue发送消息
	 *
	 * @param queueName
	 * @param msg
	 */
	void sendToQueue(String queueName, String msg);

	/**
	 * 创建Exchange
	 *
	 * @param exchange
	 */
	void addExchange(AbstractExchange exchange);

	/**
	 * 删除一个Exchange
	 *
	 * @param exchangeName
	 */
	boolean deleteExchange(String exchangeName);

	/**
	 * 创建一个指定的Queue
	 *
	 * @param queueName
	 * @return queueName
	 */
	String addQueue(String queueName);

	/**
	 * 删除一个queue
	 *
	 * @param queueName
	 * @return true if the queue existed and was deleted.
	 */
	boolean deleteQueue(String queueName, boolean unused, boolean empty);

	/**
	 * 绑定一个队列到一个匹配型交换器使用一个routingKey
	 *
	 * @param queue
	 * @param exchange
	 * @param routingKey
	 */
	void addBindingQueue2Exchange(String queue, String exchange, String routingKey);

	/**
	 * 绑定一个Exchange到一个匹配型Exchange 使用一个routingKey
	 *
	 * @param exchange
	 * @param topicExchange
	 * @param routingKey
	 */
	void addBindingExchange2Exchange(String exchange, String topicExchange, String routingKey);

	/**
	 * 去掉一个binding
	 *
	 * @param binding
	 */
	void removeBinding(Binding binding);
}
