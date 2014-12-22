package com.gc.irc.server.client.connector.provider;

import java.util.List;

import com.gc.irc.server.client.connector.ClientConnector;

/**
 * The Interface ClientConnectorProvider.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface ClientConnectorProvider {
	
	/**
	 * Gets the client connectors to use.
	 *
	 * @return the client connectors
	 */
	List<ClientConnector> getClientConnectors();
}
