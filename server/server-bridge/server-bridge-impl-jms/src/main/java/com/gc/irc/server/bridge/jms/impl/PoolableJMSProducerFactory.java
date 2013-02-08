package com.gc.irc.server.bridge.jms.impl;

import org.apache.commons.pool.BasePoolableObjectFactory;

import com.gc.irc.server.bridge.api.IServerBridgeProducer;

/**
 * A factory for creating PoolableJMSProducer objects.
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
    @Override
    public IServerBridgeProducer makeObject() throws Exception {
        return new JMSProducer(brokerUrl);
    }

}
