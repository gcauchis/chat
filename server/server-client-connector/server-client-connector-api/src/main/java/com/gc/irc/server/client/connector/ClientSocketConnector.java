package com.gc.irc.server.client.connector;

/**
 * <p>ClientSocketConnector interface.</p>
 *
 * @author gcauchis
 * @version 0.0.5
 */
public interface ClientSocketConnector extends ClientConnector {
	/**
	 * Change the listening port
	 *
	 * @param port
	 *            New Listening Port.
	 */
	void setPort(int port);
}
