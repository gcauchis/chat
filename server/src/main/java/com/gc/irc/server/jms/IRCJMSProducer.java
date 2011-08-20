package com.gc.irc.server.jms;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.log4j.Logger;

import com.gc.irc.common.protocol.IRCMessage;



/**
 * Contain a JMSProducer to send a message in the JMS Queue.
 * @author gcauchis
 *
 */
public class IRCJMSProducer {
	private static int nbThread = 0;
	protected static int getNbThread() {
		nbThread++;
		return nbThread;
	}
	private int id = getNbThread();
	private static final Logger logger = Logger.getLogger(IRCJMSProducer.class);
	private static Session session = JMSConnection.getSession();
	private MessageProducer messageProducer = null;
	
	public IRCJMSProducer() {
		try {
			logger.info(id+" Create the JMS producer");
			messageProducer = session.createProducer(JMSConnection.getQueue());
		} catch (JMSException e) {
			logger.fatal(id+" Fail to create the JMS Producer");
			e.printStackTrace();
			
		}
	}
	
	/**
	 * Send a message in the JMS Queue
	 * @param message Message to Send.
	 */
	public void postMessageObjectInJMS(IRCMessage objectMessage){
		logger.debug(id+" Send a message in JMS Queue.");
		ObjectMessage message = null;
		/**
		 * Create the Message
		 */
		try {
			logger.debug(id+" Create JMS Message");
			message = session.createObjectMessage();
		} catch (JMSException e) {
			logger.warn(id+" Fail to create the message.");
			e.printStackTrace();
		}
		
		/**
		 * Write the Message
		 */
		try {
			logger.debug(id+" Write the Message");
			message.setObject(objectMessage);
		} catch (JMSException e) {
			logger.warn(id+" Fail to Write the message");
			e.printStackTrace();
		}
		
		/**
		 * Post the message.
		 */
		try {
			logger.debug(id+" Post Message in JMS");
			messageProducer.send(message);
		} catch (JMSException e) {
			logger.warn(id+" Fail to post the message : "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	
}
