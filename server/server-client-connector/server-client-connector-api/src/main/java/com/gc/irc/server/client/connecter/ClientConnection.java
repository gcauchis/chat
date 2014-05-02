package com.gc.irc.server.client.connecter;

import com.gc.irc.common.entity.User;
import com.gc.irc.common.protocol.Message;

/**
 * <p>ClientConnection interface.</p>
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface ClientConnection extends Runnable {

    /**
     * Finalize Thread.
     *
     * Close all Connection.
     */
    void disconnectUser();

    /**
     * Get the user connected to this Thread.
     *
     * @return User connected to this Thread.
     */
    User getUser();

    /**
     * Send message to client.
     *
     * @param message the message
     */
    void send(final Message message);

}
