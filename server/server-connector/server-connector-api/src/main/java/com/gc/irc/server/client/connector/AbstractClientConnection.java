package com.gc.irc.server.client.connector;

import java.util.concurrent.CountDownLatch;

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
import com.gc.irc.server.bridge.ServerBridgeException;
import com.gc.irc.server.bridge.ServerBridgeProducer;
import com.gc.irc.server.client.connector.management.UsersConnectionsManagement;
import com.gc.irc.server.core.exception.ServerException;
import com.gc.irc.server.core.user.management.UserManagement;
import com.gc.irc.server.core.user.management.UserManagementAware;
import com.gc.irc.server.core.user.management.UserPicturesManagement;
import com.gc.irc.server.model.UserInformations;
import com.gc.irc.server.service.AuthenticationService;
import com.gc.irc.server.service.UserPictureService;

/**
 * An abstract client connection
 *
 * @author gcauchis
 * @version 0.0.5
 */
public abstract class AbstractClientConnection extends AbstractRunnable implements
		ClientConnection, UserManagementAware {
	
	/** The nb thread. */
    private static int nbThread = 0;
    
    private static final int MAX_INVALID_MESSAGE = 10;
    
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
    
    /** The users connections management. */
    private UsersConnectionsManagement usersConnectionsManagement;
    
    /** The users pictures management. */
    private UserPicturesManagement userPicturesManagement;
    
    /** The user picture service. */
    private UserPictureService userPictureService;
	
	 /** The bridge producer. */
    private ServerBridgeProducer serverBridgeProducer;
    
    /** The id. */
    private final int id = getNbThread();
    
    /** The is identify. */
    private boolean isIdentify = false;
    
    /** The user. */
    private User user;
    
    private int cptInvalidMessage = 0;
    
    private final CountDownLatch initLatch = new CountDownLatch(1);
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Thread#run()
     */
    /** {@inheritDoc} */
    @Override
    public void run() {
        getLog().info(getId() + " Start Thread.");
        try {
			initLatch.await();
		} catch (InterruptedException e) {
			getLog().error(getId() + " fail to wait init", e);
		}
        getLog().info(getId() + " End init.");

        try {
            authenticateProtocol();
        } catch (final ServerException e) {
            getLog().warn(getId() + " Fail to autentificate the Client : ", e);
        }

        while (isIdentify) {
            /**
             * Wait for a Message
             */
            Message messageClient = receiveMessage();
            if (messageClient == null) {
                getLog().info(getId() + " Empty message. Closing Connection.");
                break;
            }

            /**
             * Post Message bridge
             */
            post(messageClient);

        }
        disconnectUser();
    }

	/*
     * (non-Javadoc)
     * 
     * @see com.gc.irc.server.thread.impl.IGestionClientBean#disconnectUser()
     */
    /** {@inheritDoc} */
    @Override
    public final void disconnectUser() {
        getLog().debug(getId() + " Finalize Thread");

        if (user != null) {
            /**
             * stop the while in run()
             */
            isIdentify = false;

            /**
             * Remove the client from server.
             */
            getLog().debug(getId() + " Delete Client " + user.getNickName() + " from list");
            usersConnectionsManagement.disconnectClient(this);

            /**
             * Inform all the other client.
             */
            getLog().debug(getId() + " Inform all other client that the client " + user.getNickName() + " is deconnected.");
            synchronized (user) {
                user.setUserStatus(UserStatus.OFFLINE);
                post(new MessageNoticeContactInfo(user.getCopy()));
                userManagement.disconnect(user.getId());
            }
        }
        disconnect();
       
    }

	/**
	 * Disconect the user properly.
	 */
	protected abstract void disconnect();

	/** {@inheritDoc} */
	@Override
	public final User getUser() {
		return user;
	}
	
    /**
     * Identification protocol.
     *
     * @throws com.gc.irc.server.core.exception.ServerException
     *             the iRC server exception
     */
    private void authenticateProtocol() throws ServerException {
        getLog().debug("Start Login protocol");
        /**
         * Send welcome message
         */
        getLog().debug("Send Welcome Message.");
        //TODO GCS: Search Welcome message
        send(new MessageNoticeServerMessage("Welcome"));

        boolean isLogin = false;
        while (!isLogin) {
            /**
             * Wait for login/Registration Message
             */
            getLog().debug("Wait for login/Registration Message");
            final Message message = receiveMessage();

            /**
             * Answer to the client
             */
            getLog().debug("message : " + message);
            if (message instanceof MessageCommand) {
                final MessageCommand messagecmd = (MessageCommand) message;

                boolean registration = false;

                if (messagecmd instanceof MessageCommandRegister) {
                    getLog().debug("Register Message receive");
                    registration = true;
                    final MessageCommandRegister messageRegister = (MessageCommandRegister) messagecmd;
                    if (authenticationService.userLoginExist(messageRegister.getLogin())) {
                    	getLog().warn("User {} already exist !", messageRegister.getLogin());
                    } else if (authenticationService.addNewUser(messageRegister.getLogin(), messageRegister.getPassword(), messageRegister.getLogin())) {
                    	user = checkLogin(messageRegister);
                    }
                } else if (messagecmd instanceof MessageCommandLogin) {
                    getLog().debug("Login Message receive");
                    final MessageCommandLogin messageLogin = (MessageCommandLogin) messagecmd;
                    user = checkLogin(messageLogin);
                }

                if (user != null) {
                    /**
                     * User accepted
                     */
                    getLog().debug("User " + user.getNickName() + " just loggin");
                    getLog().debug("Send notice Login");
                    if (registration) {
                    	send(new MessageNoticeRegister(user));
                    } else {
                    	send(new MessageNoticeLogin(user));
                    }

                    /**
                     * init env
                     */
                    getLog().debug("Init env");
                    usersConnectionsManagement.newClientConnected(this);

                    /**
                     * Inform connected Users
                     */
                    getLog().debug("Send notice ContactInfo");
                    post(new MessageNoticeContactInfo(user));

                    /**
                     * Send list connected users.
                     */
                    send(new MessageNoticeContactsList(userManagement.getAllUsers()));

                    /**
                     * Send user's pictur to all others Users
                     */
                    if (user.hasPictur()) {
                        getLog().debug("Send user's pictur to all others Users");
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
                    	send(new MessageNoticeRegister(null));
                    } else {
                    	send(new MessageNoticeLogin(null));
                    }
                }
            } else if (message != null){
            	getLog().info("Client must ask for authentication to continue. Message: " + message);
            	send(new MessageNoticeServerMessage("Please authenticate first (anonymous not yet implemented) !"));
            }
        }
        getLog().debug("End protocole.");
    }
    
    /**
     * <p>checkLogin.</p>
     *
     * @param messageLogin a {@link com.gc.irc.common.protocol.command.MessageCommandLogin} object.
     * @return a {@link com.gc.irc.common.entity.User} object.
     */
    private User checkLogin(final MessageCommandLogin messageLogin) {
    	User user = null;
		UserInformations userInfo = authenticationService.logUser(messageLogin.getLogin(), messageLogin.getPassword());
		if (userInfo != null) {
			if (!userManagement.isLogged(userInfo.getId())) {
				user = new User(userInfo.getId(), userInfo.getNickname(), userInfo.hasPictur());
				userManagement.newUserConnected(user);
			}
		}
		return user;
	}
    
	/**
	 * Send all users pictur
	 */
	private void sendAllUsersPicturs() {
		getLog().debug(getId() + " Send all users pictur");
		userPicturesManagement.sendUsersPictures(this);
	}

	/** {@inheritDoc} */
	@Override
	public final void post(Message message) {
		getLog().debug("Send a message");
        try {
            serverBridgeProducer.post(message);
        } catch (final ServerBridgeException e) {
            getLog().error("Fail to post message", e);
        }
	}
	
	/**
	 * An identifier for the connection.
	 *
	 * @return an identifier for the connection.
	 */
	public final String getId() {
		return id +":"+getClass().getSimpleName() + (user == null ? "" : ":"+user.getNickName());
	}
	
    /**
     * Check message.
     *
     * @param message
     *            the message
     */
    protected final void checkMessage(final Message message) {
        if (user != null && message != null && user.getId() != message.getFromId()) {
            throw new InvalidSenderException("Message from " + message.getFromId() + " instead of " + user.getId());
        }
        if (message == null) {
        	cptInvalidMessage++;
        	if (cptInvalidMessage <= MAX_INVALID_MESSAGE) {
        		disconnect();
        	}
        } else {
        	cptInvalidMessage = 0;
        }
    }
    
    protected final void endInit() {
    	initLatch.countDown();
    }
    
    /**
     * Sets the authentication service.
     *
     * @param authenticationService
     *            the new authentication service
     */
    @Autowired
    public void setAuthenticationService(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Sets the server bridge producer.
     *
     * @param serverBridgeProducer
     *            the server bridge producer
     */
    @Autowired
    public void setServerBridgeProducer(final ServerBridgeProducer serverBridgeProducer) {
        this.serverBridgeProducer = serverBridgeProducer;
    }

    /**
     * <p>Setter for the field <code>userPictureService</code>.</p>
     *
     * @param userPictureService
     *            the userPictureService to set
     */
    @Autowired
    public void setUserPictureService(final UserPictureService userPictureService) {
        this.userPictureService = userPictureService;
    }

    /**
     * Sets the users connections management.
     *
     * @param usersConnectionsManagement
     *            the new users connections management
     */
    @Autowired
    public void setUsersConnectionsManagement(final UsersConnectionsManagement usersConnectionsManagement) {
        this.usersConnectionsManagement = usersConnectionsManagement;
    }

    /**
     * Sets the users pictures management.
     *
     * @param userPicturesManagement a {@link com.gc.irc.server.core.user.management.UserPicturesManagement} object.
     */
    @Autowired
    public void setUserPicturesManagement(UserPicturesManagement userPicturesManagement) {
		this.userPicturesManagement = userPicturesManagement;
	}
    
	/** {@inheritDoc} */
	@Override
	@Autowired
	public void setUserManagement(UserManagement userManagement) {
		this.userManagement = userManagement;
	}

}
