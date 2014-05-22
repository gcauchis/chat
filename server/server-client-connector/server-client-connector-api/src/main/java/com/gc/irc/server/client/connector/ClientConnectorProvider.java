package com.gc.irc.server.client.connector;

import java.util.List;

/**
 * The Interface ClientConnectorProvider.
 */
public interface ClientConnectorProvider {
	
	/**
	 * Gets the client connectors to use.
	 *
	 * @return the client connectors
	 */
	List<ClientConnector> getClientConnectors();
}
