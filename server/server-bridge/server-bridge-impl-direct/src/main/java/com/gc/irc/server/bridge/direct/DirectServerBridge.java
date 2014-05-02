package com.gc.irc.server.bridge.direct;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.bridge.IServerBridgeConsumer;
import com.gc.irc.server.bridge.IServerBridgeConsumerFactory;
import com.gc.irc.server.bridge.IServerBridgeProducer;
import com.gc.irc.server.bridge.ServerBridgeException;

/**
 * The Class DirectServerBridge.
 *
 * @author gcauchis
 * @version 0.0.4
 */
@Component
@Scope("singleton")
public class DirectServerBridge extends AbstractLoggable implements IServerBridgeProducer, IServerBridgeConsumer, IServerBridgeConsumerFactory {

    /** The messages queue. */
    private final BlockingQueue<Message> messagesQueue = new LinkedBlockingQueue<Message>();

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.bridge.api.IServerBridgeProducer#close()
     */
    /** {@inheritDoc} */
    @Override
    public void close() throws ServerBridgeException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.bridge.api.IServerBridgeConsumerFactory#getInstance()
     */
    /** {@inheritDoc} */
    @Override
    public IServerBridgeConsumer getInstance() throws ServerBridgeException {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.bridge.api.IServerBridgeProducer#post(com.gc.irc.common .protocol.IRCMessage)
     */
    /** {@inheritDoc} */
    @Override
    public void post(final Message message) throws ServerBridgeException {
        getLog().debug("post message in bridge: {}", message);
        try {
            messagesQueue.put(message);
        } catch (final InterruptedException e) {
            throw new ServerBridgeException("Fail to put message in queue", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.bridge.api.IServerBridgeConsumer#receive()
     */
    /** {@inheritDoc} */
    @Override
    public Message receive() throws ServerBridgeException {
        Message result = null;
        try {
            result = messagesQueue.take();
        } catch (final InterruptedException e) {
            throw new ServerBridgeException("Fail to take message from queue", e);
        }
        return result;
    }

}
