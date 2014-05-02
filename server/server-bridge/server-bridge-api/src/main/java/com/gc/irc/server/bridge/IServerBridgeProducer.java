package com.gc.irc.server.bridge;

import com.gc.irc.common.ILoggable;
import com.gc.irc.common.protocol.Message;

/**
 * The Interface IServerBridgeProducer.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface IServerBridgeProducer extends ILoggable {

    /**
     * Close.
     *
     * @throws com.gc.irc.server.bridge.ServerBridgeException if any.
     */
    void close() throws ServerBridgeException;

    /**
     * Post.
     *
     * @param objectMessage
     *            the object message
     * @throws com.gc.irc.server.bridge.ServerBridgeException if any.
     */
    void post(final Message objectMessage) throws ServerBridgeException;

}
