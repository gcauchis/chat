package com.gc.irc.server.bridge.api;

import com.gc.irc.common.api.ILoggable;
import com.gc.irc.common.protocol.IRCMessage;

/**
 * The Interface IServerBridgeProducer.
 */
public interface IServerBridgeProducer extends ILoggable {

    /**
     * Close.
     */
    void close() throws ServerBridgeException;

    /**
     * Post.
     * 
     * @param objectMessage
     *            the object message
     */
    void post(final IRCMessage objectMessage) throws ServerBridgeException;

}