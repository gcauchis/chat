package com.gc.irc.server.client.connector;

import com.gc.irc.common.entity.User;
import com.gc.irc.common.message.MessageSender;
import com.gc.irc.common.protocol.Message;

/**
 * <p>ClientConnection interface.</p>
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface ClientConnection extends Runnable, MessageSender {

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
    @Override
    void send(final Message message);
    
    /**
     * Wait and Receive a message send by the client.
     * 
     * @return Message received.
     */
    Message receiveMessage();
    
    /**
     * Post message to server core.
     *
     * @param message the message
     */
    void post(final Message message);

}
