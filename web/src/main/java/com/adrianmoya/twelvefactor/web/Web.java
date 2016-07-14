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
import spark.template.pebble.PebbleTemplateEngine;

public class Web {

	private final static Logger log = LoggerFactory.getLogger(Web.class);
	private static final QueueService queue = QueueFactory.getQueue();
	
	public static void main(String[] args) {
		port(Integer.parseInt(Configuration.get(Configuration.PORT)));
			
		get("/", (req, res) -> {
			Map<String, Object> attributes = new HashMap<>();
			Jedis jedis = RedisService.pool.getResource();
			jedis.set("foo", "bar");
			
			attributes.put("data", jedis.get("foo"));
			
			return new ModelAndView(attributes, "templates/index.html");
		}, new PebbleTemplateEngine());
		
		post("/processmessages", (req, res) -> {
			String messages = req.queryParams("messages");
			BufferedReader rdr = new BufferedReader(new StringReader(messages));
			for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
				queue.sendMessage(line);			    
			}
			rdr.close();
			return null;
		});
		
		enableDebugScreen();
	}

}
