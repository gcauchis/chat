package com.gc.irc.server.bridge.jms;

import org.apache.commons.pool.PoolableObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractObjectPool;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.server.bridge.ServerBridgeException;
import com.gc.irc.server.bridge.ServerBridgeProducer;

/**
 * The Class IRCJMSPoolProducer.
 *
 * @version 0.0.4
 * @author x472511
 */
@Component
@Scope("singleton")
public final class JMSPoolProducer extends AbstractObjectPool<ServerBridgeProducer> implements ServerBridgeProducer {

    /** The broker url. */
    @Value("${jms.server.url}")
    private String brokerUrl;

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.bridge.api.IServerBridgeProducer#close()
     */
    /** {@inheritDoc} */
    @Override
    public void close() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.common.abs.AbstractObjectPool#getPoolableObjectFactory()
     */
    /** {@inheritDoc} */
    @Override
    protected PoolableObjectFactory<ServerBridgeProducer> getPoolableObjectFactory() {
        return new PoolableJMSProducerFactory(brokerUrl);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.jms.IJMSProducer#postInJMS(com.gc.irc.common.protocol
     * .IRCMessage)
     */
    /** {@inheritDoc} */
    @Override
    public void post(final Message objectMessage) throws ServerBridgeException {
        final ServerBridgeProducer messageProducer = getPooledObject();

        if (messageProducer != null) {
            try {
                messageProducer.post(objectMessage);
            } finally {
                freePooledObject(messageProducer);
            }
        }
    }

    /**
     * <p>Setter for the field <code>brokerUrl</code>.</p>
     *
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
    /** {@inheritDoc} */
    @Override
    @Value("${jms.pool.size.max}")
    public void setMaxPoolSize(final int maxPoolSize) {
        super.setMaxPoolSize(maxPoolSize);
    }
}
