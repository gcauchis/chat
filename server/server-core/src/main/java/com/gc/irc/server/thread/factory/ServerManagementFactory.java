package com.gc.irc.server.thread.factory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.server.bridge.ServerBridgeConsumerFactory;
import com.gc.irc.server.core.user.management.UserManagement;
import com.gc.irc.server.core.user.management.UserManagementAware;
import com.gc.irc.server.core.user.management.UsersConnectionsManagement;
import com.gc.irc.server.handler.message.ServerMessageHandler;
import com.gc.irc.server.thread.ServeurManagement;
import com.gc.irc.server.thread.ServeurManager;

/**
 * A factory for creating ServeurMBean objects.
 *
 * @author gcauchis
 * @version 0.0.4
 */
@Component("ServeurMBeanFactory")
@Scope("singleton")
public class ServerManagementFactory extends AbstractLoggable implements ServeurManagerFactory, UserManagementAware {

    /** The server bridge consumer factory. */
    @Autowired
    private ServerBridgeConsumerFactory serverBridgeConsumerFactory;

    /** The server message handlers. */
    @Autowired
    private List<ServerMessageHandler> serverMessageHandlers;

    /** The users connections management. */
    @Autowired
    private UsersConnectionsManagement usersConnectionsManagement;
    
    /** The user management */
    private UserManagement userManagement;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.thread.factory.api.IServeurMBeanFactory#getServeurMBean
     * (com.gc.irc.server.core.ServerCore)
     */
    /** {@inheritDoc} */
    @Override
    public ServeurManager getServeurManager() {
        final ServeurManagement serveurMBean = new ServeurManagement();
        serveurMBean.setUsersConnectionsManagement(usersConnectionsManagement);
        serveurMBean.setServerMessageHandlers(serverMessageHandlers);
        serveurMBean.setServerBridgeConsumerFactory(serverBridgeConsumerFactory);
        serveurMBean.setUserManagement(userManagement);
        return serveurMBean;
    }

    /**
     * Sets the server bridge consumer factory.
     *
     * @param serverBridgeConsumerFactory
     *            the server bridge consumer factory
     */
    public void setServerBridgeConsumerFactory(final ServerBridgeConsumerFactory serverBridgeConsumerFactory) {
        this.serverBridgeConsumerFactory = serverBridgeConsumerFactory;
    }

    /**
     * Sets the server message handlers.
     *
     * @param serverMessageHandlers
     *            the new server message handlers
     */
    public void setServerMessageHandlers(final List<ServerMessageHandler> serverMessageHandlers) {
        this.serverMessageHandlers = serverMessageHandlers;
    }

    /**
     * Sets the users connections management.
     *
     * @param usersConnectionsManagement
     *            the new users connections management
     */
    public void setUsersConnectionsManagement(final UsersConnectionsManagement usersConnectionsManagement) {
        this.usersConnectionsManagement = usersConnectionsManagement;
    }
    
    /** {@inheritDoc} */
    @Override
	@Autowired
	public void setUserManagement(UserManagement userManagement) {
		this.userManagement = userManagement;
	}

}
