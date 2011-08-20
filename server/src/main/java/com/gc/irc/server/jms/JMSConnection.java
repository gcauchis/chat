package com.gc.irc.server.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

/**
 * Initialize JMS.
 * @author gcauchis
 *
 */
public class JMSConnection{
	
	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(JMSConnection.class);
	
	/** The connection. */
	private static Connection connection = null;
	
	/** The context. */
	private static Context context = null;
	
	/** The session. */
	private static Session session = null;
	
	/** The queue. */
	private static Queue queue = null;
	
	static{
		try {
			context = new InitialContext();
		} catch (NamingException e) {
			logger.fatal("Fail to create Context JMS.");
			e.printStackTrace();
			System.exit(-1);
		}
		
		
		// Create a ConnectionFactory
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

		// Create a Connection
		try {
			connection = connectionFactory.createConnection();
			connection.start();
		} catch (JMSException e) {
			logger.fatal("Fail to create a Connection JMS.");
			e.printStackTrace();
			System.exit(-1);
		}
		
		
		
		// Create a Session
		try {
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			logger.fatal("Failt to create a Session JMS");
			e.printStackTrace();
			System.exit(-1);
		}

		// Create the Queue
		try {
			queue = session.createQueue("test.prog.queue");
		} catch (JMSException e) {
			logger.fatal("Fail to create the Queue JMS");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public static Connection getConnection(){
		return connection;
	}
	
	/**
	 * Gets the context.
	 *
	 * @return the context
	 */
	public static Context getContext(){
		return context;
	}
	
	/**
	 * Gets the session.
	 *
	 * @return the session
	 */
	public static Session getSession(){
		// Create a Session
		Session session = null;
		try {
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			logger.fatal("Failt to create a Session JMS");
			e.printStackTrace();
			System.exit(-1);
		}
		return session;
	}
	
	/**
	 * Gets the queue.
	 *
	 * @return the queue
	 */
	public static Queue getQueue(){
		return queue;
	}
}
