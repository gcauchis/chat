package com.gc.irc.server.jms.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    /** The current id. */
    private Integer currentId = 0;

    /** The list pool producer jms. */
    private final Map<Integer, IJMSProducer> listPoolProducerJMS = new ConcurrentHashMap<Integer, IJMSProducer>();

    /** The pool size. */
    @Value("${jmsPoolSize}")
    private int poolSize = 10;

    /**
     * Builder generate the Producer pool.
     */
    public JMSPoolProducer() {

    }

    /**
     * Check and correct pool size.
     */
    private void checkAndCorrectPoolSize() {
        while (listPoolProducerJMS.size() < poolSize) {
            getLog().info("add a JMS producer to the pool");
            listPoolProducerJMS.put(listPoolProducerJMS.size(), new JMSProducer());
        }
    }

    /**
     * Get a producer in the pool.
     * 
     * @return An instance of a producer.
     */
    private IJMSProducer getAProducer() {
        checkAndCorrectPoolSize();
        IJMSProducer producer = null;
        synchronized (currentId) {
            currentId++;
            if (currentId == poolSize) {
                currentId = 0;
            }
            producer = listPoolProducerJMS.get(currentId);
        }
        return producer;
    }

    /**
     * Gets the pool size.
     * 
     * @return the pool size
     */
    public int getPoolSize() {
        return poolSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.jms.IJMSProducer#postInJMS(com.gc.irc.common.protocol.IRCMessage)
     */
    public void postInJMS(final IRCMessage objectMessage) {
        final IJMSProducer messageProducer = getAProducer();

        synchronized (messageProducer) {
            messageProducer.postInJMS(objectMessage);
        }
    }

    /**
     * Sets the pool size.
     * 
     * @param poolSize
     *            the new pool size
     */
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}
