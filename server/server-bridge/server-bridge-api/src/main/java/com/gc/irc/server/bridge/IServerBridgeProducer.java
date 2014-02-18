package com.gc.irc.server.bridge;

import com.gc.irc.common.ILoggable;
import com.gc.irc.common.protocol.Message;

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
    void post(final Message objectMessage) throws ServerBridgeException;

}