package com.gc.irc.server.core.user.management;

import java.util.List;

import com.gc.irc.common.ILoggable;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.client.connecter.ClientConnection;

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
    void disconnectClient(final ClientConnection client);

    /**
     * Get the Thread list of connected client.
     * 
     * @return Client's thread list.
     */
    List<ClientConnection> getClientConnected();

    /**
     * Get the thread of a selected user.
     * 
     * @param id
     *            User's Id.
     * @return The Designed User's Thread.
     */
    ClientConnection getGestionClientBeanOfUser(final long id);

    /**
     * Add the login client to the Client's list.
     * 
     * @param client
     *            New Client
     */
    void newClientConnected(final ClientConnection client);

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