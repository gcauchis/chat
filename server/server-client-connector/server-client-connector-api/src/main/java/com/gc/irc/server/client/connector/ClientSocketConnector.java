package com.gc.irc.server.client.connector;

public interface ClientSocketConnector extends ClientConnector {
	 /**
     * Change the listening port
     *
     * @param port
     *            New Listening Port.
     */
	void setPort(int port);
}
