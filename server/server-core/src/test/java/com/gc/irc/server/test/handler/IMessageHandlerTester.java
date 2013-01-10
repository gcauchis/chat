package com.gc.irc.server.test.handler;

import com.gc.irc.common.message.api.IIRCMessageHandler;
import com.gc.irc.common.protocol.IRCMessage;

public interface IMessageHandlerTester extends IIRCMessageHandler {

    /**
     * Gets the last received message.
     * 
     * @return the last received message
     */
    IRCMessage getLastReceivedMessage();

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