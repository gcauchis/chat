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
	 * An identifier for the connection.
	 *
	 * @return an identifier for the connection.
	 */
	String getId();
	
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
     * {@inheritDoc}
     *
     * Send message to client.
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
