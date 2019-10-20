package com.wait.chat;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author collinewaitire 20 Oct 2019
 */
public interface ChatServiceStreams {

	// String NEW_COMMENT = "newComments";
	String CLIENT_TO_BROKER = "clientToBroker";
	String BROKER_TO_CLIENT = "brokerToClient";
	
	String USER_HEADER = "User";

//	@Input(NEW_COMMENT)
//	SubscribableChannel newComments();

	@Output(CLIENT_TO_BROKER)
	MessageChannel clientToBroker();

	@Input(BROKER_TO_CLIENT)
	SubscribableChannel brokerToClient();
}
