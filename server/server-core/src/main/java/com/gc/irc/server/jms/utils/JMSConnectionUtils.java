package com.gc.irc.server.jms.utils;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;

import com.gc.irc.common.utils.LoggerUtils;
import com.gc.irc.server.conf.ServerConf;

/**
 * Initialize JMS.
 * 
 * @author gcauchis
 * 
 */
public final class JMSConnectionUtils {

    /** The connection. */
    private static Connection connection;

    /** The context. */
    private static Context context;

    /** The Constant logger. */
    private static final Logger LOGGER = LoggerUtils.getLogger(JMSConnectionUtils.class);

    /** The queue. */
    private static Queue queue;

    /** The session. */
    private static Session session;

    /**
     * Gets the connection.
     * 
     * @return the connection
     */
    public static Connection getConnection() {
        if (connection == null) {
            // Create a ConnectionFactory
            final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ServerConf
                    .getProperty(ServerConf.JMS_SERVER_URL, "tcp://localhost:61616"));

            // Create a Connection
            try {
                connection = connectionFactory.createConnection();
                connection.start();
            } catch (final JMSException e) {
                LOGGER.error("Fail to create a Connection JMS.", e);
                connection = null;
            }
        }
        return connection;
    }

    /**
     * Gets the context.
     * 
     * @return the context
     */
    // TODO See if used
    public static Context getContext() {
        if (context == null) {
            try {
                context = new InitialContext();
            } catch (final NamingException e) {
                LOGGER.error("Fail to create Context JMS.", e);
            }
        }
        return context;
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
    public static Session getSession() {
        if (session == null) {
            // Create a Session
            try {
                session = getConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
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
