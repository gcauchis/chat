package com.gc.irc.server.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.gc.irc.common.api.IClientMessageLine;
import com.gc.irc.common.api.IIRCMessageSender;
import com.gc.irc.common.api.impl.BasicClientMessage;
import com.gc.irc.common.connector.ConnectionThread;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.chat.IRCMessageChat;
import com.gc.irc.common.protocol.command.IRCMessageCommandLogin;
import com.gc.irc.common.protocol.command.IRCMessageCommandRegister;
import com.gc.irc.server.ServerStarter;
import com.gc.irc.server.test.handler.LoginMessageHandler;

/**
 * The Class ServerTest.
 */
public class ServerTest {

	/** The Constant SERVER_PORT. */
	private static final int SERVER_PORT = 1973;

	/** The starter thread. */
	private static Thread starterThread;

	/** The starter. */
	private static ServerStarter starter;

	/** The connection thread. */
	private ConnectionThread connectionThread;

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
	 * Prepare.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	@Before
	public void prepare() throws InterruptedException {
		connectionThread = new ConnectionThread(null, SERVER_PORT);
		connectionThread.start();
		System.out.println("Wait for connectionThread to be up");
		while (!connectionThread.isInitialized()) {
			Thread.sleep(500);
		}
		System.out.println("ConnectionThread up");
	}

	/**
	 * Clean all.
	 */
	@AfterClass
	public static void cleanAll() {
		starterThread.interrupt();
	}

	/**
	 * Clean.
	 */
	@After
	public void clean() {
		connectionThread.interrupt();
	}

	/**
	 * Basic test.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	@Test
	public void basicTest() throws InterruptedException {
		for (int i = 0; i < 5; i++) {
			System.out.println("send msg");
			sendMessage(connectionThread, getBasicMessage());
			Thread.sleep(500);
		}
	}

	/**
	 * Login.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	@Test
	public void login() throws InterruptedException {
		final LoginMessageHandler messageHandler = new LoginMessageHandler();
		connectionThread.setMessageHandler(messageHandler);
		messageHandler.reset();
		final IRCMessage login = new IRCMessageCommandLogin("test", "test");
		sendMessage(connectionThread, login);
		while (!messageHandler.isMessageRecieved()) {
			Thread.sleep(300);
		}
		if (!messageHandler.isLoginValidated()) {
			messageHandler.reset();
			final IRCMessage register = new IRCMessageCommandRegister("test",
					"test");
			sendMessage(connectionThread, register);
			while (!messageHandler.isMessageRecieved()) {
				Thread.sleep(300);
			}
		}
		assertTrue(messageHandler.isLoginValidated());
		assertNotNull(messageHandler.getLogin());
	}

	/**
	 * Send message.
	 * 
	 * @param messageSender
	 *            the message sender
	 * @param message
	 *            the message
	 */
	private void sendMessage(final IIRCMessageSender messageSender,
			final IRCMessage message) {
		messageSender.send(message);
	}

	/**
	 * Gets the basic message.
	 * 
	 * @return the basic message
	 */
	private IRCMessage getBasicMessage() {
		return new IRCMessageChat(0,
				Arrays.asList((IClientMessageLine) new BasicClientMessage(
						"message")), null);
	}
}
