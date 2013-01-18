package com.gc.irc.server.handler.message.api;

import com.gc.irc.common.api.ILoggable;
import com.gc.irc.common.protocol.IRCMessage;

/**
 * The Interface IServerMessageHandler.
 */
public interface IServerMessageHandler extends ILoggable {

    /**
     * Handle.
     * 
     * @param message
     *            the message
     */
    void handle(IRCMessage message);

    /**
     * Checks if is handled.
     * 
     * @param message
     *            the message
     * @return true, if is handled
     */
    boolean isHandled(IRCMessage message);

}
