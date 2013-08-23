package com.gc.irc.server.core.user.management.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.core.user.management.api.IUserManagement;
import com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement;
import com.gc.irc.server.core.user.management.api.UserManagementAware;
import com.gc.irc.server.service.api.IAuthenticationService;
import com.gc.irc.server.thread.api.IGestionClientBean;

/**
 * The Class UserManagement.
 */
@Component("usersConnectionsManagement")
@Scope("singleton")
public class UsersConnectionsManagement extends AbstractLoggable implements IUsersConnectionsManagement, UserManagementAware {

    /** The authentication service. */
    @Autowired
    private IAuthenticationService authenticationService;

    /** The client connecter. */
    private final List<IGestionClientBean> clientConnected = new LinkedList<IGestionClientBean>();

    /** The list thread client by id user. */
    private final Map<Long, IGestionClientBean> listThreadClientByIdUser = new ConcurrentHashMap<Long, IGestionClientBean>();

    /** The user management */
    private IUserManagement userManagement;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.core.user.management.api.IUserConnectionsManagement
     * #close()
     */
    @Override
    public void close() {
        for (final IGestionClientBean thread : clientConnected) {
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
    @Override
    public void disconnectClient(final IGestionClientBean client) {
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
    @Override
    public List<IGestionClientBean> getClientConnected() {
        return clientConnected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.core.user.management.api.IUserConnectionsManagement
     * #getGestionClientBeanOfUser(int)
     */
    @Override
    public IGestionClientBean getGestionClientBeanOfUser(final long id) {
        return listThreadClientByIdUser.get(id);
    }


    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.core.user.management.api.IUserConnectionsManagement
     * #newClientConnected(com.gc.irc.server.thread.api.IGestionClientBean)
     */
    @Override
    public void newClientConnected(final IGestionClientBean client) {
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
    @Override
    public void sendMessageToAllUsers(final Message message) {
        final List<IGestionClientBean> clientConnected = getClientConnected();

        if (authenticationService.getUser(message.getFromId()) != null) {
            if (getLog().isDebugEnabled()) {
                getLog().debug(" Send a message to all connected client from " + authenticationService.getUser(message.getFromId()).getNickname());
            }
            synchronized (clientConnected) {
                for (final IGestionClientBean client : clientConnected) {
                    if (message.getFromId() != client.getUser().getId()) {
                        synchronized (client) {
                            synchronized (client.getUser()) {
                                client.sendMessageObjetInSocket(message);
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
    @Override
    public void sendTo(final Message message, final long toId) {
        final IGestionClientBean clientCible = getGestionClientBeanOfUser(toId);
        if (clientCible != null) {
            clientCible.sendMessageObjetInSocket(message);
        }
    }

    /**
     * @param authenticationService
     *            the authenticationService to set
     */
    public void setAuthenticationService(final IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

	@Override
	@Autowired
	public void setUserManagement(IUserManagement userManagement) {
		this.userManagement = userManagement;
	}

}
