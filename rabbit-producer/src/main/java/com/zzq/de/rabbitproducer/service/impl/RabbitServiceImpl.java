package com.zzq.de.rabbitproducer.service.impl;

import com.zzq.de.rabbitproducer.service.RabbitService;
import com.zzq.de.rabbitproducer.util.RabbitManager;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author TYLER
 * @title: RabbitServiceImpl
 * @description: TODO
 * @date 2020/1/2
 */
@Service
public class RabbitServiceImpl implements RabbitService {

	@Autowired
	RabbitManager rabbitManager;

	@Override
	public Message getMessage(String messageType, Object msg) {
		return null;
	}

	@Override
	public void sendMessageToExchange(String routingKey, Object msg) {

	}

	@Override
	public void sendMessageToExchange(AbstractExchange exchange, String msg) {

	}

	@Override
	public void sendToQueue(String queueName, String msg) {

	}

	@Override
	public void addExchange(AbstractExchange exchange) {

	}

	@Override
	public boolean deleteExchange(String exchangeName) {
		return false;
	}

	/**
	 * 创建指定名称的Queue
	 * @param queueName 对列名称
	 * @return
	 */
	@Override
	public String addQueue(String queueName) {
		Queue queue = new Queue(queueName);
		return rabbitManager.addQueue(queue);
	}

	/**
	 * 删除指定名称的Queue
	 * @param queueName 对列名称
	 * @param unused 是否被使用
	 * @param empty 是否为空
	 * @return
	 */
	@Override
	public boolean deleteQueue(String queueName, boolean unused, boolean empty) {
		return rabbitManager.deleteQueue(queueName,unused,empty);
	}

	@Override
	public void addBindingQueue2Exchange(String queueName, String exchangeName, String routingKey) {
		Queue queue = new Queue(queueName);
		TopicExchange exchange = (TopicExchange) rabbitManager.createExchange(exchangeName,true,false,"topic");
		rabbitManager.addBinding(queue,exchange,routingKey);
	}

	@Override
	public void addBindingExchange2Exchange(String exchange, String topicExchange, String routingKey) {

	}

	@Override
	public void removeBinding(Binding binding) {

	}
}
