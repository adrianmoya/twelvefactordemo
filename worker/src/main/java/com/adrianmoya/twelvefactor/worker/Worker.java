package com.adrianmoya.twelvefactor.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worker {
	
	private static final Logger log = LoggerFactory.getLogger(Worker.class);  
	
	public static void main(String[] args) {
		log.info("Processing data");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("Data processed");
	}
}
