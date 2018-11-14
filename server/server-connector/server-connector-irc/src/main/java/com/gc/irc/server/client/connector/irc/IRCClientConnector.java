package com.gc.irc.server.client.connector.irc;

import org.springframework.stereotype.Component;

import com.gc.irc.server.client.connector.AbstractClientSocketConnector;
import com.gc.irc.server.client.connector.irc.factory.IRCClientConnectionFactory;

/**
 * <p>IRCClientConnector class.</p>
 *
 * @author x472511
 * @version 0.0.4
 */
@Component("ircClientConnector")
public class IRCClientConnector extends AbstractClientSocketConnector<IRCClientConnectionFactory> {

    /**
     * Instantiates a new ObjectStreamClientConnector
     */
    public IRCClientConnector() {
    	super();
    }
    
    /**
     * Instantiates a new ObjectStreamClientConnector
     *
     * @param port
     *            the port
     */
    public IRCClientConnector(final int port) {
    	super(port);
    }

}
