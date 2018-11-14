package com.gc.irc.server.client.connector.websocket;

import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.client.connector.AbstractClientConnection;
import com.gc.irc.server.client.connector.ClientConnection;

/**
 * <p>WebSocketClientConnection class.</p>
 *
 * @author x472511
 * @version 0.0.4
 */
public class WebSocketClientConnection extends AbstractClientConnection implements ClientConnection {

	/** {@inheritDoc} */
	@Override
	public void send(Message message) {
		// TODO Auto-generated method stub
	}

	/** {@inheritDoc} */
	@Override
	public Message receiveMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected void disconnect() {
		// TODO Auto-generated method stub
	}


}
