package com.gc.irc.server.client.connector.objectstream;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gc.irc.server.client.connector.ClientConnector;
import com.gc.irc.server.client.connector.ClientConnectorProvider;

@Component("objectStreamClientConnectorProvider")
public class ObjectStreamClientConnectorProvider implements
		ClientConnectorProvider {

	public List<ClientConnector> getClientConnectors() {
		ClientConnector clientConnector = new ObjectStreamClientConnector();
		return Arrays.asList(clientConnector);
	}

}
