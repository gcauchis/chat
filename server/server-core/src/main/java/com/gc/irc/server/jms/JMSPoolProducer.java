package com.gc.irc.server.jms;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.server.conf.ServerConf;

/**
 * The Class IRCJMSPoolProducer.
 */
public final class JMSPoolProducer extends AbstractLoggable {

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
    private final Map<Integer, JMSProducer> listPoolProducerJMS = new ConcurrentHashMap<Integer, JMSProducer>();

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
    private JMSProducer getAProducer() {
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

    /**
     * Post the message in JMS using one of the used producer.
     * 
     * @param objectMessage
     *            Message to send.
     */
    public void postMessageObjectInJMS(final IRCMessage objectMessage) {
        final JMSProducer messageProducer = getAProducer();

        synchronized (messageProducer) {
            messageProducer.postMessageObjectInJMS(objectMessage);
        }
    }
}
