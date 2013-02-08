package com.gc.irc.server.thread.factory.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.server.bridge.api.IServerBridgeConsumerFactory;
import com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement;
import com.gc.irc.server.handler.message.api.IServerMessageHandler;
import com.gc.irc.server.thread.api.IServeurMBean;
import com.gc.irc.server.thread.factory.api.IServeurMBeanFactory;
import com.gc.irc.server.thread.impl.ServeurMBean;

/**
 * A factory for creating ServeurMBean objects.
 */
@Component("ServeurMBeanFactory")
@Scope("singleton")
public class ServeurMBeanFactory extends AbstractLoggable implements IServeurMBeanFactory {

    /** The server bridge consumer factory. */
    @Autowired
    private IServerBridgeConsumerFactory serverBridgeConsumerFactory;

    /** The server message handlers. */
    @Autowired
    private List<IServerMessageHandler> serverMessageHandlers;

    /** The users connections management. */
    @Autowired
    private IUsersConnectionsManagement usersConnectionsManagement;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.thread.factory.api.IServeurMBeanFactory#getServeurMBean
     * (com.gc.irc.server.core.ServerCore)
     */
    @Override
    public IServeurMBean getServeurMBean() {
        final ServeurMBean serveurMBean = new ServeurMBean();
        serveurMBean.setUsersConnectionsManagement(usersConnectionsManagement);
        serveurMBean.setServerMessageHandlers(serverMessageHandlers);
        serveurMBean.setServerBridgeConsumerFactory(serverBridgeConsumerFactory);
        return serveurMBean;
    }

    /**
     * Sets the server bridge consumer factory.
     * 
     * @param serverBridgeConsumerFactory
     *            the server bridge consumer factory
     */
    public void setServerBridgeConsumerFactory(final IServerBridgeConsumerFactory serverBridgeConsumerFactory) {
        this.serverBridgeConsumerFactory = serverBridgeConsumerFactory;
    }

    /**
     * Sets the server message handlers.
     * 
     * @param serverMessageHandlers
     *            the new server message handlers
     */
    public void setServerMessageHandlers(final List<IServerMessageHandler> serverMessageHandlers) {
        this.serverMessageHandlers = serverMessageHandlers;
    }

    /**
     * Sets the users connections management.
     * 
     * @param usersConnectionsManagement
     *            the new users connections management
     */
    public void setUsersConnectionsManagement(final IUsersConnectionsManagement usersConnectionsManagement) {
        this.usersConnectionsManagement = usersConnectionsManagement;
    }

}
