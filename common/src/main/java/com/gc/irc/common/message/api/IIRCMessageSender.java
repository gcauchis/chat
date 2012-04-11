package com.gc.irc.common.message.api;

import com.gc.irc.common.api.ILoggable;
import com.gc.irc.common.protocol.IRCMessage;

/**
 * The Interface IIRCMessageSender.
 */
public interface IIRCMessageSender extends ILoggable {

    /**
     * Send.
     * 
     * @param message
     *            the message
     */
    void send(IRCMessage message);
}
