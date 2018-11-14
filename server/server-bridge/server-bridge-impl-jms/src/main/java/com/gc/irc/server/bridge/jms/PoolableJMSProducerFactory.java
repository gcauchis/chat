package com.gc.irc.server.bridge.jms;

import org.apache.commons.pool.BasePoolableObjectFactory;

import com.gc.irc.server.bridge.ServerBridgeProducer;

/**
 * A factory for creating PoolableJMSProducer objects.
 *
 * @version 0.0.4
 * @author x472511
 */
public class PoolableJMSProducerFactory extends BasePoolableObjectFactory<ServerBridgeProducer> {

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
    public ServerBridgeProducer makeObject() throws Exception {
        return new JMSProducer(brokerUrl);
    }

}
