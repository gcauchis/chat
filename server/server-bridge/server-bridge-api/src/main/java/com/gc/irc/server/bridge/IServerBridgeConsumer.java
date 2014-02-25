package com.gc.irc.server.bridge;

import com.gc.irc.common.ILoggable;
import com.gc.irc.common.protocol.Message;

/**
 * The Interface IServerBridgeConsumer.
 */
public interface IServerBridgeConsumer extends ILoggable {

    /**
     * Close.
     */
    void close() throws ServerBridgeException;

    /**
     * Receive.
     * 
     * @return the IRC message
     */
    Message receive() throws ServerBridgeException;

}
