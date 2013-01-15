package com.gc.irc.server.thread.factory.api;

import java.net.Socket;

import com.gc.irc.common.api.ILoggable;
import com.gc.irc.server.thread.api.IGestionClientBean;

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
    IGestionClientBean getGestionClientBean(Socket clientSocket);

}
