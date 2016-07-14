package com.adrianmoya.twelvefactor.common.queue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adrianmoya.twelvefactor.common.Configuration;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitQueue implements QueueService {

	private static final String QUEUE_NAME = "demo";
	private final Logger log = LoggerFactory.getLogger(RabbitQueue.class);
	
	private Connection connection;
	private Channel channel;

	public RabbitQueue() throws URISyntaxException, IOException, TimeoutException, KeyManagementException, NoSuchAlgorithmException {
		ConnectionFactory factory = new ConnectionFactory();
		URI rabbitConfig = new URI(Configuration.get(Configuration.RABBITMQ_URI));
		factory.setUri(rabbitConfig);
	    connection = factory.newConnection();
	    channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME,false, false, false, null);
	}
	
	@Override
	public void sendMessage(String message) {		 
		try {
		    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		    log.info("Sent '" + message + "' for processing");
		} catch (Exception e){
			log.error("An error occurred sending message {}", message, e);
		}
	}

	@Override
	public boolean processMessage() {
		// TODO Auto-generated method stub
		return false;
	}

}
