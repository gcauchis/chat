package com.gc.irc.server.client.connector.factory;

import java.net.Socket;

import com.gc.irc.server.client.connector.AbstractClientSocketConnection;
import com.gc.irc.server.client.connector.ClientConnection;

public abstract class AbstractClientSocketConnectionFactory <CC extends AbstractClientSocketConnection<?, ?>> extends AbstractClientConnectionFactory implements ClientSocketConnectionFactory{

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.factory.api.IGestionClientBeanFactory#
     * getGestionClientBean(java.net.Socket, com.gc.irc.server.core.ServerCore)
     */
    /** {@inheritDoc} */
    @Override
    public ClientConnection getClientConnection(final Socket clientSocket) {
        final CC clientConnection = build(clientSocket);
        fillDependency(clientConnection);
        return clientConnection;
    }

    protected abstract CC build(Socket clientSocket);


}
