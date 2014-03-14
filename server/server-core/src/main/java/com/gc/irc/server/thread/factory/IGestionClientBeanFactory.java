package com.gc.irc.server.thread.factory;

import java.net.Socket;

import com.gc.irc.common.ILoggable;
import com.gc.irc.server.client.connecter.ClientConnection;

/**
 * A factory for creating IGestionClientBean objects.
 */
public interface IGestionClientBeanFactory extends ILoggable {

    /**
     * Gets the gestion client bean.
     * 
     * @param clientSocket
     *            the client socket
     * @param parent
     *            the parent
     * @return the gestion client bean
     */
    ClientConnection getGestionClientBean(Socket clientSocket);

}
