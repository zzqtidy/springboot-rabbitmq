package com.zzq.de.rabbitproducer.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zzq.de.rabbitproducer.configurer.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TYLER
 * @title: RabbitManage
 * @description: TODO
 * @date 2020/1/2
 */
@Slf4j
@Component
public class RabbitManager {
	@Autowired
	private RabbitConfig rabbitConfig;

	private final AmqpAdmin amqpAdmin;

	private final AmqpTemplate amqpTemplate;

	/**
	 * 自定义的Exchange
	 */
	private final TopicExchange topicExchange;

	@Autowired
	public RabbitManager(AmqpAdmin amqpAdmin, AmqpTemplate amqpTemplate, TopicExchange topicExchange) {
		this.amqpAdmin = amqpAdmin;
		this.amqpTemplate = amqpTemplate;
		this.topicExchange = topicExchange;
	}


	/**
	 * 转换Message对象
	 *
	 * @param messageType 返回消息类型 MessageProperties类中常量
	 * @param msg
	 * @return
	 */
	private Message getMessage(String messageType, Object msg) {
		MessageProperties messageProperties = new MessageProperties();
		messageProperties.setContentType(messageType);
		return new Message(msg.toString().getBytes(), messageProperties);
	}

	/**
	 * 有绑定Key的Exchange发送
	 *
	 * @param routingKey
	 * @param msg
	 */
	public void sendMessageToExchange(String routingKey, Object msg) {
		Message message = getMessage(MessageProperties.CONTENT_TYPE_JSON, msg);
		amqpTemplate.send(topicExchange.getName(), routingKey, message);
	}

	/**
	 * 没有绑定KEY的Exchange发送
	 *
	 * @param exchangeName
	 * @param msg
	 */
	public void sendMessageToExchange(String exchangeName, String msg) {
		this.addExchange(exchangeName, true, false, "TOPIC");
		log.info("RabbitMQ send " + exchangeName + "->" + msg);
		amqpTemplate.convertAndSend(topicExchange.getName(), msg);
	}

	/**
	 * 给queue发送消息
	 *
	 * @param queueName
	 * @param msg
	 */
	@Async
	public void sendToQueue(String queueName, String msg) {
		Queue queue = new Queue(queueName);
		this.addQueue(queue);
		Binding binding = BindingBuilder.bind(queue).to(DirectExchange.DEFAULT).withQueueName();
		amqpAdmin.declareBinding(binding);
		MessageProperties messageProperties = new MessageProperties();
		//设置消息内容的类型，默认是 application/octet-stream 会是 ASCII 码值
//		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		Message message = new Message(msg.getBytes(), messageProperties);
		amqpTemplate.convertAndSend(DirectExchange.DEFAULT.getName(), queueName, message);
	}

	/**
	 * 创建Exchange
	 *
	 * @param exchangeName 交换机名称
	 * @param durable      是否持久化
	 * @param autoDelete   是否自动删除
	 * @param exchangeType 交换机类型：TOPIC，DIRECT，FANOUT
	 * @return
	 */
	private boolean addExchange(String exchangeName, boolean durable, boolean autoDelete, String exchangeType) {
		try {

			AbstractExchange exchange = createExchange(exchangeName, durable, autoDelete, exchangeType);
			amqpAdmin.declareExchange(exchange);
			return true;
		} catch (Exception ex) {
			log.error("创建Exchange[{}]异常：{}", exchangeName, ex.getMessage());
			return false;
		}
	}

	public AbstractExchange createExchange(String exchangeName, boolean durable, boolean autoDelete, String exchangeType) {
		AbstractExchange exchange = null;
		if (exchangeType.toUpperCase().equals("TOPIC")) {
			exchange = new TopicExchange(exchangeName, durable, autoDelete);
		} else if (exchangeType.toUpperCase().equals("DIRECT")) {
			exchange = new DirectExchange(exchangeName, durable, autoDelete);
		}
		if (exchangeType.toUpperCase().equals("FANOUT")) {
			exchange = new FanoutExchange(exchangeName, durable, autoDelete);
		}
		return exchange;
	}

	/**
	 * 删除一个Exchange
	 *
	 * @param exchangeName
	 */
	public boolean deleteExchange(String exchangeName) {
		return amqpAdmin.deleteExchange(exchangeName);
	}

	/**
	 * 创建一个指定的Queue
	 *
	 * @param queue
	 * @return queueName
	 */
	public String addQueue(Queue queue) {
		return amqpAdmin.declareQueue(queue);
	}

	/**
	 * Delete a queue.
	 *
	 * @param queueName the name of the queue.
	 * @param unused    true if the queue should be deleted only if not in use.
	 * @param empty     true if the queue should be deleted only if empty.
	 * @return
	 */
	public boolean deleteQueue(String queueName, boolean unused, boolean empty) {
		try {
			amqpAdmin.deleteQueue(queueName, unused, empty);
			return true;
		} catch (Exception ex) {
			log.error("删除队列{}异常:{}", queueName, ex.getMessage());
			return false;
		}
	}

	/**
	 * 删除一个queue
	 *
	 * @param queueName
	 * @return true if the queue existed and was deleted.
	 */
	public boolean deleteQueue(String queueName) {
		return amqpAdmin.deleteQueue(queueName);
	}

	/**
	 * 绑定一个队列到一个匹配型交换器使用一个routingKey
	 *
	 * @param queue
	 * @param exchange
	 * @param routingKey
	 */
	public void addBinding(Queue queue, TopicExchange exchange, String routingKey) {
		Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
		amqpAdmin.declareBinding(binding);
	}

	/**
	 * 绑定一个Exchange到一个匹配型Exchange 使用一个routingKey
	 *
	 * @param exchange
	 * @param topicExchange
	 * @param routingKey
	 */
	public void addBinding(Exchange exchange, TopicExchange topicExchange, String routingKey) {
		Binding binding = BindingBuilder.bind(exchange).to(topicExchange).with(routingKey);
		amqpAdmin.declareBinding(binding);
	}

	/**
	 * 去掉一个binding
	 *
	 * @param binding
	 */
	public void removeBinding(Binding binding) {
		amqpAdmin.removeBinding(binding);
	}

	private CorrelationData getCorrelationData() {
		long timestamp = System.currentTimeMillis();
		String timestampStr = String.valueOf(timestamp);
		CorrelationData correlationData = new CorrelationData(timestampStr);
		return correlationData;
	}

	public List<String> getAllQueueFromRabbitApi() {
		String result = getRabbitApiData("/api/queues");
		List<String> list = getParamValueListFromJsonList(result,"name");
		return list;
	}
	public List<String> getQueueFromRabbitApi(String vhost) {
		String result = getRabbitApiData("/api/queues/"+vhost);
		List<String> list = getParamValueListFromJsonList(result,"name");
		return list;
	}
	public List<String> getQueueFromRabbitApi() {
		String vhost = rabbitConfig.getVirtualHost();
		return getQueueFromRabbitApi(vhost);
	}

	public String getQueueFromRabbitApi(String vhost,String queueName) {
		if(StringUtils.isEmpty(vhost))
			vhost = rabbitConfig.getVirtualHost();
		return getRabbitApiData("/api/queues/"+vhost+"/"+queueName);
	}


	public String getRabbitApiData(String urlPath) {
		// 获取队列名称
		String address = rabbitConfig.getAddresses();
		String port = rabbitConfig.getPort();
		String username = rabbitConfig.getUsername();
		String password = rabbitConfig.getPassword();
		// 根据RabbitMQ提供的队列信息, 每天定时删除动态创建的队列。
		if (!urlPath.startsWith("/"))
			urlPath = "/" + urlPath;
		String url = "http://" + address + ":" + port + urlPath;
		String result = null;
		try {
			result = RabbitHttpApiUtil.Get(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
	private List<String> getParamValueListFromJsonList(String jsonStr,String param){
		List<String> list = new ArrayList<>();
		if (!StringUtils.isEmpty(jsonStr)) {
			JSONArray jsonArray = JSON.parseArray(jsonStr);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = JSONObject.parseObject(jsonArray.getString(i));
				String name = jsonObject.getString("name");
				list.add(name);
			}
		}
		return list;
	}
}
