package com.gc.irc.server.bridge.jms.utils;

import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import javolution.util.FastMap;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;

import com.gc.irc.common.utils.LoggerUtils;

/**
 * Initialize JMS.
 * 
 * @author gcauchis
 * 
 */
public final class JMSConnectionUtils {

    /** The connection. */
    private static Map<String, Connection> connections = new FastMap<String, Connection>();

    /** The Constant logger. */
    private static final Logger LOGGER = LoggerUtils.getLogger(JMSConnectionUtils.class);

    /** The queue. */
    private static Queue queue;

    /** The session. */
    private static Session session;

    /**
     * Creates the consumer.
     * 
     * @return the message consumer
     * @throws JMSException
     *             the jMS exception
     */
    public static MessageConsumer createConsumer(final String brokerUrl) throws JMSException {
        return getSession(brokerUrl).createConsumer(getQueue());
    }

    /**
     * Gets the connection.
     * 
     * @return the connection
     */
    public static Connection getConnection(final String brokerUrl) {
        Connection connection = connections.get(brokerUrl);
        if (connection == null) {
            // Create a ConnectionFactory
            final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);

            // Create a Connection
            try {
                connection = connectionFactory.createConnection();
                connection.start();
                connections.put(brokerUrl, connection);
            } catch (final IllegalStateException e) {
                LOGGER.error("JMS in bad State", e);
                System.exit(-1);
            } catch (final JMSException e) {
                LOGGER.error("Fail to create a Connection JMS.", e);
                connection = null;
            }
        }
        return connection;
    }

    /**
     * Gets the queue.
     * 
     * @return the queue
     */
    public static Queue getQueue() {
        if (queue == null) {
            // Create the Queue
            try {
                queue = session.createQueue("irc.queue");
            } catch (final JMSException e) {
                LOGGER.error("Fail to create the Queue JMS", e);
                System.exit(-1);
            }
        }
        return queue;
    }

    /**
     * Gets the session.
     * 
     * @return the session
     */
    public static Session getSession(final String brokerUrl) {
        if (session == null) {
            // Create a Session
            try {
                session = getConnection(brokerUrl).createSession(false, Session.AUTO_ACKNOWLEDGE);
            } catch (final JMSException e) {
                LOGGER.error("Failt to create a Session JMS", e);
            }
        }
        return session;
    }

    /**
     * Instantiates a new jMS connection.
     */
    private JMSConnectionUtils() {
        super();
    }
}
