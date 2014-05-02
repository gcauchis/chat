package com.gc.irc.server.test.handler;

import com.gc.irc.common.message.IMessageHandler;
import com.gc.irc.common.protocol.Message;

/**
 * <p>IMessageHandlerTester interface.</p>
 *
 * @author gcauchis
 * @version 0.0.4
 * @since 0.0.4
 */
public interface IMessageHandlerTester extends IMessageHandler {

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
