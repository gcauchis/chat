package com.gc.irc.common.connector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;

import com.gc.irc.common.AbstractRunnable;
import com.gc.irc.common.message.MessageHandler;
import com.gc.irc.common.message.MessageSender;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.common.utils.IOStreamUtils;

/**
 * The thread wich connects the client to the server, and manages the serialized
 * objects wich are transmitted (they are defined in the com.irc.share.protocol
 * package)
 *
 * @version 0.0.4
 * @author x472511
 */
public class ConnectionHandler extends AbstractRunnable implements MessageSender {

    /** The authenticated. */
    private boolean authenticated = false;

    /** The connected to server. */
    private boolean connectedToServer = false;

    /** The host. */
    private InetAddress host = null;

    /** The initialized. */
    private boolean initialized;

    /** The in object. */
    private ObjectInputStream inObject;

    /** The manual disconnection. */
    private boolean manualDisconnection = false;

    /** The message handler. */
    private MessageHandler messageHandler;

    /** The out object. */
    private ObjectOutputStream outObject;

    /** The port. */
    private final int port;

    /** The server disconnection. */
    private boolean serverDisconnection = false;

    /** The server name. */
    private final String serverHost;

    /** The socket. */
    private Socket socket = null;

    /** The waiting for authentication. */
    private boolean waitingForAuthentication = false;

    /**
     * Instantiates a new connection thread.
     *
     * @param serverHost
     *            the ip address or name of the server If the server is on
     *            localhost, out an empty string
     * @param port
     *            the port of the connexion
     */
    public ConnectionHandler(final String serverHost, final int port) {

        this.serverHost = serverHost;
        this.port = port;

        if (StringUtils.isEmpty(serverHost)) {
            getLog().info("The server parameters will be : host=localhost" + " port=" + port);
        } else {
            getLog().info("The server parameters will be : host=" + serverHost + " port=" + port);
        }

        while (true) {
            getLog().debug("Initialisation of the connection thread. Server host : " + serverHost + " port : " + port);

            try {
                if (StringUtils.isNotEmpty(serverHost)) {
                    host = InetAddress.getByName(serverHost);
                } else {
                    host = InetAddress.getLocalHost();
                }

                break;

            } catch (final UnknownHostException e) {
                getLog().error("Impossible to find the host", e);
            }

            try {
                Thread.sleep(200);
            } catch (final InterruptedException e) {
                getLog().error("sleep interuption", e);
            }

        }

    }

    /**
     * disconnect the client from the server After that the client will
     * automatically try to reconnect.
     */
    public void disconnect() {
        try {
            manualDisconnection = true;
            setServerDisconnection(false); // because it is a manual
            // disconnection, from the client
            socket.close();
        } catch (final IOException e) {
            getLog().error("IO error", e);
        }
    }

    /**
     * Gets the port.
     *
     * @return the choosen port for the connexion with the server
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets the server name.
     *
     * @return the name or ip address of the remote server
     */
    public String getServerHost() {
        if (!serverHost.isEmpty()) {
            return serverHost;
        } else {
            return "localhost";
        }
    }

    /**
     * Is the client currently authenticated on the server ?.
     *
     * @return a boolean.
     */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * Is the client currently connected on the server ? (the client may not be
     * authenticated).
     *
     * @return a boolean.
     */
    public boolean isConnectedToServer() {
        return connectedToServer;
    }

    /**
     * <p>isInitialized.</p>
     *
     * @return a boolean.
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Checks if is server disconnection.
     *
     * @return a boolean.
     */
    public boolean isServerDisconnection() {
        return serverDisconnection;
    }

    /**
     * Checks if is waiting for authentication.
     *
     * @return a boolean.
     */
    public boolean isWaitingForAuthentication() {
        return waitingForAuthentication;
    }

    /**
     * {@inheritDoc}
     *
     * The thread infinite loop. Here, the client will try to connect to the
     * server (opening a socket, and getting input and output streams). Then,
     * the loop will wait for new serialized objects sent by the server, and
     * execute the corresponding actions.
     */
    @Override
    public void run() {

        while (true) {

            getLog().info("Trying to connect");

            try {
                socket = new Socket(host, port);
                setConnectedToServer(true);
                manualDisconnection = false;

                getLog().info("Socket successfully created.  Local port : " + socket.getLocalPort());

                /**
                 * Gestion par objet
                 */
                getLog().debug("Trying to open streams...");
                outObject = new ObjectOutputStream(socket.getOutputStream());
                getLog().debug("Output stream opened");
                inObject = new ObjectInputStream(socket.getInputStream());
                getLog().debug("Input stream opened");
                initialized = true;
                while (true) {
                    /**
                     * Reception du message.
                     */
                    getLog().debug("Waiting for an object message");
                    Message messageObject = null;
                    try {
                        messageObject = IOStreamUtils.receiveMessage(inObject);
                    } catch (final ClassNotFoundException e) {
                        getLog().error("Fail to retreive object class from stream.", e);
                        break;
                    }

                    if (messageObject == null) {
                        getLog().error("Empty IRCMessage Object received");

                        try {
                            Thread.sleep(500);
                        } catch (final InterruptedException e) {
                            getLog().warn(e.getMessage());
                        }

                    } else {
                        getLog().debug("Message received : " + messageObject.getClass());
                        if (messageHandler == null) {
                            getLog().warn("No messageHandler to handle " + messageObject);
                        } else {
                            messageHandler.handle(messageObject);
                        }
                    }
                }
            } catch (final IOException e) {
                setConnectedToServer(false);
                setWaitingForAuthentication(false);
                if (!manualDisconnection && isAuthenticated()) {
                    setServerDisconnection(true);
                }
                setAuthenticated(false);
                if (!manualDisconnection) {
                    getLog().error("The connection with the server lost", e);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.common.api.IIRCMessageSender#send(com.gc.irc.common.protocol
     * .IRCMessage)
     */
    /** {@inheritDoc} */
    @Override
    public void send(final Message message) {
        try {
            /**
             * Synchronize the socket.
             */
            if (!socket.isOutputShutdown()) {
                synchronized (inObject) {
                    synchronized (outObject) {
                        getLog().debug("Send message");
                        if (socket.isConnected()) {
                            IOStreamUtils.sendMessage(outObject, message);
                        } else {
                            getLog().warn("Socket not connected !");
                        }
                    }
                }
            } else {
                getLog().warn("Fail to send message. Finalize because output is shutdown.");
                // TODO close all properly
            }
        } catch (final IOException e) {
            getLog().warn("Fail to send the message : " + e.getMessage());
            // TODO check the socket
        }

    }

    /**
     * Send and IRC Message (will transmit a serialized object to the server).
     *
     * @param message
     *            the IRC message to send
     */
    public void sendIRCMessage(final Message message) {
        try {
            synchronized (inObject) {
                synchronized (outObject) {
                    IOStreamUtils.sendMessage(outObject, message);
                }
            }

        } catch (final SocketException e) {
            setConnectedToServer(false);
            setWaitingForAuthentication(false);
            if (!manualDisconnection && isAuthenticated()) {
                setServerDisconnection(true);
            }
            setAuthenticated(false);
            getLog().error("Socket error ", e);
        } catch (final IOException e) {
            getLog().error("IO error ", e);
        }
    }

    /**
     * Sets the authenticated.
     *
     * @param loggedIn
     *            the new authenticated
     */
    private void setAuthenticated(final boolean loggedIn) {
        authenticated = loggedIn;
    }

    /**
     * Sets the connected to server.
     *
     * @param connectedToServer
     *            the new connected to server
     */
    private void setConnectedToServer(final boolean connectedToServer) {
        this.connectedToServer = connectedToServer;
    }

    /**
     * Sets the message handler.
     *
     * @param messageHandler
     *            the new message handler
     */
    public void setMessageHandler(final MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    /**
     * Sets the server disconnection.
     *
     * @param serverDisconnection
     *            the new server disconnection
     */
    public void setServerDisconnection(final boolean serverDisconnection) {
        this.serverDisconnection = serverDisconnection;
    }

    /**
     * Sets the waiting for authentication.
     *
     * @param waitingForAuthentication
     *            the new waiting for authentication
     */
    public void setWaitingForAuthentication(final boolean waitingForAuthentication) {
        this.waitingForAuthentication = waitingForAuthentication;
    }

}
