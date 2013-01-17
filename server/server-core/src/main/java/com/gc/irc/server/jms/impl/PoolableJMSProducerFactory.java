package com.gc.irc.server.jms.impl;

import org.apache.commons.pool.BasePoolableObjectFactory;

/**
 * A factory for creating PoolableJMSProducer objects.
 */
public class PoolableJMSProducerFactory extends BasePoolableObjectFactory {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.pool.BasePoolableObjectFactory#makeObject()
     */
    @Override
    public Object makeObject() throws Exception {
        return new JMSProducer();
    }

}
