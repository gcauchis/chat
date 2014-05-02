package com.gc.irc.server.bridge;

import com.gc.irc.common.ILoggable;
import com.gc.irc.common.protocol.Message;

/**
 * The Interface IServerBridgeConsumer.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface IServerBridgeConsumer extends ILoggable {

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
