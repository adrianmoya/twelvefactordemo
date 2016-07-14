package com.adrianmoya.twelvefactor.common.queue;

public interface QueueService {

	void sendMessage(String message);
	boolean processMessage();
	
}
