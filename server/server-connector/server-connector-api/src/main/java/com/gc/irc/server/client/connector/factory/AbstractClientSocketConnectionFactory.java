package com.gc.irc.server.client.connector.factory;

import java.net.Socket;

import com.gc.irc.server.client.connector.AbstractClientSocketConnection;
import com.gc.irc.server.client.connector.ClientConnection;

/**
 * <p>Abstract AbstractClientSocketConnectionFactory class.</p>
 *
 * @author x472511
 * @version 0.0.4
 */
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

    /**
     * <p>build.</p>
     *
     * @param clientSocket a {@link java.net.Socket} object.
     * @return a CC object.
     */
    protected abstract CC build(Socket clientSocket);


}
