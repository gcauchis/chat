package com.gc.irc.server.test.handler;

import com.gc.irc.common.message.api.IMessageHandler;
import com.gc.irc.common.protocol.Message;

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
     * @return true, if is message recieved
     */
    boolean isMessageRecieved();

    /**
     * Reset.
     */
    void reset();

}