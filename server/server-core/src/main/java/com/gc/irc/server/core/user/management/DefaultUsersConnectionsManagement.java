package com.gc.irc.server.core.user.management;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.client.connector.ClientConnection;
import com.gc.irc.server.service.AuthenticationService;

/**
 * The Class UserManagement.
 *
 * @author gcauchis
 * @version 0.0.4
 */
@Component("usersConnectionsManagement")
@Scope("singleton")
public class DefaultUsersConnectionsManagement extends AbstractLoggable implements UsersConnectionsManagement, UserManagementAware {

    /** The authentication service. */
    @Autowired
    private AuthenticationService authenticationService;

    /** The client connecter. */
    private final List<ClientConnection> clientConnected = new LinkedList<ClientConnection>();

    /** The list thread client by id user. */
    private final Map<Long, ClientConnection> listThreadClientByIdUser = new ConcurrentHashMap<Long, ClientConnection>();

    /** The user management */
    private UserManagement userManagement;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.core.user.management.api.IUserConnectionsManagement
     * #close()
     */
    /** {@inheritDoc} */
    @Override
    public void close() {
        for (final ClientConnection thread : clientConnected) {
            thread.disconnectUser();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.core.user.management.api.IUserConnectionsManagement
     * #disconnectClient(com.gc.irc.server.thread.api.IGestionClientBean)
     */
    /** {@inheritDoc} */
    @Override
    public void disconnectClient(final ClientConnection client) {
        getLog().debug("Delete the deconnected Client : " + client.getUser().getNickName());
        synchronized (clientConnected) {
            synchronized (listThreadClientByIdUser) {
                getLog().debug("Remove from list clientConnecter");
                clientConnected.remove(client);
                getLog().debug("Remove from listUserConnectedById");
                userManagement.disconnect(client.getUser().getId());
                getLog().debug("Remove from lisThreadClientByIdUser");
                listThreadClientByIdUser.remove(client.getUser().getId());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.core.user.management.api.IUserConnectionsManagement
     * #getClientConnected()
     */
    /** {@inheritDoc} */
    @Override
    public List<ClientConnection> getClientConnected() {
        return clientConnected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.core.user.management.api.IUserConnectionsManagement
     * #getGestionClientBeanOfUser(int)
     */
    /** {@inheritDoc} */
    @Override
    public ClientConnection getGestionClientBeanOfUser(final long id) {
        return listThreadClientByIdUser.get(id);
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.core.user.management.api.IUserConnectionsManagement
     * #newClientConnected(com.gc.irc.server.thread.api.IGestionClientBean)
     */
    /** {@inheritDoc} */
    @Override
    public void newClientConnected(final ClientConnection client) {
        getLog().debug("Add a new Connected Client : " + client.getUser().getNickName());
        synchronized (clientConnected) {
            synchronized (listThreadClientByIdUser) {
                getLog().debug("Add to clientConnecter");
                clientConnected.add(client);
                getLog().debug("Add to listThreadClientByIdUser");
                listThreadClientByIdUser.put(client.getUser().getId(), client);
                getLog().debug("Add to listUserById");
                userManagement.newUserConnected(client.getUser());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement
     * #sendMessageToAllUsers(com.gc.irc.common.protocol.IRCMessage)
     */
    /** {@inheritDoc} */
    @Override
    public void sendMessageToAllUsers(final Message message) {
        final List<ClientConnection> clientConnected = getClientConnected();

        if (authenticationService.getUser(message.getFromId()) != null) {
            if (getLog().isDebugEnabled()) {
                getLog().debug(" Send a message to all connected client from " + authenticationService.getUser(message.getFromId()).getNickname());
            }
            synchronized (clientConnected) {
                for (final ClientConnection client : clientConnected) {
                    if (message.getFromId() != client.getUser().getId()) {
                        synchronized (client) {
                            synchronized (client.getUser()) {
                                client.send(message);
                            }
                        }
                    }
                }
            }
        } else {
            getLog().warn(" Inexisting source ID");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement
     * #sendTo(com.gc.irc.common.protocol.IRCMessage, int)
     */
    /** {@inheritDoc} */
    @Override
    public void sendTo(final Message message, final long toId) {
        final ClientConnection clientCible = getGestionClientBeanOfUser(toId);
        if (clientCible != null) {
            clientCible.send(message);
        }
    }

    /**
     * <p>Setter for the field <code>authenticationService</code>.</p>
     *
     * @param authenticationService
     *            the authenticationService to set
     */
    public void setAuthenticationService(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

	/** {@inheritDoc} */
	@Override
	@Autowired
	public void setUserManagement(UserManagement userManagement) {
		this.userManagement = userManagement;
	}

}
