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
import com.gc.irc.server.api.AbstractServerIT;
import com.gc.irc.server.test.handler.LoginMessageHandler;

/**
 * The Class ServerTest.
 */
public class ServerIT extends AbstractServerIT{

	/** The connection thread. */
	private ConnectionThread connectionThread;

	/**
	 * Prepare.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	@Before
	public void prepare() throws InterruptedException {
		connectionThread = getConnectionToServer();
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
	 * Gets the basic message.
	 * 
	 * @return the basic message
	 */
	private IRCMessage getBasicMessage() {
		return new IRCMessageChat(0,
				Arrays.asList((IClientMessageLine) new BasicClientMessage(
						"message")));
	}
}
