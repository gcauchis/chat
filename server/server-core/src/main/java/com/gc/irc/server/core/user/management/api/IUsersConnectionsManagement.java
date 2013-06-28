package com.gc.irc.server.core.user.management.api;

import java.util.List;

import com.gc.irc.common.api.ILoggable;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.thread.api.IGestionClientBean;

public interface IUsersConnectionsManagement extends ILoggable {

    /**
     * Close.
     */
    void close();

    /**
     * Delete the deconnected Client.
     * 
     * @param client
     *            Deconnected Client.
     */
    void disconnectClient(final IGestionClientBean client);

    /**
     * Get the users Connected list.
     * 
     * @return The list of all the connected users.
     */
    List<IRCUser> getAllUsers();

    /**
     * Get the Thread list of connected client.
     * 
     * @return Client's thread list.
     */
    List<IGestionClientBean> getClientConnected();

    /**
     * Get the thread of a selected user.
     * 
     * @param id
     *            User's Id.
     * @return The Designed User's Thread.
     */
    IGestionClientBean getGestionClientBeanOfUser(final int id);

    /**
     * Get the user demand if he is connected.
     * 
     * @param id
     *            User's Id.
     * @return The User selected or null if not find.
     */
    IRCUser getUser(final int id);

    /**
     * Add the login client to the Client's list.
     * 
     * @param client
     *            New Client
     */
    void newClientConnected(final IGestionClientBean client);

    /**
     * Send message to all users.
     * 
     * @param message
     *            the message
     */
    void sendMessageToAllUsers(final Message message);

    /**
     * Send to.
     * 
     * @param message
     *            the message
     * @param toId
     *            the to id
     */
    void sendTo(final Message message, final int toId);
}