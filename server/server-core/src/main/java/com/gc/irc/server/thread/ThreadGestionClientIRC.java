package com.gc.irc.server.thread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.entity.UserStatus;
import com.gc.irc.common.exception.security.IRCInvalidSenderException;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.IRCMessageType;
import com.gc.irc.common.protocol.command.IRCMessageCommand;
import com.gc.irc.common.protocol.command.IRCMessageCommandLogin;
import com.gc.irc.common.protocol.command.IRCMessageCommandRegister;
import com.gc.irc.common.protocol.item.IRCMessageItemPicture;
import com.gc.irc.common.protocol.notice.IRCMessageNoticeContactInfo;
import com.gc.irc.common.protocol.notice.IRCMessageNoticeContactsList;
import com.gc.irc.common.protocol.notice.IRCMessageNoticeLogin;
import com.gc.irc.common.protocol.notice.IRCMessageNoticeRegister;
import com.gc.irc.common.protocol.notice.IRCMessageNoticeServerMessage;
import com.gc.irc.common.utils.IOStreamUtils;
import com.gc.irc.server.auth.IRCServerAuthentification;
import com.gc.irc.server.core.ServerCore;
import com.gc.irc.server.exception.IRCServerException;
import com.gc.irc.server.jms.IRCJMSPoolProducer;
import com.gc.irc.server.persistance.IRCGestionPicture;

/**
 * Communication interface between the Client and the server.
 * 
 * @author gcauchis
 * 
 */
public class ThreadGestionClientIRC extends Thread {

    /** The nb thread. */
    private static int nbThread = 0;

    /**
     * Gets the nb thread.
     * 
     * @return the nb thread
     */
    protected static int getNbThread() {
        nbThread++;
        return nbThread;
    }

    /** The id. */
    private int id = getNbThread();

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadGestionClientIRC.class);

    /** The client socket. */
    private Socket clientSocket;

    /** The in object. */
    private ObjectInputStream inObject;

    /** The out object. */
    private ObjectOutputStream outObject;

    /** The parent. */
    private ServerCore parent;

    /** The user. */
    private IRCUser user;

    /** The is identify. */
    private boolean isIdentify = false;

    /**
     * Builder who initialize the TCP connection.
     * 
     * @param clientSocket
     *            Client's Socket.
     * @param parent
     *            Thread's Parent.
     */
    public ThreadGestionClientIRC(final Socket clientSocket, final ServerCore parent) {
        LOGGER.info(id + " Initialisation du thread.");
        this.clientSocket = clientSocket;
        this.parent = parent;

        try {
            LOGGER.debug(id + " Create inObject");
            inObject = new ObjectInputStream(clientSocket.getInputStream());
            LOGGER.debug(id + " Create outObject");
            outObject = new ObjectOutputStream(clientSocket.getOutputStream());

        } catch (final IOException e) {
            LOGGER.warn(id + " Fail to open Client's Steams to " + clientSocket.getInetAddress() + " : " + e.getMessage());
        }
        LOGGER.debug(id + " end init");
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        LOGGER.info(id + " Start Thread.");

        try {
            protocoleDAuthentification();
        } catch (final IRCServerException e) {
            LOGGER.warn(id + " Fail to autentificate the Client : " + e.getMessage());
        }

        IRCMessage messageClient;
        while (isIdentify) {
            /**
             * Wait for a Message
             */
            messageClient = null;
            messageClient = receiveMessage();
            if (messageClient == null) {
                LOGGER.info(id + " Empty message. Closing Connection.");
                break;
            }

            /**
             * Post Message in JMS
             */
            postMessageObjectInJMS(messageClient);

        }
        finalizeClass();
    }

    /**
     * Finalize Thread.
     * 
     * Close all Connection.
     */
    public void finalizeClass() {
        LOGGER.debug(id + " Finalize Thread");

        if (user != null) {
            /**
             * stop the while in run()
             */
            isIdentify = false;

            /**
             * Remove the client from server.
             */
            LOGGER.debug(id + " Delete Client " + user.getNickName() + " from list");
            parent.disconnectClient(this);

            /**
             * Inform all the other client.
             */
            LOGGER.debug(id + " Inform all other client that the client " + user.getNickName() + " is deconnected.");
            synchronized (user) {
                user.setUserStatus(UserStatus.OFFLINE);
                postMessageObjectInJMS(new IRCMessageNoticeContactInfo(user.getCopy()));
                IRCServerAuthentification.getInstance().getUser(user.getId()).deconnected();
            }
        }

        /**
         * Closing Socket.
         */
        try {
            LOGGER.info(id + " Closing Client's connection " + clientSocket.getInetAddress() + ".");
            if (!clientSocket.isInputShutdown()) {
                LOGGER.debug(id + " Closing inObject");
                inObject.close();
            }
            if (!clientSocket.isOutputShutdown()) {
                LOGGER.debug(id + " Closing outObject");
                outObject.close();
            }
            if (!clientSocket.isClosed()) {
                LOGGER.debug(id + " Closing clientSocket");
                clientSocket.close();
            }
        } catch (final IOException e) {
            LOGGER.warn(id + " Fail to close Client's connection " + clientSocket.getInetAddress() + " : " + e.getMessage());
        }
        try {
            super.finalize();
        } catch (final Throwable e) {
            LOGGER.warn(id + " Fail to finalize class : " + e.getMessage());
        }
    }

    /**
     * Send and IRCMessage to the Client.
     * 
     * @param message
     *            Message to send.
     */
    public void envoyerMessageObjetSocket(final IRCMessage message) {
        try {
            /**
             * Synchronize the socket.
             */
            if (!clientSocket.isOutputShutdown()) {
                synchronized (inObject) {
                    synchronized (outObject) {
                        LOGGER.debug(id + " Send message to " + user.getNickName());
                        if (clientSocket.isConnected()) {
                            if (!clientSocket.isOutputShutdown()) {
                                IOStreamUtils.sendMessage(outObject, message);
                            } else {
                                LOGGER.warn(id + " Output is Shutdown !");
                            }
                        } else {
                            LOGGER.warn(id + " Socket not connected !");
                        }
                    }
                }
            } else {
                LOGGER.warn(id + " Fail to send message. Finalize because output is shutdown.");
                finalizeClass();
            }
        } catch (final IOException e) {
            LOGGER.warn(id + " Fail to send the message : " + e.getMessage());
            socketAlive();
        }
    }

    /**
     * Wait and Receive a message send by the client.
     * 
     * @return Message received.
     */
    private IRCMessage receiveMessage() {
        IRCMessage message = null;
        try {
            LOGGER.debug(id + " Wait for a message in the socket.");
            message = IOStreamUtils.receiveMessage(inObject);
            checkMessage(message);
        } catch (final ClassNotFoundException e) {
            LOGGER.warn(id + " Fail to receive a message : " + e.getMessage());
            socketAlive();
        } catch (final IOException e) {
            LOGGER.warn(id + " Fail to receive a message : " + e.getMessage());
            socketAlive();
        }
        return message;
    }

    /**
     * Check message.
     * 
     * @param message
     *            the message
     */
    private void checkMessage(final IRCMessage message) {
        if (message != null && user.getId() != message.getFromId()) {
            throw new IRCInvalidSenderException("Message from " + message.getFromId() + " instead of " + user.getId());
        }
    }

    /**
     * Send a message in the JMS Queue.
     * 
     * @param objectMessage
     *            the object message
     */
    private void postMessageObjectInJMS(final IRCMessage objectMessage) {
        LOGGER.debug(id + " Send a message in JMS Queue.");
        IRCJMSPoolProducer.getInstance().postMessageObjectInJMS(objectMessage);
    }

    /**
     * Get the id of the Thread. <strong>Warning : </strong> This id is not the
     * user id.
     * 
     * @return Id of this.
     */
    public int getIdThread() {
        return id;
    }

    /**
     * Identification protocol.
     * 
     * @throws IRCServerException
     *             the iRC server exception
     */
    private void protocoleDAuthentification() throws IRCServerException {
        LOGGER.debug("Start Login protocol");
        IRCMessage messageInit = new IRCMessageNoticeServerMessage(ServerCore.getMessageAcceuil());
        /**
         * Send welcome message
         */
        try {
            LOGGER.debug("Send Welcome Message.");
            synchronized (inObject) {
                synchronized (outObject) {
                    IOStreamUtils.sendMessage(outObject, messageInit);
                }
            }
        } catch (final IOException e) {
            LOGGER.warn(id + " Fail to send Welcome message : " + e.getMessage());
            throw new IRCServerException(e);
        }

        boolean isLogin = false;
        while (!isLogin) {
            /**
             * Wait for login/Registration Message
             */
            try {
                LOGGER.debug(id + " Wait for login/Registration Message");
                messageInit = IOStreamUtils.receiveMessage(inObject);
            } catch (final ClassNotFoundException e) {
                LOGGER.warn(id + " Fail to receive the Login/Registration Message : " + e.getMessage());
                throw new IRCServerException(e);
            } catch (final IOException e) {
                LOGGER.warn(id + " Fail to receive the Login/Registration Message : " + e.getMessage());
                throw new IRCServerException(e);
            }

            /**
             * Answer to the client
             */
            LOGGER.debug(id + " Type : " + messageInit.getType());
            if (messageInit.getType() == IRCMessageType.COMMAND) {
                final IRCMessageCommand messagecmd = (IRCMessageCommand) messageInit;

                boolean registration = false;
                final IRCServerAuthentification auth = IRCServerAuthentification.getInstance();
                LOGGER.debug(id + " type : " + messagecmd.getCommandType());

                switch (messagecmd.getCommandType()) {
                case LOGIN:
                    LOGGER.debug(id + " Login Message receive");
                    final IRCMessageCommandLogin messageLogin = (IRCMessageCommandLogin) messagecmd;
                    user = auth.logUser(messageLogin.getLogin(), messageLogin.getPassword());
                    break;

                case REGISTER:
                    LOGGER.debug(id + " Register Message receive");
                    registration = true;
                    final IRCMessageCommandRegister messageRegister = (IRCMessageCommandRegister) messagecmd;
                    if (auth.addUser(messageRegister.getLogin(), messageRegister.getPassword(), messageRegister.getLogin())) {
                        user = auth.logUser(messageRegister.getLogin(), messageRegister.getPassword());
                    }
                    break;

                default:
                    break;
                }

                if (user != null) {
                    /**
                     * User accepted
                     */
                    LOGGER.debug(id + " User " + user.getNickName() + " is just loggin");
                    if (registration) {
                        messageInit = new IRCMessageNoticeRegister(user);
                    } else {
                        messageInit = new IRCMessageNoticeLogin(user);
                    }

                    try {
                        LOGGER.debug(id + " Send notice Login");
                        IOStreamUtils.sendMessage(outObject, messageInit);
                    } catch (final IOException e) {
                        LOGGER.warn(id + " Fail to send notice Login : " + e.getMessage());
                        throw new IRCServerException(e);
                    }

                    /**
                     * init env
                     */
                    LOGGER.debug(id + " Init env");
                    parent.newClientConnected(this);

                    /**
                     * Inform connected Users
                     */
                    messageInit = new IRCMessageNoticeContactInfo(user);
                    LOGGER.debug(id + " Send notice ContactInfo");
                    postMessageObjectInJMS(messageInit);

                    /**
                     * Send list connected users.
                     */
                    messageInit = new IRCMessageNoticeContactsList(parent.getAllUsers());

                    LOGGER.debug("\n\nConnected users : ");
                    for (final IRCUser u : parent.getAllUsers()) {
                        LOGGER.debug("\t" + u.getNickName());
                    }
                    LOGGER.debug("\n\nConnected users threads : ");
                    for (final ThreadGestionClientIRC t : parent.getClientConnecter()) {
                        LOGGER.debug("\t" + t.getUser().getNickName());
                    }
                    try {
                        LOGGER.debug(id + " Send list connected users.");
                        synchronized (inObject) {
                            synchronized (outObject) {
                                IOStreamUtils.sendMessage(outObject, messageInit);
                            }
                        }
                    } catch (final IOException e) {
                        LOGGER.warn(id + " Fail to send list connected users.");
                        e.printStackTrace();
                        throw new IRCServerException(e);
                    }

                    /**
                     * Send user's pictur to all others Users
                     */
                    if (user.hasPictur()) {
                        LOGGER.debug(id + " Send user's pictur to all others Users");
                        final IRCGestionPicture gestionPcture = IRCGestionPicture.getInstance();
                        final IRCMessageItemPicture picture = gestionPcture.getPictureOf(user.getId());
                        if (picture != null) {
                            postMessageObjectInJMS(picture);
                        }
                    }

                    /**
                     * Send all users pictur
                     */
                    LOGGER.debug(id + " Send all users pictur");
                    synchronized (inObject) {
                        synchronized (outObject) {
                            auth.sendUsersPicture(outObject);
                        }
                    }

                    isLogin = true;
                    isIdentify = true;
                } else {
                    /**
                     * Fail to login
                     */
                    if (registration) {
                        messageInit = new IRCMessageNoticeRegister(null);
                    } else {
                        messageInit = new IRCMessageNoticeLogin(null);
                    }
                    try {
                        synchronized (inObject) {
                            synchronized (outObject) {
                                IOStreamUtils.sendMessage(outObject, messageInit);
                            }
                        }
                    } catch (final IOException e) {
                        LOGGER.warn(id + " Fail to send the message : " + e.getMessage());
                        throw new IRCServerException(e);
                    }
                }
            }
        }
        LOGGER.debug(id + " End protocole.");
    }

    /**
     * Get the user connected to this Thread.
     * 
     * @return User connected to this Thread.
     */
    public IRCUser getUser() {
        return user;
    }

    /**
     * Test if the socket is already open. If socket is closed or a problem is
     * remark the thread is finalize.
     */
    private void socketAlive() {
        LOGGER.info(id + " Test if the socket have no problem.");
        if (clientSocket.isClosed() || clientSocket.isInputShutdown() || clientSocket.isOutputShutdown() || !clientSocket.isBound()
                || !clientSocket.isConnected()) {
            LOGGER.error(id + " A problem find on the Socket. Closing Connection");
            finalizeClass();
        }
    }
}
