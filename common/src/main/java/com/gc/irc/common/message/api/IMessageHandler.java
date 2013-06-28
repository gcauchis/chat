package com.gc.irc.common.message.api;

import com.gc.irc.common.api.ILoggable;
import com.gc.irc.common.protocol.Message;

/**
 * The Interface IMessageHandler.
 */
public interface IMessageHandler extends ILoggable {

    /**
     * Handle.
     * 
     * @param message
     *            the message
     */
    void handle(Message message);
}
