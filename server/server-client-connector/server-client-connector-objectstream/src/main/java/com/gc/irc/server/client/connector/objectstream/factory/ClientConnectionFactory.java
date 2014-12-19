package com.gc.irc.server.client.connector.objectstream.factory;

import java.net.Socket;

import com.gc.irc.common.Loggable;
import com.gc.irc.server.client.connector.ClientConnection;

/**
 * A factory for creating IGestionClientBean objects.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface ClientConnectionFactory extends Loggable {

    /**
     * Gets the gestion client bean.
     *
     * @param clientSocket
     *            the client socket
     * @return the gestion client bean
     */
    ClientConnection getClientConnection(Socket clientSocket);

}
