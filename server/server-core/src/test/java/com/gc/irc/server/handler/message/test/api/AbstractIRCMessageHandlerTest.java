package com.gc.irc.server.handler.message.test.api;

import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.server.handler.message.api.IServerMessageHandler;

/**
 * The Class AbstractIRCMessageHandlerTest.
 * 
 * @param <MSGH>
 *            the generic type
 * @param <MSG>
 *            the generic type
 */

public abstract class AbstractIRCMessageHandlerTest<MSGH extends IServerMessageHandler, MSG extends IRCMessage> {

    /** The message handler. */
    private MSGH messageHandler;

    /**
     * Gets the message handler.
     * 
     * @return the message handler
     */
    public MSGH getMessageHandler() {
        return messageHandler;
    }

    /**
     * Sets the message handler.
     * 
     * @param messageHandler
     *            the new message handler
     */
    public void setMessageHandler(MSGH messageHandler) {
        this.messageHandler = messageHandler;
    }

}
