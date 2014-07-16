package com.gc.irc.server.bridge;

import com.gc.irc.common.Loggable;
import com.gc.irc.common.protocol.Message;

/**
 * The Interface IServerBridgeConsumer.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface ServerBridgeConsumer extends Loggable {

    /**
     * Close.
     *
     * @throws com.gc.irc.server.bridge.ServerBridgeException if any.
     */
    void close() throws ServerBridgeException;

    /**
     * Receive.
     *
     * @return the IRC message
     * @throws com.gc.irc.server.bridge.ServerBridgeException if any.
     */
    Message receive() throws ServerBridgeException;

}
