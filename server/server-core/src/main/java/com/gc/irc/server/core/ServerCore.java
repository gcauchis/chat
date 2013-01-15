package com.gc.irc.server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gc.irc.common.abs.AbstractLoggable;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.server.conf.ServerConf;
import com.gc.irc.server.persistance.PersiteUsers;
import com.gc.irc.server.thread.api.IGestionClientBean;
import com.gc.irc.server.thread.factory.api.IGestionClientBeanFactory;
import com.gc.irc.server.thread.impl.GestionClientBean;
import com.gc.irc.server.thread.impl.ServeurMBean;

// TODO: Auto-generated Javadoc
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

    /** The client connecter. */
    private final List<GestionClientBean> clientConnecter = Collections.synchronizedList(new ArrayList<GestionClientBean>());

    /** The gestion client bean factory. */
    @Autowired
    private IGestionClientBeanFactory gestionClientBeanFactory;

    /** The list thread client by id user. */
    private final Map<Integer, GestionClientBean> listThreadClientByIdUser = Collections.synchronizedMap(new HashMap<Integer, GestionClientBean>());

    /** The list user by id. */
    private final Map<Integer, IRCUser> listUserById = Collections.synchronizedMap(new HashMap<Integer, IRCUser>());

    /** The port. */
    @Value("${server.port}")
    private int port = -1;

    /** The pull thread serveur. */
    private final List<ServeurMBean> pullThreadServeur = Collections.synchronizedList(new ArrayList<ServeurMBean>());

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
     * Delete the deconnected Client.
     * 
     * @param client
     *            Deconnected Client.
     */
    public void disconnectClient(final IGestionClientBean client) {
        getLog().debug("Delete the deconnected Client : " + client.getUser().getNickName());
        synchronized (clientConnecter) {
            synchronized (listUserById) {
                synchronized (listThreadClientByIdUser) {
                    getLog().debug("Remove from list clientConnecter");
                    clientConnecter.remove(client);
                    getLog().debug("Remove from listUserConnectedById");
                    listUserById.remove(client.getUser().getId());
                    getLog().debug("Remove from lisThreadClientByIdUser");
                    listThreadClientByIdUser.remove(client.getUser().getId());
                }
            }
        }

        /**
         * Persist the change.
         */
        PersiteUsers.persistListUser(getAllUsers());
    }

    /**
     * Finalize the Server.
     */
    public void finalizeClass() {
        for (final ServeurMBean thread : pullThreadServeur) {
            thread.finalizeClass();
        }

        for (final IGestionClientBean thread : clientConnecter) {
            thread.disconnectUser();
        }
        try {
            super.finalize();
        } catch (final Throwable e) {
            getLog().warn("Problem when finalize the Server", e);
        }
    }

    /**
     * Get the users Connected list.
     * 
     * @return The list of all the connected users.
     */
    public List<IRCUser> getAllUsers() {
        List<IRCUser> list = null;
        synchronized (listUserById) {
            list = new ArrayList<IRCUser>(listUserById.values());
        }
        return list;
    }

    /**
     * Get the Thread list of connected client.
     * 
     * @return Client's thread list.
     */
    public List<GestionClientBean> getClientConnecter() {
        return clientConnecter;
    }

    /**
     * Get the thread of a selected user.
     * 
     * @param id
     *            User's Id.
     * @return The Designed User's Thread.
     */
    public IGestionClientBean getThreadOfUser(final int id) {
        return listThreadClientByIdUser.get(id);
    }

    /**
     * Get the user demand if he is connected.
     * 
     * @param id
     *            User's Id.
     * @return The User selected or null if not find.
     */
    public IRCUser getUser(final int id) {
        return listUserById.get(id);
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
        ServeurMBean threadServer = new ServeurMBean(this);
        threadServer.start();

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

        for (int i = 1; i < nbThreadServeur; i++) {
            threadServer = new ServeurMBean(this);
            threadServer.start();
            pullThreadServeur.add(threadServer);
        }

    }

    /**
     * Add the login client to the Client's list.
     * 
     * @param client
     *            New Client
     */
    public void newClientConnected(final GestionClientBean client) {
        getLog().debug("Add a new Connected Client : " + client.getUser().getNickName());
        synchronized (clientConnecter) {
            synchronized (listUserById) {
                synchronized (listThreadClientByIdUser) {
                    getLog().debug("Add to clientConnecter");
                    clientConnecter.add(client);
                    getLog().debug("Add to listThreadClientByIdUser");
                    listThreadClientByIdUser.put(client.getUser().getId(), client);
                    getLog().debug("Add to listUserById");
                    listUserById.put(client.getUser().getId(), client.getUser());
                }
            }
        }

        /**
         * Persist the change.
         */
        PersiteUsers.persistListUser(getAllUsers());
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
        final Runnable gestionClient = gestionClientBeanFactory.getGestionClientBean(clientSocket, this);
        getLog().debug("End Client's Thread Initialization.");
        new Thread(gestionClient).start();
    }

}
