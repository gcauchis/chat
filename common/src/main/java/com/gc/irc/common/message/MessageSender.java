package com.gc.irc.common.message;

import com.gc.irc.common.Loggable;
import com.gc.irc.common.protocol.Message;

/**
 * The Interface IIRCMessageSender.
 *
 * @version 0.0.4
 * @author x472511
 */
public interface MessageSender extends Loggable {

    /**
     * Send.
     *
     * @param message
     *            the message
     */
    void send(Message message);
}
