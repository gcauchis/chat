package com.gc.irc.server.api;

import org.apache.activemq.broker.BrokerService;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.gc.irc.common.connector.ConnectionHandler;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.message.api.IIRCMessageSender;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.command.IRCMessageCommand;
import com.gc.irc.common.protocol.command.IRCMessageCommandLogin;
import com.gc.irc.common.protocol.command.IRCMessageCommandRegister;
import com.gc.irc.server.ServerStarter;
import com.gc.irc.server.test.handler.AbstractMessageHandlerTester;
import com.gc.irc.server.test.handler.LoginMessageHandler;

/**
 * The Class AbstractServerTest.
 */
public abstract class AbstractServerTest {
	/** The jms broker. */
	private static BrokerService jmsBroker;

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
		starterThread.interrupt();
	}

	/**
	 * Inits the.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	@BeforeClass
	public static synchronized void lauchServer() throws InterruptedException {
		if (jmsBroker == null) {
			jmsBroker = new BrokerService();
			try {
				jmsBroker.addConnector("tcp://localhost:61616");
			} catch (final Exception e) {
				System.out.println("Fail to initialize jms broker: " + e);
				e.printStackTrace();
			}
			jmsBroker.setPersistent(false);
			try {
				jmsBroker.start();
			} catch (final Exception e) {
				System.out.println("Fail to start jms broker: " + e);
				e.printStackTrace();
			}
		}
		if (starter == null) {
			starter = new ServerStarter();
			starterThread = new Thread(starter);
			starterThread.start();
			System.out.println("Wait for server to be up");
			while (!starter.isInitialized()) {
				Thread.sleep(500);
			}
			System.out.println("Server up");
		}
	}

	/**
	 * Gets the connection to server.
	 * 
	 * @return the connection to server
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	protected ConnectionHandler getConnectionToServer()
			throws InterruptedException {
		final ConnectionHandler connectionHandler = new ConnectionHandler(null,
				SERVER_PORT);
		new Thread(connectionHandler).start();

		System.out.println("Wait for connectionHandler to be up");
		while (!connectionHandler.isInitialized()) {
			Thread.sleep(500);
		}
		System.out.println("connectionHandler up");
		return connectionHandler;
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
	protected IRCUser login(final ConnectionHandler connectionThread,
			final String login, final String password)
			throws InterruptedException {
		return sendCommandMessageForLogin(connectionThread,
				new IRCMessageCommandLogin(login, password));
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
	protected IRCUser loginAndRegister(
			final ConnectionHandler connectionThread, final String login,
			final String password) throws InterruptedException {
		IRCUser user = login(connectionThread, login, password);
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
	protected IRCUser register(final ConnectionHandler connectionThread,
			final String login, final String password)
			throws InterruptedException {
		return sendCommandMessageForLogin(connectionThread,
				new IRCMessageCommandRegister(login, password));
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
	private IRCUser sendCommandMessageForLogin(
			final ConnectionHandler connectionThread,
			final IRCMessageCommand messageCommand) throws InterruptedException {
		final LoginMessageHandler messageHandler = new LoginMessageHandler();
		connectionThread.setMessageHandler(messageHandler);
		messageHandler.reset();
		sendMessage(connectionThread, messageCommand);
		waitForMessageInHandler(messageHandler);
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
	protected void sendMessage(final IIRCMessageSender messageSender,
			final IRCMessage message) {
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
	protected void waitForMessageInHandler(
			final AbstractMessageHandlerTester messageHandler)
			throws InterruptedException {
		while (!messageHandler.isMessageRecieved()) {
			Thread.sleep(300);
		}
	}
}
