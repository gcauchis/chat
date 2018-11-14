package com.gc.irc.server.client.connector.factory;

import java.net.Socket;

import com.gc.irc.common.Loggable;
import com.gc.irc.server.client.connector.ClientConnection;

/**
 * <p>ClientSocketConnectionFactory interface.</p>
 *
 * @version 0.0.5
 * @author x472511
 */
public interface ClientSocketConnectionFactory extends Loggable {

	/**
	 * <p>getClientConnection.</p>
	 *
	 * @param clientSocket a {@link java.net.Socket} object.
	 * @return a {@link com.gc.irc.server.client.connector.ClientConnection} object.
	 */
	ClientConnection getClientConnection(final Socket clientSocket);
}
