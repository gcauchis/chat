package com.gc.irc.server.jms;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.server.conf.ServerConf;

/**
 * The Class IRCJMSPoolProducer.
 */
public class IRCJMSPoolProducer extends AbstractLoggable {

    /** The list pool producer jms. */
    private Map<Integer, IRCJMSProducer> listPoolProducerJMS = Collections.synchronizedMap(new HashMap<Integer, IRCJMSProducer>());

    /** The pool size. */
    private int poolSize = Integer.parseInt(ServerConf.getConfProperty(ServerConf.JMS_POOL_SIZE, "10"), 10);

    /** The current id. */
    private Integer currentId = 0;

    /** The instance. */
    private static IRCJMSPoolProducer instance = null;

    /**
     * Builder generate the Producer pool.
     */
    private IRCJMSPoolProducer() {
        getLog().info("Create pool JMS producer");

        for (int i = 0; i < poolSize; i++) {
            listPoolProducerJMS.put(i, new IRCJMSProducer());
        }
    }

    /**
     * Get an instance of the pool.
     * 
     * @return An unique instance of the pool.
     */
    public static IRCJMSPoolProducer getInstance() {
        if (instance == null) {
            instance = new IRCJMSPoolProducer();
        }
        return instance;
    }

    /**
     * Post the message in JMS using one of the used producer.
     * 
     * @param objectMessage
     *            Message to send.
     */
    public void postMessageObjectInJMS(final IRCMessage objectMessage) {
        final IRCJMSProducer messageProducer = getAProducer();

        synchronized (messageProducer) {
            messageProducer.postMessageObjectInJMS(objectMessage);
        }
    }

    /**
     * Get a producer in the pool.
     * 
     * @return An instance of a producer.
     */
    private IRCJMSProducer getAProducer() {
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
}
