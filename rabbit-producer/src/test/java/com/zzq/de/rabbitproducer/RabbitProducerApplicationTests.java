package com.zzq.de.rabbitproducer;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class RabbitProducerApplicationTests {

//	@Test
//	void contextLoads() {
//	}

	@Autowired
	private RabbitProducer producer;

	@Test
	public void testStringSend() {
		for (int i = 0; i < 10; i++) {
			producer.stringSend();
		}
	}

}
