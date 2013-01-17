package com.gc.irc.server.jms.impl;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.server.jms.api.IJMSProducer;

/**
 * The Class IRCJMSPoolProducer.
 */
@Component("jmsProducer")
@Scope("singleton")
public final class JMSPoolProducer extends AbstractLoggable implements IJMSProducer {

    /** The pool size. */
    @Value("${jms.pool.size.max}")
    private int maxPoolSize = 10;

    /** The pool. */
    private final GenericObjectPool pool;

    /**
     * Builder generate the Producer pool.
     */
    public JMSPoolProducer() {
        pool = new GenericObjectPool(new BasePoolableObjectFactory() {

            @Override
            public Object makeObject() throws Exception {
                return new JMSProducer();
            }
        });
    }

    /**
     * Gets the max pool size.
     * 
     * @return the max pool size
     */
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.jms.IJMSProducer#postInJMS(com.gc.irc.common.protocol.IRCMessage)
     */
    public void postInJMS(final IRCMessage objectMessage) {
        IJMSProducer messageProducer = null;
        try {
            messageProducer = (IJMSProducer) pool.borrowObject();
        } catch (Exception e) {
            getLog().error("Fail to build JMSProducer", e);
        }

        if (messageProducer != null) {
            try {
                messageProducer.postInJMS(objectMessage);
            } finally {
                try {
                    pool.returnObject(messageProducer);
                } catch (Exception e) {
                    getLog().error("Fail to return JMSProducer", e);
                }
            }
        }

    }

    /**
     * Sets the max pool size.
     * 
     * @param maxPoolSize
     *            the new max pool size
     */
    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        pool.setMaxActive(maxPoolSize);
    }
}
