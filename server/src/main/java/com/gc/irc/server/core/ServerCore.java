package com.gc.irc.server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.server.conf.ServerConf;
import com.gc.irc.server.persistance.PersiteUsers;
import com.gc.irc.server.thread.ThreadGestionClientIRC;
import com.gc.irc.server.thread.ThreadServeurIRC;

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
public class ServerCore {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerCore.class);

    /** The nb thread serveur. */
    private static int nbThreadServeur = Integer.parseInt(ServerConf.getConfProperty(ServerConf.NB_CONSUMER_THREAD, "1"));

    /** The server socket. */
    private static ServerSocket serverSocket = null;

    /** The port. */
    private int port = -1;

    /** The client connecter. */
    private List<ThreadGestionClientIRC> clientConnecter = Collections.synchronizedList(new ArrayList<ThreadGestionClientIRC>());

    /** The pull thread serveur. */
    private List<ThreadServeurIRC> pullThreadServeur = Collections.synchronizedList(new ArrayList<ThreadServeurIRC>());

    /** The list user by id. */
    private Map<Integer, IRCUser> listUserById = Collections.synchronizedMap(new HashMap<Integer, IRCUser>());

    /** The list thread client by id user. */
    private Map<Integer, ThreadGestionClientIRC> listThreadClientByIdUser = Collections.synchronizedMap(new HashMap<Integer, ThreadGestionClientIRC>());

    /** The message acceuil. */
    private static String messageAcceuil = "Welcome on our server.";

    /**
     * Used to change the welcoming message.
     * 
     * @param messageAcceuil
     *            The new message.
     */
    public static void setMessageAcceuil(final String messageAcceuil) {
        ServerCore.messageAcceuil = messageAcceuil;
    }

    /**
     * Get the welcome message.
     * 
     * @return The welcome message.
     */
    public static String getMessageAcceuil() {
        return messageAcceuil;
    }

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
     * Initialize the server.
     */
    public void initServeur() {
        LOGGER.info("Initialise server.");
        if (port < 0) {
            throw new IllegalArgumentException("The port should be set");
        }

        /**
         * Listen the designed Port
         */
        try {
            serverSocket = new ServerSocket(port);
        } catch (final IOException e) {
            LOGGER.error("Impossible to open the socket.", e);
            System.exit(-1);
        }
        LOGGER.info("Server initialize. Listen port " + port);

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
        // logger.fatal("Malformed Object Name (JMX MBean Server).");
        // e.printStackTrace();
        // System.exit(-1);
        // } catch (NullPointerException e) {
        // logger.fatal("Can not register the object ThreadServerIRC (JMX MBean Server).");
        // e.printStackTrace();
        // }

        // Create the Thread
        ThreadServeurIRC threadServer = new ThreadServeurIRC(this);
        threadServer.start();

        // try {
        // mbs.registerMBean(threadServer, name);
        // } catch (InstanceAlreadyExistsException e) {
        // logger.fatal("Instance Already Exists (JMX MBean Server).", e);
        // System.exit(-1);
        // } catch (MBeanRegistrationException e) {
        // logger.fatal("Unable to register MBean (JMX MBean Server).", e);
        // System.exit(-1);
        // } catch (NotCompliantMBeanException e) {
        // logger.fatal("Not Compliant MBean (JMX MBean Server).", e);
        // System.exit(-1);
        // }

        for (int i = 1; i < nbThreadServeur; i++) {
            threadServer = new ThreadServeurIRC(this);
            threadServer.start();
            pullThreadServeur.add(threadServer);
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
        LOGGER.debug("Nouveau port : " + port);
    }

    /**
     * Wait for new client. When a client connect to the sever stat a Thred fot
     * him.
     */
    public void waitClient() {
        Socket clientSocket = null;
        try {
            LOGGER.debug("Wait for a client");
            clientSocket = serverSocket.accept();
            LOGGER.debug("Client " + clientSocket.getInetAddress() + " is connected");
        } catch (final IOException e) {
            LOGGER.warn("Timeout or Connection error.", e);
            return;
        }
        final Thread thread = new ThreadGestionClientIRC(clientSocket, this);
        LOGGER.debug("End Client's Thread Initialization.");
        thread.start();
    }

    /**
     * Add the login client to the Client's list.
     * 
     * @param client
     *            New Client
     */
    public void newClientConnected(final ThreadGestionClientIRC client) {
        LOGGER.debug("Add a new Connected Client : " + client.getUser().getNickName());
        synchronized (clientConnecter) {
            synchronized (listUserById) {
                synchronized (listThreadClientByIdUser) {
                    LOGGER.debug("Add to clientConnecter");
                    clientConnecter.add(client);
                    LOGGER.debug("Add to listThreadClientByIdUser");
                    listThreadClientByIdUser.put(client.getUser().getId(), client);
                    LOGGER.debug("Add to listUserById");
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
     * Delete the deconnected Client.
     * 
     * @param client
     *            Deconnected Client.
     */
    public void disconnectClient(final ThreadGestionClientIRC client) {
        LOGGER.debug("Delete the deconnected Client : " + client.getUser().getNickName());
        synchronized (clientConnecter) {
            synchronized (listUserById) {
                synchronized (listThreadClientByIdUser) {
                    LOGGER.debug("Remove from list clientConnecter");
                    clientConnecter.remove(client);
                    LOGGER.debug("Remove from listUserConnectedById");
                    listUserById.remove(client.getUser().getId());
                    LOGGER.debug("Remove from lisThreadClientByIdUser");
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
     * Get the Thread list of connected client.
     * 
     * @return Client's thread list.
     */
    public List<ThreadGestionClientIRC> getClientConnecter() {
        return clientConnecter;
    }

    /**
     * Get the users Connected list.
     * 
     * @return The list of all the connected users.
     */
    public ArrayList<IRCUser> getAllUsers() {
        ArrayList<IRCUser> list = null;
        synchronized (listUserById) {
            list = new ArrayList<IRCUser>(listUserById.values());
        }
        return list;
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
     * Get the thread of a selected user.
     * 
     * @param id
     *            User's Id.
     * @return The Designed User's Thread.
     */
    public ThreadGestionClientIRC getThreadOfUser(final int id) {
        return listThreadClientByIdUser.get(id);
    }

    /**
     * Finalize the Server.
     */
    public void finalizeClass() {
        for (final ThreadServeurIRC thread : pullThreadServeur) {
            thread.finalizeClass();
        }

        for (final ThreadGestionClientIRC thread : clientConnecter) {
            thread.finalizeClass();
        }
        try {
            super.finalize();
        } catch (final Throwable e) {
            LOGGER.warn("Problem when finalize the Server");
            e.printStackTrace();
        }
    }

}
