package com.gc.irc.common.message;

import com.gc.irc.common.Loggable;
import com.gc.irc.common.protocol.Message;

/**
 * The Interface IMessageHandler.
 *
 * @version 0.0.4
 * @author x472511
 */
public interface MessageHandler extends Loggable {

    /**
     * Handle.
     *
     * @param message
     *            the message
     */
    void handle(Message message);
}
