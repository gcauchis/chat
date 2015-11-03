package com.gc.irc.server.client.connector.websocket;

import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.client.connector.AbstractClientConnection;
import com.gc.irc.server.client.connector.ClientConnection;

public class WebSocketClientConnection extends AbstractClientConnection implements ClientConnection {

	@Override
	public void send(Message message) {
		// TODO Auto-generated method stub
	}

	@Override
	public Message receiveMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void closeConnection() {
		// TODO Auto-generated method stub
	}


}
