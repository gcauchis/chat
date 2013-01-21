package com.gc.irc.server.thread.factory.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement;
import com.gc.irc.server.handler.message.api.IServerMessageHandler;
import com.gc.irc.server.jms.api.IJMSProducer;
import com.gc.irc.server.service.api.IAuthenticationService;
import com.gc.irc.server.service.api.IUserPictureService;
import com.gc.irc.server.thread.api.IServeurMBean;
import com.gc.irc.server.thread.factory.api.IServeurMBeanFactory;
import com.gc.irc.server.thread.impl.ServeurMBean;

/**
 * A factory for creating ServeurMBean objects.
 */
@Component("ServeurMBeanFactory")
@Scope("singleton")
public class ServeurMBeanFactory extends AbstractLoggable implements IServeurMBeanFactory {

    /** The authentication service. */
    @Autowired
    private IAuthenticationService authenticationService;

    /** The jms producer. */
    @Autowired
    private IJMSProducer jmsProducer;

    /** The num passage max. */
    @Value("${nbMessageMaxPassage}")
    private int numPassageMax = 10;

    /** The server message handlers. */
    @Autowired
    private List<IServerMessageHandler> serverMessageHandlers;

    /** The user picture service. */
    @Autowired
    private IUserPictureService userPictureService;

    /** The users connections management. */
    @Autowired
    private IUsersConnectionsManagement usersConnectionsManagement;

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.factory.api.IServeurMBeanFactory#getServeurMBean (com.gc.irc.server.core.ServerCore)
     */
    @Override
    public IServeurMBean getServeurMBean() {
        final ServeurMBean serveurMBean = new ServeurMBean();
        serveurMBean.setUsersConnectionsManagement(usersConnectionsManagement);
        serveurMBean.setJmsProducer(jmsProducer);
        serveurMBean.setNumPassageMax(numPassageMax);
        serveurMBean.setAuthenticationService(authenticationService);
        serveurMBean.setUserPictureService(userPictureService);
        serveurMBean.setServerMessageHandlers(serverMessageHandlers);
        return serveurMBean;
    }

    /**
     * Sets the authentication service.
     * 
     * @param authenticationService
     *            the new authentication service
     */
    public void setAuthenticationService(final IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Sets the jms producer.
     * 
     * @param jmsProducer
     *            the new jms producer
     */
    public void setJmsProducer(final IJMSProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }

    /**
     * Sets the num passage max.
     * 
     * @param numPassageMax
     *            the new num passage max
     */
    public void setNumPassageMax(final int numPassageMax) {
        this.numPassageMax = numPassageMax;
    }

    /**
     * Sets the server message handlers.
     * 
     * @param serverMessageHandlers
     *            the new server message handlers
     */
    public void setServerMessageHandlers(List<IServerMessageHandler> serverMessageHandlers) {
        this.serverMessageHandlers = serverMessageHandlers;
    }

    /**
     * @param userPictureService
     *            the userPictureService to set
     */
    public void setUserPictureService(final IUserPictureService userPictureService) {
        this.userPictureService = userPictureService;
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
