package com.adrianmoya.twelvefactor.common.queue;

public class QueueFactory {

	public static QueueService getQueue(){
		try {
			return new RabbitQueue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
