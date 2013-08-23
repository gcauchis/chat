package com.gc.irc.server.core.user.management.api;

import java.util.List;

import com.gc.irc.common.api.ILoggable;
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
    IGestionClientBean getGestionClientBeanOfUser(final long id);

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
    void sendTo(final Message message, final long toId);
}