package com.zzq.de.rabbitproducer.controller;

import com.zzq.de.rabbitproducer.RabbitProducer;
import com.zzq.de.rabbitproducer.dto.JsonResponse;
import com.zzq.de.rabbitproducer.service.RabbitService;
import com.zzq.de.rabbitproducer.util.RabbitManager;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author TYLER
 * @title: TestSendMq
 * @description: TODO
 * @date 2019/12/31
 */
@RestController
@RequestMapping(value = "/mq")
public class TestSendMq {
	@Autowired
	private RabbitProducer rabbitProducer;

	@Autowired
	RabbitService rabbitService;

	@Autowired
	RabbitManager rabbitManager;

	@RequestMapping(value = "/queue_add")
	public String queueAdd(){
		rabbitService.addQueue("com.hello."+System.currentTimeMillis());
		return "OK";
	}

	@RequestMapping(value = "/queue_del")
	public String queueDel(String qname){
		rabbitService.deleteQueue(qname,false,true);
		return "OK";
	}
	@RequestMapping(value = "/queue_bind")
	public JsonResponse queueBind(String qname,String exname,String rKey){
		Queue queue = new Queue(qname);
		TopicExchange exchange = (TopicExchange) rabbitManager.createExchange(exname, true, false, "topic");
		rabbitManager.addBinding(queue,exchange,rKey+".#");
		return new JsonResponse().success();
	}
	@RequestMapping(value = "/api_info")
	public JsonResponse apiInfo(){
		List<String> allQueueFromRabbitApi = rabbitManager.getAllQueueFromRabbitApi();
		List<String> queueFromRabbitApi = rabbitManager.getQueueFromRabbitApi();
		rabbitManager.getQueueFromRabbitApi("", "com.queue.notify.hello")
//		String result = rabbitManager.getRabbitApiData("/api/bindings");//所有绑定关系
//		String result1 = rabbitManager.getRabbitApiData("/api/users/");//所有用户
//
//		String result3 = rabbitManager.getRabbitApiData("/api/exchanges/testhost/com.exchange.notify.hello/bindings/source");//特定exchange的绑定信息
//		String result4 = rabbitManager.getRabbitApiData("/api/exchanges/testhost");//特定vhost下所有的exchange信息
//		String result5 = rabbitManager.getRabbitApiData("/api/exchanges");//所有exchange的信息
//		String result6 = rabbitManager.getRabbitApiData("/api/queues");//所有queue的信息
//		String result7 = rabbitManager.getRabbitApiData("/api/queues/testhost");//特定vhost下所有queue的信息
//		String result2 = rabbitManager.getRabbitApiData("/api/queues/testhost/com.queue.notify.hello/bindings");//对应vhost下特定队列的绑定关系
//		String result8 = rabbitManager.getRabbitApiData("/api/vhosts");//所有vhost
//		String result9 = rabbitManager.getRabbitApiData("/api/vhosts/testhost");//特定vhost
		return new JsonResponse().success(allQueueFromRabbitApi);
	}

	@RequestMapping(value = "/send")
	public JsonResponse sendMq(){
		try {
			rabbitProducer.send("123",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JsonResponse().success();
	}
}
