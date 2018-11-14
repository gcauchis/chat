package com.gc.irc.server.client.connector.objectstream;


import org.springframework.stereotype.Component;

import com.gc.irc.server.client.connector.AbstractClientSocketConnector;
import com.gc.irc.server.client.connector.objectstream.factory.ObjectStreamClientConnectionFactory;

/**
 * The Class ObjectStreamClientConnector.
 *
 * @version 0.0.4
 * @author x472511
 */
@Component("ObjectStreamClientConnector")
public class ObjectStreamClientConnector extends AbstractClientSocketConnector<ObjectStreamClientConnectionFactory> {

    /**
     * Instantiates a new ObjectStreamClientConnector
     */
    public ObjectStreamClientConnector() {
    	super();
    }
    
    /**
     * Instantiates a new ObjectStreamClientConnector
     *
     * @param port
     *            the port
     */
    public ObjectStreamClientConnector(final int port) {
    	super(port);
    }

}
