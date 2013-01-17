package com.gc.irc.server.jms.api;

import com.gc.irc.common.protocol.IRCMessage;

public interface IJMSProducer {

    /**
     * Send a message in the JMS Queue.
     * 
     * @param objectMessage
     *            the object message
     */
    void postInJMS(final IRCMessage objectMessage);

}