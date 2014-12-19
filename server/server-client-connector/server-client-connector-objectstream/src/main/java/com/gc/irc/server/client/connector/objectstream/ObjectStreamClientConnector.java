package com.gc.irc.server.client.connector.objectstream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gc.irc.server.client.connector.AbstractClientConnector;
import com.gc.irc.server.client.connector.objectstream.factory.ObjectStreamClientConnectionFactory;

/**
 * The Class ObjectStreamClientConnector.
 *
 * @author gcauchis
 * @version 0.0.4
 */
@Component("ObjectStreamClientConnector")
public class ObjectStreamClientConnector extends AbstractClientConnector {

	/** The server socket. */
    private static ServerSocket serverSocket = null;
    
    /** The port. */
    @Value("${server.port}")
    private int port = -1;
    
    /** The gestion client bean factory. */
    @Autowired
    private ObjectStreamClientConnectionFactory gestionClientBeanFactory;
    
    /**
     * Instantiates a new ObjectStreamClientConnector
     */
    public ObjectStreamClientConnector() {
    }
    
    /**
     * Instantiates a new ObjectStreamClientConnector
     *
     * @param port
     *            the port
     */
    public ObjectStreamClientConnector(final int port) {
        if (this.port != port) {
            setPort(port);
        }
    }
    
    /**
     * Change the listening port
     *
     * Don't forget to use initServer() after use this method.
     *
     * @param port
     *            New Listening Port.
     */
    public void setPort(final int port) {
        this.port = port;
        getLog().debug("New port : " + port);
    }
    
	/** {@inheritDoc} */
	@Override
	public void initConnector() {
		getLog().info("Initialise server.");
        if (port < 0) {
            throw new IllegalArgumentException("Port should be set");
        }

        /**
         * Listen the designed Port
         */
        try {
            serverSocket = new ServerSocket(port);
        } catch (final IOException e) {
            getLog().error("Impossible to open the socket.", e);
            System.exit(-1);
        }
        getLog().info("Connector initializes. Listening port " + port);

	}

	/** {@inheritDoc} */
	@Override
	public void waitClient() {
		Socket clientSocket = null;
        try {
            getLog().debug("Wait for a client");
            clientSocket = serverSocket.accept();
            getLog().debug("Client " + clientSocket.getInetAddress() + " is connected");
        } catch (final IOException e) {
            getLog().warn("Timeout or Connection error.", e);
            return;
        }
        final Runnable gestionClient = gestionClientBeanFactory.getClientConnection(clientSocket);
        getLog().debug("End Client's Thread Initialization.");
        new Thread(gestionClient).start();

	}
	
	 /**
     * Sets the gestion client bean factory.
     *
     * @param gestionClientBeanFactory
     *            the new gestion client bean factory
     */
    public void setGestionClientBeanFactory(final ObjectStreamClientConnectionFactory gestionClientBeanFactory) {
        this.gestionClientBeanFactory = gestionClientBeanFactory;
    }

}
