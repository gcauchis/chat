package com.gc.irc.server.test.handler;

import com.gc.irc.common.message.MessageHandler;
import com.gc.irc.common.protocol.Message;

/**
 * <p>IMessageHandlerTester interface.</p>
 *
 * @version 0.0.4
 * @since 0.0.4
 */
public interface MessageHandlerTester extends MessageHandler {

    /**
     * Gets the last received message.
     *
     * @return the last received message
     */
    Message getLastReceivedMessage();

    /**
     * Checks if is message recieved.
     *
     * @return a boolean.
     */
    boolean isMessageRecieved();

    /**
     * Reset.
     */
    void reset();

}
