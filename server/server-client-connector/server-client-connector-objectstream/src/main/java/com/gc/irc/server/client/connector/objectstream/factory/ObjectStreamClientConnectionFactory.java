package com.gc.irc.server.client.connector.objectstream.factory;

import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.server.bridge.ServerBridgeProducer;
import com.gc.irc.server.client.connector.ClientConnection;
import com.gc.irc.server.client.connector.management.UsersConnectionsManagement;
import com.gc.irc.server.client.connector.objectstream.ObjectStreamClientConnection;
import com.gc.irc.server.core.user.management.UserManagement;
import com.gc.irc.server.core.user.management.UserManagementAware;
import com.gc.irc.server.core.user.management.UserPicturesManagement;
import com.gc.irc.server.service.AuthenticationService;
import com.gc.irc.server.service.UserPictureService;

/**
 * A factory for creating GestionClientBean objects.
 *
 * @author gcauchis
 * @version 0.0.4
 */
@Component("gestionClientBeanObjectStreamFactory")
@Scope("singleton")
public class ObjectStreamClientConnectionFactory extends AbstractLoggable implements ClientConnectionFactory, UserManagementAware {

    /** The authentication service. */
    @Autowired
    private AuthenticationService authenticationService;

    /** The jms producer. */
    @Autowired
    private ServerBridgeProducer serverBridgeProducer;

    /** The user picture service. */
    @Autowired
    private UserPictureService userPictureService;

    /** The users connections management. */
    @Autowired
    private UsersConnectionsManagement usersConnectionsManagement;
    
    /** The users pictures management. */
    @Autowired
    private UserPicturesManagement userPicturesManagement;
    
    /** The user management */
    private UserManagement userManagement;

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.factory.api.IGestionClientBeanFactory#
     * getGestionClientBean(java.net.Socket, com.gc.irc.server.core.ServerCore)
     */
    /** {@inheritDoc} */
    @Override
    public ClientConnection getClientConnection(final Socket clientSocket) {
        final ObjectStreamClientConnection gestionClientBean = new ObjectStreamClientConnection(clientSocket);
        gestionClientBean.setUsersConnectionsManagement(usersConnectionsManagement);
        gestionClientBean.setServerBridgeProducer(serverBridgeProducer);
        gestionClientBean.setAuthenticationService(authenticationService);
        gestionClientBean.setUserPictureService(userPictureService);
        gestionClientBean.setUserManagement(userManagement);
        gestionClientBean.setUserPicturesManagement(userPicturesManagement);
        return gestionClientBean;
    }

    /**
     * Sets the authentication service.
     *
     * @param authenticationService
     *            the new authentication service
     */
    public void setAuthenticationService(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Sets the jms producer.
     *
     * @param jmsProducer
     *            the new jms producer
     */
    public void setJmsProducer(final ServerBridgeProducer jmsProducer) {
        this.serverBridgeProducer = jmsProducer;
    }

    /**
     * Sets the user connections management.
     *
     * @param userConnectionsManagement
     *            the new user connections management
     */
    public void setUserConnectionsManagement(final UsersConnectionsManagement userConnectionsManagement) {
        usersConnectionsManagement = userConnectionsManagement;
    }

    /**
     * <p>Setter for the field <code>userPictureService</code>.</p>
     *
     * @param userPictureService
     *            the userPictureService to set
     */
    public void setUserPictureService(final UserPictureService userPictureService) {
        this.userPictureService = userPictureService;
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

	/**
	 * <p>Setter for the field <code>userPicturesManagement</code>.</p>
	 *
	 * @param userPicturesManagement a {@link com.gc.irc.server.core.user.management.UserPicturesManagement} object.
	 */
	public void setUserPicturesManagement(UserPicturesManagement userPicturesManagement) {
		this.userPicturesManagement = userPicturesManagement;
	}

}
