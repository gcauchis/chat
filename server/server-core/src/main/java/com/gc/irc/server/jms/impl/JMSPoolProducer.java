package com.gc.irc.server.jms.impl;

import org.apache.commons.pool.PoolableObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.abs.AbstractObjectPool;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.server.jms.api.IJMSProducer;

/**
 * The Class IRCJMSPoolProducer.
 */
@Component("jmsProducer")
@Scope("singleton")
public final class JMSPoolProducer extends AbstractObjectPool<IJMSProducer> implements IJMSProducer {

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.common.abs.AbstractObjectPool#getPoolableObjectFactory()
     */
    @Override
    protected PoolableObjectFactory getPoolableObjectFactory() {
        return new PoolableJMSProducerFactory();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.jms.IJMSProducer#postInJMS(com.gc.irc.common.protocol.IRCMessage)
     */
    public void postInJMS(final IRCMessage objectMessage) {
        IJMSProducer messageProducer = getPollableObject();

        if (messageProducer != null) {
            try {
                messageProducer.postInJMS(objectMessage);
            } finally {
                freePoolableObject(messageProducer);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.common.abs.AbstractObjectPool#setMaxPoolSize(int)
     */
    @Override
    @Value("${jms.pool.size.max}")
    public void setMaxPoolSize(int maxPoolSize) {
        super.setMaxPoolSize(maxPoolSize);
    }
}
