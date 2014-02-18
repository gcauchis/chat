package com.gc.irc.server.bridge.jms.impl;

import org.apache.commons.pool.PoolableObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractObjectPool;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.bridge.api.IServerBridgeProducer;
import com.gc.irc.server.bridge.api.ServerBridgeException;

/**
 * The Class IRCJMSPoolProducer.
 */
@Component
@Scope("singleton")
public final class JMSPoolProducer extends AbstractObjectPool<IServerBridgeProducer> implements IServerBridgeProducer {

    /** The broker url. */
    @Value("${jms.server.url}")
    private String brokerUrl;

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.bridge.api.IServerBridgeProducer#close()
     */
    @Override
    public void close() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.common.abs.AbstractObjectPool#getPoolableObjectFactory()
     */
    @Override
    protected PoolableObjectFactory<IServerBridgeProducer> getPoolableObjectFactory() {
        return new PoolableJMSProducerFactory(brokerUrl);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.jms.IJMSProducer#postInJMS(com.gc.irc.common.protocol
     * .IRCMessage)
     */
    @Override
    public void post(final Message objectMessage) throws ServerBridgeException {
        final IServerBridgeProducer messageProducer = getPooledObject();

        if (messageProducer != null) {
            try {
                messageProducer.post(objectMessage);
            } finally {
                freePooledObject(messageProducer);
            }
        }
    }

    /**
     * @param brokerUrl
     *            the brokerUrl to set
     */
    public void setBrokerUrl(final String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.common.abs.AbstractObjectPool#setMaxPoolSize(int)
     */
    @Override
    @Value("${jms.pool.size.max}")
    public void setMaxPoolSize(final int maxPoolSize) {
        super.setMaxPoolSize(maxPoolSize);
    }
}
