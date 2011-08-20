package com.gc.irc.server.jms;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.server.conf.ServerConf;

public class IRCJMSPoolProducer {
	private static final Logger logger = Logger.getLogger(IRCJMSPoolProducer.class);
	private Map<Integer, IRCJMSProducer> listPoolProducerJMS = Collections.synchronizedMap(new HashMap<Integer, IRCJMSProducer>());
	private int poolSize = Integer.parseInt(ServerConf.getConfProperty("pullJMS", "10"), 10);
	private Integer currentId = 0;
	
	private static IRCJMSPoolProducer instance = null;
	
	/**
	 * Builder generate the Producer pool
	 */
	private IRCJMSPoolProducer() {
		logger.info("Create pool JMS producer");
		
		for (int i = 0 ; i < poolSize ; i++) {
			listPoolProducerJMS.put(i, new IRCJMSProducer());
		}
	}
	
	/**
	 * Get an instance of the pool.
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
	 * @param objectMessage Message to send.
	 */
	public void postMessageObjectInJMS(IRCMessage objectMessage) {
		IRCJMSProducer messageProducer = getAProducer();
		
		synchronized (messageProducer) {
			messageProducer.postMessageObjectInJMS(objectMessage);
		}
	}
	
	/**
	 * Get a producer in the pool.
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
