package com.gc.irc.server.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gc.irc.common.api.IClientMessageLine;
import com.gc.irc.common.api.impl.BasicClientMessageLine;
import com.gc.irc.common.connector.ConnectionThread;
import com.gc.irc.common.entity.IRCUser;
import com.gc.irc.common.protocol.IRCMessage;
import com.gc.irc.common.protocol.chat.IRCMessageChat;
import com.gc.irc.server.api.AbstractServerIT;
import com.gc.irc.server.test.handler.SimpleMessageHandler;

/**
 * The Class TwoUserBasicTestIT.
 */
public class TwoUserBasicTestIT extends AbstractServerIT{
	
	/** The connection user1. */
	private ConnectionThread connectionUser1;
	
	/** The user1. */
	private IRCUser user1;
	
	/** The connection user2. */
	private ConnectionThread connectionUser2;
	
	/** The user2. */
	private IRCUser user2;
	
	/**
	 * Prepare.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	@Before
	public void prepare() throws InterruptedException {
		connectionUser1 = getConnectionToServer();
		user1 = loginAndRegister(connectionUser1, "TwoUserBasicTestITUser1Login", "TwoUserBasicTestITUser1Password");
		assertNotNull(user1);
		connectionUser2 = getConnectionToServer();
		user2 = loginAndRegister(connectionUser2, "TwoUserBasicTestITUser2Login", "TwoUserBasicTestITUser2Password");
		assertNotNull(user2);
	}
	
	/**
	 * Clean.
	 */
	@After
	public void clean() {
		connectionUser1.disconnect();
		connectionUser1.interrupt();
		connectionUser2.disconnect();
		connectionUser2.interrupt();
	}
	
	/**
	 * Basic test.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	@Test
	public void globalConvers() throws InterruptedException {
		String messageStr = "FisrtMessage";
		SimpleMessageHandler messageHandlerUser1 = new SimpleMessageHandler();
		connectionUser1.setMessageHandler(messageHandlerUser1);
		SimpleMessageHandler messageHandlerUser2 = new SimpleMessageHandler();
		connectionUser2.setMessageHandler(messageHandlerUser2);
		IRCMessage currentSendedMessage = buildSimpleMessage(user1, messageStr);
		messageHandlerUser2.reset();
		sendMessage(connectionUser1, currentSendedMessage);
		waitForMessageInHandler(messageHandlerUser2);
		IRCMessage receivedMessage = messageHandlerUser2.getLastReceivedMessage();
		assertNotNull(receivedMessage);
		assertTrue(receivedMessage instanceof IRCMessageChat);
		IRCMessageChat receivedChatMessage = (IRCMessageChat)receivedMessage;
		assertNotNull(receivedChatMessage.getLines());
		assertEquals(1, receivedChatMessage.getLines().size());
		IClientMessageLine clientMessageLine = receivedChatMessage.getLines().get(0);
		assertNotNull(clientMessageLine);
		assertTrue(clientMessageLine instanceof BasicClientMessageLine);
		BasicClientMessageLine basicClientMessageLine = (BasicClientMessageLine) clientMessageLine;
		assertEquals(messageStr, basicClientMessageLine.getMessage());
	}
	
	/**
	 * Builds the simple message.
	 *
	 * @param user the user
	 * @param message the message
	 * @return the iRC message
	 */
	private IRCMessage buildSimpleMessage(IRCUser user, String message) {
		return new IRCMessageChat(user.getId(),
				Arrays.asList((IClientMessageLine) new BasicClientMessageLine(message)));
	}

}
