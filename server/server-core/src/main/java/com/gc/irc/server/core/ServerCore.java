package com.gc.irc.server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.server.client.connector.management.UsersConnectionsManagement;
import com.gc.irc.server.client.connector.objectstream.factory.ClientConnectionFactory;
import com.gc.irc.server.thread.ServeurManager;
import com.gc.irc.server.thread.factory.ServeurManagerFactory;

/**
 * Main class.
 *
 * Start the server. Start the ThreadPull.
 *
 * When a Client is connected a Thread is start to discuss with.
 *
 * @author gcauchis
 * @version 0.0.4
 */
@Component("serverCore")
public class ServerCore extends AbstractLoggable {

    /** The welcome message. */
    private static String welcomeMessage = "Welcome on our server.";

    /** The server socket. */
    private static ServerSocket serverSocket = null;

    /**
     * Get the welcome message.
     *
     * @return The welcome message.
     */
    public static String getWelcomeMessage() {
        return welcomeMessage;
    }

    /**
     * Used to change the welcoming message.
     *
     * @param welcomeMessage
     *            The new message.
     */
    public static void setWelcomeMessage(final String welcomeMessage) {
        ServerCore.welcomeMessage = welcomeMessage;
    }

    /** The gestion client bean factory. */
    @Autowired
    private ClientConnectionFactory gestionClientBeanFactory;

    /** The nb thread serveur. */
    @Value("${nbConsumerThread}")
    private int nbThreadServeur = 1;

    /** The port. */
    @Value("${server.port}")
    private int port = -1;

    /** The pull thread serveur. */
    private final List<ServeurManager> pullThreadServeur = Collections.synchronizedList(new ArrayList<ServeurManager>());

    /** The serveur m bean factory. */
    @Autowired
    private ServeurManagerFactory serveurMBeanFactory;

    /** The users connections management. */
    @Autowired
    private UsersConnectionsManagement usersConnectionsManagement;

    /**
     * Instantiates a new server core.
     */
    public ServerCore() {
    }

    /**
     * Instantiates a new server core.
     *
     * @param port
     *            the port
     */
    public ServerCore(final int port) {
        if (this.port != port) {
            setPort(port);
        }
    }

    /**
     * Finalize the Server.
     */
    public void close() {
        for (final ServeurManager thread : pullThreadServeur) {
            thread.close();
        }

        usersConnectionsManagement.close();
    }

    /**
     * Initialize the server.
     */
    public void initServeur() {
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
        getLog().info("Server initialize. Listen port " + port);


        for (int i = 0; i < nbThreadServeur; i++) {
            getLog().info("Build serveurMBean {}", i);
            final ServeurManager serveurMBean = serveurMBeanFactory.getServeurManager();
            new Thread(serveurMBean).start();
            pullThreadServeur.add(serveurMBean);
        }

    }

    /**
     * Sets the gestion client bean factory.
     *
     * @param gestionClientBeanFactory
     *            the new gestion client bean factory
     */
    public void setGestionClientBeanFactory(final ClientConnectionFactory gestionClientBeanFactory) {
        this.gestionClientBeanFactory = gestionClientBeanFactory;
    }

    /**
     * <p>Setter for the field <code>nbThreadServeur</code>.</p>
     *
     * @param nbThreadServeur
     *            the nbThreadServeur to set
     */
    public void setNbThreadServeur(final int nbThreadServeur) {
        this.nbThreadServeur = nbThreadServeur;
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
        getLog().debug("Nouveau port : " + port);
    }

    /**
     * Sets the serveur m bean factory.
     *
     * @param serveurMBeanFactory
     *            the new serveur m bean factory
     */
    public void setServeurMBeanFactory(final ServeurManagerFactory serveurMBeanFactory) {
        this.serveurMBeanFactory = serveurMBeanFactory;
    }

    /**
     * Sets the user connections management.
     *
     * @param userConnectionsManagement
     *            the new user connections management
     */
    public void setUserConnectionsManagement(final UsersConnectionsManagement userConnectionsManagement) {
        usersConnectionsManagement = userConnectionsManagement;
    }

    /**
     * <p>Setter for the field <code>usersConnectionsManagement</code>.</p>
     *
     * @param usersConnectionsManagement
     *            the usersConnectionsManagement to set
     */
    public void setUsersConnectionsManagement(final UsersConnectionsManagement usersConnectionsManagement) {
        this.usersConnectionsManagement = usersConnectionsManagement;
    }

    /**
     * Wait for new client. When a client connect to the sever stat a Thred fot
     * him.
     */
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

}
