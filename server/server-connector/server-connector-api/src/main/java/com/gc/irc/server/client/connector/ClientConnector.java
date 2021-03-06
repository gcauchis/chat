package com.gc.irc.server.client.connector;

/**
 * The Interface ClientConnector.
 *
 * @version 0.0.4
 * @author x472511
 */
public interface ClientConnector extends Runnable {

	/**
	 * Initialize the connector.
	 */
	void initConnector();
	
	/**
	 * Wait client. Create and run the {@link com.gc.irc.server.client.connector.ClientConnection}.
	 */
	void waitClient();
	
    /**
     * Checks if is initialized.
     *
     * @return a boolean.
     */
    boolean isInitialized();
}
