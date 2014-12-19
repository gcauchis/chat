package com.gc.irc.server.thread.factory;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.server.bridge.ServerBridgeConsumerFactory;
import com.gc.irc.server.client.connector.management.UsersConnectionsManagement;
import com.gc.irc.server.core.user.management.UserManagement;
import com.gc.irc.server.core.user.management.UserManagementAware;
import com.gc.irc.server.handler.message.ServerMessageHandler;
import com.gc.irc.server.thread.ServerManagement;
import com.gc.irc.server.thread.ServerManager;

/**
 * A factory for creating ServeurMBean objects.
 *
 * @author gcauchis
 * @version 0.0.4
 */
@Component("ServeurMBeanFactory")
@Scope("singleton")
public class ServerManagementFactory extends AbstractLoggable implements ServerManagerFactory, ApplicationContextAware {

    private ApplicationContext applicationContext;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.thread.factory.api.IServeurMBeanFactory#getServeurMBean
     * (com.gc.irc.server.core.ServerCore)
     */
    /** {@inheritDoc} */
    @Override
    public ServerManager getServeurManager() {
        return applicationContext.getBean(ServerManagement.class);
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}

}
