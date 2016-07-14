package com.adrianmoya.twelvefactor.common;

public class Configuration {

	public static final String PORT = "PORT";
	public static final String REDIS_URI = "REDIS_URI";
	public static final String RABBITMQ_URI = "RABBITMQ_URI";
	
	public static String get(String name){
		if(System.getenv(name) != null){
			return System.getenv(name);
		}
		throw new IllegalStateException("Environment var not set: "+name);
	}
}
