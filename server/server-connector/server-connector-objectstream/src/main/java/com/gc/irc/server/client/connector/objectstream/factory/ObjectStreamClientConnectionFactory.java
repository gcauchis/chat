package com.gc.irc.server.client.connector.objectstream.factory;

import java.net.Socket;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gc.irc.server.client.connector.factory.AbstractClientSocketConnectionFactory;
import com.gc.irc.server.client.connector.objectstream.ObjectStreamClientConnection;

/**
 * A factory for creating GestionClientBean objects.
 *
 * @version 0.0.4
 * @author x472511
 */
@Component("objectStreamClientConnectionFactory")
@Scope("singleton")
public class ObjectStreamClientConnectionFactory extends AbstractClientSocketConnectionFactory<ObjectStreamClientConnection> {

	/** {@inheritDoc} */
	@Override
	protected ObjectStreamClientConnection build(Socket clientSocket) {
		return new ObjectStreamClientConnection(clientSocket);
	}

}
