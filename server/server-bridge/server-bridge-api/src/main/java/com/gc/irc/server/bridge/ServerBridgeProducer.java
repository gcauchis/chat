package com.gc.irc.server.bridge;

import com.gc.irc.common.Loggable;
import com.gc.irc.common.protocol.Message;

/**
 * The Interface IServerBridgeProducer.
 *
 * @version 0.0.4
 * @author x472511
 */
public interface ServerBridgeProducer extends Loggable {

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
