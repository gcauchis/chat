package com.gc.irc.server.thread;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;

import com.gc.irc.common.AbstractRunnable;
import com.gc.irc.common.entity.User;
import com.gc.irc.common.entity.UserStatus;
import com.gc.irc.common.exception.security.InvalidSenderException;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.common.protocol.command.MessageCommand;
import com.gc.irc.common.protocol.command.MessageCommandLogin;
import com.gc.irc.common.protocol.command.MessageCommandRegister;
import com.gc.irc.common.protocol.item.MessageItemPicture;
import com.gc.irc.common.protocol.notice.MessageNoticeContactInfo;
import com.gc.irc.common.protocol.notice.MessageNoticeContactsList;
import com.gc.irc.common.protocol.notice.MessageNoticeLogin;
import com.gc.irc.common.protocol.notice.MessageNoticeRegister;
import com.gc.irc.common.protocol.notice.MessageNoticeServerMessage;
import com.gc.irc.common.utils.IOStreamUtils;
import com.gc.irc.server.bridge.ServerBridgeException;
import com.gc.irc.server.bridge.ServerBridgeProducer;
import com.gc.irc.server.client.connector.ClientConnection;
import com.gc.irc.server.core.ServerCore;
import com.gc.irc.server.core.user.management.UserManagement;
import com.gc.irc.server.core.user.management.UserManagementAware;
import com.gc.irc.server.core.user.management.UserPicturesManagement;
import com.gc.irc.server.core.user.management.UsersConnectionsManagement;
import com.gc.irc.server.exception.ServerException;
import com.gc.irc.server.model.UserInformations;
import com.gc.irc.server.service.AuthenticationService;
import com.gc.irc.server.service.UserPictureService;

/**
 * Communication between the Client and the server using an {@link java.io.ObjectOutputStream}.
 *
 * @author gcauchis
 * @version 0.0.4
 */
public class ObjectStreamClientConnection extends AbstractRunnable implements ClientConnection, UserManagementAware {

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
    private AuthenticationService authenticationService;
    
    /** The user management */
    private UserManagement userManagement;

    /** The client socket. */
    private final Socket clientSocket;

    /** The id. */
    private final int id = getNbThread();

    /** The in object. */
    private ObjectInputStream inObject;

    /** The is identify. */
    private boolean isIdentify = false;

    /** The out object. */
    private ObjectOutputStream outObject;

    /** The jms producer. */
    private ServerBridgeProducer serverBridgeProducer;

    /** The user. */
    private User user;

    /** The user picture service. */
    private UserPictureService userPictureService;

    /** The users connections management. */
    private UsersConnectionsManagement usersConnectionsManagement;
    
    /** The users pictures management. */
    private UserPicturesManagement userPicturesManagement;

    /**
     * Builder who initialize the TCP connection.
     *
     * @param clientSocket
     *            Client's Socket.
     */
    public ObjectStreamClientConnection(final Socket clientSocket) {
        getLog().info(id + " Initialisation du thread.");
        this.clientSocket = clientSocket;

        try {
            getLog().debug(id + " Create inObject");
            inObject = new ObjectInputStream(clientSocket.getInputStream());
            getLog().debug(id + " Create outObject");
            outObject = new ObjectOutputStream(clientSocket.getOutputStream());

        } catch (final IOException e) {
            getLog().warn(id + " Fail to open Client's Steams to " + clientSocket.getInetAddress() + " : ", e);
        }
        getLog().debug(id + " end init");
    }

    /**
     * Check message.
     * 
     * @param message
     *            the message
     */
    private void checkMessage(final Message message) {
        if (message != null && user.getId() != message.getFromId()) {
            throw new InvalidSenderException("Message from " + message.getFromId() + " instead of " + user.getId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.impl.IGestionClientBean#disconnectUser()
     */
    /** {@inheritDoc} */
    @Override
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
                post(new MessageNoticeContactInfo(user.getCopy()));
                userManagement.disconnect(user.getId());
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
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.impl.IGestionClientBean#getUser()
     */
    /** {@inheritDoc} */
    @Override
    public User getUser() {
        return user;
    }

    /**
     * Send a message in the JMS Queue.
     * 
     * @param objectMessage
     *            the object message
     */
    private void post(final Message objectMessage) {
        getLog().debug("Send a message");
        try {
            serverBridgeProducer.post(objectMessage);
        } catch (final ServerBridgeException e) {
            getLog().error("Fail to post message", e);
        }
    }

    /**
     * Identification protocol.
     * 
     * @throws ServerException
     *             the iRC server exception
     */
    private void authenticateProtocol() throws ServerException {
        getLog().debug("Start Login protocol");
        Message messageInit = new MessageNoticeServerMessage(ServerCore.getWelcomeMessage());
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
            getLog().warn(id + " Fail to send Welcome message : ", e);
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
                getLog().warn(id + " Fail to receive the Login/Registration Message : ", e);
                throw new ServerException(e);
            } catch (final IOException e) {
                getLog().warn(id + " Fail to receive the Login/Registration Message : ", e);
                throw new ServerException(e);
            }

            /**
             * Answer to the client
             */
            getLog().debug(id + " message : " + messageInit);
            if (messageInit instanceof MessageCommand) {
                final MessageCommand messagecmd = (MessageCommand) messageInit;

                boolean registration = false;

                if (messagecmd instanceof MessageCommandRegister) {
                    getLog().debug(id + " Register Message receive");
                    registration = true;
                    final MessageCommandRegister messageRegister = (MessageCommandRegister) messagecmd;
                    if (authenticationService.userLoginExist(messageRegister.getLogin())) {
                    	getLog().warn("User {} already exist !", messageRegister.getLogin());
                    } else if (authenticationService.addNewUser(messageRegister.getLogin(), messageRegister.getPassword(), messageRegister.getLogin())) {
                    	checkLogin(messageRegister);
                    }
                } else if (messagecmd instanceof MessageCommandLogin) {
                    getLog().debug(id + " Login Message receive");
                    final MessageCommandLogin messageLogin = (MessageCommandLogin) messagecmd;
                    checkLogin(messageLogin);
                }

                if (user != null) {
                    /**
                     * User accepted
                     */
                    getLog().debug(id + " User " + user.getNickName() + " just loggin");
                    if (registration) {
                        messageInit = new MessageNoticeRegister(user);
                    } else {
                        messageInit = new MessageNoticeLogin(user);
                    }

                    try {
                        getLog().debug(id + " Send notice Login");
                        IOStreamUtils.sendMessage(outObject, messageInit);
                    } catch (final IOException e) {
                        getLog().warn(id + " Fail to send notice Login : ", e);
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
                    messageInit = new MessageNoticeContactInfo(user);
                    getLog().debug(id + " Send notice ContactInfo");
                    post(messageInit);

                    /**
                     * Send list connected users.
                     */
                    messageInit = new MessageNoticeContactsList(userManagement.getAllUsers());

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
                        final MessageItemPicture picture = userPictureService.getPictureOf(user.getId());
                        if (picture != null) {
                            post(picture);
                        }
                    }

                    sendAllUsersPicturs();

                    isLogin = true;
                    isIdentify = true;
                } else {
                    /**
                     * Fail to login
                     */
                    if (registration) {
                        messageInit = new MessageNoticeRegister(null);
                    } else {
                        messageInit = new MessageNoticeLogin(null);
                    }
                    try {
                        synchronized (inObject) {
                            synchronized (outObject) {
                                IOStreamUtils.sendMessage(outObject, messageInit);
                            }
                        }
                    } catch (final IOException e) {
                        getLog().warn(id + " Fail to send the message : ", e);
                        throw new ServerException(e);
                    }
                }
            }
        }
        getLog().debug(id + " End protocole.");
    }

	private void sendAllUsersPicturs() {
		/**
		 * Send all users pictur
		 */
		getLog().debug(id + " Send all users pictur");
		synchronized (inObject) {
		    synchronized (outObject) {
		        userPicturesManagement.sendUsersPictures(outObject);
		    }
		}
	}

	private void checkLogin(final MessageCommandLogin messageLogin) {
		UserInformations userInfo = authenticationService.logUser(messageLogin.getLogin(), messageLogin.getPassword());
		if (userInfo != null) {
			if (!userManagement.isLogged(userInfo.getId())) {
				user = new User(userInfo.getId(), userInfo.getNickname(), userInfo.hasPictur());
				userManagement.newUserConnected(user);
			}
		}
	}

    /**
     * Wait and Receive a message send by the client.
     * 
     * @return Message received.
     */
    private Message receiveMessage() {
        Message message = null;
        try {
            getLog().debug(id + " Wait for a message in the socket.");
            message = IOStreamUtils.receiveMessage(inObject);
            checkMessage(message);
        } catch (final ClassNotFoundException e) {
            getLog().warn(id + " Fail to receive a message : ", e);
            socketAlive();
        } catch (final EOFException e) {
            getLog().warn("{} Fail to receive a message {}", id, e.getMessage());
            socketAlive();
        } catch (final IOException e) {
            getLog().warn(id + " Fail to receive a message : ", e);
            socketAlive();
        }
        return message;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    /** {@inheritDoc} */
    @Override
    public void run() {
        getLog().info(id + " Start Thread.");

        try {
            authenticateProtocol();
        } catch (final ServerException e) {
            getLog().warn(id + " Fail to autentificate the Client : ", e);
        }

        Message messageClient;
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
            post(messageClient);

        }
        disconnectUser();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.gc.irc.server.thread.impl.IGestionClientBean#envoyerMessageObjetSocket
     * (com.gc.irc.common.protocol.IRCMessage)
     */
    /** {@inheritDoc} */
    @Override
    public void send(final Message message) {
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
    public void setAuthenticationService(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Sets the server bridge producer.
     *
     * @param serverBridgeProducer
     *            the server bridge producer
     */
    public void setServerBridgeProducer(final ServerBridgeProducer serverBridgeProducer) {
        this.serverBridgeProducer = serverBridgeProducer;
    }

    /**
     * <p>Setter for the field <code>userPictureService</code>.</p>
     *
     * @param userPictureService
     *            the userPictureService to set
     */
    public void setUserPictureService(final UserPictureService userPictureService) {
        this.userPictureService = userPictureService;
    }

    /**
     * Sets the users connections management.
     *
     * @param usersConnectionsManagement
     *            the new users connections management
     */
    public void setUsersConnectionsManagement(final UsersConnectionsManagement usersConnectionsManagement) {
        this.usersConnectionsManagement = usersConnectionsManagement;
    }

    /**
     * Sets the users pictures management.
     *
     * @param userPicturesManagement a {@link com.gc.irc.server.core.user.management.UserPicturesManagement} object.
     */
    public void setUserPicturesManagement(UserPicturesManagement userPicturesManagement) {
		this.userPicturesManagement = userPicturesManagement;
	}

	/**
     * Test if the socket is already open. If socket is closed or a problem is
     * remark the thread is finalize.
     */
    private void socketAlive() {
        getLog().info(id + " Test if the socket have no problem.");
        if (clientSocket.isClosed() || clientSocket.isInputShutdown() || clientSocket.isOutputShutdown() || !clientSocket.isBound()
                || !clientSocket.isConnected()) {
            getLog().error(id + " A problem find on the Socket. Closing Connection");
            disconnectUser();
        }
    }

	/** {@inheritDoc} */
	@Override
	@Autowired
	public void setUserManagement(UserManagement userManagement) {
		this.userManagement = userManagement;
	}
}
