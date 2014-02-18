package com.gc.irc.server.handler.message;

import com.gc.irc.common.ILoggable;
import com.gc.irc.common.protocol.Message;

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
    void handle(Message message);

    /**
     * Checks if is handled.
     * 
     * @param message
     *            the message
     * @return true, if is handled
     */
    boolean isHandled(Message message);

}
