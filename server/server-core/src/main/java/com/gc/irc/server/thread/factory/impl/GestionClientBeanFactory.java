package com.gc.irc.server.thread.factory.impl;

import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.server.core.user.management.api.IUserConnectionsManagement;
import com.gc.irc.server.thread.api.IGestionClientBean;
import com.gc.irc.server.thread.factory.api.IGestionClientBeanFactory;
import com.gc.irc.server.thread.impl.GestionClientBean;

/**
 * A factory for creating GestionClientBean objects.
 */
@Component("gestionClientBeanFactory")
@Scope("singleton")
public class GestionClientBeanFactory extends AbstractLoggable implements IGestionClientBeanFactory {

    /** The user connections management. */
    @Autowired
    private IUserConnectionsManagement userConnectionsManagement;

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.factory.api.IGestionClientBeanFactory#getGestionClientBean(java.net.Socket, com.gc.irc.server.core.ServerCore)
     */
    @Override
    public IGestionClientBean getGestionClientBean(Socket clientSocket) {
        return new GestionClientBean(clientSocket, userConnectionsManagement);
    }

    public void setUserConnectionsManagement(IUserConnectionsManagement userConnectionsManagement) {
        this.userConnectionsManagement = userConnectionsManagement;
    }

}