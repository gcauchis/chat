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

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.server.conf.ServerConf;
import com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement;
import com.gc.irc.server.thread.api.IServeurMBean;
import com.gc.irc.server.thread.factory.api.IGestionClientBeanFactory;
import com.gc.irc.server.thread.factory.api.IServeurMBeanFactory;

/**
 * Main class.
 * 
 * Start the server. Start the ThreadPull.
 * 
 * When a Client is connected a Thread is start to discuss with.
 * 
 * @author gcauchis
 * 
 */
@Component("serverCore")
public class ServerCore extends AbstractLoggable {

    /** The message acceuil. */
    private static String messageAcceuil = "Welcome on our server.";

    /** The nb thread serveur. */
    private static int nbThreadServeur = Integer.parseInt(ServerConf.getConfProperty(ServerConf.NB_CONSUMER_THREAD, "1"));

    /** The server socket. */
    private static ServerSocket serverSocket = null;

    /**
     * Get the welcome message.
     * 
     * @return The welcome message.
     */
    public static String getMessageAcceuil() {
        return messageAcceuil;
    }

    /**
     * Used to change the welcoming message.
     * 
     * @param messageAcceuil
     *            The new message.
     */
    public static void setMessageAcceuil(final String messageAcceuil) {
        ServerCore.messageAcceuil = messageAcceuil;
    }

    /** The gestion client bean factory. */
    @Autowired
    private IGestionClientBeanFactory gestionClientBeanFactory;

    /** The port. */
    @Value("${server.port}")
    private int port = -1;

    /** The pull thread serveur. */
    private final List<IServeurMBean> pullThreadServeur = Collections.synchronizedList(new ArrayList<IServeurMBean>());

    /** The serveur m bean factory. */
    @Autowired
    private IServeurMBeanFactory serveurMBeanFactory;

    /** The users connections management. */
    @Autowired
    private IUsersConnectionsManagement usersConnectionsManagement;

    /**
     * Builder, Initialize the server. The port is 1973.
     */
    public ServerCore() {
    }

    /**
     * Builder, Initialize the server. The port is given.
     * 
     * @param port
     *            New port.
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
        for (final IServeurMBean thread : pullThreadServeur) {
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

        /**
         * Start thread server pull. The first thread is registered as a MBean.
         */

        // // Get the Platform MBean Server
        // MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        //
        // // Construct the ObjectName for the MBean we will register
        // ObjectName name = null;
        // try {
        // name = new ObjectName("com.irc.server.thread:type=ThreadServeurIRC");
        // } catch (MalformedObjectNameException e) {
        // getLog().fatal("Malformed Object Name (JMX MBean Server).");
        // e.printStackTrace();
        // System.exit(-1);
        // } catch (NullPointerException e) {
        // getLog().fatal("Can not register the object ThreadServerIRC (JMX MBean Server).");
        // e.printStackTrace();
        // }

        // Create the Thread
        // IServeurMBean threadServer = new ServeurMBean(this);
        // threadServer.start();

        // try {
        // mbs.registerMBean(threadServer, name);
        // } catch (InstanceAlreadyExistsException e) {
        // getLog().fatal("Instance Already Exists (JMX MBean Server).", e);
        // System.exit(-1);
        // } catch (MBeanRegistrationException e) {
        // getLog().fatal("Unable to register MBean (JMX MBean Server).", e);
        // System.exit(-1);
        // } catch (NotCompliantMBeanException e) {
        // getLog().fatal("Not Compliant MBean (JMX MBean Server).", e);
        // System.exit(-1);
        // }

        for (int i = 0; i < nbThreadServeur; i++) {
            IServeurMBean serveurMBean = serveurMBeanFactory.getServeurMBean();
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
    public void setGestionClientBeanFactory(IGestionClientBeanFactory gestionClientBeanFactory) {
        this.gestionClientBeanFactory = gestionClientBeanFactory;
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
    public void setServeurMBeanFactory(IServeurMBeanFactory serveurMBeanFactory) {
        this.serveurMBeanFactory = serveurMBeanFactory;
    }

    public void setUserConnectionsManagement(IUsersConnectionsManagement userConnectionsManagement) {
        usersConnectionsManagement = userConnectionsManagement;
    }

    /**
     * Wait for new client. When a client connect to the sever stat a Thred fot him.
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
        final Runnable gestionClient = gestionClientBeanFactory.getGestionClientBean(clientSocket);
        getLog().debug("End Client's Thread Initialization.");
        new Thread(gestionClient).start();
    }

}
