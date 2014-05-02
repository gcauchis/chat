package com.gc.irc.server.handler.message;

import com.gc.irc.common.ILoggable;
import com.gc.irc.common.protocol.Message;

/**
 * The Interface IServerMessageHandler.
 *
 * @author gcauchis
 * @version 0.0.4
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
     * @return a boolean.
     */
    boolean isHandled(Message message);

}
