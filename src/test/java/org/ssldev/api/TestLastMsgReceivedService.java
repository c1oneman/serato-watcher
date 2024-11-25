package org.ssldev.api;

import java.util.LinkedList;
import java.util.List;

import org.ssldev.core.messages.MessageIF;
import org.ssldev.core.services.ServiceIF;

/**
 * test service 
 */
public class TestLastMsgReceivedService implements ServiceIF {
	public MessageIF lastMsgRecvd;
	public List<MessageIF> allMsgsRecvd = new LinkedList<>();
	
	@Override
	public void notify(MessageIF msg) {
		lastMsgRecvd = msg;
		allMsgsRecvd.add(msg);
	}
	@Override
	public void shutdown() {}
	@Override
	public String getName() {return "test service";}
	
}
