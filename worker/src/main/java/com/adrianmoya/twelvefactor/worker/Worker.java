package com.adrianmoya.twelvefactor.worker;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adrianmoya.twelvefactor.common.Configuration;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Worker {
	
	private static final Logger log = LoggerFactory.getLogger(Worker.class);
	private static final String QUEUE_NAME = "demo";  
	
	public static void main(String[] args) throws IOException, TimeoutException, URISyntaxException {
		ConnectionFactory factory = new ConnectionFactory();
		URI rabbitConfig = new URI(Configuration.get(Configuration.RABBITMQ_URI));
	    factory.setHost(rabbitConfig.getHost());
	    factory.setPort(rabbitConfig.getPort());
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
		channel.basicQos(1);

	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    log.info(" [*] Waiting for messages. To exit press CTRL+C");
	    
	    Consumer consumer = new DefaultConsumer(channel) {
	        @Override
	        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
	            throws IOException {
	          String message = new String(body, "UTF-8");
	          log.info("Received {} for processing",message);
	          try {
	              doWork(message);
	            } finally {
	              log.info("Done processing");
	              channel.basicAck(envelope.getDeliveryTag(), false);
	          }
	        }
	      };
	      channel.basicConsume(QUEUE_NAME, false, consumer);	    
	}

	private static void doWork(String message) {
		StringBuffer result = new StringBuffer(message);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		log.info("Process result: {}", result.reverse());
	}
}
