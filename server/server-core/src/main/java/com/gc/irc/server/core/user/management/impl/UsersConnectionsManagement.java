package com.gc.irc.server.core.user.management.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement;
import com.gc.irc.server.persistance.PersiteUsers;
import com.gc.irc.server.thread.api.IGestionClientBean;

/**
 * The Class UserManagement.
 */
@Component("usersConnectionsManagement")
@Scope("singleton")
public class UsersConnectionsManagement extends AbstractLoggable implements IUsersConnectionsManagement {

    /** The client connecter. */
    private final List<IGestionClientBean> clientConnected = new LinkedList<IGestionClientBean>();

    /** The list thread client by id user. */
    private final Map<Integer, IGestionClientBean> listThreadClientByIdUser = new ConcurrentHashMap<Integer, IGestionClientBean>();

    /** The list user by id. */
    private final Map<Integer, IRCUser> listUserById = new ConcurrentHashMap<Integer, IRCUser>();

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.core.user.management.api.IUserConnectionsManagement#close()
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
     * @see com.gc.irc.server.core.user.management.api.IUserConnectionsManagement#disconnectClient(com.gc.irc.server.thread.api.IGestionClientBean)
     */
    @Override
    public void disconnectClient(IGestionClientBean client) {
        getLog().debug("Delete the deconnected Client : " + client.getUser().getNickName());
        synchronized (clientConnected) {
            synchronized (listUserById) {
                synchronized (listThreadClientByIdUser) {
                    getLog().debug("Remove from list clientConnecter");
                    clientConnected.remove(client);
                    getLog().debug("Remove from listUserConnectedById");
                    listUserById.remove(client.getUser().getId());
                    getLog().debug("Remove from lisThreadClientByIdUser");
                    listThreadClientByIdUser.remove(client.getUser().getId());
                }
            }
        }

        /**
         * Persist the change.
         */
        PersiteUsers.persistListUser(getAllUsers());

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.core.user.management.api.IUserConnectionsManagement#getAllUsers()
     */
    @Override
    public List<IRCUser> getAllUsers() {
        List<IRCUser> list = null;
        synchronized (listUserById) {
            list = new ArrayList<IRCUser>(listUserById.values());
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.core.user.management.api.IUserConnectionsManagement#getClientConnected()
     */
    @Override
    public List<IGestionClientBean> getClientConnected() {
        return clientConnected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.core.user.management.api.IUserConnectionsManagement#getGestionClientBeanOfUser(int)
     */
    @Override
    public IGestionClientBean getGestionClientBeanOfUser(int id) {
        return listThreadClientByIdUser.get(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.core.user.management.api.IUserConnectionsManagement#getUser(int)
     */
    @Override
    public IRCUser getUser(int id) {
        return listUserById.get(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.core.user.management.api.IUserConnectionsManagement#newClientConnected(com.gc.irc.server.thread.api.IGestionClientBean)
     */
    @Override
    public void newClientConnected(IGestionClientBean client) {
        getLog().debug("Add a new Connected Client : " + client.getUser().getNickName());
        synchronized (clientConnected) {
            synchronized (listUserById) {
                synchronized (listThreadClientByIdUser) {
                    getLog().debug("Add to clientConnecter");
                    clientConnected.add(client);
                    getLog().debug("Add to listThreadClientByIdUser");
                    listThreadClientByIdUser.put(client.getUser().getId(), client);
                    getLog().debug("Add to listUserById");
                    listUserById.put(client.getUser().getId(), client.getUser());
                }
            }
        }

        /**
         * Persist the change.
         */
        PersiteUsers.persistListUser(getAllUsers());

    }

}