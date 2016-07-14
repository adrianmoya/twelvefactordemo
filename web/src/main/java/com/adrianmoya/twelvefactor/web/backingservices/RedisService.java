package com.adrianmoya.twelvefactor.web.backingservices;

import java.net.URI;
import java.net.URISyntaxException;

import com.adrianmoya.twelvefactor.common.Configuration;

import redis.clients.jedis.JedisPool;

public class RedisService {
	
	public static JedisPool pool;
	private static URI redisURI;
	
	static {
		try {
			redisURI = new URI(Configuration.get(Configuration.REDIS_URI));
			pool = new JedisPool(redisURI);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
}
