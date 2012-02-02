package com.gc.irc.server;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import com.gc.irc.server.core.ServerCore;

/**
 * The Class ServerStarter.
 */
public class ServerStarter {
	
	/** The Constant LOGGER. */
	private final static Logger LOGGER = Logger.getLogger(ServerStarter.class);

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("spring-application-config.xml"));
		ServerCore core = (ServerCore) beanFactory.getBean("serverCore");
		LOGGER.info("Init server");
		core.initServeur();
		LOGGER.info("Start Waiting for client");
		while (true) {
			core.waitClient();
		}
	}
}
