package com.gc.irc.server.client.connector.management;

import java.util.List;

import com.gc.irc.common.Loggable;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.client.connector.ClientConnection;

/**
 * <p>IUsersConnectionsManagement interface.</p>
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface UsersConnectionsManagement extends Loggable {

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
