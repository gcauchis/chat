package com.gc.irc.server.thread.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.gc.irc.common.abs.AbstractRunnable;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.entity.UserStatus;
import com.gc.irc.common.exception.security.InvalidSenderException;
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
import com.gc.irc.server.auth.api.IAuthenticationService;
import com.gc.irc.server.core.ServerCore;
import com.gc.irc.server.core.user.management.api.IUsersConnectionsManagement;
import com.gc.irc.server.exception.ServerException;
import com.gc.irc.server.jms.api.IJMSProducer;
import com.gc.irc.server.persistance.UserPictureManagement;
import com.gc.irc.server.thread.api.IGestionClientBean;

/**
 * Communication interface between the Client and the server.
 * 
 * @author gcauchis
 * 
 */
public class GestionClientBean extends AbstractRunnable implements IGestionClientBean {

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

    /** The authentication service. */
    private IAuthenticationService authenticationService;

    /** The client socket. */
    private final Socket clientSocket;

    /** The id. */
    private final int id = getNbThread();

    /** The in object. */
    private ObjectInputStream inObject;

    /** The is identify. */
    private boolean isIdentify = false;

    /** The jms producer. */
    private IJMSProducer jmsProducer;

    /** The out object. */
    private ObjectOutputStream outObject;

    /** The user. */
    private IRCUser user;

    /** The users connections management. */
    private IUsersConnectionsManagement usersConnectionsManagement;

    /**
     * Builder who initialize the TCP connection.
     * 
     * @param clientSocket
     *            Client's Socket.
     */
    public GestionClientBean(final Socket clientSocket) {
        getLog().info(id + " Initialisation du thread.");
        this.clientSocket = clientSocket;

        try {
            getLog().debug(id + " Create inObject");
            inObject = new ObjectInputStream(clientSocket.getInputStream());
            getLog().debug(id + " Create outObject");
            outObject = new ObjectOutputStream(clientSocket.getOutputStream());

        } catch (final IOException e) {
            getLog().warn(id + " Fail to open Client's Steams to " + clientSocket.getInetAddress() + " : " + e.getMessage());
        }
        getLog().debug(id + " end init");
    }

    /**
     * Check message.
     * 
     * @param message
     *            the message
     */
    private void checkMessage(final IRCMessage message) {
        if (message != null && user.getId() != message.getFromId()) {
            throw new InvalidSenderException("Message from " + message.getFromId() + " instead of " + user.getId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.impl.IGestionClientBean#disconnectUser()
     */
    public void disconnectUser() {
        getLog().debug(id + " Finalize Thread");

        if (user != null) {
            /**
             * stop the while in run()
             */
            isIdentify = false;

            /**
             * Remove the client from server.
             */
            getLog().debug(id + " Delete Client " + user.getNickName() + " from list");
            usersConnectionsManagement.disconnectClient(this);

            /**
             * Inform all the other client.
             */
            getLog().debug(id + " Inform all other client that the client " + user.getNickName() + " is deconnected.");
            synchronized (user) {
                user.setUserStatus(UserStatus.OFFLINE);
                postInJMS(new IRCMessageNoticeContactInfo(user.getCopy()));
                authenticationService.getUser(user.getId()).diconnected();
            }
        }

        /**
         * Closing Socket.
         */
        try {
            getLog().info(id + " Closing Client's connection " + clientSocket.getInetAddress() + ".");
            if (!clientSocket.isInputShutdown()) {
                getLog().debug(id + " Closing inObject");
                inObject.close();
            }
            if (!clientSocket.isOutputShutdown()) {
                getLog().debug(id + " Closing outObject");
                outObject.close();
            }
            if (!clientSocket.isClosed()) {
                getLog().debug(id + " Closing clientSocket");
                clientSocket.close();
            }
        } catch (final IOException e) {
            getLog().warn(id + " Fail to close Client's connection " + clientSocket.getInetAddress() + " : " + e.getMessage());
        }
        try {
            super.finalize();
        } catch (final Throwable e) {
            getLog().warn(id + " Fail to finalize class : " + e.getMessage());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.impl.IGestionClientBean#getIdThread()
     */
    public int getIdThread() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.impl.IGestionClientBean#getUser()
     */
    public IRCUser getUser() {
        return user;
    }

    /**
     * Send a message in the JMS Queue.
     * 
     * @param objectMessage
     *            the object message
     */
    private void postInJMS(final IRCMessage objectMessage) {
        getLog().debug(id + " Send a message in JMS Queue.");
        jmsProducer.postInJMS(objectMessage);
    }

    /**
     * Identification protocol.
     * 
     * @throws ServerException
     *             the iRC server exception
     */
    private void protocoleDAuthentification() throws ServerException {
        getLog().debug("Start Login protocol");
        IRCMessage messageInit = new IRCMessageNoticeServerMessage(ServerCore.getMessageAcceuil());
        /**
         * Send welcome message
         */
        try {
            getLog().debug("Send Welcome Message.");
            synchronized (inObject) {
                synchronized (outObject) {
                    IOStreamUtils.sendMessage(outObject, messageInit);
                }
            }
        } catch (final IOException e) {
            getLog().warn(id + " Fail to send Welcome message : " + e.getMessage());
            throw new ServerException(e);
        }

        boolean isLogin = false;
        while (!isLogin) {
            /**
             * Wait for login/Registration Message
             */
            try {
                getLog().debug(id + " Wait for login/Registration Message");
                messageInit = IOStreamUtils.receiveMessage(inObject);
            } catch (final ClassNotFoundException e) {
                getLog().warn(id + " Fail to receive the Login/Registration Message : " + e.getMessage());
                throw new ServerException(e);
            } catch (final IOException e) {
                getLog().warn(id + " Fail to receive the Login/Registration Message : " + e.getMessage());
                throw new ServerException(e);
            }

            /**
             * Answer to the client
             */
            getLog().debug(id + " Type : " + messageInit.getType());
            if (messageInit.getType() == IRCMessageType.COMMAND) {
                final IRCMessageCommand messagecmd = (IRCMessageCommand) messageInit;

                boolean registration = false;
                getLog().debug(id + " type : " + messagecmd.getCommandType());

                switch (messagecmd.getCommandType()) {
                case LOGIN:
                    getLog().debug(id + " Login Message receive");
                    final IRCMessageCommandLogin messageLogin = (IRCMessageCommandLogin) messagecmd;
                    user = authenticationService.logUser(messageLogin.getLogin(), messageLogin.getPassword());
                    break;

                case REGISTER:
                    getLog().debug(id + " Register Message receive");
                    registration = true;
                    final IRCMessageCommandRegister messageRegister = (IRCMessageCommandRegister) messagecmd;
                    if (authenticationService.addUser(messageRegister.getLogin(), messageRegister.getPassword(), messageRegister.getLogin())) {
                        user = authenticationService.logUser(messageRegister.getLogin(), messageRegister.getPassword());
                    }
                    break;

                default:
                    break;
                }

                if (user != null) {
                    /**
                     * User accepted
                     */
                    getLog().debug(id + " User " + user.getNickName() + " is just loggin");
                    if (registration) {
                        messageInit = new IRCMessageNoticeRegister(user);
                    } else {
                        messageInit = new IRCMessageNoticeLogin(user);
                    }

                    try {
                        getLog().debug(id + " Send notice Login");
                        IOStreamUtils.sendMessage(outObject, messageInit);
                    } catch (final IOException e) {
                        getLog().warn(id + " Fail to send notice Login : " + e.getMessage());
                        throw new ServerException(e);
                    }

                    /**
                     * init env
                     */
                    getLog().debug(id + " Init env");
                    usersConnectionsManagement.newClientConnected(this);

                    /**
                     * Inform connected Users
                     */
                    messageInit = new IRCMessageNoticeContactInfo(user);
                    getLog().debug(id + " Send notice ContactInfo");
                    postInJMS(messageInit);

                    /**
                     * Send list connected users.
                     */
                    messageInit = new IRCMessageNoticeContactsList(usersConnectionsManagement.getAllUsers());

                    try {
                        getLog().debug(id + " Send list connected users.");
                        synchronized (inObject) {
                            synchronized (outObject) {
                                IOStreamUtils.sendMessage(outObject, messageInit);
                            }
                        }
                    } catch (final IOException e) {
                        getLog().warn(id + " Fail to send list connected users.", e);
                        throw new ServerException(e);
                    }

                    /**
                     * Send user's pictur to all others Users
                     */
                    if (user.hasPictur()) {
                        getLog().debug(id + " Send user's pictur to all others Users");
                        final UserPictureManagement gestionPcture = UserPictureManagement.getInstance();
                        final IRCMessageItemPicture picture = gestionPcture.getPictureOf(user.getId());
                        if (picture != null) {
                            postInJMS(picture);
                        }
                    }

                    /**
                     * Send all users pictur
                     */
                    getLog().debug(id + " Send all users pictur");
                    synchronized (inObject) {
                        synchronized (outObject) {
                            authenticationService.sendUsersPicture(outObject);
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
                        getLog().warn(id + " Fail to send the message : " + e.getMessage());
                        throw new ServerException(e);
                    }
                }
            }
        }
        getLog().debug(id + " End protocole.");
    }

    /**
     * Wait and Receive a message send by the client.
     * 
     * @return Message received.
     */
    private IRCMessage receiveMessage() {
        IRCMessage message = null;
        try {
            getLog().debug(id + " Wait for a message in the socket.");
            message = IOStreamUtils.receiveMessage(inObject);
            checkMessage(message);
        } catch (final ClassNotFoundException e) {
            getLog().warn(id + " Fail to receive a message : " + e.getMessage());
            socketAlive();
        } catch (final IOException e) {
            getLog().warn(id + " Fail to receive a message : " + e.getMessage());
            socketAlive();
        }
        return message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run() {
        getLog().info(id + " Start Thread.");

        try {
            protocoleDAuthentification();
        } catch (final ServerException e) {
            getLog().warn(id + " Fail to autentificate the Client : " + e.getMessage());
        }

        IRCMessage messageClient;
        while (isIdentify) {
            /**
             * Wait for a Message
             */
            messageClient = null;
            messageClient = receiveMessage();
            if (messageClient == null) {
                getLog().info(id + " Empty message. Closing Connection.");
                break;
            }

            /**
             * Post Message in JMS
             */
            postInJMS(messageClient);

        }
        disconnectUser();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.impl.IGestionClientBean#envoyerMessageObjetSocket(com.gc.irc.common.protocol.IRCMessage)
     */
    public void sendMessageObjetInSocket(final IRCMessage message) {
        try {
            /**
             * Synchronize the socket.
             */
            if (!clientSocket.isOutputShutdown()) {
                synchronized (inObject) {
                    synchronized (outObject) {
                        getLog().debug(id + " Send message to " + user.getNickName());
                        if (clientSocket.isConnected()) {
                            IOStreamUtils.sendMessage(outObject, message);
                        } else {
                            getLog().warn(id + " Socket not connected !");
                        }
                    }
                }
            } else {
                getLog().warn(id + " Fail to send message. Finalize because output is shutdown.");
                disconnectUser();
            }
        } catch (final IOException e) {
            getLog().warn(id + " Fail to send the message : " + e.getMessage());
            socketAlive();
        }
    }

    /**
     * Sets the authentication service.
     * 
     * @param authenticationService
     *            the new authentication service
     */
    public void setAuthenticationService(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Sets the jms producer.
     * 
     * @param jmsProducer
     *            the new jms producer
     */
    public void setJmsProducer(IJMSProducer jmsProducer) {
        this.jmsProducer = jmsProducer;
    }

    /**
     * Sets the users connections management.
     * 
     * @param usersConnectionsManagement
     *            the new users connections management
     */
    public void setUsersConnectionsManagement(IUsersConnectionsManagement usersConnectionsManagement) {
        this.usersConnectionsManagement = usersConnectionsManagement;
    }

    /**
     * Test if the socket is already open. If socket is closed or a problem is remark the thread is finalize.
     */
    private void socketAlive() {
        getLog().info(id + " Test if the socket have no problem.");
        if (clientSocket.isClosed() || clientSocket.isInputShutdown() || clientSocket.isOutputShutdown() || !clientSocket.isBound()
                || !clientSocket.isConnected()) {
            getLog().error(id + " A problem find on the Socket. Closing Connection");
            disconnectUser();
        }
    }
}
