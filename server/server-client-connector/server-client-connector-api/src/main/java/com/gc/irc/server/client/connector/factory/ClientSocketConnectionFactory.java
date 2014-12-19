package com.gc.irc.server.client.connector.factory;

import java.net.Socket;

import com.gc.irc.common.Loggable;
import com.gc.irc.server.client.connector.ClientConnection;

public interface ClientSocketConnectionFactory extends Loggable {

	ClientConnection getClientConnection(final Socket clientSocket);
}
