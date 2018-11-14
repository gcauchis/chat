package com.gc.irc.server.bridge;

import com.gc.irc.common.Loggable;

/**
 * The Interface IServerBridgeConsumerFactory.
 *
 * @version 0.0.4
 * @author x472511
 */
public interface ServerBridgeConsumerFactory extends Loggable {

    /**
     * Gets the instance.
     *
     * @return the instance
     * @throws com.gc.irc.server.bridge.ServerBridgeException if any.
     */
    ServerBridgeConsumer getInstance() throws ServerBridgeException;

}
