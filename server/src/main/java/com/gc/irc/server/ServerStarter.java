package com.gc.irc.server;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * The Class ServerStarter.
 */
public class ServerStarter {
	
	/** The Constant LOGGER. */
	private final static Logger LOGGER = Logger.getLogger(ServeurCore.class);

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		LOGGER.info("Start server");
		BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("spring-application-config.xml"));
		ServeurCore core = (ServeurCore) beanFactory.getBean("serveurCore");
		core.initServeur();
		while (true) {
			core.attenteClient();
		}

	}

}
