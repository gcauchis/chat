package com.gc.irc.server.thread.factory.impl;

import java.net.Socket;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.server.core.ServerCore;
import com.gc.irc.server.thread.api.IGestionClientBean;
import com.gc.irc.server.thread.factory.api.IGestionClientBeanFactory;
import com.gc.irc.server.thread.impl.GestionClientBean;

/**
 * A factory for creating GestionClientBean objects.
 */
@Component("gestionClientBeanFactory")
@Scope("singleton")
public class GestionClientBeanFactory extends AbstractLoggable implements IGestionClientBeanFactory {

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.factory.api.IGestionClientBeanFactory#getGestionClientBean(java.net.Socket, com.gc.irc.server.core.ServerCore)
     */
    @Override
    public IGestionClientBean getGestionClientBean(Socket clientSocket, ServerCore parent) {
        return new GestionClientBean(clientSocket, parent);
    }

}
