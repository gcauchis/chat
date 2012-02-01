package com.gc.irc.server.conf;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * An object use to configure the server.
 * @author gcauchis
 *
 */
public class ServerConf {
	
	public static final String NB_CONSUMER_THREAD = "nbConsumerThread";
	public static final String NB_MESSAGE_MAX_PASSAGE = "nbMessageMaxPassage";
	public static final String JMS_POOL_SIZE = "jmsPoolSize";
	
	/** The properties. */
	private static Properties properties = null;
	
	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(ServerConf.class);
	
	/**
	 * Get the property.
	 * @param key Key of the property.
	 * @param defaultValue Default value.
	 * @return The Property value.
	 */
	public static synchronized String getConfProperty(String key, String defaultValue) {
		logger.debug("Search for "+key);
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(ServerConf.class.getResourceAsStream("/conf/servConf.properties"));
			} catch (IOException e) {
				logger.warn("Cannot load properties file : "+e.getMessage());
				e.printStackTrace();
				return defaultValue;
			}
		}
		return properties.getProperty(key, defaultValue).trim();
	}
}
