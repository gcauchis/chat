package com.gc.irc.server.client.connector.irc.factory;

import java.net.Socket;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.server.client.connector.factory.AbstractClientSocketConnectionFactory;
import com.gc.irc.server.client.connector.irc.IRCClientConnection;

/**
 * <p>IRCClientConnectionFactory class.</p>
 *
 * @author x472511
 * @version 0.0.4
 */
@Component("ircClientConnectionFactory")
@Scope("singleton")
public class IRCClientConnectionFactory  extends AbstractClientSocketConnectionFactory<IRCClientConnection> {

	/** {@inheritDoc} */
	@Override
	protected IRCClientConnection build(Socket clientSocket) {
		return new IRCClientConnection(clientSocket);
	}

}
