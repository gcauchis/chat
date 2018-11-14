package com.gc.irc.server.client.connector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;

import com.gc.irc.server.client.connector.factory.ClientSocketConnectionFactory;


/**
 * <p>Abstract AbstractClientSocketConnector class.</p>
 *
 * @version 0.0.5
 * @author x472511
 */
public abstract class AbstractClientSocketConnector<FACT extends ClientSocketConnectionFactory> extends
		AbstractClientConnector implements ClientSocketConnector {

	/** The server socket. */
    private static ServerSocket serverSocket = null;
    
    /** The port. */
    private int port = -1;
    
    /** The gestion client bean factory. */
    @Autowired
    private FACT clientConnectionFactory;
    
    /**
     * Instantiates a new ObjectStreamClientConnector
     */
    public AbstractClientSocketConnector() {
    }
    
    /**
     * Instantiates a new ObjectStreamClientConnector
     *
     * @param port
     *            the port
     */
    public AbstractClientSocketConnector(final int port) {
        if (this.port != port) {
            setPort(port);
        }
    }
    
    /**
     * {@inheritDoc}
     *
     * Change the listening port
     *
     * Don't forget to use initServer() after use this method.
     */
    @Override
    public void setPort(final int port) {
        this.port = port;
        getLog().debug("New port : " + port);
    }
    
	/** {@inheritDoc} */
	@Override
	public void initConnector() {
		getLog().info(getClass().getSimpleName() + ": Initialise server.");
        if (port < 0) {
            throw new IllegalArgumentException("Port should be set");
        }

        /**
         * Listen the designed Port
         */
        try {
            serverSocket = new ServerSocket(port);
        } catch (final IOException e) {
            getLog().error(getClass().getSimpleName() + ": Impossible to open the socket.", e);
            System.exit(-1);
        }
        getLog().info(getClass().getSimpleName() + ": Connector initializes. Listening port " + port);

	}

	/** {@inheritDoc} */
	@Override
	public void waitClient() {
		Socket clientSocket = null;
        try {
            getLog().info(getClass().getSimpleName() + ": Wait for a client");
            clientSocket = serverSocket.accept();
            getLog().info(getClass().getSimpleName() + ": Client " + clientSocket.getInetAddress() + " is connected");
        } catch (final IOException e) {
            getLog().warn(getClass().getSimpleName() + ": Timeout or Connection error.", e);
            return;
        }
        final ClientConnection gestionClient = clientConnectionFactory.getClientConnection(clientSocket);
        getLog().debug(getClass().getSimpleName() + ": End Client's Thread Initialization.");
        new Thread(gestionClient, gestionClient.getId()).start();

	}
	
	/**
	 * Sets the gestion client bean factory.
	 *
	 * @param clientConnectionFactory a FACT object.
	 */
	@Autowired
    public void setClientConnectionFactory(final FACT clientConnectionFactory) {
        this.clientConnectionFactory = clientConnectionFactory;
    }

}
