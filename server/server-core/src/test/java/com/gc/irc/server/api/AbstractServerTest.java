package com.gc.irc.server.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;

import com.gc.irc.common.api.ILoggable;
import com.gc.irc.common.connector.ConnectionHandler;
import com.gc.irc.common.entity.User;
import com.gc.irc.common.message.api.IMessageSender;
import com.gc.irc.common.protocol.Message;
import com.gc.irc.common.protocol.command.MessageCommand;
import com.gc.irc.common.protocol.command.MessageCommandLogin;
import com.gc.irc.common.protocol.command.MessageCommandRegister;
import com.gc.irc.common.protocol.notice.MessageNoticeServerMessage;
import com.gc.irc.common.utils.LoggerUtils;
import com.gc.irc.server.ServerStarter;
import com.gc.irc.server.test.handler.IMessageHandlerTester;
import com.gc.irc.server.test.handler.LoginMessageHandler;
import com.gc.irc.server.test.handler.SimpleMessageHandler;
import com.gc.irc.server.test.handler.message.impl.MessageCommandTestDeleteUserHandler;
import com.gc.irc.server.test.protocol.command.MessageCommandTestDeleteUser;
import com.gc.irc.server.test.utils.entity.UserContextEntity;

/**
 * The Class AbstractServerTest.
 */
public abstract class AbstractServerTest /* extends UnitilsJUnit4 */implements ILoggable {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerUtils.getLogger(AbstractServerTest.class);

    /** The Constant SERVER_PORT. */
    protected static final int SERVER_PORT = 1973;

    /** The starter. */
    private static ServerStarter starter;

    /** The starter thread. */
    private static Thread starterThread;

    /**
     * Clean all.
     */
    @AfterClass
    public static void cleanAll() {
        LOGGER.info("Close env");
        if (starterThread != null) {
            starterThread.interrupt();
        }
    }

    /**
     * Inits the.
     * 
     * @throws InterruptedException
     *             the interrupted exception
     */
    @BeforeClass
    public static synchronized void lauchServer() throws InterruptedException {
        LOGGER.info("Start Server");
        if (starter == null) {
            starter = new ServerStarter();
            starterThread = new Thread(starter);
            starterThread.start();
            LOGGER.info("Wait for server to be up");
            while (!starter.isInitialized()) {
                Thread.sleep(500);
            }
            LOGGER.info("Server up");
        }
    }

    /** The impl logger. */
    private Logger implLogger;

    /**
     * Gets the connected user.
     * 
     * @return the connected user
     * @throws InterruptedException
     */
    protected final UserContextEntity getConnectedUser(final String login, final String password) throws InterruptedException {
        final ConnectionHandler connection = getConnectionToServer();
        assertNotNull(connection);
        final User user = loginAndRegister(connection, login, password);
        assertNotNull(user);
        final UserContextEntity context = new UserContextEntity(user, connection);
        context.cleanMessageHandler();
        return context;
    }

    /**
     * Gets the connection to server.
     * 
     * @return the connection to server
     * @throws InterruptedException
     *             the interrupted exception
     */
    protected final ConnectionHandler getConnectionToServer() throws InterruptedException {
        final ConnectionHandler connectionHandler = new ConnectionHandler(null, SERVER_PORT);
        final IMessageHandlerTester messageHandler = new SimpleMessageHandler();
        connectionHandler.setMessageHandler(messageHandler);
        new Thread(connectionHandler).start();

        getLog().info("Wait for connectionHandler to be up");
        while (!connectionHandler.isInitialized()) {
            Thread.sleep(500);
        }

        getLog().info("connectionHandler up");
        waitForMessageInHandler(messageHandler);
        assertTrue("" + messageHandler.getLastReceivedMessage(), messageHandler.getLastReceivedMessage() instanceof MessageNoticeServerMessage);

        return connectionHandler;
    }

    /**
     * Gets the log.
     * 
     * @return the log
     */
    @Override
    public Logger getLog() {
        if (implLogger == null) {
            implLogger = LoggerUtils.getLogger(getClass());
        }
        return implLogger;
    }

    /**
     * Login.
     * 
     * @param connectionThread
     *            the connection thread
     * @param login
     *            the login
     * @param password
     *            the password
     * @return the iRC user
     * @throws InterruptedException
     *             the interrupted exception
     */
    protected final User login(final ConnectionHandler connectionThread, final String login, final String password) throws InterruptedException {
        return sendCommandMessageForLogin(connectionThread, new MessageCommandLogin(login, password));
    }

    /**
     * Login.
     * 
     * @param connectionThread
     *            the connection thread
     * @param login
     *            the login
     * @param password
     *            the password
     * @throws InterruptedException
     *             the interrupted exception
     */
    protected final User loginAndRegister(final ConnectionHandler connectionThread, final String login, final String password) throws InterruptedException {
        User user = login(connectionThread, login, password);
        if (user == null) {
            user = register(connectionThread, login, password);
        }
        return user;
    }

    /**
     * Register.
     * 
     * @param connectionThread
     *            the connection thread
     * @param login
     *            the login
     * @param password
     *            the password
     * @return the iRC user
     * @throws InterruptedException
     *             the interrupted exception
     */
    protected final User register(final ConnectionHandler connectionThread, final String login, final String password) throws InterruptedException {
        return sendCommandMessageForLogin(connectionThread, new MessageCommandRegister(login, password));
    }

    /**
     * Send command message for login.
     * 
     * @param connectionThread
     *            the connection thread
     * @param messageCommand
     *            the message command
     * @return the iRC user
     * @throws InterruptedException
     *             the interrupted exception
     */
    private User sendCommandMessageForLogin(final ConnectionHandler connectionThread, final MessageCommand messageCommand) throws InterruptedException {
        final LoginMessageHandler messageHandler = new LoginMessageHandler();
        connectionThread.setMessageHandler(messageHandler);
        messageHandler.reset();
        sendAndWaitForMessageInHandler(messageHandler, connectionThread, messageCommand);
        connectionThread.setMessageHandler(null);
        return messageHandler.getLogin();
    }

    /**
     * Send message.
     * 
     * @param messageSender
     *            the message sender
     * @param message
     *            the message
     */
    protected final void sendMessage(final IMessageSender messageSender, final Message message) {
        messageSender.send(message);
    }

    /**
     * Wait for message in handler.
     * 
     * @param messageHandler
     *            the message handler
     * @throws InterruptedException
     *             the interrupted exception
     */
    protected final void waitForMessageInHandler(final IMessageHandlerTester messageHandler) throws InterruptedException {
        while (!messageHandler.isMessageRecieved()) {
            Thread.sleep(300);
        }
    }
    
    /**
     * 
     * @param messageHandler
     * @param messageSender
     * @param message
     * @return
     * @throws InterruptedException
     */
    protected final Message sendAndWaitForMessageInHandler(final IMessageHandlerTester messageHandler, final IMessageSender messageSender, final Message message) throws InterruptedException {
    	sendMessage(messageSender, message);
        waitForMessageInHandler(messageHandler);
        return messageHandler.getLastReceivedMessage();
    }
    
    /**
     * 
     * @param user
     * @throws InterruptedException 
     */
    protected final void removeTestUser(UserContextEntity contextEntity) throws InterruptedException {
    	final IMessageHandlerTester messageHandler = new SimpleMessageHandler();
    	contextEntity.setMessageHandler(messageHandler);
    	Message message = sendAndWaitForMessageInHandler(contextEntity.getMessageHandler(), contextEntity.getConnectionUser(), new MessageCommandTestDeleteUser(contextEntity.getUser().getId()));
    	assertNotNull(message);
    	assertTrue(message instanceof MessageNoticeServerMessage);
    	assertEquals("DELETE"+contextEntity.getUser().getId(), ((MessageNoticeServerMessage)message).getMessage());
    }
    
    /**
     * 
     * @param user
     * @param connectionUser
     * @throws InterruptedException
     */
    protected final void removeTestUser(final User user, final ConnectionHandler connectionUser) throws InterruptedException {
    	removeTestUser(new UserContextEntity(user, connectionUser));
    }
    
    /**
     * 
     * @param contextUser
     * @throws InterruptedException
     */
    protected final void finalizeTestContext(UserContextEntity contextUser) throws InterruptedException {
		removeTestUser(contextUser);
		contextUser.cleanMessageHandler();
        contextUser.disconnect();
	}

}
