package com.gc.irc.server.handler.message;

import com.gc.irc.common.Loggable;
import com.gc.irc.common.protocol.Message;

/**
 * The Interface IServerMessageHandler.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface ServerMessageHandler extends Loggable {

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
