package com.adrianmoya.twelvefactor.web;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adrianmoya.twelvefactor.common.Configuration;
import com.adrianmoya.twelvefactor.common.queue.QueueFactory;
import com.adrianmoya.twelvefactor.common.queue.QueueService;
import com.adrianmoya.twelvefactor.web.backingservices.RedisService;
import redis.clients.jedis.Jedis;
import spark.ModelAndView;
import spark.Session;
import spark.route.RouteOverview;
import spark.template.pebble.PebbleTemplateEngine;

public class Web {

	private static final String SESSION_VALUE_PARAM_NAME = "sessionValue";
	private static final String REDIS_VALUE_PARAM_NAME = "redisValue";
	private final static Logger log = LoggerFactory.getLogger(Web.class);
	private static final QueueService queue = QueueFactory.getQueue();
	
	public static void main(String[] args) {
		//Configure Logger
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "DEBUG");
		System.setProperty(org.slf4j.impl.SimpleLogger.LOG_FILE_KEY, "System.out");
		
		int port = Integer.parseInt(Configuration.get(Configuration.PORT));
		port(port);
				
		get("/", (request, response) -> {
			Map<String, Object> attributes = new HashMap<>();
			Jedis jedis = RedisService.pool.getResource();

			attributes.put("port", port);
			
			String sessionValue = request.session().attribute(SESSION_VALUE_PARAM_NAME);
			if(sessionValue == null) sessionValue = "None";
			attributes.put(SESSION_VALUE_PARAM_NAME, sessionValue);
			
			String redisValue = jedis.get(REDIS_VALUE_PARAM_NAME);
			if(redisValue == null) redisValue = "None";
			attributes.put(REDIS_VALUE_PARAM_NAME, redisValue);

			return new ModelAndView(attributes, "templates/index.html");
		}, new PebbleTemplateEngine());
		
		get("/messagescount", (request, response) -> {
			return String.valueOf(queue.messageCount());
		});
		
		post("/processmessages", (request, response) -> {
			String messages = request.queryParams("messages");
			BufferedReader rdr = new BufferedReader(new StringReader(messages));
			for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
				queue.sendMessage(line);			    
			}
			rdr.close();
			response.redirect("/");
			return null;
		});
		
		post("/setsessionvalue", (request,response) -> {
			String sessionValue = request.queryParams(SESSION_VALUE_PARAM_NAME);
			request.session().attribute(SESSION_VALUE_PARAM_NAME, sessionValue);
			response.redirect("/");
			return null;
		});
		
		post("/setredisvalue", (request,response) -> {
			String redisValue = request.queryParams(REDIS_VALUE_PARAM_NAME);
			Jedis jedis = RedisService.pool.getResource();
			jedis.set(REDIS_VALUE_PARAM_NAME, redisValue);
			response.redirect("/");
			return null;
		});
		
		enableDebugScreen();
		RouteOverview.enableRouteOverview("/routes");
	}

}
