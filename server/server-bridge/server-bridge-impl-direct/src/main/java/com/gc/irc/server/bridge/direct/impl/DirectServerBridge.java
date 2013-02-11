package com.gc.irc.server.bridge.direct.impl;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.server.bridge.api.IServerBridgeConsumer;
import com.gc.irc.server.bridge.api.IServerBridgeConsumerFactory;
import com.gc.irc.server.bridge.api.IServerBridgeProducer;
import com.gc.irc.server.bridge.api.ServerBridgeException;

/**
 * The Class DirectServerBridge.
 */
@Component
@Scope("singleton")
public class DirectServerBridge extends AbstractLoggable implements IServerBridgeProducer, IServerBridgeConsumer, IServerBridgeConsumerFactory {

    /** The lock consumer. */
    private Semaphore lockConsumer = new Semaphore(0, true);

    /** The lock producer. */
    private Semaphore lockProducer = new Semaphore(1, true);

    /** The messages queue. */
    private Queue<IRCMessage> messagesQueue = new LinkedBlockingQueue<IRCMessage>();

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.bridge.api.IServerBridgeProducer#close()
     */
    @Override
    public void close() throws ServerBridgeException {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.bridge.api.IServerBridgeConsumerFactory#getInstance()
     */
    @Override
    public IServerBridgeConsumer getInstance() throws ServerBridgeException {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.bridge.api.IServerBridgeProducer#post(com.gc.irc.common
     * .protocol.IRCMessage)
     */
    @Override
    public void post(final IRCMessage message) throws ServerBridgeException {
        getLog().debug("post message in bridge: {}", message);
        try {
            lockProducer.acquire();
        } catch (final InterruptedException e) {
            throw new ServerBridgeException("Fail to acquire lock", e);
        }
        messagesQueue.add(message);
        lockConsumer.release();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.bridge.api.IServerBridgeConsumer#receive()
     */
    @Override
    public IRCMessage receive() throws ServerBridgeException {
        try {
            getLog().debug("Wait for message in");
            lockConsumer.acquire();
        } catch (final InterruptedException e) {
            throw new ServerBridgeException("Fail to acquire lock", e);
        }
        final IRCMessage result = messagesQueue.poll();
        getLog().debug("poped message: {}", result);
        lockProducer.release();
        return result;
    }

}
