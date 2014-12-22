package com.gc.irc.server.client.connector.irc.provider;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.server.client.connector.ClientConnector;
import com.gc.irc.server.client.connector.ClientSocketConnector;
import com.gc.irc.server.client.connector.irc.IRCClientConnector;
import com.gc.irc.server.client.connector.provider.ClientConnectorProvider;

@Component("ircClientConnectorProvider")
public class IRCClientConnectorProvider extends AbstractLoggable implements
		ClientConnectorProvider, ApplicationContextAware {

	/**
	 * The client connector.
	 */
	private ClientSocketConnector clientConnector;

	private ApplicationContext applicationContext;

	@Value("${irc.server.port}")
	private int port = -1;

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

	/**
	 * <p>
	 * Getter for the field <code>clientConnector</code>.
	 * </p>
	 *
	 * @return a {@link com.gc.irc.server.client.connector.ClientConnector}
	 *         object.
	 */
	public ClientConnector getClientConnector() {
		if (clientConnector == null) {
			clientConnector = applicationContext
					.getBean(IRCClientConnector.class);
			clientConnector.setPort(port);
		}
		return clientConnector;
	}

	/** {@inheritDoc} */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * Change the listening port
	 *
	 * @param port
	 *            New Listening Port.
	 */
	public void setPort(final int port) {
		this.port = port;
	}

}
