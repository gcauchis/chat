package com.gc.irc.server.thread.factory.impl;

import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement;
import com.gc.irc.server.jms.api.IJMSProducer;
import com.gc.irc.server.service.api.IAuthenticationService;
import com.gc.irc.server.service.api.IUserPictureService;
import com.gc.irc.server.thread.api.IGestionClientBean;
import com.gc.irc.server.thread.factory.api.IGestionClientBeanFactory;
import com.gc.irc.server.thread.impl.GestionClientBean;

/**
 * A factory for creating GestionClientBean objects.
 */
@Component("gestionClientBeanFactory")
@Scope("singleton")
public class GestionClientBeanFactory extends AbstractLoggable implements IGestionClientBeanFactory {

    /** The authentication service. */
    @Autowired
    private IAuthenticationService authenticationService;

    /** The jms producer. */
    @Autowired
    private IJMSProducer jmsProducer;

    /** The user picture service. */
    @Autowired
    private IUserPictureService userPictureService;

    /** The users connections management. */
    @Autowired
    private IUsersConnectionsManagement usersConnectionsManagement;

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.factory.api.IGestionClientBeanFactory#
     * getGestionClientBean(java.net.Socket, com.gc.irc.server.core.ServerCore)
     */
    @Override
    public IGestionClientBean getGestionClientBean(final Socket clientSocket) {
        final GestionClientBean gestionClientBean = new GestionClientBean(clientSocket);
        gestionClientBean.setUsersConnectionsManagement(usersConnectionsManagement);
        gestionClientBean.setJmsProducer(jmsProducer);
        gestionClientBean.setAuthenticationService(authenticationService);
        gestionClientBean.setUserPictureService(userPictureService);
        return gestionClientBean;
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
     * Sets the user connections management.
     * 
     * @param userConnectionsManagement
     *            the new user connections management
     */
    public void setUserConnectionsManagement(final IUsersConnectionsManagement userConnectionsManagement) {
        usersConnectionsManagement = userConnectionsManagement;
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
