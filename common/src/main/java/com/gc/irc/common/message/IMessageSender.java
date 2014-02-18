package com.gc.irc.common.message;

import com.gc.irc.common.ILoggable;
import com.gc.irc.common.protocol.Message;

/**
 * The Interface IIRCMessageSender.
 */
public interface IMessageSender extends ILoggable {

    /**
     * Send.
     * 
     * @param message
     *            the message
     */
    void send(Message message);
}
