package com.gc.irc.server.bridge.jms;

import org.apache.commons.pool.BasePoolableObjectFactory;

import com.gc.irc.server.bridge.IServerBridgeProducer;

/**
 * A factory for creating PoolableJMSProducer objects.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public class PoolableJMSProducerFactory extends BasePoolableObjectFactory<IServerBridgeProducer> {

    /** The broker url. */
    private String brokerUrl;

    /**
     * The Constructor.
     *
     * @param brokerUrl
     *            the broker url
     */
    public PoolableJMSProducerFactory(final String brokerUrl) {
        super();
        this.brokerUrl = brokerUrl;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.pool.BasePoolableObjectFactory#makeObject()
     */
    /** {@inheritDoc} */
    @Override
    public IServerBridgeProducer makeObject() throws Exception {
        return new JMSProducer(brokerUrl);
    }

}
