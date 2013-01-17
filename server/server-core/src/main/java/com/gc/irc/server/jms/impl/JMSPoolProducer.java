package com.gc.irc.server.jms.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.server.conf.ServerConf;
import com.gc.irc.server.jms.api.IJMSProducer;

/**
 * The Class IRCJMSPoolProducer.
 */
public final class JMSPoolProducer extends AbstractLoggable implements IJMSProducer {

    /** The instance. */
    private static JMSPoolProducer instance = null;

    /**
     * Get an instance of the pool.
     * 
     * @return An unique instance of the pool.
     */
    public static JMSPoolProducer getInstance() {
        if (instance == null) {
            instance = new JMSPoolProducer();
        }
        return instance;
    }

    /** The current id. */
    private Integer currentId = 0;

    /** The list pool producer jms. */
    private final Map<Integer, IJMSProducer> listPoolProducerJMS = new ConcurrentHashMap<Integer, IJMSProducer>();

    /** The pool size. */
    private final int poolSize = Integer.parseInt(ServerConf.getConfProperty(ServerConf.JMS_POOL_SIZE, "10"), 10);

    /**
     * Builder generate the Producer pool.
     */
    private JMSPoolProducer() {
        getLog().info("Create pool JMS producer");

        for (int i = 0; i < poolSize; i++) {
            listPoolProducerJMS.put(i, new JMSProducer());
        }
    }

    /**
     * Get a producer in the pool.
     * 
     * @return An instance of a producer.
     */
    private IJMSProducer getAProducer() {
        int id = 0;
        synchronized (currentId) {
            currentId++;
            if (currentId == poolSize) {
                currentId = 0;
            }
            id = currentId;
        }
        return listPoolProducerJMS.get(id);
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
}
