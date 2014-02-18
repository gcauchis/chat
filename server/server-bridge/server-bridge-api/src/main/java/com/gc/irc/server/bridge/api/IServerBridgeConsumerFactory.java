package com.gc.irc.server.bridge.api;

import com.gc.irc.common.ILoggable;

/**
 * The Interface IServerBridgeConsumerFactory.
 */
public interface IServerBridgeConsumerFactory extends ILoggable {

    /**
     * Gets the instance.
     * 
     * @return the instance
     */
    IServerBridgeConsumer getInstance() throws ServerBridgeException;

}
