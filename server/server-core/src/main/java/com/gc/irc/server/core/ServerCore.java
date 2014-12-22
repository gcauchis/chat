package com.gc.irc.server.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gc.irc.common.AbstractLoggable;
import com.gc.irc.server.client.connector.ClientConnector;
import com.gc.irc.server.client.connector.management.UsersConnectionsManagement;
import com.gc.irc.server.client.connector.provider.ClientConnectorProvider;
import com.gc.irc.server.thread.ServerManager;
import com.gc.irc.server.thread.factory.ServerManagerFactory;

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

    /** The nb thread serveur. */
    @Value("${nbConsumerThread}")
    private int nbThreadServeur = 1;

    /** The pull thread serveur. */
    private final List<ServerManager> pullThreadServeur = Collections.synchronizedList(new ArrayList<ServerManager>());

    /** The serveur m bean factory. */
    @Autowired
    private ServerManagerFactory serveurMBeanFactory;

    /** The users connections management. */
    @Autowired
    private UsersConnectionsManagement usersConnectionsManagement;
    
    private List<ClientConnectorProvider> clientConnectorProviders;

    /**
     * Instantiates a new server core.
     */
    public ServerCore() {
    }

    /**
     * Finalize the Server.
     */
    public void close() {
        for (final ServerManager thread : pullThreadServeur) {
            thread.close();
        }

        usersConnectionsManagement.close();
    }

    /**
     * Initialize the server.
     */
    public void initServeur() {
		getLog().info(
				clientConnectorProviders.size() + " Providers of connector");
		for (ClientConnectorProvider provider : clientConnectorProviders) {
			for (ClientConnector connector : provider.getClientConnectors()) {
				new Thread(connector).start();
			}
		}

        for (int i = 0; i < nbThreadServeur; i++) {
            getLog().info("Build serveurMBean {}", i);
            final ServerManager serveurMBean = serveurMBeanFactory.getServeurManager();
            new Thread(serveurMBean).start();
            pullThreadServeur.add(serveurMBean);
        }

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
     * Sets the serveur m bean factory.
     *
     * @param serveurMBeanFactory
     *            the new serveur m bean factory
     */
    public void setServeurMBeanFactory(final ServerManagerFactory serveurMBeanFactory) {
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
     * <p>Setter for the field <code>clientConnectorProviders</code>.</p>
     *
     * @param clientConnectorProviders a {@link java.util.List} object.
     */
    @Autowired
    public void setClientConnectorProviders(
			List<ClientConnectorProvider> clientConnectorProviders) {
		this.clientConnectorProviders = clientConnectorProviders;
	}

}
