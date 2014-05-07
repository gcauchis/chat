package com.gc.irc.server.client.connector;

/**
 * The Interface ClientConnector.
 */
public interface ClientConnector {

	/**
	 * Initialize the connector.
	 */
	void initConnector();
	
	/**
	 * Wait client. Create and run the {@link ClientConnection}.
	 */
	void waitClient();
}
