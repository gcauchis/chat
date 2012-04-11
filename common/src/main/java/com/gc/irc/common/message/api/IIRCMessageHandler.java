package com.gc.irc.common.message.api;

import com.gc.irc.common.api.ILoggable;
import com.gc.irc.common.protocol.IRCMessage;

/**
 * The Interface IMessageHandler.
 */
public interface IIRCMessageHandler extends ILoggable {

    /**
     * Handle.
     * 
     * @param message
     *            the message
     */
    void handle(IRCMessage message);
}
