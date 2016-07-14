package com.adrianmoya.twelvefactor.web;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adrianmoya.twelvefactor.common.Configuration;
import com.adrianmoya.twelvefactor.web.backingservices.RedisService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import redis.clients.jedis.Jedis;
import spark.ModelAndView;
import spark.template.pebble.PebbleTemplateEngine;

public class Web {

	private final static Logger log = LoggerFactory.getLogger(Web.class); 
	
	public static void main(String[] args) {
		port(Integer.parseInt(Configuration.get(Configuration.PORT)));
		
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection;
		try {
			connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare("demo",false, false, false, null);
			String message = "Hello World!";
		    channel.basicPublish("", "demo", null, message.getBytes());
		    log.info(" [x] Sent '" + message + "'");
		    channel.close();
		    connection.close();
		} catch (Exception e){
			
		}
			
		get("/hello", (request, response) -> {
			Map<String, Object> attributes = new HashMap<>();
			Jedis jedis = RedisService.pool.getResource();
			jedis.set("foo", "bar");
			
			attributes.put("data", jedis.get("foo"));
			
			return new ModelAndView(attributes, "templates/index.html");
		}, new PebbleTemplateEngine());
		
		enableDebugScreen();
	}

}
