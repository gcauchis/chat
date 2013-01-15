package com.gc.irc.server.core;

import java.util.List;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.server.thread.api.IGestionClientBean;

public interface IUserManagement {

    /**
     * Delete the deconnected Client.
     * 
     * @param client
     *            Deconnected Client.
     */
    public abstract void disconnectClient(final IGestionClientBean client);

    /**
     * Get the users Connected list.
     * 
     * @return The list of all the connected users.
     */
    public abstract List<IRCUser> getAllUsers();

    /**
     * Get the Thread list of connected client.
     * 
     * @return Client's thread list.
     */
    public abstract List<IGestionClientBean> getClientConnected();

    /**
     * Get the thread of a selected user.
     * 
     * @param id
     *            User's Id.
     * @return The Designed User's Thread.
     */
    public abstract IGestionClientBean getGestionClientBeanOfUser(final int id);

    /**
     * Get the user demand if he is connected.
     * 
     * @param id
     *            User's Id.
     * @return The User selected or null if not find.
     */
    public abstract IRCUser getUser(final int id);

    /**
     * Add the login client to the Client's list.
     * 
     * @param client
     *            New Client
     */
    public abstract void newClientConnected(final IGestionClientBean client);

}