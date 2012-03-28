package com.gc.irc.server.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.gc.irc.common.api.IIRCMessageSender;
import com.gc.irc.common.connector.ConnectionThread;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.command.IRCMessageCommandLogin;
import com.gc.irc.common.protocol.command.IRCMessageCommandRegister;
import com.gc.irc.server.ServerStarter;
import com.gc.irc.server.test.handler.LoginMessageHandler;

/**
 * The Class AbstractServerIT.
 */
public abstract class AbstractServerIT {
	/** The Constant SERVER_PORT. */
	protected static final int SERVER_PORT = 1973;

	/** The starter thread. */
	private static Thread starterThread;

	/** The starter. */
	private static ServerStarter starter;
	
	/**
	 * Inits the.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	@BeforeClass
	public static void init() throws InterruptedException {
		starter = new ServerStarter();
		starterThread = new Thread(starter);
		starterThread.start();
		System.out.println("Wait for server to be up");
		while (!starter.isInitialized()) {
			Thread.sleep(500);
		}
		System.out.println("Server up");
	}
	
	/**
	 * Clean all.
	 */
	@AfterClass
	public static void cleanAll() {
		starterThread.interrupt();
	}
	
	/**
	 * Gets the connection to server.
	 *
	 * @return the connection to server
	 * @throws InterruptedException the interrupted exception
	 */
	protected ConnectionThread getConnectionToServer() throws InterruptedException {
		ConnectionThread connectionThread = new ConnectionThread(null, SERVER_PORT);
		connectionThread.start();
		System.out.println("Wait for connectionThread to be up");
		while (!connectionThread.isInitialized()) {
			Thread.sleep(500);
		}
		System.out.println("ConnectionThread up");
		return connectionThread;
	}
	
	/**
	 * Login.
	 *
	 * @param connectionThread the connection thread
	 * @param login the login
	 * @param password the password
	 * @throws InterruptedException the interrupted exception
	 */
	protected IRCUser login(ConnectionThread connectionThread, String login, String password) throws InterruptedException {
		final LoginMessageHandler messageHandler = new LoginMessageHandler();
		connectionThread.setMessageHandler(messageHandler);
		messageHandler.reset();
		final IRCMessage loginMessage = new IRCMessageCommandLogin(login, password);
		sendMessage(connectionThread, loginMessage);
		while (!messageHandler.isMessageRecieved()) {
			Thread.sleep(300);
		}
		if (!messageHandler.isLoginValidated()) {
			messageHandler.reset();
			final IRCMessage register = new IRCMessageCommandRegister(login,password);
			sendMessage(connectionThread, register);
			while (!messageHandler.isMessageRecieved()) {
				Thread.sleep(300);
			}
		}
		assertTrue(messageHandler.isLoginValidated());
		assertNotNull(messageHandler.getLogin());
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
}
