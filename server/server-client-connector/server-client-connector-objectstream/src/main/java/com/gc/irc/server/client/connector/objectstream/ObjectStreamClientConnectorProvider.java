package com.gc.irc.server.client.connector.objectstream;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.gc.irc.server.client.connector.ClientConnector;
import com.gc.irc.server.client.connector.ClientConnectorProvider;

/**
 * <p>ObjectStreamClientConnectorProvider class.</p>
 *
 * @author gcauchis
 * @version 0.0.4
 */
@Component("objectStreamClientConnectorProvider")
public class ObjectStreamClientConnectorProvider implements
		ClientConnectorProvider, ApplicationContextAware {
	
	/**
	 * The client connector.
	 */
	private ClientConnector clientConnector;
	
	private ApplicationContext applicationContext;

	/**
	 * <p>
	 * getClientConnectors.
	 * </p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<ClientConnector> getClientConnectors() {

		return Arrays.asList(getClientConnector());
	}

	public ClientConnector getClientConnector() {
		if (clientConnector == null)
		{
			clientConnector = applicationContext.getBean(ObjectStreamClientConnector.class);
		}
		return clientConnector;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
}
