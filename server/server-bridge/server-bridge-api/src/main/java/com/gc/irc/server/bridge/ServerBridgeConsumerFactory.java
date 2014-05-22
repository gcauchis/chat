package com.gc.irc.server.bridge;

import com.gc.irc.common.ILoggable;

/**
 * The Interface IServerBridgeConsumerFactory.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public interface ServerBridgeConsumerFactory extends ILoggable {

    /**
     * Gets the instance.
     *
     * @return the instance
     * @throws com.gc.irc.server.bridge.ServerBridgeException if any.
     */
    ServerBridgeConsumer getInstance() throws ServerBridgeException;

}
