package com.zzq.de.rabbitproducer.controller;

import com.zzq.de.rabbitproducer.RabbitProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	@RequestMapping(value = "/send")
	public String sendMq(){
		rabbitProducer.stringSend();
		return "OK";
	}
}
